package br.unicarioca.redesepistemicas.bo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import br.unicarioca.redesepistemicas.view.ConfiguracoesPanel;

/**
 * Responsavel por salvar o estado da aplicacao
 *
 */
public class SalvarSnapShoot {
	
	private boolean pastasVerificadas = false;
	private static SalvarSnapShoot instance;
	private static int snapshotN=0;
	private static int snapshotManN=0;
	private ConfiguracoesPanel configuracoesPanel;
	
	{
		criaIndex();
	}
	
	private SalvarSnapShoot(){
		pastasVerificadas = false;
	}
	
	public static SalvarSnapShoot getInstance() {
		if(instance==null){
			instance = new SalvarSnapShoot();
		}
		return instance;
	}
	
	/**
	 * Salva um screenshot
	 * @param bi
	 * @param n
	 */
	public void salvar(BufferedImage bi,long n) {
		try {
			snapshotN++;
			if(!pastasVerificadas){
				File dir = new File("screenshots/automatica");
				if(!dir.exists())dir.mkdir();
				dir = new File("screenshots/automatica/imagens");
				if(!dir.exists())dir.mkdir();
			}
			//salva o png
			ImageIO.write(bi, "png", new File("screenshots/automatica/imagens/f"+snapshotN+".png"));
			//salvar o html
			StringBuilder html=new StringBuilder();
			String configuracao = criarHtmlConf();
			html.append("<html><head><title>RedeEpistemica</title></head><body>");
			html.append("<div id=\"nav\">");
						
			if (snapshotN > 1)
				html.append("<a href=\"p").append(snapshotN-1).append(".html\">Anterior</a> - ");
			else
				html.append("Anterior - ");
			
			html.append("<a href=\"p").append(snapshotN+1).append(".html\">Pr&oacute;xima</a>");
			html.append("</div>");
			html.append("<img src=\"imagens/f").append(snapshotN).append(".png\">");
			html.append("<div id=\"conf\">");
			html.append("Passo: ").append(n).append("<br />");
			html.append(configuracao);
			html.append("</div>");
			html.append("</body></html>");
			FileUtils.writeStringToFile(new File("screenshots/automatica/p"+snapshotN+".html"), html.toString(), "iso-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvar(BufferedImage bi) {
		try {
			snapshotManN++;
			if(!pastasVerificadas){
				File dir = new File("screenshots/manual");
				if(!dir.exists())dir.mkdir();
				dir = new File("screenshots/manual/imagens");
				if(!dir.exists())dir.mkdir();
			}
			//salva o png
			ImageIO.write(bi, "png", new File("screenshots/manual/imagens/m"+snapshotManN+".png"));
			//salvar o html
			StringBuilder html=new StringBuilder();
			String configuracao = criarHtmlConf();
			html.append("<html><head><title>RedeEpistemica</title></head><body>");
			html.append("<div id=\"nav\">");
			
			if (snapshotManN > 1)
				html.append("<a href=\"p").append(snapshotManN-1).append(".html\">Anterior</a> - ");
			else
				html.append("Anterior - ");
			
			
			html.append("<a href=\"p").append(snapshotManN+1).append(".html\">Pr&oacute;xima</a>");
			html.append("</div>");
			html.append("<img src=\"imagens/m").append(snapshotManN).append(".png\">");
			html.append("<div id=\"conf\">");
			//html.append("Passo: ").append(n).append("<br />");
			html.append(configuracao);
			html.append("</div>");
			html.append("</body></html>");
			FileUtils.writeStringToFile(new File("screenshots/manual/p"+snapshotManN+".html"), html.toString(), "iso-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String criarHtmlConf() throws Exception{
		StringBuilder retorno=new StringBuilder();
		Method metodos[] = configuracoesPanel.getClass().getMethods();
		for(Method metodo : metodos){
			if(metodo.getName().startsWith("getTxt")){
				//pegar o label
				Method metodoLabel = configuracoesPanel.getClass().getMethod(metodo.getName().replace("getTxt","getLbl"));
				JTextField obj = (JTextField)metodo.invoke(configuracoesPanel);
				JLabel label = (JLabel)metodoLabel.invoke(configuracoesPanel);
				retorno.append(label.getText());
				retorno.append(" ");
				retorno.append(obj.getText());
				retorno.append("<br />");
			}else if(metodo.getName().startsWith("getChk")){
				//pegar o label
				Method metodoLabel = configuracoesPanel.getClass().getMethod(metodo.getName().replace("getChk","getLbl"));
				JCheckBox obj = (JCheckBox)metodo.invoke(configuracoesPanel);
				JLabel label = (JLabel)metodoLabel.invoke(configuracoesPanel);
				retorno.append(label.getText());
				retorno.append(" ");
				retorno.append(obj.isSelected());
				retorno.append("<br />");
			}else if(metodo.getName().startsWith("getSpn")){
				//pegar o label
				Method metodoLabel = configuracoesPanel.getClass().getMethod(metodo.getName().replace("getSpn","getLbl"));
				JSpinner obj = (JSpinner)metodo.invoke(configuracoesPanel);
				JLabel label = (JLabel)metodoLabel.invoke(configuracoesPanel);
				retorno.append(label.getText());
				retorno.append(" ");
				retorno.append(obj.getValue());
				retorno.append("<br />");
			}
		}
		return retorno.toString();
	}
	
	private void criaIndex(){
		try{
			File dir = new File("screenshots");
			if(!dir.exists())dir.mkdir();
						
			//salvar o html
			StringBuilder html=new StringBuilder();
			
			html.append("<html><head><title>RedeEpistemica - ScreenShots</title></head><body>");
			html.append("<div id=\"nav\">");
			html.append("<h3>Screenshots</h3>");
			html.append("<ul>");
			html.append("<li /><a href=\"automatica/p1.html\">Screenshots Automáticas</a>");
			html.append("<li /><a href=\"manual/p1.html\">Screenshots Manual</a>");
			html.append("</ul>");
			html.append("</div>");
			html.append("</body></html>");
			FileUtils.writeStringToFile(new File("screenshots/index.html"), html.toString(), "iso-8859-1");		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ConfiguracoesPanel getConfiguracoesPanel() {
		return configuracoesPanel;
	}
	public void setConfiguracoesPanel(ConfiguracoesPanel configuracoesPanel) {
		this.configuracoesPanel = configuracoesPanel;
	}
}
