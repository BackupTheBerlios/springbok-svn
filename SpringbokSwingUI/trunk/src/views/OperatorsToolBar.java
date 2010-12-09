package views;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class OperatorsToolBar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel algebraPanel = null;
	private JPanel sqlPanel = null;
	private JPanel defaultPanel = null;
	private ActionListener actionListener = null;

	public OperatorsToolBar() {
		super("Operations");
		algebraPanel = new JPanel();
		algebraPanel
				.setLayout(new BoxLayout(algebraPanel, BoxLayout.LINE_AXIS));
		algebraPanel
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(algebraPanel);

		defaultPanel = new JPanel();
		defaultPanel
				.setLayout(new BoxLayout(defaultPanel, BoxLayout.LINE_AXIS));
		defaultPanel
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(defaultPanel);
		
		sqlPanel = new JPanel();
		sqlPanel
				.setLayout(new BoxLayout(sqlPanel, BoxLayout.LINE_AXIS));
		sqlPanel
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(sqlPanel);

		makeToolBarButton("Select", "Select", "select", "select", algebraPanel);
		makeToolBarButton("Project", "Project", "project", "project",
				algebraPanel);
		makeToolBarButton("Assign", "Assign", "assign", "assign", algebraPanel);

		algebraPanel.add(Box.createHorizontalStrut(50));

		makeToolBarButton("Convert", "Convert", "convert", "convert",
				defaultPanel);
		defaultPanel.add(Box.createHorizontalStrut(50));
		
		makeToolBarButton("Validate", "Validate", "validate", "validate",
				sqlPanel);
		makeToolBarButton("Execute", "Execute", "execute", "execute",
				sqlPanel);

	}
	
	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	protected JButton makeToolBarButton(String imageName, String actionCommand,
			String toolTipText, String altText, JPanel panel) {
		// Look for the image.
		String imgLocation = "images" + '/' + imageName + ".png";
		URL imageURL = OperatorsToolBar.class.getClassLoader().getResource(
				imgLocation);

		// Create and initialize the button.
		JButton button = new JButton();
		// button.setBorder(null);
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);
		//button.addActionListener(this.actionListener);

		if (imageURL != null) { // image found
			button.setIcon(new ImageIcon(imageURL, altText));
		} else { // no image found
			button.setText(altText);
			System.err.println("Resource not found: " + imgLocation);
		}
		button.setSize(20, 16);
		panel.add(button);

		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Select".equals(e.getActionCommand())) {
			System.out.println("Pressed Select");
			this.actionListener.actionPerformed(e);
		} else if ("Project".equals(e.getActionCommand())) {
			System.out.println("Pressed Pelect");
			this.actionListener.actionPerformed(e);
		} else if ("Assign".equals(e.getActionCommand())) {
			System.out.println("Pressed Assign");
			this.actionListener.actionPerformed(e);
		} else if ("Validate".equals(e.getActionCommand())) {
			System.out.println("Pressed Validate");
			this.actionListener.actionPerformed(e);
		} else if ("Execute".equals(e.getActionCommand())) {
			System.out.println("Pressed Execute");
			this.actionListener.actionPerformed(e);
		} else if ("Convert".equals(e.getActionCommand())) {
			System.out.println("Pressed Convert");
			this.actionListener.actionPerformed(e);
		}
	}

	public void setVisibilityOfAlgebraPanel(boolean flag) {
		algebraPanel.setVisible(flag);
	}
	
	public void setVisibilityOfSqlPanel(boolean flag) {
		sqlPanel.setVisible(flag);
	}
	
	public void setVisibilityOfDefaultPanel(boolean flag) {
		defaultPanel.setVisible(flag);
	}
}
