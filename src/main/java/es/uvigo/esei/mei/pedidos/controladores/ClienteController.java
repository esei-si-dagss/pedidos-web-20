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

import es.uvigo.esei.mei.pedidos.entidades.Cliente;
import es.uvigo.esei.mei.pedidos.entidades.Direccion;
import es.uvigo.esei.mei.pedidos.servicios.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	@Autowired
	ClienteService clienteService;

	/**
	 * Model encapsula el modelo (en este caso sera un Model vacio para ser
	 * inicializado)
	 */
	@GetMapping
	public String prepararListarClientes(Model modelo) {
		List<Cliente> clientes = clienteService.buscarTodos();
		modelo.addAttribute("clientes", clientes);
		modelo.addAttribute("nombreCliente", "");
		modelo.addAttribute("nombreLocalidad", "");
		return "cliente/listado_clientes";
	}

	/**
	 * @RequestParam captura los parámetros de la peticion (en este caso cuerpo del
	 *               POST) cuyo nombre coincida con el nombre de los parametros
	 */
	@PostMapping
	public String actualizarListarClientes(@RequestParam(required = false) String nombreCliente,
			@RequestParam(required = false) String nombreLocalidad, Model modelo) {
		List<Cliente> clientes;
		if ((nombreCliente != null) && !nombreCliente.isEmpty()) {
			clientes = clienteService.buscarPorNombre(nombreCliente);
		} else if ((nombreLocalidad != null) && !nombreLocalidad.isEmpty()) {
			clientes = clienteService.buscarPorLocalidad(nombreLocalidad);
		} else {
			clientes = clienteService.buscarTodos();
		}
		modelo.addAttribute("clientes", clientes);
		return "cliente/listado_clientes";
	}

	/**
	 * @PathVariable vincula el parametro a un segmento de la URI
	 */
	@GetMapping("{dni}/eliminar")
	public String borrarCliente(@PathVariable("dni") String dni, Model modelo) {
		Cliente cliente = clienteService.buscarPorDNI(dni);
		if (cliente != null) {
			clienteService.eliminar(cliente);
			return "redirect:/clientes";
		} else {
			modelo.addAttribute("mensajeError", "Cliente no encontrado");
			return "error";
		}
	}

	/**
	 * ModelAndView encapsula (equivalente a modificar el Model recibido como
	 * parametro y retornar un String con la siguiente vista)
	 */
	@GetMapping("nuevo")
	public ModelAndView prepararNuevoCliente() {
		Cliente cliente = new Cliente();
		cliente.setDireccion(new Direccion());

		ModelAndView result = new ModelAndView();
		result.addObject("cliente", cliente);
		result.addObject("esNuevo", true);
		result.setViewName("cliente/editar_cliente");
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
	public String crearCliente(@Valid @ModelAttribute Cliente cliente, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			clienteService.crear(cliente);
			return "redirect:/clientes";
		} else {
			return null;
		}
	}

	@GetMapping("{dni}")
	public String prepararEditarCliente(@PathVariable("dni") String dni, Model modelo) {
		try {
			Cliente cliente = clienteService.buscarPorDNI(dni);
			modelo.addAttribute("cliente", cliente);
			modelo.addAttribute("esNuevo", false);
			return "cliente/editar_cliente";
		} catch (EntityNotFoundException e) {
			modelo.addAttribute("error", "Cliente no encontrado");
			return "error";
		}
	}


	@PostMapping("{dni}")
	public String actualizarCliente(@Valid @ModelAttribute Cliente cliente, BindingResult resultado) {
		if (!resultado.hasErrors()) {
			clienteService.modificar(cliente);
			return "redirect:/clientes";
		} else {
			return null;
		}
	}

}
