package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;

//Como queremos o Equals e HashCode apenas do ID, precisamos adicionar 2 annotations pra especificar isto

//Esta Annotation é o conjunto de: @ Getters, Setters, toString, EqualsAndHashCode e RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@JsonRootName("gastronomia")	
public class Cozinha {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@JsonIgnore					//Ignora/Oculta está propriedade da Representaçao do Recurso
//	@JsonProperty("titulo")		//Altera o nome da propriedade na Representaçao do Recurso
	@Column(nullable = false)
	private String nome;	
}
