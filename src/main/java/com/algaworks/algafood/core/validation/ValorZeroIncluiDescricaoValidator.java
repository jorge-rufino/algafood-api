package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object>{

	private String valorField;
	private String descricaoField;
	private String descricaoObrigatoria;
	
	
	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}
	
	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {

		boolean valido = true;
		
		try {
//	Passamos a Classe alvo, o nome do atributo, depois que queremos o metodo "get()" deste atributo	
//			No exemplo vamos estar pegando o valor da "taxaFrete"
			BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorField)
					.getReadMethod().invoke(objetoValidacao);
			
//			No exemplo vamos estar pegando o valor do "nome"
			String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField)
					.getReadMethod().invoke(objetoValidacao);
			
			if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
				
//	Passa as Strings para minusculo e verifica se a "descriçao" contem a "descricaoObrigatoria"
				valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
			}
		
			return valido;
			
		} catch (Exception e) {
			throw new ValidationException(e);
		}		
	}

}
