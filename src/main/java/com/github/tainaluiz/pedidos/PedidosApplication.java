package com.github.tainaluiz.pedidos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

@SpringBootApplication
public class PedidosApplication implements CommandLineRunner {

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

	public static void main(String[] args) {
		SpringApplication.run(PedidosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initDatabase();
	}

	private void initDatabase() throws ParseException {
		Categoria cat1 = new Categoria("Informática");
		Categoria cat2 = new Categoria("Escritório");
		Categoria cat3 = new Categoria("Cozinha");
		Categoria cat4 = new Categoria("Saúde");
		Categoria cat5 = new Categoria("Brinquedos");
		Categoria cat6 = new Categoria("Moda");
		Categoria cat7 = new Categoria("Casa");

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));

		Produto p1 = new Produto("Computador", 2000d);
		Produto p2 = new Produto("Impressora", 800d);
		Produto p3 = new Produto("Mouse", 80d);

		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().add(cat1);

		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado("Minas Gerais");
		Estado est2 = new Estado("São Paulo");

		estadoRepository.saveAll(Arrays.asList(est1, est2));

		Cidade c1 = new Cidade("Uberlândia", est1);
		Cidade c2 = new Cidade("São Paulo", est2);
		Cidade c3 = new Cidade("Campinas", est2);

		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		Cliente cli1 = new Cliente("Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		clienteRepository.save(cli1);

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

}
