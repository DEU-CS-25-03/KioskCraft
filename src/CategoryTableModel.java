import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CategoryTableModel extends AbstractTableModel {
    private final String[] columnNames = {"카테고리", ""};
    private final List<String> data;

    public CategoryTableModel(List<String> data) {
        this.data = data;
    }

    public String getCategoryAt(int row) {
        return data.get(row);
    }

    public void setData(List<String> newData) {
        data.clear();
        data.addAll(newData);
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columnNames.length; }
    @Override public String getColumnName(int column) { return columnNames[column]; }
    @Override public boolean isCellEditable(int row, int column) { return column == 1; }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) return data.get(rowIndex);
        else return "X";
    }
}
