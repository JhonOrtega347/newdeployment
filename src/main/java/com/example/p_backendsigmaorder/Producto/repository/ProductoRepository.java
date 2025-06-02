package com.example.p_backendsigmaorder.Producto.repository;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar productos por nombre (ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos por categoría
    List<Producto> findByCategoria(Categoria categoria);

    // Buscar productos por nombre y categoría
    List<Producto> findByNombreContainingIgnoreCaseAndCategoria(String nombre, Categoria categoria);

    // Búsqueda personalizada con JPQL
    @Query("SELECT p FROM Producto p WHERE " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%',:nombre,'%'))) AND " +
            "(:categoria IS NULL OR p.categoria = :categoria)")
    List<Producto> buscarProductos(@Param("nombre") String nombre,
                                   @Param("categoria") Categoria categoria);

    List<Producto> findByLocal(Local local);
    // Opcionalmente también podrías buscar por el ID del local
    List<Producto> findByLocalId(Long localId);

}
