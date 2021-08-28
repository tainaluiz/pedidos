package com.github.tainaluiz.pedidos.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.tainaluiz.pedidos.domain.Cidade;
import com.github.tainaluiz.pedidos.domain.Estado;
import com.github.tainaluiz.pedidos.dto.CidadeDTO;
import com.github.tainaluiz.pedidos.dto.EstadoDTO;
import com.github.tainaluiz.pedidos.services.CidadeService;
import com.github.tainaluiz.pedidos.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;

	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> list = service.findAll();
		List<EstadoDTO> listDTO = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(listDTO);
	}

	@GetMapping("/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Long estadoId) {
		List<Cidade> list = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> listDTO = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok(listDTO);
	}

}
