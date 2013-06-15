import javax.swing.table.AbstractTableModel;

//defines a new table model appropriated for our table
public class ZModel extends AbstractTableModel{
	private Object[][] data;
	private String[] title;

	public ZModel(Object[][] data, String[] title){
		this.data = data;
		this.title = title;
	}

	public String getColumnName(int col) {
		return this.title[col];
	}

	public int getColumnCount() {
		return this.title.length;
	}

	public int getRowCount() {
		return this.data.length;
	}

	public Object getValueAt(int row, int col) {
		return this.data[row][col];
	}

	public void setValueAt(Object value, int row, int col) {
		this.data[row][col] = value;
	}

	public Class getColumnClass(int col){
		return this.data[0][col].getClass();
	}

	public boolean isCellEditable(int row, int col){
		if(col==0 || col==2)
			return false;
		else
			return true;
	}
}