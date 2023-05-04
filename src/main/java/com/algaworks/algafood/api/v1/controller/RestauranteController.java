package com.algaworks.algafood.api.v1.controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeDtoAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoDtoAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.RestauranteInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeDto;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoDto;
import com.algaworks.algafood.api.v1.model.RestauranteDto;
import com.algaworks.algafood.api.v1.model.input.RestauranteInputDto;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.CozinhaService;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private RestauranteDtoAssembler restauranteDtoAssembler;
	
	@Autowired
	private RestauranteInputDtoDisassembler restauranteInputDisassembler;
	
	@Autowired
	private RestauranteBasicoDtoAssembler restauranteBasicoDtoAssembler;
	
	@Autowired
	private RestauranteApenasNomeDtoAssembler restauranteApenasNomeDtoAssembler;
	
//	@JsonView(RestauranteView.Resumo.class)
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<RestauranteBasicoDto>> listar(ServletWebRequest request){
		
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacaoRestaurante = restauranteService.getDataUltimaAtualizacao();
		OffsetDateTime dataUltimaAtualizacaoCozinha = cozinhaService.getDataUltimaAtualizacao();
		
		var segundosDataRestaurante = new BigDecimal(dataUltimaAtualizacaoRestaurante.toEpochSecond());
		var segundosDataCozinha = new BigDecimal(dataUltimaAtualizacaoCozinha.toEpochSecond());
		var somaSegundos = segundosDataRestaurante.add(segundosDataCozinha);
		
		if(dataUltimaAtualizacaoRestaurante != null && dataUltimaAtualizacaoCozinha != null) {
			eTag = String.valueOf(somaSegundos);
		}
		
		if(request.checkNotModified(eTag)) {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED) 	//Status 304
		               .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic())
		               .eTag(eTag)
		               .build();
		}
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.eTag("")
				.body(restauranteBasicoDtoAssembler.toCollectionModel(restauranteService.listar()));
	}

//	@JsonView(RestauranteView.ApenasNome.class)
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping(params = "projecao=apenas-nome")
	public CollectionModel<RestauranteApenasNomeDto> listarApenasNome(){		
		return restauranteApenasNomeDtoAssembler.toCollectionModel(restauranteService.listar());
	}

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping(value = "/{id}")
	public RestauranteDto buscarId(@PathVariable Long id){
		Restaurante restaurante = restauranteService.buscarPorId(id);
		
		return restauranteDtoAssembler.toModel(restaurante);
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDto adicionar(@RequestBody @Valid RestauranteInputDto restauranteInput){		
		try {
			Restaurante restaurante =restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteDtoAssembler.toModel(restauranteService.salvar(restaurante));
			
//		Podemos utilizar o operador "|" em vez de fazer um outro bloco "try/catch" pois ambas disparam a mesma exception
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
	}
		
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{id}")
	public RestauranteDto atualizar(@PathVariable Long id,@RequestBody @Valid RestauranteInputDto restauranteInput){
	
//		Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		
		Restaurante restauranteAtual = restauranteService.buscarPorId(id);		
			
		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
		
	//Não precisamos ignorar a "dataAtualizacao" pois o hibernate se encarrega disso
//		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro"
//				,"produtos");
		try {
			return restauranteDtoAssembler.toModel(restauranteService.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		} catch (CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		restauranteService.deletar(id);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
//	Caso fosse passado um "restauranteId" que nao existe, ia disparar o erro "404" porem o erro adequado é "400"
//	portanto precisamos fazer o "try/catch"
		try {
			restauranteService.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			restauranteService.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fecharRestaurante(@PathVariable Long restauranteId) {
		restauranteService.fecharRestaurante(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrirRestaurante(@PathVariable Long restauranteId) {
		restauranteService.abrirRestaurante(restauranteId);
		return ResponseEntity.noContent().build();
	}
}
