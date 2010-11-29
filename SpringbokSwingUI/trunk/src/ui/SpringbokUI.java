package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import model.DataModel;
import model.Result;
import model.UIMode;
import model.UIModel;
import views.ClosableTabbedPane;
import views.NewAlias;
import views.OperatorsToolBar;
import views.ResultPanel;
import controllers.DAOController;
import event.IModelUpdateListener;
import exception.CoreException;

public class SpringbokUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel desktop;
	private OperatorsToolBar toolBarOperators;
	private JSplitPane splitPane;

	private JMenu menuMode;

	private JMenuItem menuItemRelation;
	private JRadioButtonMenuItem menuItemAlgebraMode;
	private JRadioButtonMenuItem menuItemSqlMode;
	private JCheckBoxMenuItem menuItemOperatorTools;

	private JEditorPane editorPane = null;
	private ClosableTabbedPane tabbedPane = null;
	private JSplitPane outputSplitPane = null;

	private JTextPane textPane = null;

	// private DatabaseManager databaseManager = null;
	private DAOController controller = null;

	// models
	private UIModel uiModel;

	// update listeners
	private IModelUpdateListener uiModelUpdateListener;
	private ActionListener actionListener;

	@Override
	protected void frameInit() {
		super.frameInit();
		uiModel = new UIModel();
		setupModelUpdateListeners();
	}

	@Override
	public void dispose() {
		clearModelUpdateListener();
		super.dispose();
	}

	public SpringbokUI(String arg0) throws HeadlessException {
		super(arg0);

		// Make the big window be indented 50 pixels from each edge
		// of the screen.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height
				- inset * 2);

		// Set up the GUI.
		// desktop = new JDesktopPane(); //a specialized layered pane
		// desktop = new JLayeredPane();
		// desktop.setLayout(new BorderLayout());
		desktop = new JPanel();
		desktop.setLayout(new BorderLayout());

		setContentPane(desktop);
		setJMenuBar(createMenuBar());

		toolBarOperators = new OperatorsToolBar();
		toolBarOperators.addActionListener(actionListener);
		// toolBarOperators.setVisible(false);
		desktop.add(toolBarOperators, BorderLayout.NORTH);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		desktop.add(splitPane);

		editorPane = new JEditorPane();
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		Dimension editorMinimumSize = new Dimension(screenSize.width - inset
				* 2, 50);
		editorScrollPane.setMinimumSize(editorMinimumSize);

		splitPane.setTopComponent(editorScrollPane);

		outputSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		outputSplitPane.setOneTouchExpandable(true);
		outputSplitPane.setDividerLocation(450);

		tabbedPane = new ClosableTabbedPane();
		outputSplitPane.setTopComponent(tabbedPane);

		textPane = new JTextPane();
		JScrollPane outputscrollPane = new JScrollPane(textPane);
		Dimension outputMinimumSize = new Dimension(screenSize.width - inset
				* 2, 50);
		outputscrollPane.setMinimumSize(outputMinimumSize);
		outputSplitPane.setBottomComponent(textPane);

		splitPane.setBottomComponent(outputSplitPane);

		updateMenuItems();
		updateUI();

		// desktop.add(scrollPane, BorderLayout.CENTER);

		// Make dragging a little faster but perhaps uglier.
		// desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

	}

	/**
	 * @return
	 */
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		menuMode = new JMenu("Mode");
		menuMode.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menuMode);

		menuItemAlgebraMode = new JRadioButtonMenuItem("Algebra");
		menuItemAlgebraMode.setMnemonic(KeyEvent.VK_A);
		menuItemAlgebraMode.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuItemAlgebraMode.setActionCommand("algebra");
		menuItemAlgebraMode.addActionListener(this);
		menuMode.add(menuItemAlgebraMode);

		menuItemSqlMode = new JRadioButtonMenuItem("SQL");
		menuItemSqlMode.setMnemonic(KeyEvent.VK_S);
		menuItemSqlMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		menuItemSqlMode.setActionCommand("sql");
		menuItemSqlMode.addActionListener(this);
		menuMode.add(menuItemSqlMode);

		ButtonGroup buttonGroupMode = new ButtonGroup();
		buttonGroupMode.add(menuItemAlgebraMode);
		buttonGroupMode.add(menuItemSqlMode);

		// Set up the lone menu.
		JMenu menuNew = new JMenu("New");
		menuNew.setMnemonic(KeyEvent.VK_N);
		menuBar.add(menuNew);

		// Set up the first menu item.
		JMenuItem menuItemDatabase = new JMenuItem("Alias");
		menuItemDatabase.setMnemonic(KeyEvent.VK_A);
		menuItemDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		menuItemDatabase.setActionCommand("alias");
		menuItemDatabase.addActionListener(this);
		menuNew.add(menuItemDatabase);

		// Set up the second menu item.
		menuItemRelation = new JMenuItem("Relation");
		menuItemRelation.setMnemonic(KeyEvent.VK_R);
		menuItemRelation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.ALT_MASK));
		menuItemRelation.setActionCommand("relation");
		menuItemRelation.addActionListener(this);
		menuNew.add(menuItemRelation);

		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menuView);

		menuItemOperatorTools = new JCheckBoxMenuItem("Operators Tool Bar");
		menuItemOperatorTools.setMnemonic(KeyEvent.VK_O);
		menuItemOperatorTools.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.ALT_MASK));
		menuItemOperatorTools.setActionCommand("operatorsToolBar");
		menuItemOperatorTools.addActionListener(this);
		menuView.add(menuItemOperatorTools);

		return menuBar;
	}

	// Create a new internal frame.
	protected void createFrame() {
		NewAlias newDatabase = new NewAlias(uiModel);
		newDatabase.setVisible(true);
	}

	protected OperatorsToolBar createOperatorsToolBar() {
		OperatorsToolBar operatorsToolBar = new OperatorsToolBar();
		JButton button = new JButton("name");
		operatorsToolBar.add(button);
		return operatorsToolBar;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
		// JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		SpringbokUI frame = new SpringbokUI("Springbok");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setVisible(true);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// try {
		// UIManager.setLookAndFeel(
		// UIManager.getSystemLookAndFeelClassName());
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (UnsupportedLookAndFeelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		createAndShowGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("alias".equals(e.getActionCommand())) {
			createFrame();
		} else if ("relation".equals(e.getActionCommand())) {
			// quit();
		} else if ("operatorsToolBar".equals(e.getActionCommand())) {
			if (((JCheckBoxMenuItem) e.getSource()).isSelected()) {
				toolBarOperators.setVisible(true);
			} else {
				toolBarOperators.setVisible(false);
			}
		} else if ("algebra".equals(e.getActionCommand())) {
			uiModel.setUiMode(UIMode.ALGEBRA);
		} else if ("sql".equals(e.getActionCommand())) {
			uiModel.setUiMode(UIMode.SQL);
		}
	}

	private void updateMenuItems() {
		// disable some menus or menu items when Alias is not set
		menuMode.setEnabled(uiModel.getAlias() != null);
		menuItemRelation.setEnabled(uiModel.getAlias() != null);

		// disable tool bar buttons
		toolBarOperators
				.setVisibilityOfDefaultPanel(uiModel.getAlias() != null);

		if (uiModel.getAlias() != null) {
			try {

				controller = DAOController.getInstance(uiModel.getAlias());
				// databaseManager = new DAOController()
				// .getDatabaseManager(uiModel.getAlias());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// // Update title if Alias is there
		// if(uiModel.getAlias() != null) {
		// Alias alias = uiModel.getAlias();
		// Properties properties = alias.getProperties();
		// this.setTitle(this.getTitle() + " - Connected to " +
		// alias.getDriver());
		// }
	}

	private void updateUI() {

		if (uiModel.getUiMode() == UIMode.SQL) {
			// update menu items
			menuItemOperatorTools.setEnabled(false);
			menuItemOperatorTools.setSelected(false);

			// update Operators tool bar visibility
			// toolBarOperators.setVisible(false);
			toolBarOperators.setVisibilityOfAlgebraPanel(false);

			// update mode
			menuItemSqlMode.setSelected(true);

		} else if (uiModel.getUiMode() == UIMode.ALGEBRA) {
			// update menu items
			menuItemOperatorTools.setEnabled(true);
			menuItemOperatorTools.setSelected(true);

			// update Operators tool bar visibility
			// toolBarOperators.setVisible(true);
			toolBarOperators.setVisibilityOfAlgebraPanel(true);

			// update mode
			menuItemAlgebraMode.setSelected(true);
		}

	}

	private void updateTabbedPane(DataModel dataModel) {
		// tabbedPane.removeAll();
		if (dataModel != null) {						
			String closeImgLocation = "images" + File.separator + "TabCloseIcon.png";
//			URL imageURL = OperatorsToolBar.class.getClassLoader().getResource(
//					closeImgLocation);
//			ImageIcon icon = new ImageIcon(imageURL);			
			ResultPanel resultPanel = new ResultPanel(dataModel);
			tabbedPane.addTab(dataModel.getName(), resultPanel);			
			tabbedPane.setSelectedComponent(resultPanel);			
		}
	}

	/**
	 * Setup the model update listeners
	 */
	private void setupModelUpdateListeners() {
		if (uiModelUpdateListener == null) {
			uiModelUpdateListener = new IModelUpdateListener() {
				public void modelChanged(Object change) {
					// if (getContainer() != null)
					// getContainer().updateButtons();
					updateUI();
					updateMenuItems();
				}
			};
		}
		if (uiModel != null)
			uiModel.addUpdateListener(uiModelUpdateListener);

		if (actionListener == null) {
			actionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if ("Select".equals(e.getActionCommand())) {
						System.out.println("Pressed Select");						
						StringBuffer buffer = new StringBuffer(editorPane
								.getText());
						int i = editorPane.getCaretPosition();
						if (i == buffer.length()) {
							buffer.append('\u03C3');
						} else {
							buffer.insert(i, '\u03C3');
						}
						editorPane.setText(buffer.toString());						
					} else if ("Project".equals(e.getActionCommand())) {
						System.out.println("Pressed Pelect");
						StringBuffer buffer = new StringBuffer(editorPane
								.getText());
						int i = editorPane.getCaretPosition();
						if (i == buffer.length()) {
							buffer.append('\u03C0');
						} else {
							buffer.insert(i, '\u03C0');
						}
						editorPane.setText(buffer.toString());						
					} else if ("Assign".equals(e.getActionCommand())) {
						System.out.println("Pressed Assign");
						StringBuffer buffer = new StringBuffer(editorPane
								.getText());
						int i = editorPane.getCaretPosition();
						if (i == buffer.length()) {
							buffer.append('\u2190');
						} else {
							buffer.insert(i, '\u2190');
						}
						editorPane.setText(buffer.toString());
					} else if ("Validate".equals(e.getActionCommand())) {
						System.out.println("Pressed Validate");
					} else if ("Execute".equals(e.getActionCommand())) {
						String text = editorPane.getSelectedText();
						System.out.println(text);
						// block for empty strings
						if (text == null || text.length() == 0)
							return;

						try {
							// if multiple queries are there by separating ";"
							// then take one by one
							String[] queries = text.split(";");
							for (String query : queries) {
								
								if(query != null)
									query = query.trim();	
								
								if (query == null || query.length() == 0)
									continue;
								
								long t1 = System.currentTimeMillis();
								Result result = controller
										.runStatement(query);
								long t2 = System.currentTimeMillis();
								textPane
										.setText("Query executed successfully, took "
												+ (t2 - t1)
												/ 1000.0
												+ " seconds");
								if (result != null
										&& result.getDataModel() != null) {
									updateTabbedPane(result.getDataModel());
								}
							}						
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							textPane.setText(e1.getLocalizedMessage());
						}
					}

				}

			};
		}

	}

	/**
	 * Clears the model update listener.
	 */
	private void clearModelUpdateListener() {
		if (uiModel != null && uiModelUpdateListener != null)
			uiModel.removeUpdateListener(uiModelUpdateListener);

	}
}
