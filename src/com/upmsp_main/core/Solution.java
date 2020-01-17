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
import java.util.Random;

import com.upmsp_main.instances.ReadInstances;
//import com.upmsp.localsearch.LocalSearch;

public class Solution implements Cloneable{
	
	private ReadInstances arquivo;
	private List<Machine> solucao;
	
	public Solution(ReadInstances arq){
		this.arquivo = arq;
		this.solucao = new ArrayList<>();
	}
	public Solution(Machine maq){
		this.solucao = new ArrayList<>();
		this.solucao.add(maq);
	}
	
	public Solution(){
		this.solucao = new ArrayList<>();
	}
	
	@Override
	public Solution clone() throws CloneNotSupportedException {
		Solution solCp = new Solution(arquivo);
		int tam = solucao.size();
		for(int i = 0;i < tam;i++){
			solCp.solucao.add(new Machine());
			int tam_mq = this.solucao.get(i).getSizeMaq();
			for(int j = 0;j < tam_mq;j++){
				int elemento = this.solucao.get(i).getJob(j);
				solCp.getMaq(i).addJobToMaq(elemento);
			}
		}
		return solCp;
	}
	
	
	/*
	 * Função refatorada para melhorar performance
	 * Refatoração feita dia 15 de Jan de 2020
	 * @author Cesar
	 * 
	 * */
	/*@Override
	public Solution clone() throws CloneNotSupportedException {
		Solution solCp = new Solution(arquivo);
		int tam = solucao.size();
		for (Machine machine : solucao) {
			solCp.solucao.add(new Machine());
			solucao.forEach();
		}
			for(int j = 0;j < tam_mq;j++){
				int elemento = this.solucao.get(i).getJob(j);
				solCp.getMaq(i).addJobToMaq(elemento);
			}
		}
		return solCp;
	}*/
	
	public void construction_greedy(){
		
		int num_maq = arquivo.getN_maqs();
		int num_jobs = arquivo.getN_jobs();
		int value = Integer.MAX_VALUE;
		int position_maq = -1;
		int position_job = -1;
		int aux = Integer.MAX_VALUE;
		
		for(int x = 0;x < num_maq;x++){
			this.setMaqSolucao(new Machine());
		}
		
		for(int i = 0;i < num_jobs;i++){
			for(int j = 0;j < num_maq;j++){
				aux = arquivo.getT_exec(j, i);
				if(aux < value){
					value = aux;
					position_maq = j;
					position_job = i;
				}
			}
			this.solucao.get(position_maq).addJobToMaq(position_job);
			aux = Integer.MAX_VALUE;
			value = Integer.MAX_VALUE;
		}
	}
	
	public void ConstroiSolution(){
		List<Integer> candidatos = new ArrayList<>(arquivo.getN_jobs());
		for(int i = 0;i < arquivo.getN_jobs();i++){
			candidatos.add(i);
		}
		for(int x = 0;x < arquivo.getN_maqs();x++){
			this.setMaqSolucao(new Machine());
		}
		
		int k = 0;
		while(k < arquivo.getN_jobs()){
			for(int j = 0;j < arquivo.getN_maqs();j++){
				this.solucao.get(j).addJobToMaq(candidatos.get(k));
				k++;
				if(k >= arquivo.getN_jobs())
					break;
			} 
		}
	}
	
	public int makespan(){
		int mspan = 0;
		List<Integer>mspans = new ArrayList<>(arquivo.getN_maqs());
		
		for(int t = 0;t < arquivo.getN_maqs();t++)
			mspans.add(this.getMaq(t).tempoMaq(arquivo, t));

		mspan = Collections.max(mspans);
		
		return mspan;
	}
	
	public int indice_makespan(){
		int makespan = this.makespan();
		int index = -1;
		if(Tempos().contains(makespan))
			index = Tempos().indexOf(makespan);

		return index;
	}
	
	public List<Integer> Tempos(){
		
		List<Integer>tempos = new ArrayList<>(arquivo.getN_maqs());
		
		for(int t = 0;t < arquivo.getN_maqs();t++){
			tempos.add(this.getMaq(t).tempoMaq(arquivo, t));
		}
		
		return tempos;
	}
	/*
	 * Esta função recebe a lista de indices de maquinas alteradas pelo movimeto e a 
	 * lista de makespan anterior, o calculo é feito apenas nas máquinas alteradas
	 * e as duas listas são mescladas e retornadas. Com isso o makespan não é recalculado
	 * completamente, Esta mesma ideia deverá ser aplicada de forma análoga no calculo de 
	 * tempo da máquina
	 * 
	 */
	public List<Integer> makespan(List<Integer>tempos, List<Integer> alter_maqs){
		int mspan = 0;
		List<Integer>mspans = tempos;
		for(int t = 0;t < alter_maqs.size();t++){
			mspans.set(alter_maqs.get(t), this.getMaq(alter_maqs.get(t)).tempoMaq(arquivo, alter_maqs.get(t)));
		}
		//mspan = Collections.max(mspans);
		return mspans;
	}
	
	public List<Integer> maior_menor(){
		List<Integer> maior_menor = new ArrayList<>();
		int indice_menor = -1;
		int indice_maior = -1;
		int valor_menor = Integer.MAX_VALUE;
		int valor_maior = Integer.MIN_VALUE;
		int aux = 0;
		
		for(int i = 0;i < arquivo.getN_maqs();i++){
			aux = this.getMaq(i).tempoMaq(arquivo, i);
			if(aux < valor_menor){
				valor_menor = aux;
				indice_menor = i;
				if(valor_menor > valor_maior){
					valor_maior = valor_menor;
					indice_maior = i;
					}
			}
			if(aux > valor_maior){
				valor_maior = aux;
				indice_maior = i;
				if(valor_maior < valor_menor){
					valor_menor = valor_maior;
					indice_maior = i;
				}
			}
		}
		maior_menor.add(indice_menor);
		maior_menor.add(valor_menor);
		maior_menor.add(indice_maior);
		maior_menor.add(valor_maior);
		
		return maior_menor;
	}
	/**
	 * 
	 * Métodos: verifica_nulidade e corrige_nulidade
	 * implementados em: 5/11/17
	 * Objetivos: Verificar e corrigir problemas de máquinas sem alocação de tarefas.
	 *  
	 */
	public void verifica_nulidade(){
		for(int t = 0;t < arquivo.getN_maqs();t++){
			if(this.getMaq(t).getSizeMaq() == 0){
				this.corrige_nulidade(t);
			}
		}
	}
	
	public void corrige_nulidade(int indice){
		Random rnd = new Random();
		int indice_mkspan = this.maior_menor().get(2);
		int size_mkspan = this.getMaq(indice_mkspan).getSizeMaq();
		int pos = rnd.nextInt(size_mkspan);
		this.getMaq(indice).addJobToMaq(this.getMaq(indice_mkspan).getJob(pos));
		this.getMaq(indice_mkspan).removeJobToMaq(pos);
	}
	
	public List<Machine> getSolucao() {
		return solucao;
	}
	
	public int getSizeSol(){
		return this.solucao.size();
	}
	
	public Machine getMaq(int i) {
		return solucao.get(i);
	}
	
	public void setMaqSolucao(Machine maq) {
		this.solucao.add(maq);
	}
	
    public void swap_Machine(int i, int j){
    	Collections.swap(this.solucao, i, j);
    }
	
	public ReadInstances getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(ReadInstances arquivo) {
		this.arquivo = arquivo;
	}
	
	public int get_T_exec(int i_maq, int i_job){
		return this.arquivo.getT_exec(i_maq, this.getMaq(i_maq).getJob(i_job));
	}
	public int get_T_prep(int i_maq, int i_job_ant, int i_job){
		return this.arquivo.getT_prep(i_maq, this.getMaq(i_maq).getJob(i_job_ant), this.getMaq(i_maq).getJob(i_job));
	}
	
	public int get_T_prep(int i_maq, int i_job_ant, int i_maq2,int i_job){
		return this.arquivo.getT_prep(i_maq, this.getMaq(i_maq).getJob(i_job_ant), this.getMaq(i_maq2).getJob(i_job));
	}
	
	public int get_T_prep(int i_maq1, int i_job_ant, int i_maq2, int i_job, int maq_calc){
		return this.arquivo.getT_prep(maq_calc, this.getMaq(i_maq1).getJob(i_job_ant), this.getMaq(i_maq2).getJob(i_job));
	}
	
	public void print_solution(){
		System.out.println();
		for(int x = 0;x < arquivo.getN_maqs();x++){
			System.out.printf("Maq-%d\tTempo: %d\n",
					x,this.solucao.get(x).tempoMaq(arquivo, x));
			for(int y = 0;
					y < this.getMaq(x).getSizeMaq();
					y++){
				System.out.printf("%d ",this.solucao.get(x).getJob(y));
			}
			//System.out.printf("\nTempo:\t%d \n",this.solucao.get(x).tempoMaq(arquivo, x));
			System.out.println();
		}
		System.out.println("Makespan .........: "+this.makespan());
		System.out.println();
	}
	
	public void print_makespan(){
		System.out.println("Makespan .........: "+this.makespan());
	}
	

}
