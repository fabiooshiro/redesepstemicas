package br.unicarioca.rottweiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ComunidadeHtml {
	private static final Logger logger = Logger.getLogger(ScrapHtml.class);
	public final static String END_COMUNIDADE = "<div class=\"listdivi\"></div>";
	public final static String SEPARADOR="<div class=\"listitem\">"; 
	private final static Pattern pat = Pattern.compile("<a\\s+href=\"(/Main#Community\\?cmm=([0-9]*))\">(.*?)</a>",Pattern.DOTALL);

	//private final static Pattern pat = Pattern.compile("<a\\s+href=\"/Main#Community\\?cmm=([0-9]*)\">(*.?)</a>");
	
	private static Comunidade parseLink(Matcher mat) {
		Comunidade comunidade = new Comunidade();
		comunidade.setNome(mat.group(3));
		comunidade.setCid(mat.group(2));
		if(comunidade.getNome().startsWith("<")){
			//nao pode comecar com <
			return null;
		}else{
			return comunidade;
		}
	}
	private static Comunidade parseHtml(String codHtml){
		String INI_TIT = "<h3 class=\"smller\">";
		String END_TIT = "</h3>";
		int ini = codHtml.indexOf(INI_TIT);
		if(ini!=-1){
			ini+=INI_TIT.length();
			int fim = codHtml.indexOf(END_TIT,ini);
			if(fim!=-1){
				Matcher mat = pat.matcher(codHtml.substring(ini,fim));
				if(mat.find()){
					return parseLink(mat);
				}else{
					throw new RuntimeException("Comunidade nao encontrada em '" + codHtml.substring(ini,fim) + "'");
				}
			}
		}
		return null;
	}
	/**
	 * <a  href="/Main#Community?cmm=139">
	 * @param codHtml
	 * @return
	 */
	public static List<Comunidade> findAll(String codHtml){
		List<Comunidade> retorno = new ArrayList<Comunidade>();
		
		String comunidadesHtml[]=codHtml.split(SEPARADOR);
		for (int i = 1; i < comunidadesHtml.length; i++) {
			int fim = comunidadesHtml[i].indexOf(END_COMUNIDADE);
			String comunidadeHtml = comunidadesHtml[i].substring(0,fim);
			logger.debug(comunidadeHtml);
			Comunidade comunidade = parseHtml(comunidadeHtml);
			retorno.add(comunidade);
		}
		return retorno;
	}
}
