package com.upmsp_main.main;

import java.io.IOException;

import com.upmsp_main.core.Solution;
import com.upmsp_main.experiment.BestResults;
import com.upmsp_main.experiment.Time;
import com.upmsp_main.experiment.WriteResultsFile;
import com.upmsp_main.instances.ReadInstances;
import com.upmsp_main.instances.readRuiz;
import com.upmsp_main.metaheuristic.sa.SA;

public class Teste {

	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		String complete_path = "experiment_instances/ruiz/large/I_100_10_S_1-124_1.txt";
		ReadInstances inst = new readRuiz(complete_path);
		
		//inst.imprime_tempo_exec();
		//inst.imprime_tempo_prep();
		
		Solution sol;
		BestResults best_results = new BestResults();

		
		long start = 0;
		long end_1 = 0, end_2 = 0;
		long t = 0;
		
		start = System.currentTimeMillis();
		
		sol = new Solution(inst);
		sol.ConstroiSolution();
		
		end_1 = System.currentTimeMillis();
		t = end_1 - start;
		System.out.println("Solução construída\nTempo: "+t/1000+" segundos\n");
		
		SA sa = new SA(sol, 2200, (float) 0.99, 100000, best_results);
		sol = sa.execute_sa();
		
		end_2 = System.currentTimeMillis();
		t = end_2 - start;
		sol.print_makespan();
		System.out.println("\nTempo Total: "+t/1000+" segundos");
		

	}

}
