package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Alias;
import model.DatabaseDriver;
import model.DatabaseDriverFactory;
import model.UIModel;
import net.miginfocom.swing.MigLayout;

public class NewAlias extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private TitlePanel titlePanel = null;
	private JPanel panel = null;
	private JPanel panelButtons = null;

	private JLabel errorLabel = null;
	
	private JTextField textName;
	private JTextField textUserName;
	private JTextField textPassword;
	private JComboBox comboBox;
	private JTextField textConnectionURL;

	private JButton okButton = null;
	private JButton cancelButton = null;
	private UIModel uiModel;

	public NewAlias(UIModel uiModel) {
		super("New Alias");
		this.uiModel = uiModel;
		setLayout(null);
		setResizable(false);
		titlePanel = new TitlePanel("Set Connection",
				"Set the connection properties to the database", 300);
		titlePanel.setBounds(0, 0, 300, 50);

		add(titlePanel);

		panel = new JPanel();
		panel.setBounds(2, 50, 290, 150);
		add(panel);

		MigLayout migLayout = new MigLayout("wrap 2");

		panel.setLayout(migLayout);

		JLabel labelDatabaseName = new JLabel("Name");
		panel.add(labelDatabaseName);
		textName = new JTextField();
		textName.setText("test");
		panel.add(textName, "growx, pushx");

		JLabel labelDriver = new JLabel("Driver");
		panel.add(labelDriver);
		comboBox = new JComboBox(new DatabaseDriverFactory().getAllDrivers()
				.toArray());

		JTextField textDriver = new JTextField("Driver");
		panel.add(comboBox, "growx, pushx");

		JLabel labelUserName = new JLabel("User Name");
		panel.add(labelUserName);
		textUserName = new JTextField("User Name");
		textUserName.setText("test");
		panel.add(textUserName, "growx, pushx");

		JLabel labelPassword = new JLabel("Password");
		panel.add(labelPassword);
		textPassword = new JTextField("Password");
		textPassword.setText("test");
		panel.add(textPassword, "growx, pushx");

		JLabel labelConnectionURL = new JLabel("Connection URL");
		panel.add(labelConnectionURL);
		// jdbc:derby:<dbName>;create=true
		textConnectionURL = new JTextField("jdbc:derby:test;create=true");
		panel.add(textConnectionURL, "growx, pushx");
		
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);
		
		panelButtons = new JPanel();
		panelButtons.setBounds(0, 265, 300, 50);
		panelButtons.setBorder(BorderFactory.createEtchedBorder());
		FlowLayout flowLayout = new FlowLayout(FlowLayout.TRAILING);
		flowLayout.setHgap(5);
		flowLayout.setVgap(5);
		panelButtons.setLayout(flowLayout);

		okButton = new JButton("OK");
		okButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);

		add(panelButtons);

		// ...Then set the window size or call pack...
		setSize(300, 340);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Set the window's location.
		setLocation(screenSize.width / 2 - 150, screenSize.height / 2 - 150);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if("OK".equals(arg0.getActionCommand())) {
			Properties properties = new Properties();
			properties.put("user", textUserName.getText());
			properties.put("password", textPassword.getText());			
			Alias alias = new Alias();
			alias.setName(textName.getText());
			alias.setDriver(((DatabaseDriver)comboBox.getSelectedItem()).getValue());
			alias.setDriverClass(((DatabaseDriver) comboBox.getSelectedItem())
					.getKey());
			alias.setConnectionUrl(textConnectionURL.getText());
			alias.setProperties(properties);
			this.uiModel.setAlias(alias);
		} else {
			this.uiModel.setAlias(null);
		}
		this.dispose();
	}

}
