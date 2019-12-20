/*
 * Classe criada para fazer cálculos específicos de estatística, tempos de
 * execução e etc.
 * 
 * Método de calculo de tempo de execução tempoExec criado em Tapira dia 12 de Mai de 2019
 *
 * */

package com.upmsp_main.util;

import java.util.ArrayList;
import java.util.Collections;

public class Calcs {

	public double media (ArrayList<Integer> vetor) {
		int soma = 0;
		for(int i = 0;i < vetor.size();i++) {
			soma +=vetor.get(i);
		}
		double x = (double)soma/vetor.size(); 
		return x;
		
	}
	
	public double mediana(ArrayList<Integer> vetor) {
		Collections.sort(vetor);
		
		int tipo = vetor.size() % 2;
		
		if(tipo == 1) {
			return vetor.get((vetor.size() + 1)/2 - 1);			
		}else {
			int m = vetor.size()/2;
			return (double)(vetor.get(m - 1) + vetor.get(m))/2;
		}
	}
	
	public long tempoExec(int[]shape, int c) {
		
		return shape[0] * (shape[1]/2) * c;
	
	}
}
