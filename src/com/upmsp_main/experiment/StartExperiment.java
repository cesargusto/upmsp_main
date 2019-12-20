/***********************
 * Esta classe dá o inicio do experimento criando uma array
 * com todos os arquivos existentes em um diretorio passado e então
 * passando seus nomes para a função execute_experiment. A função start executa
 * um número n de vezes (executions_number) o algoritmo para todos os arquivos contidos no
 * diretorio especificado.
 * 
 * Não é necessario passar um Array contendo os arquivos e sim utilizar
 * uma forma de extrair direto só os nomes dos arquivos passando um Array
 * de String ao invés de um Array de Files.
 * 
 * Classe criada em: 19 de out 2017
 * @author cesar
 * 
********************************************************************/
package com.upmsp_main.experiment;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.upmsp_main.util.View;

public class StartExperiment {
	
	private Properties prop;
	private ConfExperiment conf;
	
	public StartExperiment() throws Exception{
		new ReadFileConf();
		this.prop = ReadFileConf.getProp();
		this.conf = new ConfExperiment(prop);
	}
	
	public void start() throws IOException, CloneNotSupportedException{
		
		long Start = System.currentTimeMillis();
		
		String path_1 = null, path_2= null;
		String m = prop.getProperty("INST_GROUP_RUIZ");
		
		if(m.equals("1")) {
			path_1 = prop.getProperty("INSTANCE_RUIZ_SMALL");
		}else if(m.equals("2")) {
			path_1 = prop.getProperty("INSTANCE_RUIZ_LARGE");
		}else if(m.equals("0")) {
			path_1 = prop.getProperty("INSTANCE_RUIZ_SMALL");
			path_2 = prop.getProperty("INSTANCE_RUIZ_LARGE");
		}
		
		File[] name_list;
		
		if(path_2 == null){
			
			name_list = ReadDirFilesNames.leDir(path_1);//lista de arquivos contidos no diretorio
			
			for(int i = 0;i < name_list.length;i++) {
				View.title_3(name_list[i].getName());
				conf.execute_experiment(name_list[i].getName(), path_1);
			}
		}else {
			
			name_list = ReadDirFilesNames.leDir(path_1);//lista de arquivos contidos no diretorio
			
			for(int i = 0;i < name_list.length;i++) {
				View.title_3(name_list[i].getName());
				conf.execute_experiment(name_list[i].getName(), path_1);
			}
			
			name_list = ReadDirFilesNames.leDir(path_2);
			
			for(int i = 0;i < name_list.length;i++) {
				View.title_3(name_list[i].getName());
				conf.execute_experiment(name_list[i].getName(), path_2);
			}
		}
		
		long End = System.currentTimeMillis();
		long Time = End - Start;
		Time = Time / 1000;
		View.title_2("Tempo: "+Time+" segundos");
		
		WriteResultsFile write = new WriteResultsFile(prop);
		write.write_resume(Time, name_list.length);
	}
}
