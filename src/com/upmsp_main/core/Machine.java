/**
 * Classe refatorada de ArrayList para List
 * Alteração feita dia 04 de Jan de 2020
 *
 * @author cesar
 * 
 * **/

package com.upmsp_main.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.upmsp_main.instances.ReadInstances;

public class Machine {
	private List<Integer> machine;
	
	public Machine(){
		this.machine = new ArrayList<Integer>();
	}

	public int tempoMaq(ReadInstances arq, int nummaq){
		int tempo_total = 0;
		int tempo_prep = 0;
		int tempo_exec = 0;
		for(int t = 0;t < this.machine.size();t++){
			if(t!=0){
				tempo_prep = arq.getT_prep(nummaq, this.getJob(t - 1), this.getJob(t));
			}
			tempo_exec = arq.getT_exec(nummaq, this.getJob(t));
			tempo_total = tempo_total + tempo_prep + tempo_exec;
		}
		return tempo_total;
	}
	
	public List<List<Integer>> tempos_Maq(ReadInstances arq, int nummaq){
		
		List<List<Integer>> tempos = new ArrayList<>(2);
		
		List<Integer> tempos_exec = new ArrayList<>();
		List<Integer> tempos_prep = new ArrayList<>();
		
		int tempo_prep = 0;
		int tempo_exec = 0;
		
		for(int t = 0;t < this.machine.size();t++){
			if(t!=0){
				tempo_prep = arq.getT_prep(nummaq, this.getJob(t - 1), this.getJob(t));
			}
			tempo_exec = arq.getT_exec(nummaq, this.getJob(t));
			tempos_exec.add(tempo_prep);
			tempos_prep.add(tempo_exec);
			
		}
		tempos.add(tempos_prep);
		tempos.add(tempos_exec);
		
		return tempos;
	}
	
	public int tempoMaq(ReadInstances arq, int nummaq, ArrayList<ArrayList<Integer>> tempos, ArrayList<Integer>pos_alter, int ms_atual){
		int diferenca_prep = 0;
		int diferenca_exec = 0;
		
		int tempo_prep = 0;
		int tempo_exec = 0;
		
		for(int t = 0;t < pos_alter.size();t++){

			if(pos_alter.get(t)!=0){
				tempo_prep = arq.getT_prep(nummaq, this.getJob(pos_alter.get(t) - 1), this.getJob(pos_alter.get(t)));
			}			
			
			tempo_exec = arq.getT_exec(nummaq, this.getJob(pos_alter.get(t)));
			
			diferenca_prep = diferenca_prep + (tempo_prep - tempos.get(1).get(pos_alter.get(t)));
			diferenca_exec = diferenca_exec + (tempo_exec - tempos.get(0).get(pos_alter.get(t)));
			
			ms_atual = ms_atual + diferenca_prep + diferenca_exec;
			
		}
		return ms_atual;
	}
	public int soma_arrays_makespan(ArrayList<ArrayList<Integer>> tempos) {
		int makespan = 0;
		for(int i = 0;i < tempos.size(); i++) {
			for(int j = 0; j < tempos.get(i).size();j++) {
				makespan = makespan + tempos.get(i).get(j);
			}
		}
		return makespan;
	}
	
	public int getSizeMaq() {
		return machine.size();
	}
	
	public List<Integer> getMaquina() {
		return machine;
	}

	public int getJob(int i){
		return this.machine.get(i);
	}

    public void addJobToMaq(Integer job) {
        this.machine.add(job);
    }
	
	public void setJobMaq(int pos, int job) {
		this.machine.add(pos, job);
	}
	
    public void setJobToMaq(int i, Integer job) {
        this.machine.set(i, job);
    }
    
    public void insertJobToMaq(int i, Integer job) {
        this.machine.add(i, job);
    }
	
    public void removeJobToMaq(int pos){
		this.machine.remove(pos);
	}
    public void removeLastJob() {
        this.machine.remove(this.machine.size() - 1);
    }

	public void trocaJob(int i, int j){
		Collections.swap(this.machine, i, j);
	}
}
