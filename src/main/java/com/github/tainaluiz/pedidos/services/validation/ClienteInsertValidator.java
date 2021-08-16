package com.github.tainaluiz.pedidos.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.tainaluiz.pedidos.domain.Cliente;
import com.github.tainaluiz.pedidos.domain.enums.TipoCliente;
import com.github.tainaluiz.pedidos.dto.ClienteNewDTO;
import com.github.tainaluiz.pedidos.repositories.ClienteRepository;
import com.github.tainaluiz.pedidos.resources.exception.FieldMessage;
import com.github.tainaluiz.pedidos.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	ClienteRepository repo;

	@Override
	public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if (objDTO.getTipo() != null) {
			TipoCliente tipoCliente = TipoCliente.toEnum(objDTO.getTipo());
			if (tipoCliente == null) {
				list.add(new FieldMessage("tipo", "Tipo inválido. Valores possíveis 1 e 2"));
			} else {
				if (TipoCliente.PESSOAFISICA.equals(tipoCliente) && !BR.isValidCPF(objDTO.getCpfOuCnpj())) {
					list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
				}

				if (TipoCliente.PESSOAJURIDICA.equals(tipoCliente) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())) {
					list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
				}
			}
		}

		if (objDTO.getEmail() != null && !objDTO.getEmail().isEmpty()) {
			Cliente aux = repo.findByEmail(objDTO.getEmail());
			if (aux != null) {
				list.add(new FieldMessage("email", "Email já existente"));
			}
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}

		return list.isEmpty();
	}

}
