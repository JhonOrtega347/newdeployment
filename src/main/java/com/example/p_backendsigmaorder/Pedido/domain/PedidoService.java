package com.example.p_backendsigmaorder.Pedido.domain;

import com.example.p_backendsigmaorder.Carrito.DTO.CarritoProductoDTO;
import com.example.p_backendsigmaorder.Carrito.DTO.CarritoResponseDTO;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoService;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoItemDTO;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import com.example.p_backendsigmaorder.Pedido.domain.Pedido;
import com.example.p_backendsigmaorder.Pedido.domain.PedidoItem;
import com.example.p_backendsigmaorder.Pedido.repository.PedidoRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoResponseDTO crearDesdeCarrito() {
        // Obtener datos del carrito y usuario
        CarritoResponseDTO carritoDto = carritoService.obtenerCarritoDTO();
        Usuario usuario = getUsuario();

        // 1) Crear Pedido inicial sin items
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotalPrecio(BigDecimal.valueOf(carritoDto.getPrecioTotal()));
        pedido.setTotalPeso(BigDecimal.valueOf(carritoDto.getPesoTotal()));
        pedido = pedidoRepository.save(pedido);

        // 2) Mapear items del carrito a PedidoItem
        List<PedidoItem> items = new ArrayList<>();
        for (CarritoProductoDTO prod : carritoDto.getProductos()) {
            PedidoItem it = new PedidoItem();
            it.setPedido(pedido);
            it.setProductoId(prod.getProductoId());
            it.setNombre(prod.getNombre());
            BigDecimal precioUnitario = BigDecimal.valueOf(prod.getPrecio());
            it.setPrecioUnitario(precioUnitario);
            it.setCantidad(prod.getCantidad());
            it.setSubtotal(precioUnitario.multiply(BigDecimal.valueOf(prod.getCantidad())));
            items.add(it);
        }

        // 3) Asignar items y guardar de nuevo
        pedido.setItems(items);
        Pedido pedidoCompleto = pedidoRepository.save(pedido);

        // 4) Convertir a DTO y devolver
        return mapToDTO(pedidoCompleto);
    }

    private Usuario getUsuario() {
        String correo = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private PedidoResponseDTO mapToDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());

        List<PedidoItemDTO> itemDTOs = new ArrayList<>();
        for (PedidoItem it : pedido.getItems()) {
            PedidoItemDTO pidto = new PedidoItemDTO();
            pidto.setProductoId(it.getProductoId());
            pidto.setNombre(it.getNombre());
            pidto.setPrecioUnitario(it.getPrecioUnitario());
            pidto.setCantidad(it.getCantidad());
            pidto.setSubtotal(it.getSubtotal());
            itemDTOs.add(pidto);
        }
        dto.setItems(itemDTOs);

        dto.setTotalPrecio(pedido.getTotalPrecio());
        dto.setTotalPeso(pedido.getTotalPeso());
        dto.setStatus(pedido.getStatus());
        dto.setCreadoEn(pedido.getCreadoEn());
        dto.setActualizadoEn(pedido.getActualizadoEn());

        return dto;
    }
}
