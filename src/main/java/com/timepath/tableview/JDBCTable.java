package com.timepath.tableview;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JDBCTable implements TableModel {

    CachedRowSet      rowSet;
    ResultSetMetaData metadata;
    int               numcols, numrows;

    public JDBCTable(CachedRowSet rowSetArg) throws SQLException {
        this.rowSet = rowSetArg;
        this.metadata = this.rowSet.getMetaData();
        numcols = metadata.getColumnCount();
        this.rowSet.beforeFirst();
        this.numrows = 0;
        while(this.rowSet.next()) {
            this.numrows++;
        }
        this.rowSet.beforeFirst();
    }

    public void addEventHandlersToRowSet(RowSetListener listener) {
        this.rowSet.addRowSetListener(listener);
    }

    public void close() {
        try {
            rowSet.getStatement().close();
        } catch(SQLException e) {
            JDBCUtilities.printSQLException(e);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public int getColumnCount() {
        return numcols;
    }

    public int getRowCount() {
        return numrows;
    }

    public String getColumnName(int column) {
        try {
            return this.metadata.getColumnLabel(column + 1);
        } catch(SQLException e) {
            return e.toString();
        }
    }

    public Class getColumnClass(int column) {
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            this.rowSet.absolute(rowIndex + 1);
            Object o = this.rowSet.getObject(columnIndex + 1);
            if(o == null) { return null; } else return o.toString();
        } catch(SQLException e) {
            return e.toString();
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setValueAt(Object value, int row, int column) {
        System.out.println("Calling setValueAt row " + row + ", column " + column);
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
