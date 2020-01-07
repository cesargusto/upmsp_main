/**
 * Esta classe foi criada em 23 de Maio de 2019 pra resolver o problema da
 * leitura de instancias diferentes sem a repetição desnecessária de código.
 * A classe ReadInstances é uma super classe e suas filhas serão responsaveis por
 * conter código específico atendenddo as peculiaridades de cada arquivo.
 * 
 * 
 * Esta classe foi refatorada em 7 de Jan de 2020, foi feita uma importante mudança
 * da estrutura Vector para HashMap com o objetivo de melhora perfórmance de busca e 
 * a thread Safe
 * 
 * 
 * @autor Cesar
 * 
 * */

package com.upmsp_main.instances;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class ReadInstances {

	protected FileInputStream  is;
	protected InputStreamReader isr;
	protected BufferedReader br;
	protected StringTokenizer tk;
	protected int n_jobs;
	protected int n_maqs;
	protected Map<String, Integer> tempo_exec;
	protected Map<String, Integer> tempo_prep;
	
	public ReadInstances(String file_name) throws IOException{
		
		this.is = new FileInputStream(file_name);
		this.isr = new InputStreamReader(this.is);
		this.br = new BufferedReader(this.isr);
		this.tempo_exec = new HashMap<String, Integer>();
		this.tempo_prep = new HashMap<String, Integer>();
	}
	
	/**
	 * Métodos abstratos que deverão ser implementados pelas classes filhas
	 * 
	 * */
	public abstract void ler(String nome) throws IOException;
	public abstract int getT_exec(int n_maq, int n_job);	
	public abstract int getT_prep(int i_maq, int i_job_ant, int i_job);
	public abstract int getT_prep(int i_maq, int i_job_ant, int i_maq2, int i_job);
	
	public int getN_jobs() {
		return n_jobs;
	}

	public int getN_maqs() {
		return n_maqs;
	}
	
	public Map<String, Integer> getMatriz_T_exec(){
		return this.tempo_exec;
	}
	
	public Map<String, Integer> getMatriz_T_prep(){
		return this.tempo_prep;
	}
	
	public void imprime_tempo_prep() {
		System.out.println();
		for(int i = 0;i < n_jobs*n_maqs;i++){
			for(int j = 0;j < n_jobs;j++){
				String key = "P_"+(i)+"_"+j;
				System.out.printf("%d ", tempo_prep.get(key));
			}
			System.out.println();
		}
	}
	
	public void imprime_tempo_exec() {
		System.out.println();
		for(int i = 0;i < n_maqs;i++){
			for(int j = 0;j < n_jobs;j++){
				String key = "E_"+i+"_"+j;
				System.out.printf("%d ", tempo_exec.get(key));
			}
			System.out.println();
		}	
	}
		
}
