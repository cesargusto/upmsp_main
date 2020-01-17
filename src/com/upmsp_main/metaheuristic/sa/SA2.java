package com.upmsp_main.metaheuristic.sa;

import java.util.ArrayList;
import java.util.Random;

import com.upmsp_main.experiment.BestResults;
import com.upmsp_main.core.Solution;

public class SA2 {
	
	private Solution solucao;
	private Moviment m_sa;
	private Better_mov bm;
	private BestResults best_results;
	private double T_INICIAL;
	private double ALF;
	private int SAMAX;
	
	public SA2(Solution solucao, double t_inicial, float alfa, int samax, BestResults best_results) {
		this.solucao = solucao;
		this.m_sa = new Moviment(solucao);
		this.T_INICIAL = t_inicial;
		this.ALF = alfa;
		this.SAMAX = samax;
		this.best_results = best_results;
		this.bm = new Better_mov();
	}
	
	public SA2(Solution solucao, int samax) {
		this.solucao = solucao;
		this.m_sa = new Moviment(solucao);
		this.T_INICIAL = 900.0;
		this.ALF = 0.94;
		this.SAMAX = samax;
		this.bm = new Better_mov();
	}
	
	public Solution execute_sa() throws CloneNotSupportedException {
		Solution melhor_solucao;
		Solution solucao_linha;
		long fo_solucao = Integer.MAX_VALUE;
		//long fo_solucao_linha = Integer.MAX_VALUE;
		ArrayList<Integer> fo_solucao_linha = new ArrayList<>();
		int IterT = 0;			// iterations number in temperature
		double T = T_INICIAL;	// initial temperature
		Random rnd = new Random();
		
		melhor_solucao = solucao.clone();	// best solution receive the solution was given
		
		while(T > 1){
			while(IterT < SAMAX){
				IterT += 1;
				
				fo_solucao = solucao.makespan();
				solucao_linha = solucao.clone();
				//solucao_linha = this.gera_vizinho(solucao_linha);
				fo_solucao_linha = this.gera_vizinho(solucao_linha);
				//fo_solucao_linha = solucao_linha.makespan();
				
				//solucao_linha.print_solution();
				
				long Alfa = fo_solucao_linha.get(4) - fo_solucao;
				
				if(Alfa < 0){
					solucao = solucao_linha.clone();					
					if(fo_solucao_linha.get(4) < melhor_solucao.makespan()){
						melhor_solucao = solucao.clone();
						
						bm.grava_movimento(melhor_solucao, m_sa.getBetter_mov());
						System.out.println("Melhora SA :"+fo_solucao_linha.get(4));
						//melhor_solucao.print_solution();
						//System.out.println();
						
					}
				}
				else{
					Double x = rnd.nextDouble();
					Double exp = Math.pow(Math.E, (-1*Alfa)/T); 
					if(x < exp){
						solucao = solucao_linha.clone();
						bm.grava_movimento(solucao, m_sa.getBetter_mov());
						
					}
				}
			}
			//System.out.printf("\nTemperatura:\t%.4f\tMakespan:\t%d\n", T, fo_solucao_linha);
			
			T = ALF * T;
			IterT = 0;
			//this.best_results.setMakespan_list(melhor_solucao.makespan());
		}
		//solucao = melhor_solucao.clone();
		//int fo_melhor = solucao.makespan();
		//this.best_results.setBest_list(fo_melhor);
		return melhor_solucao;
	}
	
	
	//Retornará um inteiro
	public ArrayList<Integer> gera_vizinho(Solution s){
		
		int indice_maior = this.solucao.maior_menor().get(2);
		
		Random rnd = new Random();
		int num_movimentos = 5;
		int opcao = 1 + rnd.nextInt(num_movimentos);
		
		
		switch(opcao){
		case 1:
			return m_sa.insert_intra(s);
		case 2:
			return m_sa.insert_extra(s);
		case 3:
			return m_sa.troca_intra(s);
		case 4:
			if(solucao.getMaq(this.solucao.maior_menor().get(0)).getSizeMaq() > 3)
				return m_sa.troca_extra(s);
			else
				return m_sa.insert_extra(s);
		case 5:
			if(solucao.getMaq(indice_maior).getSizeMaq() > 2)
				return m_sa.two_realloc(s);
			else
				return m_sa.troca_extra(s);
		default:
			System.out.println("Problema com o valor aleatório.");
		}
		return null;
		//System.out.println("Movimento escolhido:\t"+opcao+"\n");
	}
}
