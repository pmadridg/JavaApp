/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Pablo
 */
public class ConexionBD {
    
    private static ConexionBD instance;
    private ConexionBD(){
    }
    
    public static ConexionBD getInstance(){
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }
    
    public Connection getConnection(){
        Connection cn = null;
        String url = "jdbc:mysql://localhost/ventascorp";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(url,"root", "");
	} catch (Exception ex1) {
            System.err.println(ex1.getMessage());
        }        
        return cn;
    }
    
    public void close(Connection cn){
        try{
            if (cn != null) cn.close();
        }catch (Exception ex1){
            System.err.println(ex1.getMessage());
        }
    }
    
    public void close(Statement stm){
        try{
            if (stm != null) stm.close();
        }catch (Exception ex1){
            System.err.println(ex1.getMessage());
        }
    }
    
    public void close(ResultSet rs){
        try{
            if (rs != null) rs.close();
        }catch (Exception ex1){
            System.err.println(ex1.getMessage());
        }
    }
    
}
