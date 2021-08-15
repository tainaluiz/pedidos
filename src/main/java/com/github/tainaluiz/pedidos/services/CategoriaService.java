package com.github.tainaluiz.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Categoria;
import com.github.tainaluiz.pedidos.repositories.CategoriaRepository;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Long id) throws ObjectNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				String.format("Objeto %s não encontrado. Id: %d", Categoria.class.getSimpleName(), id)));
	}
}