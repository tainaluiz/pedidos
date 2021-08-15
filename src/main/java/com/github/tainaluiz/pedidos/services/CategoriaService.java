package com.github.tainaluiz.pedidos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Categoria;
import com.github.tainaluiz.pedidos.repositories.CategoriaRepository;
import com.github.tainaluiz.pedidos.services.exceptions.DataIntegrityException;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Long id) throws ObjectNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				String.format("Objeto %s não encontrado. Id: %d", Categoria.class.getSimpleName(), id)));
	}

	public Categoria insert(Categoria obj) {
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) throws ObjectNotFoundException {
		find(obj.getId());
		return repo.save(obj);
	}

	public void delete(Long id) throws DataIntegrityException {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
		}
	}
}
