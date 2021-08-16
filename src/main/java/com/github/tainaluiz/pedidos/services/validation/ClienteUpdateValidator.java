package com.github.tainaluiz.pedidos.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.github.tainaluiz.pedidos.domain.Cliente;
import com.github.tainaluiz.pedidos.dto.ClienteDTO;
import com.github.tainaluiz.pedidos.repositories.ClienteRepository;
import com.github.tainaluiz.pedidos.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ClienteRepository repo;

	@Override
	public boolean isValid(ClienteDTO objDTO, ConstraintValidatorContext context) {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		Long uriId = Long.parseLong(map.get("id"));

		List<FieldMessage> list = new ArrayList<>();

		if (objDTO.getEmail() != null && !objDTO.getEmail().isEmpty()) {
			Cliente aux = repo.findByEmail(objDTO.getEmail());
			if (aux != null && !aux.getId().equals(uriId)) {
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
