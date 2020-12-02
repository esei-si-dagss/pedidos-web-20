package es.uvigo.esei.mei.pedidos.servicios;

import java.util.List;

import es.uvigo.esei.mei.pedidos.entidades.Articulo;
import es.uvigo.esei.mei.pedidos.entidades.Familia;

public interface ArticuloService {
	public Articulo crear(Articulo articulo);
	public Articulo modificar(Articulo articulo);
	public void eliminar(Articulo articulo);
	public Articulo buscarPorId(Long id);
	public List<Articulo> buscarTodos();
	public List<Articulo> buscarPorDescripcion(String patron);
	public List<Articulo> buscarPorFamilia(Long idFamilia);
	
	public Familia crearFamilia(Familia familia);
	public Familia modificarFamilia(Familia familia);
	public void eliminarFamilia(Familia familia);
	public Familia buscarFamiliaPorId(Long id);
	public List<Familia> buscarFamilias();
	public List<Familia> buscarFamiliasPorDescripcion(String patron);
	
}
