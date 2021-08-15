package com.github.tainaluiz.pedidos.domain.enums;

public enum TipoCliente {

	PESSOAFISICA(1, "Pessoa Física", "PF"), PESSOAJURIDICA(2, "Pessoa Jurídica", "PJ");

	private int codigo;
	private String descricao;
	private String sigla;

	private TipoCliente(int codigo, String descricao, String sigla) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static TipoCliente toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}

		for (TipoCliente x : values()) {
			if (codigo.equals(x.getCodigo())) {
				return x;
			}
		}

		throw new IllegalArgumentException("Id inválido: " + codigo);
	}

}
