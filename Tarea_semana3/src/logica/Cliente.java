/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.ConexionBD;

/**
 *
 * @author Pablo
 */
public class Cliente {
    
    public String ruc;
    public String nombre;
    public String direccion;

    public Cliente() {
    }

    public Cliente(String ruc, String nombre, String direccion) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
        public void consultar(){        
         //Objetos JDBC
        Connection cn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        
        //Sentencias sql
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cli_ruc, cli_raz, cli_dir ");
        sql.append("FROM tb_cliente ");
        sql.append("WHERE cli_ruc = ?");
        
        try{
            //Obtener la conexion a la BD
            cn = ConexionBD.getInstance().getConnection();
            
            System.out.println("Cliente.consultar: Conexión OK");
            
            //Enviar sql con parámetros
            pstm = cn.prepareStatement(sql.toString());
            pstm.setString(1, this.ruc);
            
            //Obtener el resultado de la consulta
            rs = pstm.executeQuery();            
            System.out.println("Cuenta.consultar: Query ejecutado");
            
            //El resultado de la consulta debe devolver solo una Cuenta
            if(rs.next()){
                Cliente cliente = new Cliente(rs.getString("cli_ruc"),rs.getString("cli_raz"), rs.getString("cli_dir"));
                //Cada Cuenta almacena un objeto Cliente
                this.setNombre(rs.getString("cli_raz"));
                this.setDireccion(rs.getString("cli_dir"));       
            }else{
                Cliente cliente = new Cliente("no existe","Cuenta no existe","no existe");
                //Cada Cuenta almacena un objeto Cliente
                this.setNombre("error");
                this.setDireccion("error");                
            }
            
            System.out.println("Cuenta.consultar: Valores del query asignado");

        }catch(Exception ex1){
            System.err.println(ex1.getMessage());
        }finally{
            //Liberar recursos
            ConexionBD.getInstance().close(rs);
            ConexionBD.getInstance().close(pstm);
            ConexionBD.getInstance().close(cn);
            System.out.println("Movimiento.consultar: Recursos liberados");
        }               
    }
}
