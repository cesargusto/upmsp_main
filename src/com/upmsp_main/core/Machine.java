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
import java.util.LinkedList;

import com.upmsp_main.instances.ReadInstances;

public class Machine {
	private List<Integer> machine;
	private ReadInstances arquivo;
	private int tempo_maq;
	private int position;
	
	public Machine(){
		this.machine = new LinkedList<Integer>();
		this.tempo_maq = 0;
	}

	public Machine(ReadInstances arq, int position){
		this.machine = new ArrayList<Integer>();
		this.arquivo = arq;
		this.tempo_maq = 0;
		this.position = position;
	}
	
	public int getTempo_maq() {
		return tempo_maq;
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
		return (Integer)this.machine.get(i);
	}

    public void addJobToMaq(Integer job) {
        
    	this.machine.add(job);
    	
    	int t = this.machine.size();
        
        int tempo_prep = 0;
        int tempo_exec = 0;
        
        if(this.machine.size() > 1)
        	tempo_prep = this.arquivo.getT_prep(position, this.machine.get(t-2), this.machine.get(t-1));
        tempo_exec = this.arquivo.getT_exec(position, this.machine.get(t-1));
        
        this.tempo_maq = this.tempo_maq + tempo_prep + tempo_exec;  
    }
	
    //insere o valor na posição
	public void setJobMaq(int pos, int job) {
		this.machine.add(pos, job);
		
		int t_prep_antigo = 0;
		int t_prep_novo_antes = 0;
		int t_prep_novo_depois = 0;
        int tempo_exec = 0;
        
        int t = this.machine.size();
        if(pos > 0) {
        	t_prep_antigo = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos+1));
        	t_prep_novo_antes = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos));
        	if(pos == t - 1)
        		t_prep_novo_depois = 0;
        	else
        		t_prep_novo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }else {
        	t_prep_novo_antes = 0;
        	t_prep_novo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }
        tempo_exec = this.arquivo.getT_exec(position, this.machine.get(pos));
        
        this.tempo_maq = this.tempo_maq + (-1) * t_prep_antigo + 
        		t_prep_novo_antes + t_prep_novo_depois + tempo_exec;  
	}
	
	//substitui o valor - remove o antigo
    public void setJobToMaq(int i, Integer job) {

        this.machine.set(i, job);
    }
    
    //insere o valor na posição - igual setJobMaq
    public void insertJobToMaq(int pos, Integer job) {
        this.machine.add(pos, job);
        
        int t_prep_antigo = 0;
		int t_prep_novo_antes = 0;
		int t_prep_novo_depois = 0;
        int tempo_exec = 0;
        
        int t = this.machine.size();
        if(pos > 0) {
        	t_prep_antigo = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos+1));
        	t_prep_novo_antes = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos));
        	if(pos == t - 1)
        		t_prep_novo_depois = 0;
        	else
        		t_prep_novo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }else {
        	t_prep_novo_antes = 0;
        	t_prep_novo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }
        tempo_exec = this.arquivo.getT_exec(position, this.machine.get(pos));
        
        this.tempo_maq = this.tempo_maq + (-1) * t_prep_antigo + 
        		t_prep_novo_antes + t_prep_novo_depois + tempo_exec;
    }
	
    public void removeJobToMaq(int pos){
    	
    	int t_prep_antigo_antes = 0;
    	int t_prep_antigo_depois = 0;
    	int t_prep_novo = 0;
		
        int tempo_exec = 0;
        
        int t = this.machine.size();
        if(pos > 0) {
        	t_prep_antigo_antes = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos));
        	if(pos == t - 1) {
        		t_prep_novo = 0;
        		t_prep_antigo_depois = 0;
        		}
        	else {
        		t_prep_antigo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        		t_prep_novo = this.arquivo.getT_prep(position, this.machine.get(pos-1), this.machine.get(pos+1));	
        	}
        }else {
        	t_prep_novo = 0;
        	t_prep_antigo_antes = 0;
        	t_prep_antigo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }
        tempo_exec = this.arquivo.getT_exec(position, this.machine.get(pos));
        
        this.tempo_maq = this.tempo_maq + t_prep_novo + 
        		(-1) * t_prep_antigo_depois + (-1) * t_prep_antigo_antes + (-1) * tempo_exec; 
        
    	this.machine.remove(pos);
		
	}
    
    public void removeLastJob() {
        this.machine.remove(this.machine.size() - 1);
    }

	public void trocaJob(int i, int j){
		
		int t_antigo_antes_i = 0;
    	int t_antigo_depois_i = 0;
    	
    	int t_antigo_antes_j = 0;
    	int t_antigo_depois_j = 0;
    	
    	int t_novo_antes_i = 0;
    	int t_novo_depois_i = 0;
    	
    	int t_novo_antes_j = 0;
    	int t_novo_depois_j = 0;
    	
        int t = this.machine.size();
        
     // trata o envolvimento do primeiro elemento
		if (i == 0) {
			t_antigo_antes_i = 0; // setup do primeiro é 0
			t_novo_antes_i = 0; // setup depois da troca também
		} else {
			t_antigo_antes_i = s.get_T_prep(M, i - 1, i);
			t_novo_antes_i = s.get_T_prep(M, i - 1, j);
		}

		// trata o envolvimento do último elemento
		if (j + 1 == t) {
			t_antigo_depois_j = 0;
			t_novo_depois_i = 0;
		} else {
			t_antigo_depois_j = s.get_T_prep(M, j, j + 1);
			t_novo_depois_j = s.get_T_prep(M, i, j + 1);
		}

		// trata a consecutividade
		if (j - i == 1) {
			p_depois_i = 0;
			p_t_antes_j = 0;
			p_t_depois_i = s.get_T_prep(M, j, i);
			p_antes_j = s.get_T_prep(M, j - 1, j);
		}
		// trata a situação comum resultade
		else {
			p_depois_i = s.get_T_prep(M, i, i + 1);
			p_t_depois_i = s.get_T_prep(M, j, i + 1);
			p_antes_j = s.get_T_prep(M, j - 1, j);
			p_t_antes_j = s.get_T_prep(M, j - 1, i);
		}

		makespan_corrente = makespan_atual + 
				(-1) * p_antes_i + p_t_antes_i + 
				(-1) * p_depois_i+ p_t_depois_i + 
				(-1) * p_antes_j + p_t_antes_j + 
				(-1) * p_depois_j + p_t_depois_j;
        
        if(i > 0) {
        	t_antigo_antes_i = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos+1));
        	t_prep_novo_antes = this.arquivo.getT_prep(position, this.machine.get(pos - 1), this.machine.get(pos));
        	if(pos == t - 1)
        		t_prep_novo_depois = 0;
        	else
        		t_prep_novo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }else {
        	t_prep_novo_antes = 0;
        	t_prep_novo_depois = this.arquivo.getT_prep(position, this.machine.get(pos), this.machine.get(pos+1));
        }
        tempo_exec = this.arquivo.getT_exec(position, this.machine.get(pos));
        
        this.tempo_maq = this.tempo_maq + (-1) * t_prep_antigo + 
        		t_prep_novo_antes + t_prep_novo_depois + tempo_exec;
		Collections.swap(this.machine, i, j);
	}
}
