package pl.grm.lobby.gui;

import java.awt.BorderLayout;

import javax.swing.*;

import pl.grm.lobby.connection.Connection;

public class ConnectionTab extends JPanel {

	private static final long serialVersionUID = 1L;
	private Connection connection;
	private JLabel labelID;
	private JLabel labelPort;
	private JLabel labelUserName;

	public ConnectionTab(Connection connection) {
		super();
		this.setName(connection.getName());
		this.connection = connection;

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panelID = new JPanel();
		panel_1.add(panelID);

		JLabel lblId = new JLabel("ID: ");
		panelID.add(lblId);

		labelID = new JLabel("");
		panelID.add(labelID);

		JPanel panelLocalPort = new JPanel();
		panel_1.add(panelLocalPort);

		JLabel lblPort = new JLabel("Port: ");
		panelLocalPort.add(lblPort);

		labelPort = new JLabel("");
		panelLocalPort.add(labelPort);

		JPanel panelUserName = new JPanel();
		panel_1.add(panelUserName);

		JLabel lblUserName = new JLabel("User Name: ");
		panelUserName.add(lblUserName);

		labelUserName = new JLabel("");
		panelUserName.add(labelUserName);
	}

	public void fillUP() {
		labelID.setText(connection.getID() + "");
		labelPort.setText(connection.getPort() + "");
		labelUserName.setText(connection.getUser().getName());
	}
}
