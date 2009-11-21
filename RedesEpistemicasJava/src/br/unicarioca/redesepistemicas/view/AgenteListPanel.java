package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.CicloVidaAgenteListener;

public class AgenteListPanel extends JPanel implements MouseListener,CicloVidaAgenteListener{
	private static final long serialVersionUID = 1L;

	private JList list;
	private DefaultListModel listModel;
	public AgenteListPanel() {
		this.setLayout(new BorderLayout());
		listModel = new DefaultListModel();
		list = new JList(listModel);
		this.add(list,BorderLayout.CENTER);
		this.addMouseListener(this);
		list.addMouseListener(this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void criado(AgenteEpistemico agente) {
		listModel.addElement(agente);
	}
	public void reiniciar() {
		listModel.clear();
	}
}
