/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uthh.TiendaInventario;

/**
 *
 * @author erika
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
 
public class VentanaTienda extends JFrame {
 
    
    private static final String AUTOR = "Erika Mareli 4°A";
 
    // Lista donde se guardan los productos del inventario
    private final ArrayList<Producto> inventario = new ArrayList<>();
 
    // ---------- Controles de la ventana ----------
    private final JTextField txtNombre = new JTextField(12);
    private final JTextField txtPrecio = new JTextField(6);
    private final JTextField txtStock = new JTextField(5);
    private final JCheckBox chkOferta = new JCheckBox("En oferta (-20%)");
    private final JTextField txtCantidad = new JTextField("1", 5);
    private final JTextArea areaMensajes = new JTextArea(6, 40);
 
    // Tabla que muestra el inventario (las celdas no se pueden editar)
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"Producto", "Precio", "Stock"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
 
    /** Construye y organiza todos los componentes de la ventana. */
    public VentanaTienda() {
        super("Tienda - Inventario y Ventas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
 
        // Encabezado con el nombre del autor (visible en la GUI)
        JLabel lblAutor = new JLabel("Autor: " + AUTOR, SwingConstants.CENTER);
        lblAutor.setFont(lblAutor.getFont().deriveFont(Font.BOLD, 14f));
        add(lblAutor, BorderLayout.NORTH);
 
        // Centro: tabla con los productos (solo se puede seleccionar una fila)
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane panelTabla = new JScrollPane(tabla);
        panelTabla.setPreferredSize(new Dimension(520, 180));
        add(panelTabla, BorderLayout.CENTER);
 
        // Panel de captura de un producto nuevo
        JPanel panelForm = new JPanel(new FlowLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Nuevo producto"));
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Precio $:"));
        panelForm.add(txtPrecio);
        panelForm.add(new JLabel("Stock:"));
        panelForm.add(txtStock);
        panelForm.add(chkOferta);
 
        // Panel de acciones sobre el producto seleccionado
        JButton btnAgregar = new JButton("Agregar producto");
        JButton btnVender = new JButton("Vender");
        JButton btnReabastecer = new JButton("Reabastecer");
        JButton btnValor = new JButton("Valor del inventario");
        JPanel panelAcciones = new JPanel(new FlowLayout());
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Acciones (selecciona un producto)"));
        panelAcciones.add(btnAgregar);
        panelAcciones.add(new JLabel("Cantidad:"));
        panelAcciones.add(txtCantidad);
        panelAcciones.add(btnVender);
        panelAcciones.add(btnReabastecer);
        panelAcciones.add(btnValor);
 
        // Área de mensajes (historial de operaciones)
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
 
        // Parte inferior: formulario + acciones + mensajes, apilados
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));
        panelSur.add(panelForm);
        panelSur.add(panelAcciones);
        panelSur.add(new JScrollPane(areaMensajes));
        add(panelSur, BorderLayout.SOUTH);
 
        // Eventos de los botones
        btnAgregar.addActionListener(e -> agregarProducto());
        btnVender.addActionListener(e -> venderProducto());
        btnReabastecer.addActionListener(e -> reabastecerProducto());
        btnValor.addActionListener(e -> mostrarValorInventario());
 
        pack();
        setLocationRelativeTo(null);   // centra la ventana
    }
 
    /**
     * Lee los campos y crea un Producto o un ProductoOferta según el
     * checkbox (polimorfismo). Lo agrega al inventario.
     */
    private void agregarProducto() {
        try {
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            Producto p;
            if (chkOferta.isSelected()) {
                p = new ProductoOferta(nombre, precio, stock);
            } else {
                p = new Producto(nombre, precio, stock);
            }
            inventario.add(p);
            refrescarTabla();
            mensaje("Producto agregado: " + p.getNombre());
            txtNombre.setText("");
            txtPrecio.setText("");
            txtStock.setText("");
        } catch (NumberFormatException ex) {
            error("Precio y stock deben ser números válidos.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());   // mensaje de validación del constructor
        }
    }
 
    /** Vende unidades del producto seleccionado en la tabla. */
    private void venderProducto() {
        Producto p = productoSeleccionado();
        if (p == null) return;
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double total = p.vender(cantidad);
            refrescarTabla();
            mensaje(String.format("Venta: %d x %s = $%,.2f", cantidad, p.getNombre(), total));
        } catch (NumberFormatException ex) {
            error("La cantidad debe ser un número entero.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }
 
    /** Agrega unidades al inventario del producto seleccionado. */
    private void reabastecerProducto() {
        Producto p = productoSeleccionado();
        if (p == null) return;
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            p.reabastecer(cantidad);
            refrescarTabla();
            mensaje(String.format("Se agregaron %d unidad(es) de %s", cantidad, p.getNombre()));
        } catch (NumberFormatException ex) {
            error("La cantidad debe ser un número entero.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }
 
    /** Suma el valor en inventario de todos los productos. */
    private void mostrarValorInventario() {
        double total = 0;
        for (Producto p : inventario) {
            total += p.calcularValorInventario();
        }
        mensaje(String.format("Valor total del inventario: $%,.2f", total));
    }
 
    /** Devuelve el producto de la fila seleccionada (o null si no hay). */
    private Producto productoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            error("Selecciona un producto de la tabla.");
            return null;
        }
        return inventario.get(fila);
    }
 
    /** Vuelve a dibujar la tabla con los datos actuales del inventario. */
    private void refrescarTabla() {
        int seleccionada = tabla.getSelectedRow();   // recordar la fila elegida
        modeloTabla.setRowCount(0);                  // limpiar la tabla
        for (Producto p : inventario) {
            String nombre = p.getNombre();
            if (p instanceof ProductoOferta) {       // marca los productos en oferta
                nombre += " (oferta)";
            }
            modeloTabla.addRow(new Object[]{
                nombre, String.format("$%,.2f", p.getPrecio()), p.getStock()});
        }
        if (seleccionada >= 0 && seleccionada < inventario.size()) {
            tabla.setRowSelectionInterval(seleccionada, seleccionada);
        }
    }
 
    /** Agrega una línea al historial de mensajes. */
    private void mensaje(String texto) {
        areaMensajes.append(texto + "\n");
    }
 
    /** Muestra un cuadro de diálogo de error. */
    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Dato inválido", JOptionPane.ERROR_MESSAGE);
    }
 
    /** Punto de entrada: crea la ventana en el hilo de eventos de Swing. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaTienda().setVisible(true));
    }
}