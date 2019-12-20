/*******************************************************
 * Esta classe armazena melhores soluções e caminhos de convergência
 * para posterior gravação e análise.
 * 
 * Aterações de ampliação da classe feita em 23 de out 2017 as 23:40
 * @author cesar
 * 
 */
package com.upmsp_main.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import com.upmsp_main.core.Solution;
import com.upmsp_main.util.Calcs;

public class BestResults {
	
	private ArrayList<Integer> makespan_list; //curva de convergencia
	private ArrayList<Integer> best_list;	//Melhor de cada execução
	private Solution best_solution;			//Melhor solução
	private ArrayList<Solution> elite_set;	//conjunto de melhores soluções
	private Calcs calc;
	private Properties prop;
	private int size_ts;
	private int n_execs;
	private ArrayList<ArrayList<Integer>> tabela_t;
	private ArrayList<Integer> value_t;
	private ArrayList<Double> mediaValuesT;
	
	public BestResults() {
		this.makespan_list = new ArrayList<>();
		this.best_list = new ArrayList<>();
		this.best_solution = new Solution();
		this.elite_set = new ArrayList<Solution>();
		this.calc = new Calcs();
	}
	
	public BestResults(Properties prop) {
		this.makespan_list = new ArrayList<>();
		this.best_list = new ArrayList<>();
		this.best_solution = new Solution();
		this.elite_set = new ArrayList<Solution>();
		this.calc = new Calcs();
		this.prop = prop;
		String ts = this.prop.getProperty("VALUES_T");
		String[] t_s = ts.split(",");
		this.size_ts = t_s.length;
		this.n_execs = Integer.parseInt(this.prop.getProperty("N_EXEC"));
		this.value_t = new ArrayList<>(size_ts);
		this.tabela_t = new ArrayList<>(n_execs);
		this.mediaValuesT = new ArrayList<>(size_ts);
	}

	public ArrayList<Integer> getMakespan_list() {
		return makespan_list;
	}
	
	public Integer getElement_list(int i) {
		return makespan_list.get(i);
	}
	
	public int getSize_list() {
		return makespan_list.size();
	}

	public void setMakespan_list(int makespan) {
		this.makespan_list.add(makespan);
	}

	public int getSize_best_list() {
		return best_list.size();
	}
	
	public Integer getElement_Best_list(int i) {
		return best_list.get(i);
	}
	
	public ArrayList<Integer> getBest_list() {
		return best_list;
	}
	
	public Integer getBest_mkpan(int i) {
		return best_list.get(i);
	}

	public void setBest_list(long fo_solucao_linha) {
		this.best_list.add((int) fo_solucao_linha);
	}

	public Solution getBest_solution() {
		return best_solution;
	}

	public void setBest_solution(Solution best_solution) {
		this.best_solution = best_solution;
	}

	public ArrayList<Solution> getElite_set() {
		return elite_set;
	}
	
	public Solution getSolutionElite_set(int i) {
		return elite_set.get(i);
	}

	public void setElite_set(Solution solution) {
		this.elite_set.add(solution);
	}
	
	public int getMelhor() {
		return Collections.min(this.getBest_list());
	}
	
	public int getPior() {
		return Collections.max(this.getBest_list());
	}
	
	public double getMedia() {
		return this.calc.media(this.getBest_list());
	}
	public double getMediana() {
		return this.calc.mediana(this.getBest_list());
	}
	
	public void setValueT(int v) {
		this.value_t.add(v);
	}
	
	public ArrayList<Integer> getValueT(){
		return this.value_t;
	}
	
	public void setTabelaT(ArrayList<Integer> t) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> temp = (ArrayList<Integer>) t.clone();
		this.tabela_t.add(temp);
	}
	
	public ArrayList<ArrayList<Integer>> getTabelaT(){
		return this.tabela_t;
	}
	
	public void clean_valueT() {
		this.value_t.clear();
	}
	
	public ArrayList<Integer> soma_valuesT(){
		ArrayList<Integer>soma = new ArrayList<>();
		int temp = 0;
		for (int i = 0; i < this.tabela_t.size(); i++) {
			for (int j = 0; j < this.size_ts; j++) {
				if(soma.size() < this.size_ts) {
					soma.add(this.tabela_t.get(i).get(j));
				}
				else {
					temp = this.tabela_t.get(i).get(j);
					temp = temp + soma.get(j);
					soma.set(j, temp);
				}
			}
		}
		//this.mediaValuesT.add(null);
		return soma;
	}
	
	public void calc_media_valuesT(ArrayList<Integer> values){
		ArrayList<Double> media = new ArrayList<>(values.size());
		for (int i = 0; i < values.size(); i++) {
			media.add((double)values.get(i)/this.n_execs);
		}
		this.setMediaValuesT(media);
	}
	
	public void setMediaValuesT(ArrayList<Double> media) {
		this.mediaValuesT = media;
	}
	
	public double getMediaValuesT(int index) {
		return this.mediaValuesT.get(index);
	}
	

	
}
 