package com.algaworks.algafood;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.algaworks.algafood.modelo.Cliente;
import com.algaworks.algafood.service.AtivacaoClienteService;

//Indica que a classe é um "controlador"
@Controller
public class MeuPrimeiroController {
	
	private AtivacaoClienteService ativacaoClienteService;
	
	public MeuPrimeiroController(AtivacaoClienteService ativacaoClienteService) {	
		this.ativacaoClienteService = ativacaoClienteService;
	}

	//Caminho para chamar a classe
	//Retorno do metodo será a resposta do corpo da requisição (body)
	@GetMapping("/hello")
	@ResponseBody
	public String hello() {
		
		Cliente joao = new Cliente("joao", "joao@gmail.com", "99999-9999");
		
		ativacaoClienteService.ativar(joao);
		
		return "Hello!";
	}
}
