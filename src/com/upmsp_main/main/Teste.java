package com.upmsp_main.main;

import java.io.IOException;

import com.upmsp_main.core.Solution;
import com.upmsp_main.experiment.BestResults;
import com.upmsp_main.experiment.Time;
import com.upmsp_main.experiment.WriteResultsFile;
import com.upmsp_main.instances.ReadInstances;
import com.upmsp_main.instances.readRuiz;
import com.upmsp_main.metaheuristic.sa.SA;
import com.upmsp_main.metaheuristic.sa.MovimentoSA2;

public class Teste {

	/*
	 * 		long start = 0;
		long end = 0;
		long t = 0;
		
		start = System.currentTimeMillis();
		
		sol = new Solution(inst);
		sol.ConstroiSolution();
		
		sol = sa.execute_sa();
		
		end = System.currentTimeMillis();
		t = end - start;
		
		sol.print_makespan();
		//System.out.println("\nTempo Total: "+t/1000+" segundos");
		 * 
		 * */
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		String complete_path = "experiment_instances/ruiz/small/I_10_2_S_1-124_1.txt";
		ReadInstances inst = new readRuiz(complete_path);
		
		inst.imprime_tempo_exec();
		inst.imprime_tempo_prep();
		
		Solution sol;
		MovimentoSA2 sa_i = new MovimentoSA2();

		
		sol = new Solution(inst);
		sol.ConstroiSolution();
		
		sol.print_solution();
		
		//System.out.println(sa_i.insert_intra(sol));
		sa_i.insert_extra(sol).print_solution();
		

	}

}
