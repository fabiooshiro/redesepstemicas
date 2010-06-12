package br.unicarioca.redesepistemicas.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaJTable extends JTable{
	private static final long serialVersionUID = 1L;
	private static ParEpistemico parModelo;
	private static Logger logger = Logger.getLogger(CrencaJTable.class);
	private static int shiftCol2Right = 2;
	private CrencaTableModel crencaTableModel;
	public CrencaJTable(ParEpistemico parModelo) {
		crencaTableModel = new CrencaTableModel(parModelo); 
		this.setModel(crencaTableModel);
		this.getColumnModel().getColumn(0).setCellRenderer(new ColorCellRenderer());
		this.getColumnModel().getColumn(0).setCellEditor(new ColorCellEditor());
		this.getColumnModel().getColumn(0).setMaxWidth(20);//cor
		this.getColumnModel().getColumn(1).setMaxWidth(30);//nome
		//this.getColumnModel().getColumn(2).setMaxWidth(20);//checkbox?
	}
	
	public void addRow(ParEpistemico parR, Color color, Boolean boolean1) {
		Object obs[] = new Object[shiftCol2Right+parR.getSizeAntecedente()+parR.getSizeConsequente()];
		logger.info("Criando uma linha com "+obs.length+" colunas");
		int j=0;
		obs[0]=color;
		for(int i = shiftCol2Right;i<obs.length;i++){
			if(i<parR.getSizeAntecedente()+shiftCol2Right){
				obs[i] = parR.getDoubleAntecedentes().get(j); 
			}else{
				obs[i] = parR.getDoubleConsequentes().get(shiftCol2Right+1+j-obs.length); 
			}
			j++;
		}
		crencaTableModel.addRow(obs);
	}
	
	public void addRow(ParEpistemico parR) {
		Object obs[] = new Object[shiftCol2Right+parR.getSizeAntecedente()+parR.getSizeConsequente()];
		logger.info("Criando uma linha com "+obs.length+" colunas");
		int j=0;
		for(int i = shiftCol2Right;i<obs.length;i++){
			if(i<parR.getSizeAntecedente()+shiftCol2Right){
				obs[i] = parR.getDoubleAntecedentes().get(j); 
			}else{
				obs[i] = parR.getDoubleConsequentes().get(shiftCol2Right+1+j-obs.length); 
			}
			j++;
		}
		crencaTableModel.addRow(obs);
	}
	public void addRow() {
		Object obj[] = new Object[CrencaJTable.getParModelo().getSizeAntecedente()+CrencaJTable.getParModelo().getSizeConsequente()+shiftCol2Right];
		obj[0] = new Color(33,23,54);
		logger.info("Criando linha com " + obj.length + " colunas");
		for (int i = shiftCol2Right; i < obj.length; i++) {
			obj[i] = Math.random();
		}		
		crencaTableModel.addRow(obj);
	}
	public static void setParModelo(ParEpistemico parModelo) {
		CrencaJTable.parModelo = parModelo;
	}
	
	public static ParEpistemico getParModelo() {
		return parModelo;
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
	                                      "Escolha uma cor para a cren�a",
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
	
	
	
}