package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.Experimento;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

/**
 * Visualiza a Crenca
 */
public class CrencaView extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CrencaView.class);
	private CrencaJTable jTable;
	private CrencaTableModel defaultTableModel;
	private JButton btnAtualizar;
	private AgenteEpistemico agente;
	private JButton btnMonitorarCrencas;
	private Experimento experimento;
		
	public CrencaView(Set<ParEpistemico> crencas){
		jTable = new CrencaJTable(crencas.iterator().next());
		
		btnMonitorarCrencas = new JButton("Monitorar");
		
		btnMonitorarCrencas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				monitora();
			}
		});
		
		JPanel sul = new JPanel(new FlowLayout());
		sul.add(btnMonitorarCrencas);
		
		this.setLayout(new BorderLayout());
		popularTabela(crencas);
		this.add(sul, BorderLayout.SOUTH);
		this.add(new JScrollPane(jTable),BorderLayout.CENTER);
	}
	
	public Experimento getExperimento(){
		return experimento;
	}
	
	public void setExperimento(Experimento experimento){
		this.experimento = experimento;
	}
	
	public CrencaView(AgenteEpistemico agente) {
		this.agente = agente;
		jTable = new CrencaJTable(agente.getCrencas().get(0));
		btnAtualizar = new JButton("Atualizar");
		popularTabela();
		
		btnAtualizar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizar();
			}
		});
		
		this.setLayout(new BorderLayout());
		JPanel sul = new JPanel(new FlowLayout());
		sul.add(btnAtualizar);
		
		this.add(new JScrollPane(jTable),BorderLayout.CENTER);
		this.add(sul,BorderLayout.SOUTH);
	}
	
	private void monitora(){
		experimento = new Experimento();
		for(int row= 0; row < jTable.getRowCount(); row++){
			if((Boolean) jTable.getValueAt(row, 2)){
				experimento.addCrenca((ParEpistemico) jTable.getValueAt(row, 0), (Color) jTable.getValueAt(row, 1));
			}
		}
	}
	
	private void popularTabela(){
		List<ParEpistemico> pares = agente.getCrencas();
		for(ParEpistemico par:pares){
			ParEpistemico parR = agente.interpretar(par);
			jTable.addRow(parR);
		}
	}
	
	private void popularTabela(Set<ParEpistemico> crencas){
		if(crencas == null) return;
		Iterator<ParEpistemico> it = crencas.iterator();
		while(it.hasNext()){
			jTable.addRow(it.next(),new Color(33,23,54),new Boolean(false));
		}
	}
	private void atualizar(){
		int r = defaultTableModel.getRowCount();
		for(int i=0;i<r;i++){
			defaultTableModel.removeRow(0);
		}
		popularTabela();
	}
}
