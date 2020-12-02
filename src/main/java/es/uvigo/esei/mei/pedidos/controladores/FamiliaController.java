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
import es.uvigo.esei.mei.pedidos.servicios.ArticuloService;

@Controller
@RequestMapping("/familias")
public class FamiliaController {
	@Autowired
	ArticuloService articuloService;

	@GetMapping
	public String prepararListarFamilias(Model modelo) {
		List<Familia> familias = articuloService.buscarFamilias();
		modelo.addAttribute("familias", familias);
		modelo.addAttribute("descripcionFamilia", "");
		return "familia/listado_familias";
	}

	@PostMapping
	public String actualizarListarFamilias(@RequestParam(required = false) String descripcionFamilia, Model modelo) {
		List<Familia> familias;
		if ((descripcionFamilia != null) && !descripcionFamilia.isEmpty()) {
			familias = articuloService.buscarFamiliasPorDescripcion(descripcionFamilia);
		} else {
			familias = articuloService.buscarFamilias();
		}
		modelo.addAttribute("familias", familias);
		return "familia/listado_familias";
	}


	@GetMapping("nueva")
	public ModelAndView prepararNuevaFamilia() {
		Familia familia = new Familia();

		ModelAndView result = new ModelAndView();
		result.addObject("familia", familia);
		result.addObject("esNueva", true);
		result.setViewName("familia/editar_familia");
		return result;
	}

	@PostMapping("nueva")
	public String crearFamilia(@Valid @ModelAttribute Familia familia, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			articuloService.crearFamilia(familia);
			return "redirect:/familias";
		} else {
			return null;
		}
	}

	@GetMapping("{id}")
	public String prepararEditarFamilia(@PathVariable("id") Long id, Model modelo) {
		try {
			Familia familia = articuloService.buscarFamiliaPorId(id);
			modelo.addAttribute("familia", familia);
			modelo.addAttribute("esNueva", false);
			return "familia/editar_familia";
		} catch (EntityNotFoundException e) {
			modelo.addAttribute("error", "Familia no encontrada");
			return "error";
		}
	}


	@PostMapping("{id}")
	public String actualizarFamilia(@Valid @ModelAttribute Familia familia, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			articuloService.modificarFamilia(familia);
			return "redirect:/familias";
		} else {
			return null;
		}
	}
}
