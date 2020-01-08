/****************************************************************
 * Esta classe foi criada em setembro de 2017
 * Recebeu modificações de correção no dia 18 de out 2017
 * 
 * Esta classe é responsável por movimentos da metaheuristica SA.
 * Estes movimentos são aleatórios e executam apenas uma vez sobre
 * a solução e são escolhidos de maneira aleatória.
 * 
 * Recebeu correções nos movimentos em 23 de out de 2017
 * 
 * Esta classe obeteve melhoras em 4 de jan de 2020
 * 
 * @author cesar
 * 
 *****************************************************************/

package com.upmsp_main.metaheuristic.sa;

import java.util.ArrayList;
import java.util.Random;

import com.upmsp_main.core.Solution;

public class SA_InsertIntra implements Cloneable {
	
	int p_antes_i = 0; // tempo de preparação do job de índice i(i-1, i)
	int p_t_antes_i = 0; // tempo de preparação depois da troca (i-1, j)

	int p_depois_i = 0; // tempo de preparação do job a frente do i (i, i+1)
	int p_t_depois_i = 0; // tempo de preparação do job a frente do i depois da troca (j, i+1)

	int p_antes_j = 0;
	int p_t_antes_j = 0;

	int p_depois_j = 0;
	int p_t_depois_j = 0;

	
	public Solution shift(Solution s){//insertIntra
		//inserção é realizada apenas se a maquina tem mais do que 1 tarefa
		//este método pega uma tarefa aleatoria e insere em uma outra posição
		//aleatória na mesma maquina.
		Random rnd = new Random();

		int makespan_menor = s.makespan();
		int makespan_corrente = 0;
		int M = s.indice_makespan();
		int nM = -1;
		int size_maq = -1;
		int makespan_atual = makespan_menor;

		//ArrayList<Integer> tempos = this.duplicate_times(s.Tempos());

		//makespan_menor = tempos.get(M);
		makespan_atual = makespan_menor;
		size_maq = s.getMaq(M).getSizeMaq();
		
		//indice da máquina makespan
		int indice_maior = s.maior_menor().get(2);
		
		//garanti que o movimento só acontecerá em uma máquina com mais de 1 tarefa
		if(s.getMaq(indice_maior).getSizeMaq() > 1) {
			int pm1 = -1;
			int pm2 = -1;
			
			//gera os dois numeros aleatórios que serão os indices de troca
			//eles não podem ser iguais, logo o laço garantirá isso
			while(pm1 == pm2){
				pm1 = rnd.nextInt(s.getMaq(indice_maior).getSizeMaq());
				pm2 = rnd.nextInt(s.getMaq(indice_maior).getSizeMaq() - 1);
			}
			
			//armazena o job escolhido
			int job = s.getMaq(indice_maior).getJob(pm1);
			
			s.getMaq(indice_maior).removeJobToMaq(pm1);
			s.getMaq(indice_maior).setJobMaq(pm2, job);
			
			// trata o envolvimento do primeiro elemento
			if (pm1 == 0) {
				p_antes_i = 0;
				p_t_antes_i = 0;
			} else {
				p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
				p_t_antes_i = s.get_T_prep(M, pm1 -1, pm1 + 1);
			}

			// trata o envolvimento do último elemento
			if (pm2 == size_maq -1) {
				p_depois_j = 0;
				p_t_depois_i =  0;
			} else {
				p_depois_j = s.get_T_prep(M, pm2, pm2 + 1);
				p_t_depois_i = s.get_T_prep(M, pm1, pm2 + 1);
			}

			// trata a consecutividade
			if (pm2 - pm1 == 1) {
				p_antes_i = p_antes_i;
				p_depois_i = s.get_T_prep(M, pm1, pm2);//nulo
				p_antes_j = 0;//remove
				p_depois_j = p_depois_j;
				
				p_t_antes_i = p_t_antes_i;
				p_t_depois_i = p_t_depois_i;
				p_t_antes_j = 0;
				p_t_depois_j = s.get_T_prep(M, pm2, pm1);
			}
			// trata a situação comum resultante
			else {
				p_antes_i = p_antes_i;
				p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);//remove							
				p_antes_j = 0;//s.get_T_prep(M, j-1, j);
				p_depois_j = p_depois_j;
				
				p_t_antes_i = p_t_antes_i;
				p_t_depois_i = p_t_depois_i;
				p_t_antes_j = 0;
				p_t_depois_j = s.get_T_prep(M, pm2, pm1);
			}

			makespan_corrente = makespan_atual + 
					(-1) * p_antes_i + p_t_antes_i + 
					(-1) * p_depois_i + p_t_depois_i + 
					(-1) * p_antes_j + p_t_antes_j + 
					(-1) * p_depois_j + p_t_depois_j;
		}
		
		return s;
		
	}
}
