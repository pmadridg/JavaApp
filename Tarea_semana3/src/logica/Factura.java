/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;
import util.ConexionBD;

/**
 *
 * @author Pablo
 */
public class Factura {
    
    public String numeroFactura;
    public String fecha;
    public Cliente cliente;
    public int cantidad;
    public Producto producto;
    public int nroDetalle;
    public TableModel tablaDetalle;

    public Factura() {
    }

    public Factura(String numeroFactura, String fecha, Cliente cliente, int cantidad, Producto producto, int nroDetalle, TableModel tablaDetalle) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.producto = producto;
        this.nroDetalle = nroDetalle;
        this.tablaDetalle = tablaDetalle;
    }

    public Factura(String numeroFactura, String fecha) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        
    }
    
    

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

  

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getNroDetalle() {
        return nroDetalle;
    }

    public void setNroDetalle(int nroDetalle) {
        this.nroDetalle = nroDetalle;
    }

    public TableModel getTablaDetalle() {
        return tablaDetalle;
    }

    public void setTablaDetalle(TableModel tablaDetalle) {
        this.tablaDetalle = tablaDetalle;
    }
    
    
    
    public void registrarCabecera (){
    
         Connection cn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
       // List <Factura> ListaFactura = null;
        int reg = 0;
       // double cuentaSaldo =0;
        //Sentencias sql
        //String sqlFacturaInsert = "";
        
        
        String sqlFacturaInsert = "INSERT INTO tb_factura (fact_id, fact_fec,cli_ruc) VALUES(?,?,?) ";
        cn = ConexionBD.getInstance().getConnection();
            
        System.out.println("Movimiento.registrarTransferencia: Conexión ok");
        try {
            pstm = cn.prepareStatement(sqlFacturaInsert);
            //Enviar los parámetros de la sentencia SQL
            pstm.setString(1, this.numeroFactura);
            pstm.setString(2, this.fecha);
            pstm.setString(3, this.cliente.getRuc());
           
            
            //Ejecutar la sentencia en la BD
            reg = pstm.executeUpdate(); //devuelve # reg. afectados           
            System.out.println("Registros insertados: " + reg);
            //rs = pstm.executeQuery(); 
            
            System.out.println("Movimiento.registrarTransferencia: Consultar Saldo Cuenta Origen");
        } catch (SQLException ex) {
            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        
    }
    
    public void registrarDetalle(String facturaId) throws Exception{
    
        Connection cn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
       
        int reg = 0;
        double cuentaSaldo =0;
        
        
        StringBuilder sqlDetalleInsert = new StringBuilder();
        sqlDetalleInsert.append("INSERT INTO tb_detalle_factura (fac_id, pro_cod, df_qty ");
        sqlDetalleInsert.append("VALUES(?,?,?) ");
       
        
        
        StringBuilder sqlStockUpdate = new StringBuilder();
        sqlStockUpdate.append("UPDATE tb_producto SET pro_stock = ? ");
        sqlStockUpdate.append("WHERE pro_cod = ?");
        
        String sqlMovimientoSelectNum = "SELECT LAST_INSERT_ID() AS num";
        try {
            if (rs.next()){
                this.producto.setStock(rs.getInt("pro_stock"));
                //this.cuenta.setCliente(new Cliente(rs.getInt("cli_id"), rs.getString("cli_nom")));
            }
            
            //Validar Saldo disponible 
            if (this.getCantidad() > this.producto.getStock() ){
                System.out.println("Movimiento.registrarTransferencia: Saldo insuficiente");    
                throw new Exception("Saldo insuficiente");                
            }
            
            /************************************************
             * REGISTRAR LA TRANSACCIÓN RETIRO - DEPOSITO
             ************************************************/            
            //Registrar Retiro del Importe en la Cuenta Origen
            
            cuentaSaldo = this.getProducto().getStock() - this.cantidad; // Retiro
            pstm = cn.prepareStatement(sqlDetalleInsert.toString());             
            pstm.setString(1, facturaId); //Fecha actual
            pstm.setString(2, this.producto.getCodigo());
            pstm.setInt(3, this.getCantidad());
            
            
            //Ejecutar la sentencia en la BD
            reg = pstm.executeUpdate(); //devuelve # reg. afectados
            
            System.out.println("Movimiento.registrarTransferencia: Registrar Retiro");
            
            //Obtener el num transacción Cuenta Origen
            pstm = cn.prepareStatement(sqlMovimientoSelectNum);
            rs = pstm.executeQuery();
            
            System.out.println("Movimiento.registrarTransferencia: Consultar Nro Transacción");
            
            if(rs.next()){
                this.numeroFactura = rs.getString("fact_id");
            }                
            
             //Actualizar el Saldo en la Cuenta Origen
            pstm = cn.prepareStatement(sqlStockUpdate.toString());
            pstm.setString(1, this.producto.getCodigo());
            pstm.setInt(2, this.producto.getStock());
                        
             //Ejecutar la sentencia en la BD
            reg = pstm.executeUpdate(); //devuelve # reg. afectados
            
            System.out.println("Movimiento.registrarTransferencia: Actualizar Saldo Cuenta Origen");
            
             cn.commit();
            
        } catch (SQLException ex) {
            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        
    }
    
    public void registrarFactura() throws Exception{
    
      //Objetos JDBC
        Connection cn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List <Factura> ListaFactura = null;
        int reg = 0;
        double cuentaSaldo =0;
        //Sentencias sql
        //String sqlFacturaInsert = "";
        
        
        String sqlFacturaInsert = "INSERT INTO tb_factura (fac_id, fact_fec,cli_ruc) VALUES(?,?,?) ";
        
        StringBuilder sqlDetalleInsert = new StringBuilder();
        sqlDetalleInsert.append("INSERT INTO tb_detalle_factura (fac_id, pro_cod, df_qty ");
        sqlDetalleInsert.append("VALUES(?,?,?) ");
       
        
        
        StringBuilder sqlStockUpdate = new StringBuilder();
        sqlStockUpdate.append("UPDATE tb_producto SET pro_stock = ? ");
        sqlStockUpdate.append("WHERE pro_cod = ?");
        
        String sqlMovimientoSelectNum = "SELECT LAST_INSERT_ID() AS num";
        
        try{
            //Obtener la conexion a la BD
            cn = ConexionBD.getInstance().getConnection();
            
            System.out.println("Movimiento.registrarTransferencia: Conexión ok");
            
            //**** INICIAR TRANSACCIÓN ****//
            cn.setAutoCommit(false);
            
            /************************************************
             * CONSULTAR SALDOS DE CUENTAS ORIGEN - DESTINO
             ************************************************/
            //Consultar Saldo de Cuenta Origen
            //pstm = cn.prepareStatement(sqlCuentaSelect.toString());
            pstm = cn.prepareStatement(sqlFacturaInsert);
             //Enviar los parámetros de la sentencia SQL
            
            pstm.setString(1, this.numeroFactura);
            pstm.setString(2, this.fecha);
            pstm.setString(3, this.cliente.getRuc());
           
            
            //Ejecutar la sentencia en la BD
            reg = pstm.executeUpdate(); //devuelve # reg. afectados           
            System.out.println("Registros insertados: " + reg);
            rs = pstm.executeQuery(); 
            
            System.out.println("Movimiento.registrarTransferencia: Consultar Saldo Cuenta Origen");
            
            if (rs.next()){
                this.producto.setStock(rs.getInt("pro_stock"));
                //this.cuenta.setCliente(new Cliente(rs.getInt("cli_id"), rs.getString("cli_nom")));
            }
            
            //Consultar Saldo de Cuenta Destino
            /*pstm = cn.prepareStatement(sqlCuentaSelect.toString());
            pstm.setString(1, cuentaDestino.getNumero());
            //Ejecutar la sentencia en la BD
            rs = pstm.executeQuery();
            
            System.out.println("Movimiento.registrarTransferencia: Consultar Saldo Cuenta Destino");
            
            if (rs.next()){
                cuentaDestino.setSaldo(rs.getDouble("cue_sldo"));
                cuentaDestino.setCliente(new Cliente(rs.getInt("cli_id"), rs.getString("cli_nom")));
            }*/
            
            
            //Validar Saldo disponible 
            if (this.getCantidad() > this.producto.getStock() ){
                System.out.println("Movimiento.registrarTransferencia: Saldo insuficiente");    
                throw new Exception("Saldo insuficiente");                
            }
            
            /************************************************
             * REGISTRAR LA TRANSACCIÓN RETIRO - DEPOSITO
             ************************************************/            
            //Registrar Retiro del Importe en la Cuenta Origen
            cuentaSaldo = this.getProducto().getStock() - this.cantidad; // Retiro
            pstm = cn.prepareStatement(sqlDetalleInsert.toString());             
            pstm.setString(1, this.getNumeroFactura()); //Fecha actual
            pstm.setString(2, this.producto.getCodigo());
            pstm.setInt(3, this.getCantidad());
            
            
            //Ejecutar la sentencia en la BD
            reg = pstm.executeUpdate(); //devuelve # reg. afectados
            
            System.out.println("Movimiento.registrarTransferencia: Registrar Retiro");
            
            //Obtener el num transacción Cuenta Origen
            pstm = cn.prepareStatement(sqlMovimientoSelectNum);
            rs = pstm.executeQuery();
            
            System.out.println("Movimiento.registrarTransferencia: Consultar Nro Transacción");
            
            if(rs.next()){
                this.numeroFactura = rs.getString("fact_id");
            }                
            
            //Actualizar el Saldo en la Cuenta Origen
            pstm = cn.prepareStatement(sqlStockUpdate.toString());
            pstm.setString(1, getNumeroFactura());
            pstm.setInt(2, this.producto.getStock());
                        
             //Ejecutar la sentencia en la BD
            reg = pstm.executeUpdate(); //devuelve # reg. afectados
            
            System.out.println("Movimiento.registrarTransferencia: Actualizar Saldo Cuenta Origen");
            
           
            
            //**** CONFIRMAR TRANSACCIÓN ****//
            cn.commit();
            
            System.out.println("Movimiento.registrarTransferencia: Transacción confirmada");
            

        }catch(SQLException ex1){            
            try{
                //**** CANCELAR TRANSACCIÓN ****//
                cn.rollback();
                System.out.println("Movimiento.registrarTransferencia: Transacción cancelada");
            }catch(Exception ex){
                System.err.println(ex.getMessage());
            }   
            System.err.println(ex1.getMessage());
            throw  new Exception(ex1.getMessage());            
        }catch(Exception ex2){
            try{
                //**** CANCELAR TRANSACCIÓN ****//
                cn.rollback();
                System.out.println("Movimiento.registrarTransferencia: Transacción cancelada");
            }catch(Exception ex){
                System.err.println(ex.getMessage());
            }               
            System.err.println(ex2.getMessage());
            throw ex2;
        }finally{
            //Liberar recursos
            ConexionBD.getInstance().close(rs);
            ConexionBD.getInstance().close(pstm);
            ConexionBD.getInstance().close(cn);
            System.out.println("Movimiento.consultar: Recursos liberados");
        }
    }
    
    public void registrarFacturaCompleta() throws Exception {
    
     registrarCabecera();
     String pto= producto.getCodigo();
     int qty = getCantidad();
     String fact = getNumeroFactura();
     for (int row =0; row <nroDetalle; row++)
     {
         //numeroFactura = (String)(getTablaDetalle().getValueAt(row,0));
          pto = (String)(getTablaDetalle().getValueAt(row,1));
          qty = (int)(getTablaDetalle().getValueAt(row,4));
          registrarDetalle(fact);
     }
        
    
    }
}
