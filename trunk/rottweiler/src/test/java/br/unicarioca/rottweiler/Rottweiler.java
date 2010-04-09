package br.unicarioca.rottweiler;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

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
		selenium.selectFrame("orkutFrame");
		selenium.click("link=Amigos");
		selenium.waitForPageToLoad("30000");
		String ln[] = selenium.getAllLinks();
		for (int i = 0; i < ln.length; i++) {
			System.out.println("link -> '" + ln[i] + "'");
		}
		//System.out.println(selenium.getHtmlSource());
	}
}
