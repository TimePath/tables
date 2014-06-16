package com.timepath.tableview;

import com.sun.rowset.CachedRowSetImpl;
import org.apache.commons.io.IOUtils;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends JFrame implements RowSetListener {

    JDBCUtilities settings;
    Connection    conn;
    JTabbedPane tabs = new JTabbedPane();

    public Main(JDBCUtilities settingsArg) throws SQLException {
        super("Table viewer");
        this.settings = settingsArg;
        conn = settings.getConnection();
        init(conn);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    conn.close();
                } catch(SQLException sqle) {
                    JDBCUtilities.printSQLException(sqle);
                }
                System.exit(0);
            }
        });
        String[] types = { "TABLE" };
        ResultSet rs = conn.getMetaData().getTables(null, null, "%", types);
        while(rs.next()) {
            String name = rs.getString("TABLE_NAME");
            JTable table;
            table = new JTable(); // Displays the table
            JDBCTable model = new JDBCTable(getContentsOfCoffeesTable(name));
            model.addEventHandlersToRowSet(this);
            table.setModel(model);
            tabs.add(name, new JScrollPane(table));
        }
        Container contentPane = getContentPane();
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        contentPane.add(tabs, c);
    }

    public static void main(String[] args) throws Exception {
        JDBCUtilities myJDBCTutorialUtilities = new JDBCUtilities();
        try {
            JFrame frame = new Main(myJDBCTutorialUtilities);
            frame.pack();
            frame.setVisible(true);
        } catch(SQLException sqle) {
            JDBCUtilities.printSQLException(sqle);
        } catch(Exception e) {
            System.out.println("Unexpected exception");
            e.printStackTrace();
        }
    }

    public CachedRowSet getContentsOfCoffeesTable(String table) throws SQLException {
        CachedRowSet crs = null;
        try {
            crs = new CachedRowSetImpl();
            crs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
            crs.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            crs.setUrl(settings.urlString);
            crs.setCommand("select * from " + table);
            crs.execute();
        } catch(SQLException e) {
            JDBCUtilities.printSQLException(e);
        }
        return crs;
    }

    private void init(final Connection connection) throws SQLException {
        Statement stmt = null;
        try {
            for(String sql : IOUtils.toString(this.getClass().getResourceAsStream("/init.sql")).split(";")) {
                stmt = connection.createStatement();
                stmt.execute(sql);
            }
        } catch(SQLException e) {
            JDBCUtilities.printSQLException(e);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(stmt != null) { stmt.close(); }
        }
    }

    public void rowSetChanged(RowSetEvent event) { }

    public void rowChanged(RowSetEvent event) { }

    public void cursorMoved(RowSetEvent event) { }
}
