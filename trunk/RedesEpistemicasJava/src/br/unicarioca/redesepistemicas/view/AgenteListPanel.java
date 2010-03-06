package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.bo.InfoListener;
import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.CicloVidaAgenteListener;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

public class AgenteListPanel extends JPanel implements CicloVidaAgenteListener {
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(AgenteListPanel.class);
	private JList jList;
	private DefaultListModel listModel;
	private JLabel lblAgentes;

	private RedeEpistemica redeEpistemica;
	private ArrayList<AgenteEpistemico> listAgentes = new ArrayList<AgenteEpistemico>();

	
	public AgenteListPanel() {
		this.setLayout(new BorderLayout());
		lblAgentes = new JLabel("Agentes:         ");
		listModel = new DefaultListModel();
		jList = new JList(listModel);
		this.add(lblAgentes, BorderLayout.NORTH);
		this.add(jList, BorderLayout.CENTER);
		Thread t = new Thread() {
			public void run() {
				while (isVisible()) {
					try {
						Thread.sleep(5000);
						refresh();
					} catch (Exception e) {
					}

				}
			}
		};
		//t.start();
	}

	public synchronized void refresh() {
		logger.info("Ordenando " + listModel.getSize() + " agentes...");
		int tot = listModel.getSize() - 1;
		jList.setIgnoreRepaint(true);
		if(!redeEpistemica.isNormalizarPesos()){
			for(int j=0;j<tot;j++){
				AgenteEpistemico a = (AgenteEpistemico)listModel.get(j);
				a.setPesoReputacao(null);
			}
		}
		for (int j = tot; j >0; j--) {
			for (int i = 0; i < j; i++) {
				AgenteEpistemico a = (AgenteEpistemico)listModel.get(i);
				AgenteEpistemico b = (AgenteEpistemico)listModel.get(i+1);
				if(a.getPesoReputacao()<b.getPesoReputacao()){
					listModel.set(i, b);
					listModel.set(i+1, a);
				}
			}
		}
		logger.debug("Calculado " + listAgentes.size());
		
		//list.revalidate();
		//list.repaint();
	}

	@Override
	public synchronized void addKeyListener(KeyListener l) {
		jList.addKeyListener(l);
	}
	
	public void addListSelectionListener(ListSelectionListener l){
		jList.addListSelectionListener(l);
	}
	@Override
	public void criado(AgenteEpistemico agente) {


		boolean ok = listAgentes.add(agente);
		if (!ok)
			logger.error("Ja existe agente " + agente.getId());
		listModel.addElement(agente);
	}

	@Override
	public void morto(AgenteEpistemico agente) {
		listAgentes.remove(agente);
		listModel.removeElement(agente);
	}

	public void reiniciar() {
		listModel.clear();
		listAgentes.clear();
	}

	public List<AgenteEpistemico> getSelecionados(){
		int[] arr = jList.getSelectedIndices();
		List<AgenteEpistemico> res = new ArrayList<AgenteEpistemico>();
		for(int i=0;i<arr.length;i++){
			res.add((AgenteEpistemico)listModel.get(arr[i]));
		}
		return res;
	}

	public JList getJList() {
		return jList;
	}

	public int indexOf(AgenteEpistemico agente) {
		return listModel.indexOf(agente);
	}
	public void setRedeEpistemica(RedeEpistemica redeEpistemica) {
		this.redeEpistemica = redeEpistemica;
	}

}
