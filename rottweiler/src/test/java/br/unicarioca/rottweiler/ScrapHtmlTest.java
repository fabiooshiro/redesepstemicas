package br.unicarioca.rottweiler;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class ScrapHtmlTest extends TestCase{

	private String format(Date date){
		return ScrapHtml.Meses.sdf.format(date);
	}
	public void testFindAll() throws Exception{
		URL url = this.getClass().getResource("scraps01.html");
		if(url==null) throw new Exception("html de teste nao encontrado");
		File file = new File(url.getPath().replace("%20", " "));
		String codHtml = FileUtils.readFileToString(file,"UTF-8");
		List<Scrap> list = ScrapHtml.findAll(codHtml);
		assertEquals(10,list.size());
		assertEquals("2594366226397098408", list.get(0).getFrom().getUid());
		assertEquals(format(new Date()), format(list.get(0).getDataHora()));
		
		assertEquals("12133772708258648660", list.get(1).getFrom().getUid());
		assertEquals("03/05/2010", format(list.get(1).getDataHora()));
		
		assertEquals("15198893338852436548", list.get(2).getFrom().getUid());
		assertEquals("02/05/2010", format(list.get(2).getDataHora()));
		
		assertEquals("8719389658750091967", list.get(3).getFrom().getUid());
		assertEquals("27/04/2010", format(list.get(3).getDataHora()));
		
		assertEquals("1825134869268405666", list.get(4).getFrom().getUid());
		assertEquals("26/04/2010", format(list.get(4).getDataHora()));
		
		assertEquals("15637631777280531529", list.get(5).getFrom().getUid());
		assertEquals("26/04/2010", format(list.get(5).getDataHora()));
		
		assertEquals("2058335722275642779", list.get(6).getFrom().getUid());
		assertEquals("24/04/2010", format(list.get(6).getDataHora()));
		
		assertEquals("3544608468598580715", list.get(7).getFrom().getUid());
		assertEquals("23/04/2010", format(list.get(7).getDataHora()));
		
		assertEquals("16037723748025191199", list.get(8).getFrom().getUid());
		assertEquals("20/04/2010", format(list.get(8).getDataHora()));
		
		assertEquals("12348729874444686142", list.get(9).getFrom().getUid());
		assertEquals("19/04/2010", format(list.get(9).getDataHora()));
	}
}
