package com.github.tainaluiz.pedidos.dto;

import java.io.Serializable;

import com.github.tainaluiz.pedidos.domain.Estado;

public class EstadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;

	public EstadoDTO() {

	}

	public EstadoDTO(Estado obj) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
