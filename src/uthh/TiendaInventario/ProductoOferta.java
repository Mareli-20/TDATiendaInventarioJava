/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uthh.TiendaInventario;

/**
 *
 * @author erika
 */
public class ProductoOferta extends Producto {
 
    /**
     * Constructor: valida y construye mediante super(...), aplicando
     * el 20% de descuento al precio. Hereda todos los métodos del padre.
     */
    public ProductoOferta(String nombre, double precio, int stock) {
        super(nombre, precio * 0.80, stock);
    }
}
 