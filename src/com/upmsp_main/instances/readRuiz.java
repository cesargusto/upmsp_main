/**
 *
 * Esta classe é uma classe filha responsável por ler um tipo de instância
 *  do autor Ruiz e Vallada 2011. 
 *  
 *  Refatorada para utilização de hashMap substituindo a estrutura vector, 
 *  alteração feita em 07 de Jan de 2020
 *  
 *  @author cesar
 * 
 **/

package com.upmsp_main.instances;

import java.io.IOException;
import java.util.StringTokenizer;

public class readRuiz extends ReadInstances{

	public readRuiz(String file_name) throws IOException {
		super(file_name);
		String linha = br.readLine();
		this.tk = new StringTokenizer(linha, " ");
		this.n_jobs = Integer.parseInt(tk.nextToken());
		this.n_maqs = Integer.parseInt(tk.nextToken());
		this.ler(file_name);
	}

	@Override
	public void ler(String nome) throws IOException{
		String linha;
		br.readLine();
		
		String key = "";
		int valor = -1;
		
		for(int i = 0;i < n_jobs;i++){	
			
			linha = br.readLine();
			this.tk = new StringTokenizer(linha, "\t");
			
			for(int j = 0;j < n_maqs; j++){
				tk.nextToken();
				key = "E_" + j + "_" + i;
				valor = Integer.parseInt(tk.nextToken());
				tempo_exec.put(key, valor);
			}
		}
		
		br.readLine();
		int counter = 0;
		for(int i = 0;i < n_jobs*n_maqs;i++){
			if(i == 0)
				br.readLine();
			if(counter == n_jobs){
				br.readLine();
				counter = 0;
			}
			
			linha = br.readLine();
			this.tk = new StringTokenizer(linha, "\t");
			
			for(int j = 0;j < n_jobs;j++){
				key = "P_" + i + "_" + j;
				valor = Integer.parseInt(tk.nextToken());
				tempo_prep.put(key, valor);
			}
			counter++;
		}	
	}

	@Override
	public int getT_exec(int n_maq, int n_job) {
		String key = "E_" + n_maq + "_" + n_job;
		return (int) this.tempo_exec.get(key);
	}

	@Override
	public int getT_prep(int i_maq, int i_job_ant, int i_job) {
		i_job_ant = i_maq * this.n_jobs + i_job_ant;
		String key = "P_" + i_job_ant + "_" + i_job;
		return (int) this.tempo_prep.get(key);
	}

	@Override
	public int getT_prep(int i_maq, int i_job_ant, int i_maq2, int i_job) {
		// TODO Auto-generated method stub
		return 0;
	}


}
