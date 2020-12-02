package es.uvigo.esei.mei.pedidos.controladores;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uvigo.esei.mei.pedidos.entidades.Familia;
import es.uvigo.esei.mei.pedidos.entidades.Articulo;
import es.uvigo.esei.mei.pedidos.servicios.ArticuloService;

@Controller
@RequestMapping("/articulos")
public class ArticuloController {
	@Autowired
	ArticuloService articuloService;

	@GetMapping
	public String prepararListarArticulos(Model modelo) {
		modelo.addAttribute("articulos", articuloService.buscarTodos());
		modelo.addAttribute("familias", articuloService.buscarFamilias());
		modelo.addAttribute("descripcionArticulo", "");
		return "articulo/listado_articulos";
	}

	@PostMapping
	public String actualizarListarArticulo(@RequestParam(required = false) String descripcionArticulo,
			@RequestParam(required = false) Long idFamilia, Model modelo) {
		List<Articulo> articulos;
		if ((descripcionArticulo != null) && !descripcionArticulo.isEmpty()) {
			articulos = articuloService.buscarPorDescripcion(descripcionArticulo);
		} else if (idFamilia != null) {
			articulos = articuloService.buscarPorFamilia(idFamilia);
		} else {
			articulos = articuloService.buscarTodos();
		}
		modelo.addAttribute("articulos", articulos);
		modelo.addAttribute("familias", articuloService.buscarFamilias());		
		return "articulo/listado_articulos";
	}

	@GetMapping("nuevo")
	public ModelAndView prepararNuevoArticulo() {
		Articulo articulo = new Articulo();

		ModelAndView result = new ModelAndView();
		result.addObject("articulo", articulo);
		result.addObject("esNuevo", true);
		result.addObject("familias", articuloService.buscarFamilias());
		result.setViewName("articulo/editar_articulo");
		return result;
	}

	@PostMapping("nuevo")
	public String crearArticulo(@Valid @ModelAttribute Articulo articulo, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			articuloService.crear(articulo);
			return "redirect:/articulos";
		} else {
			return null;
		}
	}

	@GetMapping("{id}")
	public String prepararEditarArticulo(@PathVariable("id") Long id, Model modelo) {
		try {
			Articulo articulo = articuloService.buscarPorId(id);
			modelo.addAttribute("articulo", articulo);
			modelo.addAttribute("esNuevo", false);
			modelo.addAttribute("familias", articuloService.buscarFamilias());			
			return "articulo/editar_articulo";
		} catch (EntityNotFoundException e) {
			modelo.addAttribute("error", "Artículo no encontrado");
			return "error";
		}
	}

	@PostMapping("{id}")
	public String actualizarArticulo(@Valid @ModelAttribute Articulo articulo, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			articuloService.modificar(articulo);
			return "redirect:/articulos";
		} else {
			return null;
		}
	}
}
