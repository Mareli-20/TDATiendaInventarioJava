/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uthh.TiendaInventario;

/**
 *
 * @author erika
 */
public class Producto {
 
    // Stock máximo permitido (también evita recursiones demasiado profundas)
    private static final int STOCK_MAXIMO = 5000;
 
    // ---------- Atributos propios del modelo (privados = encapsulamiento) ----------
    private String nombre;   // Nombre del producto
    private double precio;   // Precio por unidad (en pesos)
    private int stock;       // Unidades disponibles en inventario
 
    /**
     * Constructor con validaciones pertinentes.
     * Lanza IllegalArgumentException si algún dato es inválido.
     */
    public Producto(String nombre, double precio, int stock) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        if (stock < 0 || stock > STOCK_MAXIMO) {
            throw new IllegalArgumentException("El stock debe estar entre 0 y " + STOCK_MAXIMO + ".");
        }
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
    }
 
    // ======================= MÉTODOS DE ACCESO (4) =======================
 
    /** Método 1 (getter): devuelve el nombre del producto. */
    public String getNombre() {
        return nombre;
    }
 
    /** Método 2 (getter): devuelve el precio por unidad. */
    public double getPrecio() {
        return precio;
    }
 
    /** Método 3 (getter): devuelve las unidades en inventario. */
    public int getStock() {
        return stock;
    }
 
    /** Método 4 (setter): cambia el precio validando que sea positivo. */
    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0.");
        }
        this.precio = precio;
    }
 
    // ================= MÉTODOS DE LÓGICA DEL DOMINIO (4) =================
 
    /**
     * Método 5 (lógica, RECURSIVO): calcula el total de vender 'cantidad' unidades.
     * CASO BASE: cantidad == 0 -> no hay unidades, el total es 0.
     * CONDICIÓN DE AVANCE: se llama con (cantidad - 1), acercándose al caso base.
     * Relación: total(n) = precio + total(n - 1)
     */
    public double calcularTotalVenta(int cantidad) {
        if (cantidad < 0 || cantidad > STOCK_MAXIMO) {
            throw new IllegalArgumentException("La cantidad debe estar entre 0 y " + STOCK_MAXIMO + ".");
        }
        if (cantidad == 0) {                                  // caso base
            return 0;
        }
        return precio + calcularTotalVenta(cantidad - 1);     // avance
    }
 
    /**
     * Método 6 (lógica): registra una venta. Descuenta las unidades del
     * inventario y devuelve el total cobrado (usando el método recursivo).
     */
    public double vender(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a vender debe ser mayor que 0.");
        }
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente: solo hay " + stock + " unidad(es).");
        }
        stock -= cantidad;                    // se descuenta del inventario
        return calcularTotalVenta(cantidad);  // se calcula el total de la venta
    }
 
    /** Método 7 (lógica): agrega unidades al inventario (reabastecimiento). */
    public void reabastecer(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor que 0.");
        }
        if (stock + cantidad > STOCK_MAXIMO) {
            throw new IllegalArgumentException("El stock no puede superar " + STOCK_MAXIMO + " unidades.");
        }
        stock += cantidad;
    }
 
    /** Método 8 (lógica): valor total del producto en inventario (precio x stock). */
    public double calcularValorInventario() {
        return precio * stock;
    }
}
