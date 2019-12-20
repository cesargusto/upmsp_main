/********************************************************************
 * 
 * Uma melhoria para esta classe é substituir a estrutura de dados de arrays
 * uni e bidimenssionais por uma estrutura do tipo Hash(chave/valor).
 * Esta modificação poderá fazer diferença para instâncias grandes.
 * Utilizar uma estrutura que seja safe thread.
 * 
 ********************************************************************/

package com.upmsp_main.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Instance {

	private FileInputStream  is;
	private InputStreamReader isr;
	private BufferedReader br;
	private StringTokenizer tk;
	private String linha;
	private int n_jobs;
	private int n_maqs;
	//private int n_desconhecido; //Valor existente na instancia mas desconhecida sua finalidade
	private int[][] tempo_exec;
	private int[][] tempo_prep;
	
	public Instance(String file_name) throws IOException{
		
		this.is = new FileInputStream(file_name);
		this.isr = new InputStreamReader(this.is);
		this.br = new BufferedReader(this.isr);
		this.linha = br.readLine();
		this.tk = new StringTokenizer(linha, " ");
		this.n_jobs = Integer.parseInt(tk.nextToken());
		this.n_maqs = Integer.parseInt(tk.nextToken());
		//this.n_desconhecido = Integer.parseInt(tk.nextToken());
		this.tempo_exec = new int[n_maqs][n_jobs];
		this.tempo_prep = new int[n_jobs*n_maqs][n_jobs];
		this.ler(file_name);
	}
	
	public void ler(String nome) throws IOException{
		br.readLine();
		
		for(int i = 0;i < n_jobs;i++){	
			
			this.linha = br.readLine();
			this.tk = new StringTokenizer(linha, "\t");
			
			for(int j = 0;j < n_maqs; j++){
				tk.nextToken();
				tempo_exec[j][i] = Integer.parseInt(tk.nextToken());
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
			
			this.linha = br.readLine();
			this.tk = new StringTokenizer(linha, "\t");
			
			for(int j = 0;j < n_jobs;j++){
				tempo_prep[i][j] = Integer.parseInt(tk.nextToken());
			}
			counter++;
		}
	}

	public int getN_jobs() {
		return n_jobs;
	}

	public int getN_maqs() {
		return n_maqs;
	}
	
	public int getT_exec(int n_maq, int n_job){
		return this.tempo_exec[n_maq][n_job];
	}
	
	public int getT_prep(int i_maq, int i_job_ant, int i_job){
		i_job_ant = i_maq * this.n_jobs + i_job_ant;
		return this.tempo_prep[i_job_ant][i_job];
	}
	
	public int[][] getMatriz_T_exec(){
		return this.tempo_exec;
	}
	
	public int[][] getMatriz_T_prep(){
		return this.tempo_prep;
	}

	public void imprime_tempo_exec(){
		//IMPRIME MATRIZ DE TEMPOS DE EXECUÇAO
		System.out.println();
		for(int i = 0;i < n_maqs;i++){
			for(int j = 0;j < n_jobs;j++){
				System.out.printf("%d ", tempo_exec[i][j]);
			}
			System.out.println();
		}
	}
	public void imprime_tempo_prep(){
		System.out.println();
		for(int i = 0;i < n_jobs*n_maqs;i++){
			for(int j = 0;j < n_jobs;j++){
				System.out.printf("%d ", tempo_prep[i][j]);
			}
			System.out.println();
		}
	}		
}
