package es.uvigo.esei.mei.pedidos.controladores.conversores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import es.uvigo.esei.mei.pedidos.entidades.Cliente;
import es.uvigo.esei.mei.pedidos.servicios.ClienteService;

@Component
public class ConversorArticulo implements Converter<String, Cliente> {

	@Autowired
	private ClienteService clienteService;

	@Override
	public Cliente convert(String arg) {
		return clienteService.buscarPorDNI(arg);
	}

}
