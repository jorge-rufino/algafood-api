package com.algaworks.algafood.api.v2.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CozinhaInputDtoV2 {

    @NotBlank
    private String nomeCozinha;   
}  