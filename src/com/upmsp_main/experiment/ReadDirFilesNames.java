package com.upmsp_main.experiment;

import java.io.File;

public class ReadDirFilesNames {

	public static File[] leDir(String caminho) {
		File arquivos[];
		File diretorio = new File(caminho);
		arquivos = diretorio.listFiles();
		
		return arquivos;
	}
}
