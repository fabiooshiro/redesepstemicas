package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.Antecedente;
import br.unicarioca.redesepistemicas.modelo.Consequente;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaTreinarView extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable jTable;
	private DefaultTableModel defaultTableModel;
	private JButton btnNovaCrenca;
	private JButton btnTreinarSelecionados;
	private JTextField txtQtdTreino;
	private JLabel lblQtdTreino;
	private static CrencaTreinarView instance;
	private AgenteListPanel agenteListPanel;

	public void setAgenteListPanel(AgenteListPanel agenteListPanel) {
		this.agenteListPanel = agenteListPanel;
	}

	private CrencaTreinarView() {
		btnNovaCrenca = new JButton("Nova Crença");
		txtQtdTreino = new JTextField("1000");
		lblQtdTreino = new JLabel("Qtd. Treino: ");
		btnTreinarSelecionados = new JButton("Treinar Selecionados");
		defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("Ax");
		defaultTableModel.addColumn("Ay");
		defaultTableModel.addColumn("Az");
		defaultTableModel.addColumn("Cx");
		defaultTableModel.addColumn("Cy");

		jTable = new JTable(defaultTableModel);

		jTable.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				int row = jTable.getSelectedRow();
				if (e.getKeyCode() == KeyEvent.VK_DELETE && row > -1) {
					defaultTableModel.removeRow(row);
				}
			}

			// Dont work over there
			public void keyReleased(KeyEvent e) {
			}

			// Dont work over there
			public void keyTyped(KeyEvent e) {
			}
		});

		btnNovaCrenca.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				criarNovaLinha();
			}
		});

		btnTreinarSelecionados.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				treinarAgentesSelecionados();
			}
		});

		JPanel sul = new JPanel(new FlowLayout());
		sul.add(lblQtdTreino);
		sul.add(txtQtdTreino);
		sul.add(btnNovaCrenca);
		sul.add(btnTreinarSelecionados);

		this.setLayout(new BorderLayout());
		this.add(sul, BorderLayout.SOUTH);
		this.add(new JScrollPane(jTable), BorderLayout.CENTER);
	}

	public static CrencaTreinarView getInstance() {
		if (instance == null) {
			instance = new CrencaTreinarView();
		}
		return instance;
	}

	private void criarNovaLinha() {
		defaultTableModel.addRow(new Object[] { Math.random(), Math.random(), Math.random(), Math.random(), Math.random() });
	}

	private void treinarAgentesSelecionados() {
		List<ParEpistemico> pares = getParesEpistemicos();
		List<AgenteEpistemico> agentes = agenteListPanel.getSelecionados();
		int qtd = Integer.parseInt(txtQtdTreino.getText());
		for (AgenteEpistemico agente : agentes) {
			agente.treinar(pares, qtd);
			agente.addCrencas(pares);
		}
	}

	private List<ParEpistemico> getParesEpistemicos() {
		List<ParEpistemico> retorno = new ArrayList<ParEpistemico>();
		int linhas = defaultTableModel.getRowCount();
		int colunas = defaultTableModel.getColumnCount();
		for (int i = 0; i < linhas; i++) {
			ParEpistemico par = new ParEpistemico();
			Antecedente antecedente = new Antecedente();
			Consequente consequente = new Consequente();
			for (int j = 0; j < colunas; j++) {
				if (j < 3) {
					antecedente.add(defaultTableModel.getValueAt(i, j));
				} else {
					consequente.add(defaultTableModel.getValueAt(i, j));
				}
			}
			par.setAntecedente(antecedente);
			par.setConsequente(consequente);
			retorno.add(par);
		}
		return retorno;
	}
}
