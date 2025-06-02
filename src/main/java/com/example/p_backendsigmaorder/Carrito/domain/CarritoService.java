package com.example.p_backendsigmaorder.Carrito.domain;

import com.example.p_backendsigmaorder.Carrito.DTO.CarritoProductoDTO;
import com.example.p_backendsigmaorder.Carrito.DTO.CarritoPromocionDTO;
import com.example.p_backendsigmaorder.Carrito.DTO.CarritoResponseDTO;
import com.example.p_backendsigmaorder.Carrito.DTO.MetodoEnvioDTO;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoPromocionRepository;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoProductoRepository;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoRepository;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Promocion.Repository.PromocionRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private CarritoPromocionRepository carritoPromocionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Límite y tarifas para delivery
    private static final BigDecimal MAX_WEIGHT_FOR_DELIVERY = new BigDecimal("20");
    private static final BigDecimal BASE_FEE               = new BigDecimal("5.00");
    private static final BigDecimal FEE_PER_KG             = new BigDecimal("0.50");

    // Obtiene el usuario actual desde el token JWT
    private Usuario getUsuarioActual() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Devuelve el carrito activo del usuario (crea uno si no existe)
     * y garantiza valores por defecto para shippingMethod y shippingCost.
     */
    @Transactional
    public Carrito getCarritoActual() {
        Usuario usuario = getUsuarioActual();
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    // defaults
                    nuevo.setShippingMethod(ShippingMethod.PICKUP);
                    nuevo.setShippingCost(BigDecimal.ZERO);
                    return carritoRepository.save(nuevo);
                });
        boolean needsSave = false;
        if (carrito.getShippingMethod() == null) {
            carrito.setShippingMethod(ShippingMethod.PICKUP);
            needsSave = true;
        }
        if (carrito.getShippingCost() == null) {
            carrito.setShippingCost(BigDecimal.ZERO);
            needsSave = true;
        }
        if (needsSave) {
            carrito = carritoRepository.save(carrito);
        }
        return carrito;
    }

    @Transactional
    public void agregarProducto(Long productoId, int cantidad) {
        Carrito carrito = getCarritoActual();
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        CarritoProducto cp = carrito.getProductos().stream()
                .filter(p -> p.getProducto().getId().equals(productoId))
                .findFirst().orElse(null);
        if (cp == null) {
            cp = new CarritoProducto();
            cp.setCarrito(carrito);
            cp.setProducto(producto);
            cp.setCantidad(cantidad);
            carrito.getProductos().add(cp);
        } else {
            cp.setCantidad(cp.getCantidad() + cantidad);
        }
        carritoProductoRepository.save(cp);
    }

    @Transactional
    public void removerProducto(Long productoId) {
        Carrito carrito = getCarritoActual();
        carrito.getProductos().removeIf(cp -> cp.getProducto().getId().equals(productoId));
    }

    @Transactional
    public void agregarPromocion(Long promocionId, int cantidad) {
        Carrito carrito = getCarritoActual();
        Promocion promocion = promocionRepository.findById(promocionId)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
        CarritoPromocion cp = carrito.getPromociones().stream()
                .filter(p -> p.getPromocion().getId().equals(promocionId))
                .findFirst().orElse(null);
        if (cp == null) {
            cp = new CarritoPromocion();
            cp.setCarrito(carrito);
            cp.setPromocion(promocion);
            cp.setCantidad(cantidad);
            carrito.getPromociones().add(cp);
        } else {
            cp.setCantidad(cp.getCantidad() + cantidad);
        }
        carritoPromocionRepository.save(cp);
    }

    @Transactional
    public void removerPromocion(Long promocionId) {
        Carrito carrito = getCarritoActual();
        carrito.getPromociones().removeIf(cp -> cp.getPromocion().getId().equals(promocionId));
    }

    /**
     * Devuelve el DTO completo del carrito, incluyendo shipping.
     */
    public CarritoResponseDTO obtenerCarritoDTO() {
        return mapCarritoToDTO(getCarritoActual());
    }

    /**
     * Lógica para actualizar el método de envío y calcular costos.
     */
    @Transactional
    public CarritoResponseDTO actualizarMetodoEnvio(MetodoEnvioDTO dto) {
        Carrito carrito = getCarritoActual();
        ShippingMethod method = dto.getShippingMethod() != null
                ? dto.getShippingMethod() : ShippingMethod.PICKUP;
        carrito.setShippingMethod(method);
        if (method == ShippingMethod.DELIVERY) {
            BigDecimal pesoTotal = BigDecimal.valueOf(
                    obtenerCarritoDTO().getPesoTotal());
            if (pesoTotal.compareTo(MAX_WEIGHT_FOR_DELIVERY) > 0) {
                throw new RuntimeException("El peso excede "
                        + MAX_WEIGHT_FOR_DELIVERY + " kg");
            }
            BigDecimal costo = BASE_FEE.add(pesoTotal.multiply(FEE_PER_KG));
            carrito.setShippingCost(costo);
            if (dto.getDeliveryAddress() == null
                    || dto.getDeliveryAddress().isBlank()) {
                throw new RuntimeException(
                        "La dirección es obligatoria para DELIVERY");
            }
            carrito.setDeliveryAddress(dto.getDeliveryAddress());
        } else {
            carrito.setShippingCost(BigDecimal.ZERO);
            carrito.setDeliveryAddress(null);
        }
        carritoRepository.save(carrito);
        return mapCarritoToDTO(carrito);
    }

    /**
     * Mapea la entidad Carrito a CarritoResponseDTO,
     * incluyendo productos, promociones, totales y shipping.
     */
    private CarritoResponseDTO mapCarritoToDTO(Carrito carrito) {
        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setCarritoId(carrito.getId());
        // productos
        List<CarritoProductoDTO> productos = carrito.getProductos().stream()
                .map(cp -> {
                    CarritoProductoDTO p = new CarritoProductoDTO();
                    p.setProductoId(cp.getProducto().getId());
                    p.setNombre(cp.getProducto().getNombre());
                    p.setPrecio(cp.getProducto().getPrecio());
                    p.setPeso(cp.getProducto().getPeso());
                    p.setCantidad(cp.getCantidad());
                    return p;
                }).collect(Collectors.toList());
        // promociones
        List<CarritoPromocionDTO> promociones = carrito.getPromociones().stream()
                .map(cp -> {
                    CarritoPromocionDTO p = new CarritoPromocionDTO();
                    p.setPromocionId(cp.getPromocion().getId());
                    p.setTitulo(cp.getPromocion().getNombre());
                    double sumaOriginal = cp.getPromocion().getProductos().stream()
                            .mapToDouble(Producto::getPrecio).sum();
                    p.setPrecioOriginal(sumaOriginal);
                    p.setPrecioFinal(cp.getPromocion().getPrecioFinal());
                    double pesoPromo = cp.getPromocion().getProductos().stream()
                            .mapToDouble(Producto::getPeso).sum();
                    p.setPeso(pesoPromo);
                    p.setCantidad(cp.getCantidad());
                    return p;
                }).collect(Collectors.toList());
        dto.setProductos(productos);
        dto.setPromociones(promociones);
        // totales
        double totalPrecio = productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum()
                + promociones.stream()
                .mapToDouble(p -> p.getPrecioFinal() * p.getCantidad()).sum();
        double totalPeso = productos.stream()
                .mapToDouble(p -> p.getPeso() * p.getCantidad()).sum()
                + promociones.stream()
                .mapToDouble(p -> p.getPeso() * p.getCantidad()).sum();
        dto.setPrecioTotal(totalPrecio);
        dto.setPesoTotal(totalPeso);
        // shipping
        dto.setShippingMethod(carrito.getShippingMethod());
        dto.setShippingCost(carrito.getShippingCost());
        dto.setDeliveryAddress(carrito.getDeliveryAddress());
        return dto;
    }
}
