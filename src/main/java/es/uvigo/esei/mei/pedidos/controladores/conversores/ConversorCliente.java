package es.uvigo.esei.mei.pedidos.controladores.conversores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import es.uvigo.esei.mei.pedidos.entidades.Articulo;
import es.uvigo.esei.mei.pedidos.servicios.ArticuloService;

@Component
public class ConversorCliente implements Converter<String, Articulo> {

	@Autowired
	private ArticuloService articuloService;

	@Override
	public Articulo convert(String arg) {
		Long id = Long.parseLong(arg);
		return articuloService.buscarPorId(id);
	}

}
