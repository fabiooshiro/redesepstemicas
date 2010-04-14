package br.unicarioca.rottweiler;

import java.util.List;

import com.thoughtworks.selenium.SeleneseTestCase;

public class RottweilerTest extends SeleneseTestCase {
	public static void main(String[] args) {
		try {
			RottweilerTest rott = new RottweilerTest();
			rott.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setUp() throws Exception {
		setUp("http://www.orkut.com.br/", "*chrome");
	}

	public void testTeste() throws Exception {
		selenium.open("/Main#Home");
		selenium.windowMaximize();
		System.out.println("Abra o navegador e digite a sua senha");
		Thread.sleep(15000);
		
		//listando os amigos
		selenium.selectFrame("orkutFrame");
		selenium.click("link=Amigos");
		selenium.waitForPageToLoad("30000");

		List<LinkProfile> listLinkProfile=LinkProfile.findAll(selenium.getHtmlSource());
		for (LinkProfile linkProfile : listLinkProfile) {
			System.out.println("nome -> " + linkProfile.getNome());
			System.out.println("uid -> " + linkProfile.getUid());
			System.out.println("url -> " + linkProfile.getUrl());
			System.out.println();
			selenium.open(linkProfile.getUrl());
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
			/*
			 * Lista de amigos primeira pagina
			 * /Main#FriendsList?uid=188435169541448637&rl=fpc
			 */
			selenium.open("/Main#FriendsList?uid="+linkProfile.getUid()+"&rl=fpc");
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
			/*
			 * Pagina de recados
			 * http://www.orkut.com.br/Main#Scrapbook?rl=lo&uid=12235031985893526985
			 */
			selenium.open("/Main#Scrapbook?rl=lo&uid="+linkProfile.getUid());
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
			/*
			 * Comunidades
			 * http://www.orkut.com.br/Main#ProfileC?uid=188435169541448637&rl=cpc
			 */
			selenium.open("/Main#ProfileC?uid="+linkProfile.getUid()+"&rl=cpc");
			selenium.waitForPageToLoad("30000");
			Thread.sleep(5000);
		}
	}
}
