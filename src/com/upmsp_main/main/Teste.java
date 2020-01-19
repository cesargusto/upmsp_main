package com.upmsp_main.main;

import java.io.IOException;

import com.upmsp_main.core.Solution;
import com.upmsp_main.experiment.BestResults;
import com.upmsp_main.experiment.Time;
import com.upmsp_main.experiment.WriteResultsFile;
import com.upmsp_main.instances.ReadInstances;
import com.upmsp_main.instances.readRuiz;
import com.upmsp_main.metaheuristic.sa.MovimentosSA;
import com.upmsp_main.metaheuristic.sa.SA;

public class Teste {

	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		String complete_path = "experiment_instances/preliminar/I_250_30_S_1-124_1.txt";
		ReadInstances inst = new readRuiz(complete_path);
		
		//inst.imprime_tempo_exec();
		//inst.imprime_tempo_prep();
		
		Solution sol;
		BestResults best_results = new BestResults();

		
		long start = 0;
		long end_1 = 0, end_2 = 0;
		long t = 0;
		
		
		
		sol = new Solution(inst);
		sol.ConstroiSolution();
		sol.print_solution();
		/*
		MovimentosSA msa = new MovimentosSA();
		msa.task_move(sol);
		
		sol.print_solution();
		
		System.out.println(sol.getMaq(0).getTempo_maq());
		System.out.println(sol.getMaq(1).getTempo_maq());
		*/
		start = System.currentTimeMillis();
		SA sa = new SA(sol, 1000);
		sol = sa.execute_sa();
		System.out.println("\nMAKESPAN = "+sol.makespan2());
		
		end_2 = System.currentTimeMillis();
		t = end_2 - start;
		System.out.println("\nTempo Total: "+t/1000+" segundos");
		

	}

}
