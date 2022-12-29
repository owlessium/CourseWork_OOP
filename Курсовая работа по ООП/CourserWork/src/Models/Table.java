package Models;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Table extends JTable {
    private TableType tableType;

    public Table(DefaultTableModel tableModel){
        super(tableModel);
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public void changeHeaders(Object[] headers){
        for (int i = 0; i<4; i++){
            getColumnModel().getColumn(i).setHeaderValue(headers[i]);
        }

        updateUI();
    }

}
