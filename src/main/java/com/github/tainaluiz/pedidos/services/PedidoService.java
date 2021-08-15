package com.github.tainaluiz.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Pedido;
import com.github.tainaluiz.pedidos.repositories.PedidoRepository;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	public Pedido find(Long id) throws ObjectNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				String.format("Objeto %s não encontrado. Id: %d", Pedido.class.getSimpleName(), id)));
	}
}
