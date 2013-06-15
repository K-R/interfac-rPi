import javax.swing.JComboBox;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.table.AbstractTableModel;

public class ComboRenderer extends JComboBox implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int col) {
		//make appears the kernel selected
		this.removeAllItems();
		this.addItem(((AbstractTableModel)table.getModel()).getValueAt(row, 1));
		return this;
	}
}
