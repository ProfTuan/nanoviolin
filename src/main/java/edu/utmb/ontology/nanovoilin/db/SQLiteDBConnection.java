/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package edu.utmb.ontology.nanovoilin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mac
 */
public class SQLiteDBConnection {
    
    private Connection connection = null;
    
    private String db_name = "";
    
    private SQLiteDBConnection() {
        
         
        
    }
    
    private void createTables(){
        
        try {
            //drop table if exists
            
            //create the table

            PreparedStatement statement = connection.prepareStatement(SQLCommands.create_vaccine_nanopub_table);
            statement.executeUpdate();
            clearTableData(SQLCommands.vaccine_nanopub_table);
            
        } catch (SQLException ex) {
            System.getLogger(SQLiteDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    private void clearTableData(String table_name){
        
        try {
            Statement statement = connection.createStatement();
            
            String sql = SQLCommands.clear_table.replace("table_name", table_name);
            System.out.println(sql);
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.getLogger(SQLiteDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void initConnection(String database_name){
        
        db_name = database_name;
        
        try {
            Class.forName("org.sqlite.JDBC");
 
            connection = DriverManager.getConnection("jdbc:sqlite:" + db_name);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLiteDBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.getLogger(SQLiteDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        createTables();
    }
    
    
    
    public void insertViolinNanoPub(String id, String label, String nanopub_content){
        
        try {
            //PreparedStatement statement = connection.prepareStatement("");

            PreparedStatement statement = connection.prepareCall("INSERT INTO " + db_name + " () VALUES (?)");
            
            
            
            
        } catch (SQLException ex) {
            System.getLogger(SQLiteDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public static SQLiteDBConnection getInstance() {
        return SQLiteDBConnectionHolder.INSTANCE;
    }
    
    private static class SQLiteDBConnectionHolder {

        private static final SQLiteDBConnection INSTANCE = new SQLiteDBConnection();
    }
}
