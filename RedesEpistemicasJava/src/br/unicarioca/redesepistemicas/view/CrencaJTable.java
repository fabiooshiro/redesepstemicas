package br.unicarioca.redesepistemicas.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaJTable extends JTable{
	private static final long serialVersionUID = 1L;
	private static final int PAR_COLUMN=2;
	private static final int COLOR_COLUMN=1;
	private static final int MONITORA_COLUMN=0;
	private static Logger logger = Logger.getLogger(CrencaJTable.class);
	private static int shiftCol2Right = 3;
	private CrencaTableModel crencaTableModel;
	private ParEpistemico parModelo;
	public CrencaJTable(ParEpistemico parModelo) {
		this.parModelo = parModelo;
		crencaTableModel = new CrencaTableModel(parModelo); 
		this.setModel(crencaTableModel);
		this.getColumnModel().getColumn(COLOR_COLUMN).setCellRenderer(new ColorCellRenderer());
		this.getColumnModel().getColumn(COLOR_COLUMN).setCellEditor(new ColorCellEditor());
		this.getColumnModel().getColumn(COLOR_COLUMN).setMaxWidth(20);//cor
		this.getColumnModel().getColumn(PAR_COLUMN).setMinWidth(200);//nome
		this.getColumnModel().getColumn(MONITORA_COLUMN).setMaxWidth(20);//checkbox?
	}
	
	public String getNomeInRow(int row) {
		Object obj = crencaTableModel.getValueAt(row,PAR_COLUMN);
		return obj.toString();
	}
	
	public ParEpistemico getParInRow(int row) {
		Object obj = crencaTableModel.getValueAt(row,PAR_COLUMN);
		return (ParEpistemico)obj;
	}
	
	public List<ParEpistemico> getPares() {
		List<ParEpistemico> retorno = new ArrayList<ParEpistemico>();
		try {
			
			int linhas = crencaTableModel.getRowCount();
			int colunas = crencaTableModel.getColumnCount();
			for (int i = 0; i < linhas; i++) {
				ParEpistemico par = (ParEpistemico) parModelo.clone();
				for (int j = shiftCol2Right; j < colunas; j++) {
					if (j < parModelo.getSizeAntecedente()+shiftCol2Right) {
						par.addAntecedente(Double
								.parseDouble((crencaTableModel
										.getValueAt(i, j).toString())));
					} else {
						par.addConsequente(Double
								.parseDouble((crencaTableModel
										.getValueAt(i, j).toString())));
					}
				}
				retorno.add(par);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Erro ao gerar pares: " + e.getMessage());
		}
		return retorno;
	}
	public boolean isChecked(int row) {
		
		return (Boolean) crencaTableModel.getValueAt(row,MONITORA_COLUMN);
	}
	
	public Color getColorInRow(int row) {
		Object obj = crencaTableModel.getValueAt(row,COLOR_COLUMN);
		if(obj==null || !(obj instanceof Color)){
			logger.info("sem cor " + row);
			return Color.BLACK;
		}else{
			logger.info("com cor " + row);
		}
		return (Color)obj;
	}
	
	/**
	 * Remove todos os caras
	 */
	public void clear() {
		int r = crencaTableModel.getRowCount();
		for(int i=0;i<r;i++){
			crencaTableModel.removeRow(0);
		}
	}
	
	public void addRow(ParEpistemico parR, Color color, Boolean boolean1) {
		Object objects[] = new Object[shiftCol2Right+parR.getSizeAntecedente()+parR.getSizeConsequente()];
		logger.info("Criando uma linha com "+objects.length+" colunas");
		objects[MONITORA_COLUMN] = boolean1;
		objects[COLOR_COLUMN]=color;
		objects[PAR_COLUMN]=parR;
		int i=0;
		for(int j=0;j<parR.getSizeAntecedente();j++){
			objects[i+shiftCol2Right] = parR.getDoubleAntecedentes().get(j);
			i++;
		}
		for(int j=0;j<parR.getSizeConsequente();j++){
			objects[i+shiftCol2Right] = parR.getDoubleAntecedentes().get(j);
			i++;
		}
		crencaTableModel.addRow(objects);
	}
	
	public void addRow(ParEpistemico parR) {
		addRow(parR, Color.WHITE, false);
	}
	
	public void removeRow(int row) {
		crencaTableModel.removeRow(row);
	}
	
	public void addRow() {
		Object obj[] = new Object[parModelo.getSizeAntecedente()+parModelo.getSizeConsequente()+shiftCol2Right];
		obj[0] = new Boolean(false);
		obj[1] = new Color(33,23,54);
		obj[2] = "";
		logger.info("Criando linha com " + obj.length + " colunas");
		for (int i = shiftCol2Right; i < obj.length; i++) {
			obj[i] = Math.random();
		}		
		crencaTableModel.addRow(obj);
	}
	
	class ColorCellRenderer extends JLabel implements TableCellRenderer{
		private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object color,
					boolean isSelectd, boolean hasFocus, int row, int col) {
				Color newColor =(Color) color;
				setBackground(newColor);
				setOpaque(true);
				setBorder(BorderFactory.createMatteBorder(2,5,2,5,table.getBackground()));
				table.setCellSelectionEnabled(false);
				table.setColumnSelectionAllowed(false);
            			
				return this;
			}
			
	}
	class ColorCellEditor extends AbstractCellEditor
    							 implements TableCellEditor,
    							 		    ActionListener{
		private static final long serialVersionUID = 1L;
		Color currentColor;
	    JButton button;
	    JColorChooser colorChooser;
	    JDialog dialog;
	    protected static final String EDIT = "edit";

	    public ColorCellEditor() {
	       
	        button = new JButton();
	        button.setActionCommand(EDIT);
	        button.addActionListener(this);
	        button.setBorderPainted(false);
        
	        colorChooser = new JColorChooser();
	        colorChooser.remove(1);
	        dialog = JColorChooser.createDialog(button,
	                                      "Escolha uma cor para a crença",
	                                        true,  //modal
	                                        colorChooser,
	                                        this,  //OK button handler
	                                        null); //no CANCEL button handler
	    }

	    /**
	     * Handles events from the editor button and from
	     * the dialog's OK button.
	     */
	    public void actionPerformed(ActionEvent e) {
	        if (EDIT.equals(e.getActionCommand())) {
	            //The user has clicked the cell, so
	            //bring up the dialog.
	            button.setBackground(currentColor);
	            colorChooser.setColor(currentColor);
	            dialog.setVisible(true);

	            //Make the renderer reappear.
	            fireEditingStopped();

	        } else { //User pressed dialog's "OK" button.
	            currentColor = colorChooser.getColor();
	        }
	    }

	    //Implement the one CellEditor method that AbstractCellEditor doesn't.
	    public Object getCellEditorValue() {
	        return currentColor;
	    }

	    //Implement the one method defined by TableCellEditor.
	    public Component getTableCellEditorComponent(JTable table,
	                                                 Object value,
	                                                 boolean isSelected,
	                                                 int row,
	                                                 int column) {
	        currentColor = (Color)value;
	        return button;
	    }

	}
	
	public Class<?> getColumnClass(int c) {
		Object obj = getValueAt(0, c);
		if(obj==null){
			logger.warn("Colum class == null " + c);
			if(COLOR_COLUMN==c){
				return Color.class;
			}else if(MONITORA_COLUMN==c){
				return Boolean.class;
			}else{
				return String.class;
			}
		}
		return obj.getClass();
    }	
	
}
