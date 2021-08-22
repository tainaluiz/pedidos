package com.github.tainaluiz.pedidos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tainaluiz.pedidos.domain.Cidade;
import com.github.tainaluiz.pedidos.domain.Cliente;
import com.github.tainaluiz.pedidos.domain.Endereco;
import com.github.tainaluiz.pedidos.domain.enums.TipoCliente;
import com.github.tainaluiz.pedidos.dto.ClienteDTO;
import com.github.tainaluiz.pedidos.dto.ClienteNewDTO;
import com.github.tainaluiz.pedidos.repositories.ClienteRepository;
import com.github.tainaluiz.pedidos.repositories.EnderecoRepository;
import com.github.tainaluiz.pedidos.services.exceptions.DataIntegrityException;
import com.github.tainaluiz.pedidos.services.exceptions.IllegalParamException;
import com.github.tainaluiz.pedidos.services.exceptions.NoPropertyException;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Long id) throws ObjectNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				String.format("Objeto %s não encontrado. Id: %d", Cliente.class.getSimpleName(), id)));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		return repo.save(newObj);
	}

	public void delete(Long id) throws DataIntegrityException {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos!");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer size, String orderBy, String direction)
			throws IllegalParamException, NoPropertyException {
		PageRequest pageRequest = null;
		try {
			pageRequest = PageRequest.of(page, size, Direction.valueOf(direction), orderBy);
			return repo.findAll(pageRequest);
		} catch (IllegalArgumentException e) {
			throw new IllegalParamException("Parâmetro não permitido!");
		} catch (PropertyReferenceException e) {
			throw new NoPropertyException(String.format("Propriedade %s não encontrada no objeto %s!", orderBy,
					Cliente.class.getSimpleName()));
		}
	}

	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail());
	}

	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente obj = new Cliente(objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(objDTO.getTipo()), passEncoder.encode(objDTO.getSenha()));
		Cidade cidade = new Cidade(objDTO.getCidadeId());
		Endereco endereco = new Endereco(objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(),
				objDTO.getBairro(), objDTO.getCep(), obj, cidade);
		obj.getEnderecos().add(endereco);
		obj.getTelefones().add(objDTO.getTelefone1());
		if (objDTO.getTelefone2() != null) {
			obj.getTelefones().add(objDTO.getTelefone2());
		}
		if (objDTO.getTelefone2() != null) {
			obj.getTelefones().add(objDTO.getTelefone3());
		}
		return obj;
	}

}
