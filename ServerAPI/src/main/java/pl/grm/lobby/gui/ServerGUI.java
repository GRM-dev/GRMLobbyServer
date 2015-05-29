package pl.grm.lobby.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import pl.grm.lobby.*;
import pl.grm.lobby.api.ServerMain;
import pl.grm.lobby.commands.*;
import pl.grm.lobby.connection.Connector;

public class ServerGUI extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblStatusA;
	private ClosableTabbedPane tabP;
	private JLabel lblConnAmountA;
	private JTextArea console;
	private JTextField consoleInput;
	private ServerMain serverMain;
	private CommandManager commandManager;
	private JToggleButton tglbtnStartStop;

	/**
	 * Create the frame.
	 * 
	 * @param serverMain
	 */
	public ServerGUI(ServerMain serverMain) {
		this.serverMain = serverMain;
		setTitle("Server GUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel topP = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topP.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topP, BorderLayout.NORTH);

		JMenuBar menuBar = new JMenuBar();
		topP.add(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(e -> dispose());
		mnFile.add(mntmClose);

		JMenuItem mntmStartServer = new JMenuItem("Start Server");
		mntmStartServer.addActionListener(e -> serverMain.startServer());
		mnFile.add(mntmStartServer);

		JMenuItem mntmStopServer = new JMenuItem("Stop Server");
		mntmStopServer.addActionListener(e -> serverMain.stopServer());
		mnFile.add(mntmStopServer);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenuItem mntmSettings = new JMenuItem("Settings ...");
		mnTools.add(mntmSettings);
		tglbtnStartStop = new JToggleButton("Start/Stop");
		tglbtnStartStop.addActionListener(e -> {
			if (tglbtnStartStop.isSelected()) {
				serverMain.startServer();
			} else {
				serverMain.stopServer();
			}
		});
		menuBar.add(tglbtnStartStop);

		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		menuBar.add(lblStatus);

		lblStatusA = new JLabel("Off");
		lblStatusA.setForeground(Color.RED);
		menuBar.add(lblStatusA);

		JPanel botP = new JPanel();
		contentPane.add(botP, BorderLayout.SOUTH);

		JPanel leftP = new JPanel();
		contentPane.add(leftP, BorderLayout.WEST);
		leftP.setLayout(new GridLayout(5, 1, 0, 0));

		JLabel lblIConnAmount = new JLabel("Connections:");
		leftP.add(lblIConnAmount);

		lblConnAmountA = new JLabel("0");
		leftP.add(lblConnAmountA);

		JPanel rightP = new JPanel();
		contentPane.add(rightP, BorderLayout.EAST);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		contentPane.add(splitPane);

		tabP = new ClosableTabbedPane();
		splitPane.add(tabP, JSplitPane.TOP);

		JPanel consoleP = new JPanel();
		consoleP.setLayout(new BorderLayout(0, 0));
		splitPane.add(consoleP, JSplitPane.BOTTOM);

		console = new JTextArea();
		console.setEditable(false);
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane(console);
		consoleP.add(scrollPane, BorderLayout.CENTER);

		consoleInput = new JTextField();
		consoleInput.setColumns(10);
		consoleInput.addActionListener(e -> {
			String input = consoleInput.getText();
			if (input == null || input == "") { return; }
			consoleInput.setText("");
			serverMain.getCM().executeCommand(Commands.getCommand(input), input, false, CommandType.SERVER);
			commandManager.addCommandToList(input);
		});
		consoleInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					String input = consoleInput.getText();
					if (input == null || input == "" || !commandManager.wasExecuted(input)) {
						consoleInput.setText(commandManager.getLastCommand());
					} else {
						String previousCommand = commandManager.getPreviousCommand(input);
						if (previousCommand != "") {
							consoleInput.setText(previousCommand);
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					String input = consoleInput.getText();
					String nextCommand = commandManager.getNextCommand(input);
					if (nextCommand != "" || input.equals(commandManager.getLastCommand())) {
						consoleInput.setText(nextCommand);
					}
				}
			}
		});
		consoleP.add(consoleInput, BorderLayout.SOUTH);

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				consoleInput.requestFocus();
			}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {
				serverMain.stopServer();
				System.exit(0);
			}

			@Override
			public void windowActivated(WindowEvent e) {}
		});
		CLogger.setConsole(console);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Connector) {
			CLogger.info("N Conn Upd");
			lblConnAmountA.setText(serverMain.getConnectionsAmount() + "");
		}
		if (o instanceof ServerMain) {
			lblConnAmountA.setText(serverMain.getConnectionsAmount() + "");
			if (serverMain.isRunning()) {
				lblStatusA.setText("On");
				lblStatusA.setForeground(Color.GREEN);
				tglbtnStartStop.setSelected(true);
			} else {
				lblStatusA.setText("Off");
				lblStatusA.setForeground(Color.RED);
				tglbtnStartStop.setSelected(false);
				tabP.removeAll();
			}
		}
	}

	public void addTab(ConnectionTab tab) {
		tabP.add(tab.getName(), tab);
	}

	public void removeTab(ConnectionTab tab) {
		tabP.remove(tab);
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
}
