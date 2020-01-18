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
 * @author cesar
 * 
 *****************************************************************/

package com.upmsp_main.metaheuristic.sa;

import java.util.Random;

import com.upmsp_main.core.Solution;

public class MovimentosSA implements Cloneable {

	public Solution task_move(Solution solucao){
		//Este método retira um job de uma posição aleatória da maq makespan
		//e insere esse job em uma posição aleatória na maquina de menor tempo
		Random rnd = new Random();

		int indice_maior = solucao.maior_menor().get(2);
		int indice_menor = indice_maior;
		
		if(solucao.getMaq(indice_maior).getSizeMaq() > 1) {
			while(indice_menor == indice_maior)
				indice_menor = rnd.nextInt(solucao.getSizeSol()); 
			
			//int pm1 = rnd.nextInt(solucao.getMaq(indice_maior).getSizeMaq());
			//int pm2 = rnd.nextInt(solucao.getMaq(indice_menor).getSizeMaq());
			
			int pm1 = 4;
			int pm2 = 2;
			
			int job = solucao.getMaq(indice_maior).getJob(pm1);
			solucao.getMaq(indice_menor).setJobMaq(pm2, job);
			solucao.getMaq(indice_maior).removeJobToMaq(pm1);
		}
		
		return solucao;
	}
	
	public Solution shift(Solution solucao){
		//inserção é realizada apenas se a maquina tem mais do que 1 tarefa
		//este método pega uma tarefa aleatoria e insere em uma outra posição
		//aleatória na mesma maquina.
		Random rnd = new Random();

		int indice_maior = solucao.maior_menor().get(2);
		
		if(solucao.getMaq(indice_maior).getSizeMaq() > 1) {
			int pm1 = -1;
			int pm2 = -1;
			
			while(pm1 == pm2){
				pm1 = rnd.nextInt(solucao.getMaq(indice_maior).getSizeMaq());
				pm2 = rnd.nextInt(solucao.getMaq(indice_maior).getSizeMaq() - 1);
			}
			
			int job = solucao.getMaq(indice_maior).getJob(pm1);
			solucao.getMaq(indice_maior).removeJobToMaq(pm1);
			solucao.getMaq(indice_maior).setJobMaq(pm2, job);
		}
		
		return solucao;
		
	}
	
	public Solution Switch(Solution solucao) {
		//Este método troca duas tarefas escolhidas aleatŕiamente.
		//isso só acontece se a maquina tem mais do que uma tarefa
		Random rnd = new Random();

		int indice_maior = solucao.maior_menor().get(2);
		
		if(solucao.getMaq(indice_maior).getSizeMaq() > 1) {
			int pm1 = -1;
			int pm2 = -1;
			
			while(pm1 == pm2){
				pm1 = rnd.nextInt(solucao.getMaq(indice_maior).getSizeMaq());
				pm2 = rnd.nextInt(solucao.getMaq(indice_maior).getSizeMaq());
			}
			solucao.getMaq(indice_maior).trocaJob(pm1, pm2);
		}
		
		return solucao;
	}
	
	public Solution two_swap(Solution solucao) {
		//troca de um job de uma maq pelo job de outra e vice versa
		//Este movimento não pode deixar uma máquina vazia
		Random rnd = new Random();
		
		int indice_maq1 = -1;
		int indice_maq2 = -1;
		
		//test if not same machines
		while(indice_maq1 == indice_maq2) {
			indice_maq1 = rnd.nextInt(solucao.getSizeSol());
			indice_maq2 = rnd.nextInt(solucao.getSizeSol());
		}
		
		int tamanho_maq1 = solucao.getMaq(indice_maq1).getSizeMaq();
		int p1_m1 = -1;
		if(tamanho_maq1 > 1)
			p1_m1 = rnd.nextInt(tamanho_maq1);//get a random position in this machine
		else
			p1_m1 = 0;
		
		int job_m1 = solucao.getMaq(indice_maq1).getJob(p1_m1); 
		
		int tamanho_maq2 = solucao.getMaq(indice_maq2).getSizeMaq();
		int p1_m2 = -1;
		if(tamanho_maq2 > 1)
			p1_m2 = rnd.nextInt(tamanho_maq2);
		else
			p1_m2 = 0;

		solucao.getMaq(indice_maq2).setJobMaq(p1_m2, job_m1);
		solucao.getMaq(indice_maq1).removeJobToMaq(p1_m1);	//remove this job of first machine	
		
		
		tamanho_maq2 = solucao.getMaq(indice_maq2).getSizeMaq();
		if(tamanho_maq2 > 1)
			p1_m2 = rnd.nextInt(tamanho_maq2);
		else
			p1_m2 = 0;
		
		int job_m2 = solucao.getMaq(indice_maq2).getJob(p1_m2); 
		//test if it the same job
		while(job_m1 == job_m2) {
			if(tamanho_maq2 > 1) {
				p1_m2 = rnd.nextInt(tamanho_maq2);
				job_m2 = solucao.getMaq(indice_maq2).getJob(p1_m2);
			}else {
				p1_m2 = 0;
				job_m2 = solucao.getMaq(indice_maq2).getJob(p1_m2);
			}
		}

		tamanho_maq1 = solucao.getMaq(indice_maq1).getSizeMaq();
		int p2_m1 = -1;
		if(tamanho_maq1 > 1)
			p2_m1 = rnd.nextInt(tamanho_maq1);
		else
			p2_m1 = 0;
		
		solucao.getMaq(indice_maq1).setJobMaq(p2_m1, job_m2);
		solucao.getMaq(indice_maq2).removeJobToMaq(p1_m2);
		
		return solucao;
	}
	
	public Solution two_realloc(Solution solucao){
		//Este método faz duas trocas na maquina makespan. isso só acontece
		//se a maquina tem mais que duas tarefas
		Random rnd = new Random();

		int indice_maior = solucao.maior_menor().get(2);
		
		if(solucao.getMaq(indice_maior).getSizeMaq() > 2) {
			
			int pm1 = -1;
			int pm2 = -1;
		
			int maq_size = solucao.getMaq(indice_maior).getSizeMaq();
		
			while(pm1 == pm2){
				pm1 = rnd.nextInt(maq_size);
				pm2 = rnd.nextInt(maq_size);
			}
		
			solucao.getMaq(indice_maior).trocaJob(pm1, pm2);
		
			pm1 = -1;
			pm2 = -1;
		
			while(pm1 == pm2){
				pm1 = rnd.nextInt(maq_size);
				pm2 = rnd.nextInt(maq_size);
			}
		
			solucao.getMaq(indice_maior).trocaJob(pm1, pm2);
		}
		return solucao;
	}

}
