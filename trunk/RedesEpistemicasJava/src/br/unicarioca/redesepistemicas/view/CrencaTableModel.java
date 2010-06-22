package br.unicarioca.redesepistemicas.view;

import javax.swing.table.DefaultTableModel;

import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
    public CrencaTableModel(ParEpistemico parModelo, boolean consequenteAtual) {
    	int i;
    	this.addColumn("M");
		this.addColumn("Cor");
		this.addColumn("Nome");
		for (i = 0; i < parModelo.getSizeAntecedente(); i++) {
			this.addColumn("A" + i);
		}
		for (i = 0; i < parModelo.getSizeConsequente(); i++) {
			this.addColumn("C" + i);
		}
		if(consequenteAtual){
			for (i = 0; i < parModelo.getSizeConsequente(); i++) {
				this.addColumn("C'" + i);
			}
		}
	}
	public boolean isCellEditable(int row, int col) {
    	return true;
    }
	
	
		
}
