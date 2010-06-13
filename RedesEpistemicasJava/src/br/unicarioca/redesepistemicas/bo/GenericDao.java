package br.unicarioca.redesepistemicas.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GenericDao<T> {
	public void salvar(T obj, String nome) throws IOException{
		String pasta = obj.getClass().getSimpleName();
		File dir = new File(pasta);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String data = sdf.format(new Date());
		if(!dir.exists()) dir.mkdir();
		File arq = new File(dir,data + "-" + nome);
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(arq);
			out = new ObjectOutputStream(fos);
			out.writeObject(obj);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public T getLast(Class<?> clazz){
		String pasta = clazz.getSimpleName();
		File dir = new File(pasta);
		if(!dir.exists()){
			return null;
		}
		ArrayList<String> lista = new ArrayList<String>();
		String list[] = dir.list();
		if(list==null || list.length==0){
			return null;
		}
		for(int i=0;i<list.length;i++){
			lista.add(list[i]);
		}
		Collections.sort(lista);
		String fileName = lista.get(lista.size()-1);
		
		File file = new File(dir,fileName);
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			return (T) in.readObject();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
