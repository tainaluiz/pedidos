package com.github.tainaluiz.pedidos.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tainaluiz.pedidos.domain.Produto;
import com.github.tainaluiz.pedidos.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable Long id) {
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

}
