package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number>{

	private int numeroMultiplo;	
	
	@Override
	public void initialize(Multiplo constraintAnnotation) {
		
//		Aqui faz com que pegue o "numero" passado como parametro da annotation
		this.numeroMultiplo = constraintAnnotation.numero();
	}
	
	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		boolean valido = true; 
		
		if (value != null) {
			var valorDecimal = BigDecimal.valueOf(value.doubleValue());
			var multiploDecimal = BigDecimal.valueOf(this.numeroMultiplo);
			var resto = valorDecimal.remainder(multiploDecimal);
			
//			Verifica se o resto da divisao é 0
			valido = BigDecimal.ZERO.compareTo(resto) == 0 ;
		}
		
		return valido;
	}

}
