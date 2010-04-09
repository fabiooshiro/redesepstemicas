package br.unicarioca.rottweiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkProfile {
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
		Pattern pat = Pattern.compile("<a href=\"(/Main#Profile\\?uid=([0-9]*))\">(.*?)</a>");

		Matcher mat = pat.matcher(html);
		while(mat.find()){
			LinkProfile link = new LinkProfile();
			link.nome = mat.group(3);
			link.uid = mat.group(2);
			link.url = mat.group(1);
			if(link.nome.startsWith("<")){
				//nao pode comecar com <
			}else if(link.nome.equals("perfil")){
				//nao pode ser perfil
			}else{
				retorno.add(link);
			}
		}
		return retorno;
	}
}
