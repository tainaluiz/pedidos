package com.github.tainaluiz.pedidos.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ObjectMapper objMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
			throws IOException, ServletException {
		res.setStatus(HttpStatus.UNAUTHORIZED.value());
		res.setContentType(MediaType.APPLICATION_JSON.toString());
		res.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", Calendar.getInstance().getTime());
		data.put("message", "Email ou senha inválidos");
		data.put("status", HttpStatus.UNAUTHORIZED.value());
		data.put("error", "Não autorizado");
		data.put("path", "/login");
		res.getWriter().append(objMapper.writeValueAsString(data));
	}

}
