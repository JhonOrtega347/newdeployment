package com.example.p_backendsigmaorder.Producto.domain;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.Exception.ProductoNotFoundException;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        List<Producto> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            throw new ProductoNotFoundException("No se encontraron productos en la base de datos");
        }
        return productos;
    }

    public Optional<Producto> findById(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (!producto.isPresent()) {
            throw new ProductoNotFoundException("No se encontró el producto con ID: " + id);
        }
        return producto;
    }
    public Producto save(Producto producto) {
        // Convertir nombre a formato título (primera letra mayúscula, resto minúsculas)
        if (producto.getNombre() != null) {
            producto.setNombre(capitalizarNombre(producto.getNombre()));
        }
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException(
                    "No se puede eliminar. No existe el producto con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    //-----Para poder filtrar y hacer busqueda
    public List<Producto> findByNombre(String nombre) {
        String nombreCapitalizado = capitalizarNombre(nombre);
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombreCapitalizado);
        if (productos.isEmpty()) {
            throw new ProductoNotFoundException(
                    "No se encontraron productos con el nombre: " + nombre);
        }
        return productos;
    }


    public List<Producto> buscarPorCategoria(Categoria categoria) {
        List<Producto> productos = productoRepository.findByCategoria(categoria);
        if (productos.isEmpty()) {
            throw new ProductoNotFoundException(
                    "No se encontraron productos en la categoría: " + categoria);
        }
        return productos;
    }


    public List<Producto> buscarProductos(String nombre, Categoria categoria) {
        if (nombre != null && !nombre.trim().isEmpty() && categoria != null) {
            return productoRepository.findByNombreContainingIgnoreCaseAndCategoria(nombre, categoria);
        } else if (nombre != null && !nombre.trim().isEmpty()) {
            return productoRepository.findByNombreContainingIgnoreCase(nombre);
        } else if (categoria != null) {
            return productoRepository.findByCategoria(categoria);
        } else {
            return productoRepository.findAll();
        }
    }

    // Mtodo auxiliar para capitalizar nombres
    private String capitalizarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return nombre;
        }
        String[] palabras = nombre.toLowerCase().split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                if (resultado.length() > 0) {
                    resultado.append(" ");
                }
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                        .append(palabra.substring(1));
            }
        }
        return resultado.toString();
    }

    public List<Producto> obtenerProductosPorLocal(Local local) {
        return productoRepository.findByLocal(local);
    }

    public List<Producto> obtenerProductosPorLocalId(Long localId) {
        return productoRepository.findByLocalId(localId);
    }

}
