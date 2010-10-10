package views;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import model.DataModel;

public class ResultPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultPanel(DataModel dataModel) {
		super();
		MigLayout migLayout = new MigLayout("wrap 2");

		this.setLayout(migLayout);
		
		
		TableModel tableModel = new DefaultTableModel(dataModel.getData(), dataModel.getColumnNames());		
		JTable table = new JTable(tableModel);		
		JScrollPane scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		add(scrollPane, "growx, pushx");
	}

}
