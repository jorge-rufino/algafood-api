package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LinksModel {

	private LinkModel rel;
	
	@Setter
	@Getter
	private class LinkModel {
		@Schema (example = "http://localhost:8080/v1/resource/id")
		private String href;
		//private boolean templated;
	}
}