package com.example.p_backendsigmaorder.Promocion.Service;

import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Promocion.dto.PromocionDTO;
import com.example.p_backendsigmaorder.Promocion.Exception.PromocionException;
import com.example.p_backendsigmaorder.Promocion.Repository.PromocionRepository;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromocionServiceImplem implements PromocionService {

    private final PromocionRepository promocionRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public PromocionServiceImplem(PromocionRepository promocionRepository,
                                  ProductoRepository productoRepository) {
        this.promocionRepository = promocionRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<PromocionDTO> listar() {
        return promocionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PromocionDTO obtenerPorId(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new PromocionException("Promoción " + id + " no encontrada"));
        return toDTO(promocion);
    }

    @Override
    public PromocionDTO crear(PromocionDTO dto) {
        Promocion promocion = toEntity(dto);
        Promocion guardada = promocionRepository.save(promocion);
        return toDTO(guardada);
    }

    @Override
    public PromocionDTO actualizar(Long id, PromocionDTO dto) {
        Promocion existente = promocionRepository.findById(id)
                .orElseThrow(() -> new PromocionException("Promoción " + id + " no encontrada"));

        existente.setCodigoPromocion(dto.getCodigoPromocion());
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setFechaInicio(dto.getFechaInicio());
        existente.setFechaFin(dto.getFechaFin());
        existente.setPorcentajeDescuento(dto.getPorcentajeDescuento());

        List<Producto> productos = dto.getProductosIds().stream()
                .map(pid -> productoRepository.findById(pid)
                        .orElseThrow(() -> new PromocionException("Producto no encontrado: " + pid)))
                .collect(Collectors.toList());

        existente.setProductos(productos);
        Promocion actualizado = promocionRepository.save(existente);
        return toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!promocionRepository.existsById(id)) {
            throw new PromocionException("No se puede eliminar: promoción " + id + " no encontrada");
        }
        promocionRepository.deleteById(id);
    }

    private PromocionDTO toDTO(Promocion entity) {
        PromocionDTO dto = new PromocionDTO();
        dto.setId(entity.getId());
        dto.setCodigoPromocion(entity.getCodigoPromocion());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFin(entity.getFechaFin());
        dto.setProductosIds(entity.getProductos().stream()
                .map(Producto::getId)
                .collect(Collectors.toList()));

        dto.setPorcentajeDescuento(entity.getPorcentajeDescuento());

        // Calcular precio original (suma de precios de los productos)
        double sumaPrecios = entity.getProductos().stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
        dto.setPrecioOriginal(sumaPrecios);

        // Calcular precio con descuento
        double precioFinal = sumaPrecios * (1 - entity.getPorcentajeDescuento() / 100.0);
        dto.setPrecioFinal(precioFinal);

        double pesoTotal = entity.getProductos()
                .stream()
                .mapToDouble(Producto::getPeso)
                .sum();
        dto.setPesoTotal(pesoTotal);

        return dto;
    }

    private Promocion toEntity(PromocionDTO dto) {
        Promocion entity = new Promocion();
        entity.setCodigoPromocion(dto.getCodigoPromocion());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
        entity.setPorcentajeDescuento(dto.getPorcentajeDescuento());

        List<Producto> productos = dto.getProductosIds().stream()
                .map(pid -> productoRepository.findById(pid)
                        .orElseThrow(() -> new PromocionException("Producto no encontrado: " + pid)))
                .collect(Collectors.toList());

        entity.setProductos(productos);
        return entity;
    }
    public List<Promocion> findActive() {
        LocalDate hoy = LocalDate.now();
        return promocionRepository.findByFechaInicioBeforeAndFechaFinAfter(hoy, hoy);
    }
}
