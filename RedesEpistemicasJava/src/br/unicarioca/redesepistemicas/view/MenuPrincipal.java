package br.unicarioca.redesepistemicas.view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuPrincipal extends JMenuBar{
	private static final long serialVersionUID = 1L;
	private JMenu arquivo;
	private JMenuItem salvar;
	private JMenuItem abrir;
	private JMenuItem novo;
	public MenuPrincipal() {
		arquivo = new JMenu("Arquivo");
		salvar = new JMenuItem("Salvar");
		abrir = new JMenuItem("Abrir");
		novo = new JMenuItem("Novo");
		
		salvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK ));
		abrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK ));
		novo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
		
		this.add(arquivo);
		arquivo.add(salvar);
		arquivo.add(abrir);
		arquivo.add(novo);
	}
	public JMenuItem getNovo() {
		return novo;
	}
}
