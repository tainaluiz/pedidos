package com.github.tainaluiz.pedidos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Categoria;
import com.github.tainaluiz.pedidos.domain.Produto;
import com.github.tainaluiz.pedidos.repositories.CategoriaRepository;
import com.github.tainaluiz.pedidos.repositories.ProdutoRepository;
import com.github.tainaluiz.pedidos.services.exceptions.IllegalParamException;
import com.github.tainaluiz.pedidos.services.exceptions.NoPropertyException;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Long id) throws ObjectNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				String.format("Objeto %s não encontrado. Id: %d", Produto.class.getSimpleName(), id)));
	}

	public Page<Produto> search(String nome, List<Long> ids, Integer page, Integer size, String orderBy,
			String direction) throws IllegalParamException, NoPropertyException {
		PageRequest pageRequest = null;
		try {
			pageRequest = PageRequest.of(page, size, Direction.valueOf(direction), orderBy);

			List<Categoria> categorias = categoriaRepository.findAllById(ids);

			return repo.findDistinctByNomeContainingIgnoreCaseAndCategoriasIn(nome, categorias, pageRequest);
		} catch (IllegalArgumentException e) {
			throw new IllegalParamException("Parâmetro não permitido!");
		} catch (PropertyReferenceException e) {
			throw new NoPropertyException(String.format("Propriedade %s não encontrada no objeto %s!", orderBy,
					Categoria.class.getSimpleName()));
		}
	}
}
