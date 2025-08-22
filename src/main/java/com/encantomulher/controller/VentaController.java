package com.encantomulher.controller;

import com.encantomulher.model.*;
import com.encantomulher.repository.DetalleVentasRepository;
import com.encantomulher.service.ClienteService;
import com.encantomulher.service.ProductoService;
import com.encantomulher.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final DetalleVentasRepository detalleRepo;

    public VentaController(VentaService ventaService,
                           ClienteService clienteService,
                           ProductoService productoService,
                           DetalleVentasRepository detalleRepo) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.detalleRepo = detalleRepo;
    }

    // Listar ventas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "venta";
    }

    // Nueva venta
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "venta_form";
    }

    // Guardar venta
    @PostMapping("/guardar")
    public String guardar(
        @RequestParam(required = false) Long idVenta,
        @RequestParam Long clienteId,
        @RequestParam String tipoPago,
        @RequestParam String estado,
        @RequestParam(required = false) List<Long> productoIds,
        @RequestParam(required = false) List<Integer> cantidades
    ) {
    Venta venta = (idVenta != null) ? ventaService.findByIdConDetalles(idVenta) : new Venta();

    // Cliente
    Cliente cliente = clienteService.obtenerPorId(clienteId);
    if (cliente != null) {
        venta.setCliente(cliente);
    }

    // Fecha
    if (venta.getFechaVenta() == null) venta.setFechaVenta(LocalDateTime.now());

    // Estado y Tipo de Pago
    venta.setTipoPago(Venta.TipoPago.valueOf(tipoPago.toLowerCase()));
    venta.setEstado(Venta.Estado.valueOf(estado.toLowerCase()));

    // Limpiar detalles si es edición
    if (venta.getIdVenta() != null) {
        detalleRepo.deleteAll(venta.getDetalles());
        venta.getDetalles().clear();
    }

    // Reconstruir detalles
    BigDecimal total = BigDecimal.ZERO;
    if (productoIds != null) {
        for (int i = 0; i < productoIds.size(); i++) {
            Producto p = productoService.obtenerPorId(productoIds.get(i)).orElse(null);
            if (p == null) continue;

            int qty = (cantidades != null && cantidades.size() > i) ? cantidades.get(i) : 1;
            BigDecimal precio = p.getPrecioVenta();
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(qty));

            DetalleVentas det = new DetalleVentas();
            det.setVenta(venta);
            det.setProducto(p);
            det.setCantidad(qty);
            det.setPrecioUnitario(precio);
            det.setSubtotal(subtotal);

            venta.getDetalles().add(det);
            total = total.add(subtotal);
        }
    }

    venta.setSubtotal(total);
    venta.setDescuento(BigDecimal.ZERO);
    venta.setTotal(total);

    ventaService.save(venta);
    return "redirect:/venta";
}

    // === 🔎 BUSCAR PRODUCTO (AJAX) ===
    @GetMapping("/buscarProducto")
    @ResponseBody
    public List<Map<String, Object>> buscarProducto(@RequestParam("q") String q) {
        List<Producto> productos = productoService.buscarPorNombreOCodigo(q);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Producto p : productos) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getIdProducto());
            map.put("codigo", p.getCodigoBarras());
            map.put("nombre", p.getNombre());
            map.put("descripcion", p.getDescripcion());
            map.put("precio", p.getPrecioVenta());
            result.add(map);
        }
        return result;
    }

    // === 🔎 BUSCAR CLIENTE (AJAX) ===
    @GetMapping("/buscarCliente")
    @ResponseBody
    public List<Map<String, Object>> buscarCliente(@RequestParam("q") String q) {
        List<Cliente> clientes = clienteService.buscarPorNombreODocumento(q);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Cliente c : clientes) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getIdCliente());
            map.put("nombre", c.getNombre());
            result.add(map);
        }
        return result;
    }
}