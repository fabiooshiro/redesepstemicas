package br.unicarioca.redesepistemicas.view;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Visualiza a 
 * @author fabio
 *
 */
public class CrencaView extends JPanel{
	private JTable jTable;
	private DefaultTableModel defaultTableModel;
	public CrencaView() {
		defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("Ax");
		defaultTableModel.addColumn("Ay");
		defaultTableModel.addColumn("Az");
		defaultTableModel.addColumn("Cx");
		defaultTableModel.addColumn("Cy");
		jTable = new JTable(defaultTableModel);
		
		
	}
}
