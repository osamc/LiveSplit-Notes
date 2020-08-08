package samc.livesplitnotes.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import samc.livesplitnotes.Configuration;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Settings extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JTextField txtRate;
	private JTextField txtSplit;
	private JTextField txtForward;
	private JTextField txtBack;

	private Configuration config = new Configuration();

	private Notes parent;


	public Settings(Notes parent) {
		this.parent = parent;
		this.initComponents();
		this.initListeners();
		this.loadConfig();
	}

	public void initComponents() {
		setTitle("Settings");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 355, 312);
		contentPane = new JPanel();
		this.setFocusable(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAddress = new JLabel("Livesplit Server address:");
		lblAddress.setBounds(10, 27, 142, 14);
		contentPane.add(lblAddress);

		txtAddress = new JTextField();
		txtAddress.setBounds(162, 56, 86, 20);
		txtAddress.setEditable(true);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);

		JLabel lblPort = new JLabel("Livesplit Server Port:");
		lblPort.setBounds(38, 59, 142, 14);
		contentPane.add(lblPort);

		txtPort = new JTextField();
		txtPort.setBounds(162, 24, 86, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		JLabel lblPoll = new JLabel("Livesplit Poll Rate:");
		lblPoll.setBounds(48, 87, 142, 14);
		contentPane.add(lblPoll);

		JLabel lblSplit = new JLabel("Split File at:");
		lblSplit.setBounds(86, 168, 66, 14);
		contentPane.add(lblSplit);

		JLabel lblForward = new JLabel("Forward Key:");
		lblForward.setBounds(77, 118, 75, 14);
		contentPane.add(lblForward);

		JLabel lblBack = new JLabel("Back Key:");
		lblBack.setBounds(95, 143, 57, 14);
		contentPane.add(lblBack);

		txtRate = new JTextField();
		txtRate.setBounds(162, 84, 86, 20);
		contentPane.add(txtRate);
		txtRate.setColumns(10);

		txtSplit = new JTextField();
		txtSplit.setToolTipText("New line by default");
		txtSplit.setBounds(162, 165, 86, 20);
		contentPane.add(txtSplit);
		txtSplit.setColumns(10);

		txtForward = new JTextField();

		txtForward.setBounds(162, 115, 86, 20);
		contentPane.add(txtForward);
		txtForward.setColumns(10);

		txtBack = new JTextField();

		txtBack.setBounds(162, 140, 86, 20);
		contentPane.add(txtBack);
		txtBack.setColumns(10);

		JButton btnSave = new JButton("Save Settings");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfig();
				parent.setConfig(config);
				setVisible(false);
				dispose();
			}
		});
		btnSave.setBounds(70, 227, 168, 23);
		contentPane.add(btnSave);

		Configuration c = parent.getConfig();
		if (c != null) {
			this.config = c;
		}
	}

	public void initListeners() {
		txtForward.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txtForward.setText(Integer.toString(e.getKeyCode()));
			}

			@Override
			public void keyTyped(KeyEvent e) {
				e.consume();
			}
		});

		txtBack.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txtBack.setText(Integer.toString(e.getKeyCode()));
			}

			@Override
			public void keyTyped(KeyEvent e) {
				e.consume();
			}

		});

	}

	public void loadConfig() {
		this.txtAddress.setText(this.config.getAddress());
		this.txtPort.setText(Integer.toString(this.config.getPort()));
		this.txtRate.setText(Integer.toString(this.config.getPollRateInMillis()));
		this.txtSplit.setText(this.config.getSplitAt());
		this.txtForward.setText(Integer.toString(this.config.getForwardKey()));
		this.txtBack.setText(Integer.toString(this.config.getBackKey()));
	}

	public Configuration saveConfig() {

		this.config.setAddress(this.txtAddress.getText());
		this.config.setPort(Integer.valueOf(this.txtPort.getText()));
		this.config.setPollRateInMillis(Integer.valueOf(this.txtRate.getText()));
		this.config.setBackKey(Integer.valueOf(this.txtBack.getText()));
		this.config.setForwardKey(Integer.valueOf(this.txtForward.getText()));
		this.config.setSplitAt(this.txtSplit.getText());
		
		return this.config;
	}

}
