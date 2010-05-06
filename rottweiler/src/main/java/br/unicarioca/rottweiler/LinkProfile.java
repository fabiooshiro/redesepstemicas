package br.unicarioca.rottweiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsavel por extrair os links de profiles
 * @author Fabio Issamu Oshiro
 */
public class LinkProfile {
	private final static Pattern pat = Pattern.compile("<a\\s+href=\"(/Main#Profile\\?uid=([0-9]*))\">(.*?)</a>");
	private String nome;
	private String url;
	private String uid;

	public String getUid() {
		return uid;
	}
	
	public String getNome() {
		return nome;
	}

	public String getUrl() {
		return url;
	}

	public static List<LinkProfile> findAll(String html) {
		ArrayList<LinkProfile> retorno = new ArrayList<LinkProfile>();
		// <a href="/Main#Profile?uid=3471199689122026441">*.* Edwarf Sloft
		// *.*</a>
		Matcher mat = pat.matcher(html);
		while(mat.find()){
			LinkProfile link = parseLink(mat);
			if(link!=null) retorno.add(link);
		}
		return retorno;
	}

	/**
	 * @param retorno
	 * @param mat
	 */
	private static LinkProfile parseLink(Matcher mat) {
		LinkProfile link = new LinkProfile();
		link.nome = mat.group(3);
		link.uid = mat.group(2);
		link.url = mat.group(1);
		if(link.nome.startsWith("<")){
			//nao pode comecar com <
			return null;
		}else if(link.nome.equals("perfil")){
			//nao pode ser perfil
			return null;
		}else{
			return link;
		}
	}

	/**
	 * Encontra o primeiro link de profile
	 * @param html codigo
	 * @return primeiro link para um profile que nao comece com &lt; e seja diferente de "perfil"
	 */
	public static LinkProfile findFirst(String html) {
		Matcher mat = pat.matcher(html);
		if(mat.find()){
			return parseLink(mat);
		}
		return null;
	}
}
