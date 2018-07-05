/**
 *
 */
package com.waternotifier.waternotifierlibrary;

import java.sql.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

import java.io.File;
import java.net.URI;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author rajeshmanvi
 */
public class SqliteConnection {
    
    /**
     *
     */
    Connection conn = null;
    
    public SqliteConnection() {
        // TODO Auto-generated constructor stub
    }
    
    public static Connection dbConnector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\WNDB\\Notifier.db");
//			Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\AppsRIM\\AndroidApps\\DATABASES\\Note 8\\Notifier.db");
//			JOptionPane.showMessageDialog(null, "Database Connection Successful");
            return conn;
        } catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e);
            LogToFile.log(e, "warning", "SqliteConnection.dbConnector()");
            return null;
        }
    }
    
    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    public static void createNewDatabase(String fileName) {
        
        String folderpath = "C:\\WNDB\\BACKUP";
        String url = null;

//        if (Files.exists(folder,LinkOption:NOFOLLOW_LINKS)) {
        if (!Paths.get(folderpath).toFile().isDirectory()) {
            System.out.println("Path C:\\WNDB\\BACKUP NOT exists");
            File file = new File(folderpath);
            if (file.mkdir()) {
                url = "jdbc:sqlite:" + folderpath + "\\" + fileName;
            } else {
                url = "jdbc:sqlite:C:/" + fileName;
            }
        } else {
            System.out.println("Path C:\\WNDB\\BACKUP exists");
            url = "jdbc:sqlite:C:/WNDB/BACKUP/" + fileName;
        }
        
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            LogToFile.log(e, "warning", "SqliteConnection.createNewDatabase()");
        }
    }
}
