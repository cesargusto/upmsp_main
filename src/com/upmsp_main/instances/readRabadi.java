/**
 *
 * Esta classe é uma classe filha responsável por ler um tipo de instância
 *  do autor Rabadi 2006 e 2013 
 * 
 **/

package com.upmsp_main.instances;

import java.io.IOException;
import java.util.StringTokenizer;

public class readRabadi extends ReadInstances{

	public readRabadi(String file_name) throws IOException {
		super(file_name);
		br.readLine();
		String linha = br.readLine();
		this.tk = new StringTokenizer(linha, " ");
		this.n_maqs = Integer.parseInt(tk.nextToken());
		linha = br.readLine();
		this.tk = new StringTokenizer(linha, " ");
		this.n_jobs = Integer.parseInt(tk.nextToken());
		this.tempo_exec = new int[n_maqs][n_jobs];
		this.tempo_prep = new int[n_jobs*n_maqs][n_jobs];
		this.ler(file_name);
	}

	@Override
	public void ler(String nome) throws IOException {
		
		String linha = ""; 
		
		while(linha.isEmpty())
			linha = br.readLine();
		
		for(int i = 0;i < n_jobs;i++){	

			this.tk = new StringTokenizer(linha, " ");
			
			for(int j = 0;j < n_maqs; j++)
				tempo_exec[j][i] = Integer.parseInt(tk.nextToken());
			
			linha = br.readLine();
		}
		
		for(int i = 0;i < n_jobs*n_maqs;i++){
			linha = br.readLine();
			if(!linha.isEmpty()) {
				this.tk = new StringTokenizer(linha, " ");
			
				for(int j = 0;j < n_jobs;j++){
					tempo_prep[i][j] = Integer.parseInt(tk.nextToken());
				}
			}
			else i--;
		}
	}

	@Override
	public int getT_exec(int n_maq, int n_job) {
		return this.tempo_exec[n_maq][n_job];
	}

	@Override
	public int getT_prep(int i_maq, int i_job_ant, int i_job) {
		i_job_ant = i_maq * this.n_jobs + i_job_ant;
		return this.tempo_prep[i_job_ant][i_job];
	}

	@Override
	public int getT_prep(int i_maq, int i_job_ant, int i_maq2, int i_job) {
		// TODO Auto-generated method stub
		return 0;
	}


}
