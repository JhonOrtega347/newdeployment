package com.example.p_backendsigmaorder.Producto.ProductoMapper;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.CreateProductoDTO;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.ProductoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(CreateProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setFechaVencimiento(dto.getFechaVencimiento());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());
        producto.setPedidoId(dto.getPedidoId());
        producto.setPeso(dto.getPeso());

        Local local = new Local();
        local.setId(dto.getLocalId());
        producto.setLocal(local);

        return producto;
    }

    public ProductoResponseDTO toDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getFechaVencimiento(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getPedidoId(),
                producto.getPeso(),
                producto.getLocal().getId()
        );
    }
}