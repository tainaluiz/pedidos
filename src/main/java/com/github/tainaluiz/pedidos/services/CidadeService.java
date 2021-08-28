package com.github.tainaluiz.pedidos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Cidade;
import com.github.tainaluiz.pedidos.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repo;

	public List<Cidade> findByEstado(Long estadoId) {
		return repo.findCidades(estadoId);
	}
}
