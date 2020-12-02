package es.uvigo.esei.mei.pedidos.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uvigo.esei.mei.pedidos.entidades.Articulo;

@Repository
public interface ArticuloDAO extends JpaRepository<Articulo, Long>{
	List<Articulo> findByDescripcionContaining(String patron);
	List<Articulo> findByFamiliaId(Long familiaId);
}
