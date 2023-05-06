package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteDtoAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteDto> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	@Autowired
	private AlgaSecurity algaSecurity;

	public RestauranteDtoAssembler() {
		super(RestauranteController.class, RestauranteDto.class);
	}

	@Override
	public RestauranteDto toModel(Restaurante restaurante) {
		RestauranteDto restauranteDto = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteDto);

		if (algaSecurity.podeConsultarCozinhas()) {
			restauranteDto.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		}

		if (algaSecurity.podeConsultarCidades()) {
			if (restauranteDto.getEndereco() != null && restauranteDto.getEndereco().getCidade() != null) {
				restauranteDto.getEndereco().getCidade()
				.add(algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
			}
		}

		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteDto.add(algaLinks.linkToRestaurantes("restaurantes"));
			restauranteDto.add(algaLinks.linkToProdutos(restaurante.getId(), "produtos"));
			restauranteDto.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), "formas-pagamento"));
		}

		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			if (restaurante.isAtivo())
				restauranteDto.add(algaLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));

			if (restaurante.isInativo())
				restauranteDto.add(algaLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
			
			restauranteDto.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId(), "responsaveis"));
		}

		if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
			if (restaurante.isAberto())
				restauranteDto.add(algaLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));

			if (restaurante.isFechado() && restaurante.isAtivo())
				restauranteDto.add(algaLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
		}

		return restauranteDto;
	}

	@Override
	public CollectionModel<RestauranteDto> toCollectionModel(Iterable<? extends Restaurante> entities) {
		return super.toCollectionModel(entities).add(algaLinks.linkToRestaurantes().withSelfRel());
	}

}
