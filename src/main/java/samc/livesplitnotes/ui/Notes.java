package samc.livesplitnotes.ui;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import samc.livesplitnotes.Configuration;
import samc.livesplitnotes.NoteReader;

import java.beans.PropertyVetoException;

import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Notes {

	private JFrame frame;
	private JEditorPane editorPane;

	private JToolBar toolBar;
	private JMenuBar menuBar;

	private JMenu mnConfig;
	private JCheckBoxMenuItem chckbxOnTop;
	private JCheckBoxMenuItem chckbxKeyHooks;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSettings;

	private JMenu mnConnection;
	private JMenuItem mntmConnect;

	private JFileChooser chooser;

	private NoteReader reader = new NoteReader();
	private Notes self;

	private Configuration config;

	private Socket socket;
	private GlobalKeyboardHook keyboardHook;

	ScheduledExecutorService pollThread;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notes window = new Notes();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Notes() throws PropertyVetoException {
		initialize();
	}

	private void initialize() {

		this.self = this;

		this.initComponents();
		this.initListeners();

		this.config = new Configuration();
		this.config.load();

		this.initSocket();

		if (this.config.getFile() != null) {
			this.initPanel();
		}

		this.initGlobalHooks();
		this.updateUI();

	}

	private void initComponents() {
		this.frame = new JFrame();

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == config.getBackKey()) {
					reader.previous();
				} else if (keyCode == config.getForwardKey()) {
					reader.next();
				}

				updateUI();
			}
		});

		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);

		URL icon = getClass().getResource("/favicon-32x32.png");

		frame.setIconImage(new ImageIcon(icon).getImage());

		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		editorPane = new JEditorPane();
		editorPane.setAutoscrolls(true);
		editorPane.setEditable(false);
		editorPane.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane(editorPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		frame.getContentPane().add(scrollPane);

		Preferences prefs = Preferences.userRoot().node(getClass().getName());
		chooser = new JFileChooser(prefs.get("last_used_folder", new File(".").getAbsolutePath()));

		scrollPane.setColumnHeaderView(toolBar);

		menuBar = new JMenuBar();
		menuBar.setFocusable(false);
		frame.setJMenuBar(menuBar);

		mnConnection = new JMenu("Connection");
		mntmConnect = new JMenuItem("Connect");
		mnConnection.add(mntmConnect);

		menuBar.add(mnConnection);

		mnConfig = new JMenu("Config");
		mntmOpen = new JMenuItem("Open Notes");
		mntmSettings = new JMenuItem("Settings");

		chckbxOnTop = new JCheckBoxMenuItem("Always On Top");
		chckbxKeyHooks = new JCheckBoxMenuItem("Global Keyhooks");

		mnConfig.add(chckbxOnTop);
		mnConfig.add(chckbxKeyHooks);
		mnConfig.add(mntmOpen);
		mnConfig.add(mntmSettings);

		menuBar.add(mnConfig);

	}

	private void initListeners() {
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setAlwaysOnTop(false);

				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Preferences prefs = Preferences.userRoot().node(getClass().getName());
					prefs.put("last_used_folder", chooser.getSelectedFile().getParent());
					config.setFile(chooser.getSelectedFile().getAbsolutePath());
					initPanel();
				}
			}
		});

		mntmSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings settingFrame = new Settings(self);
				frame.setAlwaysOnTop(false);
				settingFrame.setVisible(true);
				settingFrame.toFront();
				settingFrame.requestFocus();
				disableHooks();
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				config.save();
			}
		});

		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (socket == null) {
					initSocket();
				} else {
					disconnectSocket();
				}
				updateUI();
			}
		});

		chckbxOnTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.setAlwaysOnTop(chckbxOnTop.isSelected());
				updateUI();
			}
		});

		chckbxKeyHooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.setGlobalKeyHooks(chckbxKeyHooks.isSelected());
				updateUI();
			}
		});
	}

	private void initPanel() {
		this.reader = new NoteReader(this.config.getFile(), this.config.getSplitAt());
		this.updateUI();
	}

	private void updateUI() {

		if (reader.isInitialised()) {
			this.editorPane.setText(reader.getCurrentNote());
		}

		this.frame.setTitle(this.generateTitle());
		this.frame.setAlwaysOnTop(this.config.isAlwaysOnTop());
		this.chckbxOnTop.setSelected(this.config.isAlwaysOnTop());
		this.chckbxKeyHooks.setSelected(this.config.isGlobalKeyHooks());

		if (this.config.isGlobalKeyHooks()) {
			this.initGlobalHooks();
		} else {
			this.disableHooks();
		}

		this.mntmConnect.setText(this.socket != null && this.socket.isConnected() ? "Disconnect" : "Connect");

		this.config.save();

	}

	public String generateTitle() {
		String title = "Split Notes: ";
		if (this.config.getFile() != null) {
			title += new File(this.config.getFile()).getName();
		} else {
			title += "No Chosen File";
		}

		title += " - ";
		title += this.socket == null ? "Disconnected" : "Connected";

		return title;
	}

	public void setConfig(Configuration config) {
		this.config = config;
		this.updateUI();
	}

	public Configuration getConfig() {
		return this.config;
	}

	private void disableHooks() {
		if (this.keyboardHook != null) {
			this.keyboardHook.shutdownHook();
		}
	}

	private void initGlobalHooks() {

		if (this.keyboardHook != null && this.keyboardHook.isAlive()) {
			this.disableHooks();
		}

		keyboardHook = new GlobalKeyboardHook(true);

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {

			@Override
			public void keyPressed(GlobalKeyEvent event) {

				boolean watched = false;
				final int forward = config.getForwardKey();
				final int back = config.getBackKey();

				if (event.getVirtualKeyCode() == back) {
					watched = true;
					reader.previous();
				} else if (event.getVirtualKeyCode() == forward) {
					watched = true;
					reader.next();
				}

				if (watched) {
					editorPane.setText(reader.getCurrentNote());
				}
			}
		});

	}

	private void initSocket() {

		try {
			this.socket = new Socket(this.config.getAddress(), this.config.getPort());
			this.socket.setKeepAlive(true);
		} catch (Exception e) {
			this.socket = null;
			return;
		}

		Runnable r = new Runnable() {

			@Override
			public void run() {
				getSplit();
			}

		};

		pollThread = Executors.newSingleThreadScheduledExecutor();

		pollThread.scheduleAtFixedRate(r, 0, this.config.getPollRateInMillis(), TimeUnit.MILLISECONDS);
		this.updateUI();

	}

	private void disconnectSocket() {
		if (this.socket != null) {
			try {
				this.socket.close();
				this.socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	public void getSplit() {

		String str = "getsplitindex\r\n";

		try (OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			
			osw.write(str, 0, str.length());
			osw.flush();

			String res = reader.readLine();
			Integer split = Integer.valueOf(res);

			this.reader.setNote(split < 0 ? 0 : split);
			this.updateUI();
			
		} catch (IOException e) {

			this.socket = null;
			this.pollThread.shutdown();
		}

	}
}
