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
import java.util.Collections;
import java.util.Random;

import com.upmsp_main.core.Solution;

public class MovimentoSA2 implements Cloneable {
	
	int p_antes_i = 0; // tempo de preparação do job de índice i(i-1, i)
	int p_t_antes_i = 0; // tempo de preparação depois da troca (i-1, j)

	int p_depois_i = 0; // tempo de preparação do job a frente do i (i, i+1)
	int p_t_depois_i = 0; // tempo de preparação do job a frente do i depois da troca (j, i+1)

	int p_antes_j = 0;
	int p_t_antes_j = 0;

	int p_depois_j = 0;
	int p_t_depois_j = 0;

	
	public int insert_intra(Solution s){//insertIntra
		//inserção é realizada apenas se a maquina tem mais do que 1 tarefa
		//este método pega uma tarefa aleatoria e insere em uma outra posição
		//aleatória na mesma maquina.
		
		Random rnd = new Random();

		int makespan_inicial = s.makespan();
		int M = s.indice_makespan();
		int size_maq = s.getMaq(M).getSizeMaq();
		int makespan_final = Integer.MAX_VALUE;
		
		if(s.getMaq(M).getSizeMaq() > 1) {
			
			int pm1 = -1;
			int pm2 = -1;
			
			while(pm1 == pm2){
				pm1 = rnd.nextInt(s.getMaq(M).getSizeMaq());
				pm2 = rnd.nextInt(s.getMaq(M).getSizeMaq() - 1);
			}
			
			if(pm2 < pm1) {
				int aux = pm2;
				pm2 = pm1;
				pm1 = aux;
			}
				
			System.out.println();
			System.out.println("Maquina makespan: "+M);
			System.out.println("Posicao 1: "+pm1);
			System.out.println("Posicao 2: "+pm2);
			
			// trata o envolvimento do primeiro elemento
			if (pm1 == 0) {
				p_antes_i = 0;
				p_t_antes_i = 0;
			} else {
				if (pm1 == size_maq -1) {
					p_t_antes_i = 0;
				}
				else {
					p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
					p_t_antes_i = s.get_T_prep(M, pm1 -1, pm1 + 1);
				}
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
				if(pm2 - pm1 < 1)
					p_depois_i = s.get_T_prep(M, pm2, pm1);
				else{//nulo
					p_antes_i = p_antes_i;
					p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);//remove							
					p_antes_j = 0;//s.get_T_prep(M, j-1, j);
					p_depois_j = p_depois_j;
					
					p_t_antes_i = p_t_antes_i;
					p_t_depois_i = p_t_depois_i;
					p_t_antes_j = 0;
					p_t_depois_j = s.get_T_prep(M, pm2, pm1);
				}
			}

			makespan_final = makespan_inicial + 
					(-1) * p_antes_i + p_t_antes_i + 
					(-1) * p_depois_i + p_t_depois_i + 
					(-1) * p_antes_j + p_t_antes_j + 
					(-1) * p_depois_j + p_t_depois_j;
		
			return makespan_final;
		}
		else 
			return makespan_inicial;		
	}
	
	public Solution insert_extra(Solution s){//task_move
		//Este método retira um job de uma posição aleatória da maq makespan
		//e insere esse job em uma posição aleatória na maquina de menor tempo
		
		int e_antes = 0;
		int e_depois = 0;
		
		Random rnd = new Random();

		int indice_maior = s.maior_menor().get(2);
		int indice_menor = s.maior_menor().get(0);
		
		if(s.getMaq(indice_maior).getSizeMaq() > 1) {
			
			int pm1 = rnd.nextInt(s.getMaq(indice_maior).getSizeMaq());
			int pm2 = rnd.nextInt(s.getMaq(indice_menor).getSizeMaq());
			
			System.out.println();
			
			System.out.println("Posicao 1: "+pm1);
			System.out.println("Posicao 2: "+pm2);

			int makespan_final = 0;	
			int makespan_inicial = s.makespan();
			int M = s.indice_makespan();
			int m = indice_menor;
			int size_maq_M = s.getMaq(M).getSizeMaq();
			int size_maq_m = s.getMaq(m).getSizeMaq();
			
			if (size_maq_M > 1) 
			{
				
				if (i == 0) {
					p_antes_i = 0;
					p_t_antes_i = 0;
				} else {
					p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
					p_t_antes_i = s.get_T_prep(M, pm1-1, pm1 + 1);
				}

				// trata o envolvimento do último elemento
				if (i == size_maq_M) {
					p_depois_i = 0;
					p_t_depois_i =  0;
				} else {
					p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);
					p_t_depois_i = 0;
				}

				int p = s.getMaq(M).getJob(pm1);
				e_antes = s.getArquivo().getT_exec(M, p);
				e_depois = s.getArquivo().getT_exec(m, p);
				
				int pivot = s.get_T_prep(m, size_maq_m - 1,M,pm1);
			
				makespan_final = makespan_inicial + (-1)* e_antes + (-1)* p_antes_i + (-1)* p_depois_i + p_t_antes_i; 
				int m_m = tempos_copia.get(m) + e_depois + p_t_depois_i + pivot;

				int t1 = tempos_copia.get(M);
				int t2 = tempos_copia.get(m);
				
				tempos_copia.set(M, makespan_corrente);
				tempos_copia.set(m, m_m);
				
				makespan_final = Collections.max(tempos_copia); // É necessário aqui? pra que?
				
				if (makespan_corrente < makespan_star) {
					makespan_star = makespan_corrente;
					this.better_mov_final(M, i, m, size_maq_m, makespan_star, 1);
				}
				
				tempos_copia.set(M, t1);
				tempos_copia.set(m, t2);
								
			} 
			else
				System.out.println("Maquina menor que 1");
			
			int job = s.getMaq(indice_maior).getJob(pm1);
			s.getMaq(indice_menor).setJobMaq(pm2, job);
			s.getMaq(indice_maior).removeJobToMaq(pm1);
		}
		
		return s;
	}
}
