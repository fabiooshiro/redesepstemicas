package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.Experimento;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

/**
 * Visualiza a Crenca
 */
public class CrencaView extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTable jTable;
	private DefaultTableModel defaultTableModel;
	private CrencaTableModel crencaTableModel;
	private JButton btnAtualizar;
	private AgenteEpistemico agente;
	private JButton btnMonitorarCrencas;
	private Experimento experimento;
	
		
	public CrencaView(Set<ParEpistemico> crencas){
		//crencaTableModel = new CrencaTableModel();
		crencaTableModel = new CrencaTableModel();
		jTable = new JTable(crencaTableModel);
		jTable.getColumnModel().getColumn(1).setCellRenderer(new ColorCellRenderer());
		jTable.getColumnModel().getColumn(1).setCellEditor(new ColorCellEditor());
		jTable.getColumnModel().getColumn(0).setMinWidth(300);
		jTable.getColumnModel().getColumn(1).setMaxWidth(30);
		jTable.getColumnModel().getColumn(2).setMaxWidth(20);
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
			defaultTableModel.addRow(new Object[]{
					parR.getAntecedente().getX(),
					parR.getAntecedente().getY(),
					parR.getAntecedente().getZ(),
					parR.getConsequente().getX(),
					parR.getConsequente().getY()
			});
		}
	}
	
	private void popularTabela(Set<ParEpistemico> crencas){
		if(crencas == null) return;
		Iterator<ParEpistemico> it = crencas.iterator();
		Object[][] data = new Object[crencas.size()][];
		int index = 0;
		while(it.hasNext()){
			data[index] = new Object[]{it.next(),new Color(33,23,54),new Boolean(false)}; 
			index++;
		}
		crencaTableModel.setData(data);
		
	}
	private void atualizar(){
		int r = defaultTableModel.getRowCount();
		for(int i=0;i<r;i++){
			defaultTableModel.removeRow(0);
		}
		popularTabela();
	}
	
	class ColorCellRenderer extends JLabel implements TableCellRenderer{
					
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
}
