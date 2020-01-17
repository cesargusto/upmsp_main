/**
 *Esta Classe foi criada entre 13 e 17 de Janeiro de 2020 
 *o objetivo era melhorar a performance das movimentações do Simulated Annealing
 *mas houve uma piora significativa.
 *
 * @author cesar
 * */

package com.upmsp_main.metaheuristic.sa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.upmsp_main.core.Solution;

public class Moviment {
	
	private ArrayList<Integer> times;
	private ArrayList<Integer> better_mov;
	private Better_mov bm;
	protected Solution s;
	
	int p_antes_i = 0; // tempo de preparação do job de índice i(i-1, i)
	int p_t_antes_i = 0; // tempo de preparação depois da troca (i-1, j)

	int p_depois_i = 0; // tempo de preparação do job a frente do i (i, i+1)
	int p_t_depois_i = 0; // tempo de preparação do job a frente do i depois da troca (j, i+1)

	int p_antes_j = 0;
	int p_t_antes_j = 0;

	int p_depois_j = 0;
	int p_t_depois_j = 0;


	int e_antes = 0;
	int e_depois = 0;
	
	public Moviment(Solution s) {
		this.s = s;
		this.times = new ArrayList<Integer>();
		this.better_mov = new ArrayList<Integer>();
		this.bm = new Better_mov();
	}

	
	//public abstract ArrayList<Integer> execute_mov(Boolean first_imp);
	
	/**
	 * Aplica a melhor movimentação encontrada:
	 * 
	 * @param mov     lista de movimento
	 * @param solução s
	 */
	
	
	public void update_times(int M, int value) {
		this.times.set(M, value);
	}
	
	// Execução da cópia dos tempos para a lista auxiliar [tempos]
	public ArrayList<Integer> duplicate_times(ArrayList<Integer> Tempos) {
		ArrayList<Integer> t = new ArrayList<Integer>();
		t.addAll(Tempos);

		return t;
	}


	public ArrayList<Integer> getBetter_mov() {
		return better_mov;
	}

	public void setBetter_mov(ArrayList<Integer> better_mov) {
		this.better_mov = better_mov;
	}
	
	public void better_mov_final(int M, int pM, int m, int pm, int makespan, int movimento) {		
		better_mov.clear();
		better_mov.add(M);
		better_mov.add(pM);
		better_mov.add(m);
		better_mov.add(pm);
		better_mov.add(makespan);
		better_mov.add(movimento);
	}
	
	public ArrayList<Integer> insert_intra(Solution s) {// Shift
		// inserção é realizada apenas se a maquina tem mais do que 1 tarefa
		// este método pega uma tarefa aleatoria e insere em uma outra posição
		// aleatória na mesma maquina.

		Random rnd = new Random();

		ArrayList<Integer> tempos_copia = this.duplicate_times((ArrayList<Integer>) s.Tempos());
		
		int makespan_inicial = s.makespan();
		int M = s.indice_makespan();
		int size_maq = s.getMaq(M).getSizeMaq();
		int makespan_final = Integer.MAX_VALUE;

		if (s.getMaq(M).getSizeMaq() > 1) {

			int pm1 = -1;
			int pm2 = -1;

			while (pm1 == pm2) {
				pm1 = rnd.nextInt(s.getMaq(M).getSizeMaq());
				pm2 = rnd.nextInt(s.getMaq(M).getSizeMaq() - 1);
			}

			if (pm2 < pm1) {
				int aux = pm2;
				pm2 = pm1;
				pm1 = aux;
			}

			//System.out.println("Posicao 1: " + pm1);
			//System.out.println("Posicao 2: " + pm2);
			
			// trata o envolvimento do primeiro elemento
			if (pm1 == 0) {
				p_antes_i = 0;
				p_t_antes_i = 0;
			} else {
				if (pm1 == size_maq - 1) {
					p_t_antes_i = 0;
				} else {
					p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
					p_t_antes_i = s.get_T_prep(M, pm1 - 1, pm1 + 1);
				}
			}

			// trata o envolvimento do último elemento
			if (pm2 == size_maq - 1) {
				p_depois_j = 0;
				p_t_depois_i = 0;
			} else {
				p_depois_j = s.get_T_prep(M, pm2, pm2 + 1);
				p_t_depois_i = s.get_T_prep(M, pm1, pm2 + 1);
			}

			// trata a consecutividade
			if (pm2 - pm1 == 1) {
				p_antes_i = p_antes_i;
				p_depois_i = s.get_T_prep(M, pm1, pm2);// nulo
				p_antes_j = 0;// remove
				p_depois_j = p_depois_j;

				p_t_antes_i = p_t_antes_i;
				p_t_depois_i = p_t_depois_i;
				p_t_antes_j = 0;
				p_t_depois_j = s.get_T_prep(M, pm2, pm1);
			}
			// trata a situação comum resultante
			else {
				if (pm2 - pm1 < 1)
					p_depois_i = s.get_T_prep(M, pm2, pm1);
				else {// nulo
					p_antes_i = p_antes_i;
					p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);// remove
					p_antes_j = 0;// s.get_T_prep(M, j-1, j);
					p_depois_j = p_depois_j;

					p_t_antes_i = p_t_antes_i;
					p_t_depois_i = p_t_depois_i;
					p_t_antes_j = 0;
					p_t_depois_j = s.get_T_prep(M, pm2, pm1);
				}
			}

			makespan_final = makespan_inicial + (-1) * p_antes_i + p_t_antes_i + (-1) * p_depois_i + p_t_depois_i
					+ (-1) * p_antes_j + p_t_antes_j + (-1) * p_depois_j + p_t_depois_j;

			tempos_copia.set(M, makespan_final);

			makespan_final = Collections.max(tempos_copia); 
			//System.out.println(tempos_copia);

			this.better_mov_final(M, pm1, M, pm2, makespan_final, 0);
			return better_mov; 
		} else
			return null;
	}

	public ArrayList<Integer> insert_extra(Solution s) {// task_move
		// Este método retira um job de uma posição aleatória da maq makespan
		// e insere esse job em uma posição aleatória na maquina de menor tempo

		ArrayList<Integer> tempos_copia = this.duplicate_times((ArrayList<Integer>) s.Tempos());

		int makespan_final = 0;
		int makespan_inicial = s.makespan();
		int indice_maior = s.maior_menor().get(2);
		int indice_menor = s.maior_menor().get(0);
		int M = s.indice_makespan();
		int m = indice_menor;
		int size_maq_M = s.getMaq(M).getSizeMaq();
		int size_maq_m = s.getMaq(m).getSizeMaq();


		Random rnd = new Random();

		if (s.getMaq(indice_maior).getSizeMaq() > 1) {

			int pm1 = rnd.nextInt(s.getMaq(indice_maior).getSizeMaq());

			//System.out.println("Posicao 1: " + pm1);

			if (size_maq_M > 1) {

				if (pm1 == 0) {
					p_antes_i = 0;
					p_t_antes_i = 0;
				} else {
					p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
					if(pm1 >= size_maq_M - 1)
						p_t_antes_i = 0;
					else
						p_t_antes_i = s.get_T_prep(M, pm1 - 1, pm1 + 1);
				}

				// trata o envolvimento do último elemento
				if (pm1 == size_maq_M) {
					p_depois_i = 0;
					p_t_depois_i = 0;
				} else {
					if(pm1 >= size_maq_M - 1)
						p_depois_i = 0;
					else
						p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);
					p_t_depois_i = 0;
				}

				int p = s.getMaq(M).getJob(pm1);
				e_antes = s.getArquivo().getT_exec(M, p);
				e_depois = s.getArquivo().getT_exec(m, p);

				int pivot = s.get_T_prep(m, size_maq_m - 1, M, pm1);

				makespan_final = makespan_inicial + (-1) * e_antes + (-1) * p_antes_i + (-1) * p_depois_i + p_t_antes_i;
				int m_m = s.maior_menor().get(1) + e_depois + p_t_depois_i + pivot;

				tempos_copia.set(M, makespan_final);
				tempos_copia.set(m, m_m);

				// Busca o maior elemento da lista alterada
				makespan_final = Collections.max(tempos_copia); // É necessário aqui? pra que?

				this.better_mov_final(M, pm1, m, -1, makespan_final, 1);
				return better_mov;
			} 
		}
		return null;
	}

	public ArrayList<Integer> troca_intra(Solution s) {

		/*
		 * O movimento de troca_intra será feito em todas as maquinas que porventura
		 * venham a se tornar makespan.
		 */

		ArrayList<Integer> tempos = this.duplicate_times((ArrayList<Integer>) s.Tempos());
		int M = s.indice_makespan();
		int makespan_final = 0;
		int size_maq = s.getMaq(M).getSizeMaq();
		int makespan_inicial = tempos.get(M);
		
		Random rnd = new Random();

		// trata maquinas com apenas um(1)job
		if (size_maq > 1) {
			int pm1 = -1;
			int pm2 = -1;

			while (pm2 == pm1) {
				pm1 = rnd.nextInt(s.getMaq(M).getSizeMaq());
				pm2 = rnd.nextInt(s.getMaq(M).getSizeMaq());
			}

			if (pm2 < pm1) {
				int aux = pm2;
				pm2 = pm1;
				pm1 = aux;
			}

			//System.out.println("Posicao 1: " + pm1);
			//System.out.println("Posicao 2: " + pm2);

			// trata o envolvimento do primeiro elemento
			if (pm1 == 0) {
				p_antes_i = 0; // setup do primeiro é 0
				p_t_antes_i = 0; // setup depois da troca também
			} else {
				p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
				p_t_antes_i = s.get_T_prep(M, pm1 - 1, pm2);
			}

			// trata o envolvimento do último elemento
			if (pm2 + 1 == size_maq) {
				p_depois_j = 0;
				p_t_depois_j = 0;
			} else {
				p_depois_j = s.get_T_prep(M, pm2, pm2 + 1);
				p_t_depois_j = s.get_T_prep(M, pm1, pm2 + 1);
			}

			// trata a consecutividade
			if (pm2 - pm1 == 1) {
				p_depois_i = 0;
				p_t_antes_j = 0;
				p_t_depois_i = s.get_T_prep(M, pm2, pm1);
				p_antes_j = s.get_T_prep(M, pm2 - 1, pm2);
			}
			// trata a situação comum resultade
			else {
				p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);
				p_t_depois_i = s.get_T_prep(M, pm2, pm1 + 1);
				p_antes_j = s.get_T_prep(M, pm2 - 1, pm2);
				p_t_antes_j = s.get_T_prep(M, pm2 - 1, pm1);
			}

			makespan_final = makespan_inicial + (-1) * p_antes_i + p_t_antes_i + (-1) * p_depois_i + p_t_depois_i
					+ (-1) * p_antes_j + p_t_antes_j + (-1) * p_depois_j + p_t_depois_j;

			// Verifica se o movimento simulado trará ou não melhora

			tempos.set(M, makespan_final);
			makespan_final = Collections.max(tempos);
			this.better_mov_final(M, pm1, M, pm2, makespan_final, 2);
		} 

		return better_mov;
	}
	
	public ArrayList<Integer> troca_extra(Solution s) {

		ArrayList<Integer> tempos = this.duplicate_times((ArrayList<Integer>) s.Tempos());

		Random rnd = new Random();

		int makespan_final = s.makespan();
		int makespan_inicial = makespan_final;
		int sol_size = s.getSizeSol();
		int M = tempos.indexOf(makespan_inicial);
		int machine_mkpan_size = s.getMaq(M).getSizeMaq();

		int pm1 = -1; // i - posição escolhida na maq makespan
		int m2 = M; // w - maquina escolhida para alvo
		int pm2 = -1; // j - posição da maq alvo

		pm1 = rnd.nextInt(s.getMaq(M).getSizeMaq());

		while (m2 == M) {
			m2 = rnd.nextInt(sol_size);
		}

		pm2 = rnd.nextInt(s.getMaq(m2).getSizeMaq());

		//System.out.println("Posicao na makespan: " + pm1);
		//System.out.println("Maquina alvo escolhida: " + m2);
		//System.out.println("posição na Maq alvo ecolhida: " + pm2);

		
		// trata o envolvimento do primeiro elemento
		if (pm1 == 0) {
			p_antes_i = 0;
			p_t_antes_i = 0;
		} else {
			p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
			p_t_antes_i = s.get_T_prep(M, pm1 - 1, m2, pm2);
		}

		if (pm2 == 0) {
			p_antes_j = 0;
			p_t_antes_j = 0;
		} else {
			p_antes_j = s.get_T_prep(m2, pm2 - 1, pm2);
			p_t_antes_j = s.get_T_prep(m2, pm2 - 1, M, pm1, m2);
		}

		// trata o envolvimento do último elemento
		if (pm2 + 1 == s.getMaq(m2).getSizeMaq()) {
			p_depois_j = 0;
			p_t_depois_j = 0;
			p_t_depois_i = 0;
		} else {
			p_depois_j = s.get_T_prep(m2, pm2, pm2 + 1);
			p_t_depois_j = s.get_T_prep(M, pm1, m2, pm2 + 1, m2);
		}

		if (pm1 + 1 == machine_mkpan_size) {
			p_t_depois_j = 0;
			p_depois_i = 0;
		} else {
			p_t_depois_i = s.get_T_prep(m2, pm2, M, pm1 + 1, M);
			p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);
		}

		int p = s.getMaq(M).getJob(pm1);
		int e_antes_i = s.getArquivo().getT_exec(M, p);

		int q = s.getMaq(m2).getJob(pm2);
		int e_depois_i = s.getArquivo().getT_exec(M, q);

		p = s.getMaq(m2).getJob(pm2);
		int e_antes_j = s.getArquivo().getT_exec(m2, p);

		q = s.getMaq(M).getJob(pm1);
		int e_depois_j = s.getArquivo().getT_exec(m2, q);

		makespan_final = tempos.get(M) + (-1) * p_antes_i + (-1) * p_depois_i + (-1) * e_antes_i
				+ p_t_antes_i + p_t_depois_i + e_depois_i;
		int maq_corrente = tempos.get(m2) + (-1) * p_antes_j + (-1) * p_depois_j + (-1) * e_antes_j
				+ p_t_antes_j + p_t_depois_j + e_depois_j;

		tempos.set(M, makespan_final);
		tempos.set(m2, maq_corrente);

		makespan_final = Collections.max(tempos);

		this.better_mov_final(M, pm1, m2, pm2, makespan_final, 3);
		
		return better_mov;
	}

	public ArrayList<Integer> two_realloc(Solution s){
		//Este método faz duas trocas na maquina makespan. isso só acontece
		//se a maquina tem mais que duas tarefas

		Random rnd = new Random();
		int makespan_final = 0;
		int M = s.indice_makespan();
		int maq_size = s.getMaq(M).getSizeMaq();

		ArrayList<Integer> tempos = this.duplicate_times((ArrayList<Integer>) s.Tempos());

		int makespan_inicial = tempos.get(M);
		
		
		if(s.getMaq(M).getSizeMaq() > 2) {
			
			int pm1 = -1;
			int pm2 = -1;
		
			while(pm1 == pm2){
				pm1 = rnd.nextInt(maq_size);
				pm2 = rnd.nextInt(maq_size);
			}
		
			if (pm2 < pm1) {
				int aux = pm2;
				pm2 = pm1;
				pm1 = aux;
			}

			//System.out.println("Posicao 1: " + pm1);
			//System.out.println("Posicao 2: " + pm2);

			if (pm1 == 0) {
				p_antes_i = 0;
				p_t_antes_i = 0;
			} else {
				if (pm1 == maq_size - 1) {
					p_t_antes_i = 0;
				} else {
					p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
					p_t_antes_i = s.get_T_prep(M, pm1 - 1, pm1 + 1);
				}
			}

			// trata o envolvimento do último elemento
			if (pm2 == maq_size - 1) {
				p_depois_j = 0;
				p_t_depois_i = 0;
			} else {
				p_depois_j = s.get_T_prep(M, pm2, pm2 + 1);
				p_t_depois_i = s.get_T_prep(M, pm1, pm2 + 1);
			}

			// trata a consecutividade
			if (pm2 - pm1 == 1) {
				p_antes_i = p_antes_i;
				p_depois_i = s.get_T_prep(M, pm1, pm2);// nulo
				p_antes_j = 0;// remove
				p_depois_j = p_depois_j;

				p_t_antes_i = p_t_antes_i;
				p_t_depois_i = p_t_depois_i;
				p_t_antes_j = 0;
				p_t_depois_j = s.get_T_prep(M, pm2, pm1);
			}
			// trata a situação comum resultante
			else {
				if (pm2 - pm1 < 1)
					p_depois_i = s.get_T_prep(M, pm2, pm1);
				else {// nulo
					p_antes_i = p_antes_i;
					p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);// remove
					p_antes_j = 0;// s.get_T_prep(M, j-1, j);
					p_depois_j = p_depois_j;

					p_t_antes_i = p_t_antes_i;
					p_t_depois_i = p_t_depois_i;
					p_t_antes_j = 0;
					p_t_depois_j = s.get_T_prep(M, pm2, pm1);
				}
			}

			
			makespan_final = makespan_inicial + (-1) * p_antes_i + p_t_antes_i + (-1) * p_depois_i + p_t_depois_i
					+ (-1) * p_antes_j + p_t_antes_j + (-1) * p_depois_j + p_t_depois_j;

			tempos.set(M, makespan_final);
			makespan_final = Collections.max(tempos);
			this.better_mov_final(M, pm1, M, pm2, makespan_final, 0);
			s = this.bm.grava_movimento(s, better_mov);
			//System.out.println(better_mov);
			//s.print_solution();
			
			/*********************************************/
			
			pm1 = -1;
			pm2 = -1;
		
			while(pm1 == pm2){
				pm1 = rnd.nextInt(maq_size);
				pm2 = rnd.nextInt(maq_size);
			}
		
			if (pm2 < pm1) {
				int aux = pm2;
				pm2 = pm1;
				pm1 = aux;
			}
			
			//System.out.println("Posicao 3: " + pm1);
			//System.out.println("Posicao 4: " + pm2);

			if (pm1 == 0) {
				p_antes_i = 0;
				p_t_antes_i = 0;
			} else {
				if (pm1 == maq_size - 1) {
					p_t_antes_i = 0;
				} else {
					p_antes_i = s.get_T_prep(M, pm1 - 1, pm1);
					p_t_antes_i = s.get_T_prep(M, pm1 - 1, pm1 + 1);
				}
			}

			// trata o envolvimento do último elemento
			if (pm2 == maq_size - 1) {
				p_depois_j = 0;
				p_t_depois_i = 0;
			} else {
				p_depois_j = s.get_T_prep(M, pm2, pm2 + 1);
				p_t_depois_i = s.get_T_prep(M, pm1, pm2 + 1);
			}

			// trata a consecutividade
			if (pm2 - pm1 == 1) {
				p_antes_i = p_antes_i;
				p_depois_i = s.get_T_prep(M, pm1, pm2);// nulo
				p_antes_j = 0;// remove
				p_depois_j = p_depois_j;

				p_t_antes_i = p_t_antes_i;
				p_t_depois_i = p_t_depois_i;
				p_t_antes_j = 0;
				p_t_depois_j = s.get_T_prep(M, pm2, pm1);
			}
			// trata a situação comum resultante
			else {
				if (pm2 - pm1 < 1)
					p_depois_i = s.get_T_prep(M, pm2, pm1);
				else {// nulo
					p_antes_i = p_antes_i;
					p_depois_i = s.get_T_prep(M, pm1, pm1 + 1);// remove
					p_antes_j = 0;// s.get_T_prep(M, j-1, j);
					p_depois_j = p_depois_j;

					p_t_antes_i = p_t_antes_i;
					p_t_depois_i = p_t_depois_i;
					p_t_antes_j = 0;
					p_t_depois_j = s.get_T_prep(M, pm2, pm1);
				}
			}

			makespan_final = makespan_final + (-1) * p_antes_i + p_t_antes_i + (-1) * p_depois_i + p_t_depois_i
					+ (-1) * p_antes_j + p_t_antes_j + (-1) * p_depois_j + p_t_depois_j;

			tempos.set(M, makespan_final);
			makespan_final = Collections.max(tempos);
			//System.out.println(tempos);
			this.better_mov_final(M, pm1, M, pm2, makespan_final, 0);
		}
		
		return better_mov;
	}

}
