package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

/**
 * Visualiza a 
 * @author fabio
 *
 */
public class CrencaView extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTable jTable;
	private DefaultTableModel defaultTableModel;
	public CrencaView(AgenteEpistemico agente) {
		defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("Ax");
		defaultTableModel.addColumn("Ay");
		defaultTableModel.addColumn("Az");
		defaultTableModel.addColumn("Cx");
		defaultTableModel.addColumn("Cy");
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
		
		jTable = new JTable(defaultTableModel);
		
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(jTable));
	}
}
