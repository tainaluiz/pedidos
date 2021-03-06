package com.github.tainaluiz.pedidos.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.tainaluiz.pedidos.domain.Categoria;
import com.github.tainaluiz.pedidos.domain.Cidade;
import com.github.tainaluiz.pedidos.domain.Cliente;
import com.github.tainaluiz.pedidos.domain.Endereco;
import com.github.tainaluiz.pedidos.domain.Estado;
import com.github.tainaluiz.pedidos.domain.ItemPedido;
import com.github.tainaluiz.pedidos.domain.Pagamento;
import com.github.tainaluiz.pedidos.domain.PagamentoComBoleto;
import com.github.tainaluiz.pedidos.domain.PagamentoComCartao;
import com.github.tainaluiz.pedidos.domain.Pedido;
import com.github.tainaluiz.pedidos.domain.Produto;
import com.github.tainaluiz.pedidos.domain.enums.EstadoPagamento;
import com.github.tainaluiz.pedidos.domain.enums.Perfil;
import com.github.tainaluiz.pedidos.domain.enums.TipoCliente;
import com.github.tainaluiz.pedidos.repositories.CategoriaRepository;
import com.github.tainaluiz.pedidos.repositories.CidadeRepository;
import com.github.tainaluiz.pedidos.repositories.ClienteRepository;
import com.github.tainaluiz.pedidos.repositories.EnderecoRepository;
import com.github.tainaluiz.pedidos.repositories.EstadoRepository;
import com.github.tainaluiz.pedidos.repositories.ItemPedidoRepository;
import com.github.tainaluiz.pedidos.repositories.PagamentoRepository;
import com.github.tainaluiz.pedidos.repositories.PedidoRepository;
import com.github.tainaluiz.pedidos.repositories.ProdutoRepository;

@Service
public class DBService {

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public void instantiateTestDatabase() throws ParseException {
		Categoria cat1 = new Categoria("Inform??tica");
		Categoria cat2 = new Categoria("Escrit??rio");
		Categoria cat3 = new Categoria("Cozinha");
		Categoria cat4 = new Categoria("Sa??de");
		Categoria cat5 = new Categoria("Brinquedos");
		Categoria cat6 = new Categoria("Moda");
		Categoria cat7 = new Categoria("Casa");

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));

		Produto p1 = new Produto("Computador", 2000d);
		Produto p2 = new Produto("Impressora", 800d);
		Produto p3 = new Produto("Mouse", 80d);
		Produto p4 = new Produto("Fog??o", 500d);
		Produto p5 = new Produto("Geladeira", 2000d);
		Produto p6 = new Produto("Boneca", 100d);
		Produto p7 = new Produto("Camiseta", 50d);
		Produto p8 = new Produto("Cal??a", 70d);
		Produto p9 = new Produto("Mesa de escrit??rio", 100d);
		Produto p10 = new Produto("Barra de Cereal", 1.5);
		Produto p11 = new Produto("Jogo de cama", 100d);

		List<Produto> produtosCat1 = createProdutosCategoria1(cat1);

		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().add(cat1);
		p4.getCategorias().add(cat3);
		p5.getCategorias().add(cat3);
		p6.getCategorias().add(cat5);
		p7.getCategorias().add(cat6);
		p8.getCategorias().add(cat6);
		p9.getCategorias().add(cat2);
		p10.getCategorias().add(cat4);
		p11.getCategorias().add(cat7);

		produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));
		produtoRepository.saveAll(produtosCat1);

		Estado est1 = new Estado("Minas Gerais");
		Estado est2 = new Estado("S??o Paulo");

		estadoRepository.saveAll(Arrays.asList(est1, est2));

		Cidade c1 = new Cidade("Uberl??ndia", est1);
		Cidade c2 = new Cidade("S??o Paulo", est2);
		Cidade c3 = new Cidade("Campinas", est2);

		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		Cliente cli1 = new Cliente("Maria Silva", "marisilva@teste.com", "36378912377", TipoCliente.PESSOAFISICA,
				passEncoder.encode("123"));
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		Cliente cli2 = new Cliente("Ana J??lia", "anajulia@teste.com", "36378912377", TipoCliente.PESSOAFISICA,
				passEncoder.encode("123"));
		cli2.addPerfil(Perfil.ADMIN);

		clienteRepository.saveAll(Arrays.asList(cli1, cli2));

		Endereco e1 = new Endereco("Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco("Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		enderecoRepository.saveAll(Arrays.asList(e1, e2));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Pedido ped1 = new Pedido(sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(sdf.parse("10/10/2017 19:35"), cli1, e2);

		Pagamento pagto1 = new PagamentoComCartao(EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);

		Pagamento pagto2 = new PagamentoComBoleto(EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));

		ItemPedido ip1 = new ItemPedido(ped1, p1, 0d, 1, 2000d);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0d, 2, 80d);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100d, 1, 800d);

		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}

	private List<Produto> createProdutosCategoria1(Categoria cat1) {
		List<Produto> produtos = new ArrayList<>();
		for (int i = 12; i <= 50; i++) {
			Produto p = new Produto("Produto " + i, 10d);
			p.getCategorias().add(cat1);
			produtos.add(p);
		}
		return produtos;
	}

}
