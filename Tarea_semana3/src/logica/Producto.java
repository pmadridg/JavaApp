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
public class Producto {
    
    public String codigo;
    public String descripcion;
    public String unidadMedida;
    public double precio;
    public int stock;

    public Producto() {
    }

    public Producto(String codigo, String descripcion, String unidadMedida, double precio, int stock) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(String codigo) {
        this.codigo = codigo;
        this.descripcion = "";
        this.unidadMedida = "";
        this.precio = 0;
        this.stock = 0;
    }

       
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public void consultar(){        
         //Objetos JDBC
        Connection cn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        
        //Sentencias sql
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pro_cod, pro_nom, pro_um, pro_prec, pro_stock ");
        sql.append("FROM tb_producto ");
        sql.append("WHERE pro_cod = ?");
        
        try{
            //Obtener la conexion a la BD
            cn = ConexionBD.getInstance().getConnection();
            
            System.out.println("Producto.consultar: Conexión OK");
            
            //Enviar sql con parámetros
            pstm = cn.prepareStatement(sql.toString());
            pstm.setString(1, this.codigo);
            
            //Obtener el resultado de la consulta
            rs = pstm.executeQuery();            
            System.out.println("Cuenta.consultar: Query ejecutado");
            
            //El resultado de la consulta debe devolver solo una Cuenta
            if(rs.next()){
                Producto producto = new Producto(rs.getString("pro_cod"));
                        /*,rs.getString("pro_des_"), rs.getString("pro_um"),
                rs.getDouble("pro_pre"),rs.getInt("pro_stock"));*/
                //Cada Cuenta almacena un objeto Cliente
                this.setDescripcion(rs.getString("pro_nom"));
                this.setUnidadMedida(rs.getString("pro_um"));
                this.setPrecio(rs.getDouble("pro_prec"));
                this.setStock(rs.getInt("pro_stock"));
            }else{
                Producto producto = new Producto("no existe");
                //Cada Cuenta almacena un objeto Cliente
                 this.setDescripcion("no existe");
                this.setUnidadMedida("error");
                this.setPrecio(0);
                this.setStock(0);              
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
