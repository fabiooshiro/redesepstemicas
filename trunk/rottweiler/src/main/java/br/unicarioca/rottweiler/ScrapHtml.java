package br.unicarioca.rottweiler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Responsavel por extrair os objetos scraps do HTML
 * @author Fabio Issamu Oshiro
 */
public class ScrapHtml {
	private static final Logger logger = Logger.getLogger(ScrapHtml.class);
	public final static String END_SCRAP = "<div class=\"listdivi\"></div>";
	public final static String SEPARADOR="<div class=\"listitemchk\">"; 
	public final static String INI_DATE="<span class=\"rfdte\">";
	public final static String END_DATE="</span>";
	protected static class Meses{
		static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		static String meses[]={"jan","fev","mar","abr","mai","jun","jul","ago","set","out","nov","dez"};
		static String getNumber(String mes){
			for (int i = 0; i < meses.length; i++) {
				if(meses[i].equals(mes)){
					if(i>=9) return (i+1)+"";
					return "0"+(i+1);
				}
			}
			return null;
		}
	}
	protected static Date parseDate(String codHtml) throws Exception{
		//Exemplos
		// 21:54 (0 minutos atrás)
		// 10:36 (11 horas atrás)
		// 4 mai (1 dia atrás)
		int ini = codHtml.indexOf("<span class=\"rfdte\">");
		if(ini!=-1){
			ini+=INI_DATE.length();
			int fim =  codHtml.indexOf(END_DATE,ini);
			int fimParenteses = codHtml.indexOf("(",ini);
			fim = Math.min(fim, fimParenteses);
			String htmlData = codHtml.substring(ini,fim).replace("\n"," ").replace("\r","").trim();
			System.out.println("'"+htmlData+"'");
			if(htmlData.contains(":")){
				//hoje mesmo?
				return Meses.sdf.parse(Meses.sdf.format(new Date()));//soh pra tirar a hora
			}else{
				//tratar o formato
				if(htmlData.contains("/")){
					String partes[] = htmlData.split("/");
					return Meses.sdf.parse(partes[0]+"/"+partes[1]+"/20"+partes[2]);
				}else{
					String partes[] = htmlData.split(" ");
					return Meses.sdf.parse(partes[0]+"/"+Meses.getNumber(partes[1])+"/2010");
				}
			}
		}
		return null;
	}
	
	public static List<Scrap> findAll(String codHtml){
		List<Scrap> retorno = new ArrayList<Scrap>();
		
		String scrapsHtml[]=codHtml.split(SEPARADOR);
		for (int i = 1; i < scrapsHtml.length; i++) {
			int fim = scrapsHtml[i].indexOf(END_SCRAP);
			String scrapHtml = scrapsHtml[i].substring(0,fim);
			//logger.debug(scrapHtml);
			
			LinkProfile link = LinkProfile.findFirst(scrapHtml);
			Scrap scrap = new Scrap();
			Profile profile = new Profile();
			profile.setUid(link.getUid());
			profile.setNome(link.getNome());
			scrap.setFrom(profile);
			try{
				scrap.setDataHora(parseDate(scrapHtml));
			}catch(Exception e){
				logger.error("Erro ao recuperar data de " + scrapHtml,e);
			}
			retorno.add(scrap);
		}
		return retorno;
	}
}
