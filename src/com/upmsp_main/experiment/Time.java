/*
 * Classe criada para controlar os tempos de execução dos algorítmos e execuções
 * dos experimentos para análises posteriores
 * 
 * Classe criada em 8 de Maio de 2019
 * 
 * @autor Cesar
 * 
 * */

package com.upmsp_main.experiment;

import java.util.ArrayList;

public class Time {
	
	private ArrayList<Long> tempos;
	
	public Time() {
		this.tempos = new ArrayList<Long>();
	}

	public ArrayList<Long> getTempos() {
		return tempos;
	}

	public void setTempo(long tempo) {
		this.tempos.add(tempo);
	}
	
	public long SomaTempos() {
		long soma = 0;
		for(int i = 0; i < this.tempos.size();i++) {
			soma += this.tempos.get(i);
		}
		return soma;
	}
	
	public double getMediaTime() {
		double media = (double)this.SomaTempos()/this.tempos.size();
		return media / 1000; //transformação para segundos
	}

}
