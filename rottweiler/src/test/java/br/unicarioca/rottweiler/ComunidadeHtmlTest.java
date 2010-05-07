package br.unicarioca.rottweiler;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

/**
 * 
 * @author Fabio Issamu Oshiro
 *
 */
public class ComunidadeHtmlTest extends TestCase{

	public void testFindAll() throws Exception{
		URL url = this.getClass().getResource("comunidades01.html");
		if(url==null) throw new Exception("html de teste nao encontrado");
		File file = new File(url.getPath().replace("%20", " "));
		String codHtml = FileUtils.readFileToString(file,"UTF-8");
		List<Comunidade> list = ComunidadeHtml.findAll(codHtml);
		assertEquals(10,list.size());
		assertEquals("139", list.get(0).getCid());
		assertEquals("6252", list.get(1).getCid());
		assertEquals("7833", list.get(2).getCid());
		assertEquals("17429", list.get(3).getCid());
		assertEquals("19610", list.get(4).getCid());
		assertEquals("19706", list.get(5).getCid());
		assertEquals("23641", list.get(6).getCid());
		assertEquals("65071", list.get(7).getCid());
		assertEquals("70567", list.get(8).getCid());
		assertEquals("82870", list.get(9).getCid());
	}
}
