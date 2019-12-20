/*
 * Classe criada para calcular diferenças relativas entre resultados
 * diferentes, também chamado de gap. Neste trabalho calcula-se o RPD e também
 * o RPDavg que seria a média do RPD de todas as execuções.
 * 
 * @autor Cesar
 * 
 * Classe criada em um domingo em Tapira dia 12 de Maio de 2019 / dia das mães
 * 
 * */
package com.upmsp_main.util;

public class RPD {
	
	public static double rpd_i(double v_alg, double v_lit){
		return (double)(v_alg - v_lit)/v_lit; //calcula o gap
	}
	
	public static double rpdi_avg() {
		return 0;
	}

}
