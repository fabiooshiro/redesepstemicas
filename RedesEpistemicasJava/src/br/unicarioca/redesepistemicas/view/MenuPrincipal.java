package br.unicarioca.redesepistemicas.view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuPrincipal extends JMenuBar{
	private static final long serialVersionUID = 1L;
	private JMenu arquivo;
	private JMenu visualizar;
	private JCheckBoxMenuItem verLinhasVermelhas;
	private JCheckBoxMenuItem verLinhasAzuis;
	private JMenuItem salvar;
	private JMenuItem abrir;
	private JMenuItem novo;
	public MenuPrincipal() {
		arquivo = new JMenu("Arquivo");
		salvar = new JMenuItem("Salvar");
		abrir = new JMenuItem("Abrir");
		novo = new JMenuItem("Novo");
		
		visualizar = new JMenu("Visualizar");
		verLinhasVermelhas = new JCheckBoxMenuItem("Ver Linhas Vermelhas");
		verLinhasAzuis = new JCheckBoxMenuItem("Ver Linhas Azuis");
		
		salvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK ));
		abrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK ));
		novo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
		
		this.add(arquivo);
		arquivo.add(salvar);
		arquivo.add(abrir);
		arquivo.add(novo);
		
		this.add(visualizar);
		visualizar.add(verLinhasVermelhas);
		visualizar.add(verLinhasAzuis);
	}
	public JMenuItem getNovo() {
		return novo;
	}
	public JCheckBoxMenuItem getVerLinhasVermelhas() {
		return verLinhasVermelhas;
	}
	public JCheckBoxMenuItem getVerLinhasAzuis() {
		return verLinhasAzuis;
	}
}
