package br.unicarioca.rottweiler;

import java.util.List;

import com.thoughtworks.selenium.SeleneseTestCase;

public class Rottweiler extends SeleneseTestCase {
	public static void main(String[] args) {
		try {
			Rottweiler rott = new Rottweiler();
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
		}
		
	}
}
