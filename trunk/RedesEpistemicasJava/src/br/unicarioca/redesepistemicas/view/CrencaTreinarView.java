package br.unicarioca.redesepistemicas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;
import br.unicarioca.redesepistemicas.modelo.ParEpistemico;

public class CrencaTreinarView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CrencaTreinarView.class);
	static class Holder {
		static CrencaTreinarView instance = new CrencaTreinarView();
	}
	public static CrencaTreinarView getInstance() {
		return Holder.instance;
	}
	
	private CrencaJTable jTable;
	private JButton btnNovaCrenca;
	private JButton btnTreinarSelecionados;
	private JTextField txtQtdTreino;
	private JLabel lblQtdTreino;
	private AgenteListPanel agenteListPanel;
	
	public void setAgenteListPanel(AgenteListPanel agenteListPanel) {
		this.agenteListPanel = agenteListPanel;
	}

	private CrencaTreinarView() {
		btnNovaCrenca = new JButton("Nova Crença");
		txtQtdTreino = new JTextField("1000");
		lblQtdTreino = new JLabel("Qtd. Treino: ");
		btnTreinarSelecionados = new JButton("Treinar Selecionados");
		jTable = new CrencaJTable(MainFrame.parModelo, false);

		jTable.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				int row = jTable.getSelectedRow();
				if (e.getKeyCode() == KeyEvent.VK_DELETE && row > -1) {
					jTable.removeRow(row);
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

	private void criarNovaLinha() {
		jTable.addRow();
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
		return jTable.getPares();
	}

}
