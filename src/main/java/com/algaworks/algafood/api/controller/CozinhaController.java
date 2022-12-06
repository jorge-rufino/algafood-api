package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(value = "/cozinhas") 
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository repository;
	
	@GetMapping
	public List<Cozinha> listar(){
		return repository.listar();
	}
	
	@GetMapping(value = "/{cozinhaId}")
	//Como a "PathVariable" e a variavel long tem o mesmo nome, poderiamos deixar a annotation "@PathVariable" sem o parametro
	public ResponseEntity<Cozinha> buscarId(@PathVariable("cozinhaId") Long cozinhaId) {
		Cozinha cozinha = repository.buscarPorId(cozinhaId);

		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}

//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	-> Faz o mesmo que a linha de baixo
		return ResponseEntity.notFound().build();

	}

	//Se for solicitado uma resposta em XML, vai usar este método
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml(){
		return new CozinhasXmlWrapper(repository.listar());
	}
	
	//Adiciona uma nova Cozinha
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)		//Status 201 para informar que foi criado o recurso
	public Cozinha adicionar (@RequestBody Cozinha cozinha) {
		return repository.salvar(cozinha);
	}
	
	//Atualiza/Altera uma Cozinha existente
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,@RequestBody Cozinha cozinha){
		Cozinha cozinhaAtual = repository.buscarPorId(cozinhaId);
		
		if (cozinhaAtual != null) {
//			cozinhaAtual.setNome(cozinha.getNome()); 
//			(Imagina varios atributos para serem alterados? Seriam varios "set", para evitar isto, usamos o "BeanUtils" para copiar tudo de 1x)
			
//			Copia todas as propriedades do objeto criado pelo "body" do "request" para a "cozinhaAtual...
//			do terceiro paremetro em diante, serão as variaveis/propriedades que serão ignoradas.
//			Aqui foi necessário ignorar o "id" pois ele vem nulo da "request", portanto não devemos altera-lo, somente as outras variaveis
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
			
			return ResponseEntity.ok(repository.salvar(cozinhaAtual));
		}
		
		return ResponseEntity.notFound().build();
		
	}
}
