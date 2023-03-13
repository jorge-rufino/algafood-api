package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoDto;
import com.algaworks.algafood.api.model.input.ItemPedidoInputDto;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;

//Esta classe cria uma instancia de "ModelMapper" dentro do contexto do Spring

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		
		var modelMapper = new ModelMapper();
		
//		Criamos um novo mapeamento entre "Endereco" e "EnderecoDto"
		var enderecoToEnderecoDtoTypeMap = modelMapper.createTypeMap(
				Endereco.class, EnderecoDto.class);
		
//		Definimos que o "nome" do "Estado" é valor do atributo "estado" da classe "CidadeResumoDto"
		enderecoToEnderecoDtoTypeMap.<String>addMapping(
				enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(), 
				(enderecoDtoDestino, valor) -> enderecoDtoDestino.getCidade().setEstado(valor));
	
//Precisamos adicionar esta configuraçao pois como no input da requisição estamos passando um Long "produtoId" em vez de um objeto
//de "ProdutoIdInput", o Spring se perde a acaba relacionando o id do produto com o id do "ItemPedido", portanto fazemos esta config para
//que o spring ignore o Id de "ItemPedido" e assim faça o relacionamento correto.
//  Se tivessemso criado a classe "ProdutoIdInput" não precisariamos dessa config.
		
		modelMapper.createTypeMap(ItemPedidoInputDto.class, ItemPedido.class)
	    .addMappings(mapper -> mapper.skip(ItemPedido::setId));
		
		return modelMapper;
	}
}
