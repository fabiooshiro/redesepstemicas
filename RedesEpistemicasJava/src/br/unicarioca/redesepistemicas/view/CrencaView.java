package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.Experimento;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;
import br.unicarioca.redesepistemicas.modelo.RedeEpistemica;

/**
 * Visualiza a Crenca
 * @author Fabio Issamu Oshiro, Leandro Freire
 */
public class CrencaView extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CrencaView.class);
	private CrencaJTable jTable;
	private JButton btnAtualizar;
	private AgenteEpistemico agente;
	
	private Experimento experimento;
	
	public CrencaView(Set<ParEpistemico> crencas, RedeEpistemica redeEpistemica, RedeEpistemicaView redeEpistemicaView){
		logger.info("CrencaView abrindo monitor...");
		experimento = new Experimento();
		jTable = new CrencaJTable(crencas.iterator().next(), false);
		
		JPanel sul = criarPainelMonitorar(redeEpistemica, redeEpistemicaView);
		
		this.setLayout(new BorderLayout());
		popularTabela(crencas);
		this.add(sul, BorderLayout.SOUTH);
		this.add(new JScrollPane(jTable),BorderLayout.CENTER);
	}
	
	public JPanel criarPainelMonitorar(final RedeEpistemica redeEpistemica, final RedeEpistemicaView redeEpistemicaView){
		JButton btnMonitorarCrencas;
		final JTextField txtMaxDiff;
		final JTextField txtMinDiff;
		txtMaxDiff = new JTextField("0.002");
		txtMinDiff = new JTextField("0.007");
		btnMonitorarCrencas = new JButton("Monitorar");
		btnMonitorarCrencas.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Experimento experimento = new Experimento();
				double maxDiff = Double.parseDouble(txtMaxDiff.getText());
				double minDiff = Double.parseDouble(txtMinDiff.getText());
				experimento.setMaxDiff(maxDiff);
				experimento.setMinDiff(minDiff);
				for(int row= 0; row < jTable.getRowCount(); row++){
					if(jTable.isChecked(row)){
						ParEpistemico par =(ParEpistemico) jTable.getParInRow(row);
						par.setNome(jTable.getNomeInRow(row));
						par.setCor(jTable.getColorInRow(row));
						experimento.addCrenca(par);
					}
				}
				redeEpistemica.setExperimento(experimento);
				redeEpistemica.colorirAgentesDoExperimento(experimento);
				redeEpistemicaView.refresh();
			}
		});
		JPanel sul = new JPanel(new FlowLayout());
		sul.add(txtMaxDiff);
		sul.add(txtMinDiff);
		sul.add(btnMonitorarCrencas);
		return sul;
	}
	
	public Experimento getExperimento(){
		return experimento;
	}
	
	public void setExperimento(Experimento experimento){
		this.experimento = experimento;
	}
	
	public CrencaView(AgenteEpistemico agente, RedeEpistemica redeEpistemica, RedeEpistemicaView redeEpistemicaView) {
		this.agente = agente;
		jTable = new CrencaJTable(agente.getCrencas().get(0), true);
		btnAtualizar = new JButton("Atualizar");
		popularTabela();
		
		btnAtualizar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizar();
			}
		});
		
		this.setLayout(new BorderLayout());
		JPanel sul = criarPainelMonitorar(redeEpistemica, redeEpistemicaView);
		sul.add(btnAtualizar);
		
		this.add(new JScrollPane(jTable),BorderLayout.CENTER);
		this.add(sul,BorderLayout.SOUTH);
	}
	
	private void popularTabela(){
		List<ParEpistemico> pares = agente.getCrencas();
		for(ParEpistemico par:pares){
			ParEpistemico parR = agente.interpretar(par);
			jTable.addRow(par, parR);
		}
	}
	
	private void popularTabela(Set<ParEpistemico> crencas){
		if(crencas == null) return;
		Iterator<ParEpistemico> it = crencas.iterator();
		while(it.hasNext()){
			ParEpistemico par = it.next();
			jTable.addRow(par,par.getCor(),new Boolean(false), null);
		}
	}
	
	private void atualizar(){
		jTable.clear();
		popularTabela();
	}
}
