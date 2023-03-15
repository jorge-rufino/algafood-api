package com.algaworks.algafood.core.data;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.algaworks.algafood.domain.exception.NegocioException;

public class PageableTranslate {

	public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
//	O filtro aqui verifica se o parametro existe no "Map" e caso nao exista ele dispara a exception	
		var orders = pageable.getSort().stream()
				.filter(order -> {
					if (!fieldsMapping.containsKey(order.getProperty())) {
						throw new NegocioException("Parâmetro de ordenação '"+order.getProperty()+"' é inválido!");
					}
					return true;
				})
				.map(order -> new Sort.Order(order.getDirection(), fieldsMapping.get(order.getProperty())))
				.toList();
				
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
	}
}
