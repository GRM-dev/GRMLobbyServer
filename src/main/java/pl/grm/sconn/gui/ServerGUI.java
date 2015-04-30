package pl.grm.sconn.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import pl.grm.sconn.*;
import pl.grm.sconn.commands.*;
import pl.grm.sconn.connection.*;

public class ServerGUI extends JFrame implements Observer {
	private static final long	serialVersionUID	= 1L;
	private JPanel				contentPane;
	private JLabel				lblStatusA;
	private Canvas				canvas;
	private JTabbedPane			tabP;
	private JLabel				lblConnAmountA;
	private JTextArea			console;
	private JTextField			consoleInput;
	private ServerMain			serverMain;
	private CommandManager		commandManager;
	
	/**
	 * Create the frame.
	 * 
	 * @param serverMain
	 */
	public ServerGUI(ServerMain serverMain) {
		this.serverMain = serverMain;
		setTitle("Server GUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
		mntmClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mnFile.add(mntmClose);
		
		JMenuItem mntmStartServer = new JMenuItem("Start Server");
		mntmStartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverMain.startServer();
			}
		});
		mnFile.add(mntmStartServer);
		
		JMenuItem mntmStopServer = new JMenuItem("Stop Server");
		mntmStopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverMain.stopServer();
			}
		});
		mnFile.add(mntmStopServer);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmSettings = new JMenuItem("Settings ...");
		mnTools.add(mntmSettings);
		
		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		menuBar.add(lblStatus);
		
		canvas = new Canvas();
		menuBar.add(canvas);
		
		lblStatusA = new JLabel("Off");
		lblStatusA.setForeground(Color.RED);
		menuBar.add(lblStatusA);
		
		JPanel botP = new JPanel();
		contentPane.add(botP, BorderLayout.SOUTH);
		
		JPanel leftP = new JPanel();
		contentPane.add(leftP, BorderLayout.WEST);
		leftP.setLayout(new GridLayout(5, 1, 0, 0));
		
		JLabel lblIConnAmount = new JLabel("Ilosc polaczen");
		leftP.add(lblIConnAmount);
		
		lblConnAmountA = new JLabel("");
		leftP.add(lblConnAmountA);
		
		JPanel rightP = new JPanel();
		contentPane.add(rightP, BorderLayout.EAST);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane);
		
		tabP = new JTabbedPane(SwingConstants.TOP);
		splitPane.add(tabP, JSplitPane.TOP);
		
		JPanel consoleP = new JPanel();
		consoleP.setLayout(new BorderLayout(0, 0));
		splitPane.add(consoleP, JSplitPane.BOTTOM);
		
		console = new JTextArea();
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scrollPane = new JScrollPane(console);
		consoleP.add(scrollPane, BorderLayout.CENTER);
		
		consoleInput = new JTextField();
		consoleInput.setColumns(10);
		consoleInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = consoleInput.getText();
				if (input == null || input == "") { return; }
				consoleInput.setText("");
				serverMain.executeCommand(input);
				commandManager.addCommandToList(input);
			}
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
		}
		if (o instanceof ServerMain) {
			if (serverMain.isRunning()) {
				lblStatusA.setText("On");
				lblStatusA.setForeground(Color.GREEN);
			} else {
				lblStatusA.setText("Off");
				lblStatusA.setForeground(Color.RED);
			}
		}
	}
	
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
}
