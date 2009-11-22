package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.CicloVidaAgenteListener;

public class AgenteListPanel extends JPanel implements MouseListener,CicloVidaAgenteListener{
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(AgenteListPanel.class);
	private JList list;
	private DefaultListModel listModel;
	private JLabel lblAgentes;
	public AgenteListPanel() {
		this.setLayout(new BorderLayout());
		lblAgentes = new JLabel("Agentes:         ");
		listModel = new DefaultListModel();
		list = new JList(listModel);
		this.add(lblAgentes,BorderLayout.NORTH);
		this.add(list,BorderLayout.CENTER);
		this.addMouseListener(this);
		list.addMouseListener(this);
	}
	
	public void refresh(){
		list.revalidate();
		list.repaint();
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
	@Override
	public void morto(AgenteEpistemico agente) {
		listModel.removeElement(agente);
	}
	public void reiniciar() {
		listModel.clear();
	}

	
}
