package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

/**
 * Visualiza a Crenca
 */
public class CrencaView extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTable jTable;
	private DefaultTableModel defaultTableModel;
	private JButton btnAtualizar;
	private AgenteEpistemico agente;
	public CrencaView(AgenteEpistemico agente) {
		this.agente = agente;
		btnAtualizar = new JButton("Atualizar");
		defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("Ax");
		defaultTableModel.addColumn("Ay");
		defaultTableModel.addColumn("Az");
		defaultTableModel.addColumn("Cx");
		defaultTableModel.addColumn("Cy");
		popularTabela();
		
		btnAtualizar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizar();
			}
		});
		
		jTable = new JTable(defaultTableModel);
		
		
		
		this.setLayout(new BorderLayout());
		JPanel sul = new JPanel(new FlowLayout());
		sul.add(btnAtualizar);
		
		this.add(new JScrollPane(jTable),BorderLayout.CENTER);
		this.add(sul,BorderLayout.SOUTH);
	}
	private void popularTabela(){
		List<ParEpistemico> pares = agente.getCrencas();
		for(ParEpistemico par:pares){
			ParEpistemico parR = agente.interpretar(par);
			defaultTableModel.addRow(new Object[]{
					parR.getAntecedente().getX(),
					parR.getAntecedente().getY(),
					parR.getAntecedente().getZ(),
					parR.getConsequente().getX(),
					parR.getConsequente().getY()
			});
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
