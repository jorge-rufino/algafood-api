package com.algaworks.algafood.api.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

//Esta classe captura Exceptions de todos os controladores 

//Estendo esta classe, ela tratará varias exceptions internas que podemos sobrecrever os métodos

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
//	Podemos capturar exceçoes de mais de uma classe, basta adicionar chaves e vírgula como argumentos do método
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){		

		HttpStatus status = HttpStatus.NOT_FOUND;		
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String detail = ex.getMessage();
		
//		Aqui é onde de fato criamos o objeto "Problem" atraves do ".build()"
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
			
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		}
		
		if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}
		
		ProblemType problemType = ProblemType.ERRO_REQUISICAO;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String pathField = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.ERRO_REQUISICAO;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. "
				+ "Corrija e informe um valor compatível com o tipo '%s'.", 
				pathField,ex.getValue(),ex.getTargetType().getSimpleName());
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String pathField = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.ERRO_REQUISICAO;
		String detail = String.format("A propriedade '%s' não existe. Corrija ou remova esta propriedade tente novamente.", 
				pathField);
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
//	Este método sobrescrito é chamado toda vez que uma Exception interna é capturada
//	Iremos fazer nossos métodos chamarem ele tb para padronizar tudo.
//	Perceba que agora nossas exceptions que utilizam o "Problem" não caem nos "if/else"
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
//		Por padrao as Exceptions internas vem com o "body" nulo
		if (body == null) {
			body = Problem.builder()
					.status(status.value())
					.title(status.getReasonPhrase()).build();
		} 
		
		else if (body instanceof String) {
			body = Problem.builder()
					.status(status.value())
					.title((String) body).build();
		}		
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
//	Este método é um "builder/fabrica" de "ProblemType", ele retorna um "builder"
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);
	}
	
	private String joinPath(List<Reference> list) {
		return list.stream()
				.map(reference -> reference.getFieldName())
				.collect(Collectors.joining("."));
	}
}
