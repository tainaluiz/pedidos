package com.github.tainaluiz.pedidos.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Cliente;
import com.github.tainaluiz.pedidos.repositories.ClienteRepository;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailService emailService;

	private Random rand = new Random();

	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);

		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}

		String newPass = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPass));

		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		int randomCode = 0;

		if (opt == 0) {
			randomCode = rand.nextInt(10) + 48;
		} else if (opt == 1) {
			randomCode = rand.nextInt(26) + 65;
		} else {
			randomCode = rand.nextInt(26) + 97;
		}

		return (char) randomCode;
	}
}
