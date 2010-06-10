package br.unicarioca.redesepistemicas.view;


import javax.swing.table.AbstractTableModel;


public class CrencaTableModel extends AbstractTableModel {
	
	private String[] columnName = {"Crença","Cor","Monitora"};
	private Object[][] data;
	

	@Override
	public int getColumnCount() {
		return columnName.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return data[arg0][arg1];
	}
	
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
	
    public boolean isCellEditable(int row, int col) {
        if (col > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public void setData(Object[][] data){
    	this.data = data;
    }
      
    public String getColumnName(int col) {
        return columnName[col];
    }



}
