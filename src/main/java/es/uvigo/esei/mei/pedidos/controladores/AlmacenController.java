package es.uvigo.esei.mei.pedidos.controladores;

import java.util.ArrayList;
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

import es.uvigo.esei.mei.pedidos.entidades.Almacen;
import es.uvigo.esei.mei.pedidos.entidades.Articulo;
import es.uvigo.esei.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.esei.mei.pedidos.entidades.Direccion;
import es.uvigo.esei.mei.pedidos.servicios.AlmacenService;
import es.uvigo.esei.mei.pedidos.servicios.ArticuloService;

@Controller
@RequestMapping("/almacenes")
public class AlmacenController {
	@Autowired
	AlmacenService almacenService;

	@Autowired
	ArticuloService articuloService;

	@ModelAttribute("articulos")
	public List<Articulo> crearListaArticulos() {
		return articuloService.buscarTodos();
	}

	/**
	 * Model encapsula el modelo (en este caso sera un Model vacio para ser
	 * inicializado)
	 */
	@GetMapping
	public String prepararListarAlmacenes(Model modelo) {
		List<Almacen> almacenes = almacenService.buscarTodos();
		modelo.addAttribute("almacenes", almacenes);
		modelo.addAttribute("nombreAlmacen", "");
		modelo.addAttribute("nombreLocalidad", "");
		return "almacen/listado_almacenes";
	}

	/**
	 * @RequestParam captura los parámetros de la peticion (en este caso cuerpo del
	 *               POST) cuyo nombre coincida con el nombre de los parametros
	 */
	@PostMapping
	public String actualizarListarAlmacenes(@RequestParam(required = false) String nombreAlmacen,
			@RequestParam(required = false) String nombreLocalidad, @RequestParam(required = false) Long idArticulo,
			Model modelo) {
		List<Almacen> almacenes;
		if (idArticulo != null) {
			almacenes = almacenService.buscarPorArticuloId(idArticulo);
		} else if ((nombreAlmacen != null) && !nombreAlmacen.isEmpty()) {
			almacenes = almacenService.buscarPorNombre(nombreAlmacen);
		} else if ((nombreLocalidad != null) && !nombreLocalidad.isEmpty()) {
			almacenes = almacenService.buscarPorLocalidad(nombreLocalidad);
		} else {
			almacenes = almacenService.buscarTodos();
		}
		modelo.addAttribute("almacenes", almacenes);
		return "almacen/listado_almacenes";
	}

	/**
	 * @PathVariable vincula el parametro a un segmento de la URI
	 */
	@GetMapping("{id}/eliminar")
	public String borrarAlmacen(@PathVariable("id") Long id, Model modelo) {
		Almacen almacen = almacenService.buscarPorId(id);
		if (almacen != null) {
			almacenService.eliminar(almacen);
			return "redirect:/almacenes";
		} else {
			modelo.addAttribute("mensajeError", "Almacen no encontrado");
			return "error";
		}
	}

	/**
	 * ModelAndView encapsula (equivalente a modificar el Model recibido como
	 * parametro y retornar un String con la siguiente vista)
	 */
	@GetMapping("nuevo")
	public ModelAndView prepararNuevoAlmacen() {
		Almacen almacen = new Almacen();
		almacen.setDireccion(new Direccion());

		ModelAndView result = new ModelAndView();
		result.addObject("almacen", almacen);
		result.addObject("esNuevo", true);
		result.addObject("articulosAlmacen", new ArrayList<>());
		result.setViewName("almacen/editar_almacen");
		return result;
	}

	/**
	 * @Valid indica que se apliquen las validaciones BeanValidation declaradas en
	 *        el correspondiente tipo
	 * @ModelAttribute vincula con un atributo del Model con el mismo nombre de la
	 *                 variable (es opcional, el comportamiento por defecto busca en
	 *                 el Model atributos con los nombres de las variables)
	 *                 BindingRequest encapsula el resultado del binding de
	 *                 parametros de la peticion o Model con atributos de los
	 *                 objetos reales
	 */
	@PostMapping("nuevo")
	public String crearAlmacen(@Valid @ModelAttribute Almacen almacen, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			almacenService.crear(almacen);
			return "redirect:/almacenes";
		} else {
			return null;
		}
	}

	@GetMapping("{id}")
	public String prepararEditarAlmacen(@PathVariable("id") Long id, Model modelo) {
		try {
			Almacen almacen = almacenService.buscarPorId(id);
			modelo.addAttribute("almacen", almacen);
			modelo.addAttribute("esNuevo", false);
			modelo.addAttribute("articulosAlmacen", almacenService.buscarArticulosAlmacenPorAlmacenId(id));
			return "almacen/editar_almacen";
		} catch (EntityNotFoundException e) {
			modelo.addAttribute("error", "Almacen no encontrado");
			return "error";
		}
	}

	@PostMapping("{id}")
	public String actualizarAlmacen(@Valid @ModelAttribute Almacen almacen, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			almacenService.modificar(almacen);
			return "redirect:/almacenes";
		} else {
			return null;
		}
	}

	@PostMapping(path = "{id}", params = "anadirArticulo")
	public String anadirArticuloAlmacen(@PathVariable("id") Long idAlmacen, @RequestParam Long idArticulo,
			@RequestParam Integer stock) {
		if ((idArticulo != null) && (stock != null)) {
			Articulo articulo = articuloService.buscarPorId(idArticulo);
			Almacen almacen = almacenService.buscarPorId(idAlmacen);
			almacenService.crearArticuloAlmacen(new ArticuloAlmacen(articulo, almacen, stock));
		}
		return "redirect:/almacenes/"+idAlmacen;
	}

	@PostMapping(path = "{id}", params = "eliminarArticulo")
	public String eliminarArticuloAlmacen(@PathVariable("id") Long idAlmacen, @RequestParam("eliminarArticulo") Long idArticulo) {
		if (idArticulo != null) {
			almacenService.eliminarArticuloAlmacen(idArticulo, idAlmacen);
		}
		return "redirect:/almacenes/"+idAlmacen;
	}
}
