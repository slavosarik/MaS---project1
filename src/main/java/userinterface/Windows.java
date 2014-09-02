package userinterface;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import logika.Simulation;

public class Windows {

	private JFrame frame;
	private JTextField textField;
	private JButton btntart;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Windows window = new Windows();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Windows() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frame = new JFrame(
				"Dia¾kové telefónne spojenie - Modelovanie a Simulácia: Slavomír Šárik");
		frame.setBounds(100, 100, 1700, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 1664, 739);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(1468, 8, 86, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblPoetLiniek = new JLabel("Po\u010Det liniek:");
		lblPoetLiniek.setBounds(1372, 11, 86, 14);
		panel.add(lblPoetLiniek);

		btntart = new JButton("\u0160tart ");
		btntart.setBounds(1565, 7, 89, 23);
		panel.add(btntart);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 39, 1644, 689);
		panel.add(scrollPane);

		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 16));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane.setViewportView(textArea);

		btntart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				textArea.setText("");
				Simulation s = new Simulation();
				s.simulate(Integer.parseInt(textField.getText()), textArea);
			}
		});

	}

	public JButton getBtnstart() {
		return btntart;
	}

	public JTextField getTextField() {
		return textField;
	}

	public JTextArea getTextArea() {
		return textArea;
	}
}
