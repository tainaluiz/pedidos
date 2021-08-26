package com.github.tainaluiz.pedidos.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tainaluiz.pedidos.domain.Cliente;
import com.github.tainaluiz.pedidos.domain.ItemPedido;
import com.github.tainaluiz.pedidos.domain.PagamentoComBoleto;
import com.github.tainaluiz.pedidos.domain.Pedido;
import com.github.tainaluiz.pedidos.domain.enums.EstadoPagamento;
import com.github.tainaluiz.pedidos.repositories.ItemPedidoRepository;
import com.github.tainaluiz.pedidos.repositories.PagamentoRepository;
import com.github.tainaluiz.pedidos.repositories.PedidoRepository;
import com.github.tainaluiz.pedidos.security.UserSS;
import com.github.tainaluiz.pedidos.services.exceptions.AuthorizationException;
import com.github.tainaluiz.pedidos.services.exceptions.IllegalParamException;
import com.github.tainaluiz.pedidos.services.exceptions.NoPropertyException;
import com.github.tainaluiz.pedidos.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;

	public Pedido find(Long id) throws ObjectNotFoundException {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				String.format("Objeto %s não encontrado. Id: %d", Pedido.class.getSimpleName(), id)));
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);

		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}

		obj = repo.save(obj);

		pagamentoRepository.save(obj.getPagamento());

		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0d);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}

		itemPedidoRepository.saveAll(obj.getItens());

		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}

	public Page<Pedido> findPage(Integer page, Integer size, String orderBy, String direction)
			throws IllegalParamException, NoPropertyException {
		UserSS user = UserService.authenticated();

		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}

		PageRequest pageRequest = null;
		try {
			pageRequest = PageRequest.of(page, size, Direction.valueOf(direction), orderBy);
			Cliente cliente = clienteService.find(user.getId());
			return repo.findByCliente(cliente, pageRequest);
		} catch (IllegalArgumentException e) {
			throw new IllegalParamException("Parâmetro não permitido!");
		} catch (PropertyReferenceException e) {
			throw new NoPropertyException(String.format("Propriedade %s não encontrada no objeto %s!", orderBy,
					Pedido.class.getSimpleName()));
		}
	}
}
