package com.algaworks.algafood.core.jackson;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

//Serializa os objetos Page para Json

//Note que no "extends" poderiamos criar um serializador somente para o Page de Pedido mas usamos o "?" 
//para que faça a serialiação de qualquer classe que utilize o Page

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>>{

	@Override
	public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		
		gen.writeStartObject();
		
		gen.writeObjectField("content", page.getContent());
		gen.writeNumberField("pageSize", page.getSize());
		gen.writeNumberField("totalElements", page.getTotalElements());
		gen.writeNumberField("totalPages", page.getTotalPages());
		gen.writeNumberField("pageNumber", page.getNumber());
		
		gen.writeEndObject();
	}

}
