package views;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelTitle = null;
	private JLabel labelDescription = null;
	
	public TitlePanel(String title, String description, int width) {
		super();
		setLayout(null);
		labelTitle = new JLabel(title);
		labelTitle.setBounds(5, 5, width-1, 15);
		labelTitle.setFont(labelTitle.getFont().deriveFont(Font.BOLD, 14));
		add(labelTitle, 0);
		
		labelDescription = new JLabel(description);
		labelDescription.setBounds(10, 20, 280, 25);
		labelDescription.setFont(labelDescription.getFont().deriveFont(Font.ITALIC, 12));
		add(labelDescription, 1);
		setBackground(Color.WHITE);
		setSize(width, 50);
		setBorder(BorderFactory.createEtchedBorder());
	}

}
