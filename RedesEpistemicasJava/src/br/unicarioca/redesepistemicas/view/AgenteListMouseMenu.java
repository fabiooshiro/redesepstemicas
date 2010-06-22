package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;

public class AgenteListMouseMenu extends JPopupMenu {
	private static final long serialVersionUID = 1765661788449368756L;
	private JMenuItem verCrencasMenuItem;
	private JList jList;
	private AgenteEpistemico agente;
	public AgenteListMouseMenu(JList jList) {
		this.jList = jList;
		verCrencasMenuItem = new JMenuItem("Ver Crenças");
		verCrencasMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(agente.getCrencas().size()==0){
					JOptionPane.showMessageDialog(AgenteListMouseMenu.this,"Nenhuma crença");
				}else{
					JFrame jFrame = new JFrame("Crenças " + agente.getNome());
					jFrame.setLayout(new BorderLayout());
					jFrame.add(new CrencaView(agente));
					jFrame.pack();
					jFrame.setVisible(true);
				}
			}
		});
		this.add(verCrencasMenuItem);

		MouseListener popupListener = new PopupListener(this);
		jList.addMouseListener(popupListener);
	}

	protected void mostrarMenu(MouseEvent e) {
		int indexSelecionado = jList.locationToIndex(e.getPoint());
		agente = (AgenteEpistemico)jList.getModel().getElementAt(indexSelecionado);        
		jList.setSelectedIndex(indexSelecionado);
		this.show(e.getComponent(), e.getX(), e.getY());
	}

	class PopupListener extends MouseAdapter {
		JPopupMenu popup;

		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				mostrarMenu(e);
			}
		}
	}
}