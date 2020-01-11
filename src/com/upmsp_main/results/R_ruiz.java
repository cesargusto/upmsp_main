/*
 * 
 * Esta classe tem como função ler um arquivo de resultados da literatura e guardá-lo em uma
 * estrutura de dados para posterior comparação. Neste caso a função faz isso apenas para as instancias
 * de Vallada e Ruiz, mas será adaptada para ler outros arquivos caso tenham o mesmo formato.
 * Método concluído em 18 de Maio de 2018
 * 
 * @author Cesar
 * 
 */

package com.upmsp_main.results;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class R_ruiz {

	private Map<String, Integer> ruiz;
	
	public R_ruiz() {
		this.ruiz = new HashMap<>();
	}

	public void ler(String path_bound, String bound_delimit) throws IOException {

        InputStream is = new FileInputStream(path_bound);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String linha = null;
        StringTokenizer st;
        
        String inst_name = "";
        int valor = -1;
        
        while(br.ready()) {
        	linha = br.readLine();
        	st = new StringTokenizer(linha, bound_delimit);
        
        	inst_name = st.nextToken();
        	valor = Integer.parseInt(st.nextToken());
        	
        	ruiz.put(inst_name, valor);
		}
        
        br.close();
	}
	
	public int getValor(String name) {
		return ruiz.get(name);
	}
}
