package com.araki.sales.services;

import java.util.Date;
import java.util.Optional;

import com.araki.sales.model.Cliente;
import com.araki.sales.security.UserSS;
import com.araki.sales.services.exceptions.AuthorizationException;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.araki.sales.model.ItemPedido;
import com.araki.sales.model.PagamentoComBoleto;
import com.araki.sales.model.Pedido;
import com.araki.sales.model.enums.EstadoPagamento;
import com.araki.sales.repositories.ClienteRepository;
import com.araki.sales.repositories.ItemPedidoRepository;
import com.araki.sales.repositories.PagamentoRepository;
import com.araki.sales.repositories.PedidoRepository;
import com.araki.sales.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository PagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository ItemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Pedido não foi encontrado! ID:"
				+ id + " - Tipo:" + PedidoService.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
			
		}
		
		obj = pedidoRepository.save(obj);
		PagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		ItemPedidoRepository.saveAll(obj.getItens());
		
		emailService.sendOrderConfirmationHtmlEmail(obj);
//		emailService.sendOrderConfirmation(obj);
		
		return obj;
	}

	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		if (user == null){
			throw new AuthorizationException("Acesso Negado");
		}

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

		Cliente cliente = clienteService.find(user.getId());

		return pedidoRepository.findByCliente(cliente, pageRequest);
	}
}
