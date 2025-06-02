package com.example.p_backendsigmaorder.Producto.Controller;

import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.CreateProductoDTO;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.ProductoResponseDTO;
import com.example.p_backendsigmaorder.Producto.ProductoMapper.ProductoMapper;
import com.example.p_backendsigmaorder.Producto.domain.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }


    //acceso de manera publica
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        List<ProductoResponseDTO> productos = productoService.findAll().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }


    //acceso de manera publica
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(producto -> ResponseEntity.ok(productoMapper.toDTO(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProducto(@Valid @RequestBody CreateProductoDTO productoDTO) {
        var producto = productoMapper.toEntity(productoDTO);
        var savedProducto = productoService.save(producto);
        return new ResponseEntity<>(productoMapper.toDTO(savedProducto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductoDTO productoDTO) {
        return productoService.findById(id)
                .map(productoExistente -> {
                    var producto = productoMapper.toEntity(productoDTO);
                    producto.setId(id);
                    var updatedProducto = productoService.save(producto);
                    return ResponseEntity.ok(productoMapper.toDTO(updatedProducto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        return productoService.findById(id)
                .map(producto -> {
                    productoService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<ProductoResponseDTO>> findByNombre(@PathVariable String nombre) {
        List<ProductoResponseDTO> productos = productoService.findByNombre(nombre).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping("/buscar/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorCategoria(@PathVariable Categoria categoria) {
        List<ProductoResponseDTO> productos = productoService.buscarPorCategoria(categoria).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Categoria categoria) {
        List<ProductoResponseDTO> productos = productoService.buscarProductos(nombre, categoria).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping("/local/{localId}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosPorLocal(@PathVariable Long localId) {
        List<ProductoResponseDTO> productos = productoService.obtenerProductosPorLocalId(localId).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }
}