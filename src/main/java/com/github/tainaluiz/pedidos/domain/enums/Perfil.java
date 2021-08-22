package com.github.tainaluiz.pedidos.domain.enums;

public enum Perfil {

	ADMIN(1, "ROLE_ADMIN"), CLIENTE(2, "ROLE_CLIENTE");

	private int codigo;
	private String descricao;

	private Perfil(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Perfil toEnum(Integer codigo) {
		if (codigo != null) {
			for (Perfil x : values()) {
				if (codigo.equals(x.getCodigo())) {
					return x;
				}
			}
		}
		return null;
	}
}
