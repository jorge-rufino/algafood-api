package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import lombok.extern.slf4j.Slf4j;

//Esta classe captura Exceptions de todos os controladores 

//Estendo esta classe, ela tratará varias exceptions internas que podemos sobrecrever os métodos

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final String MSG_ERRO_DADOS_INVALIDOS = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro inesperado no sistema. Tente novamente e se o problema persistir,"
			+ "entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;

//	Quando buscamos um foto e o mediatype passado na requisiçao é diferente do da foto armazenada, é disparada esta
//	exception porém ela acaba caindo no método "handleExceptionInternal" que nós sobrescrevemos aqui e como ele agora está
//	retornando um "body" que não é nulo, acaba dando erro "Could not find acceptable representation", portanto temos que
//	reescreve-lo. Este erro não é perceptível para o consumidor da API, somente para desenvolvedores.
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		return ResponseEntity.status(status).headers(headers).build();
	}
	
//	Capturas as exceptions dos filtros de pesquisa
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
	
		
		return handleValidacaoInternal(ex, ex.getBindingResult(), headers, status, request);				
	}
	
//	Podemos capturar exceçoes de mais de uma classe, basta adicionar chaves e vírgula como argumentos do método
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){		

		HttpStatus status = HttpStatus.NOT_FOUND;		
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		
//		Aqui é onde de fato criamos o objeto "Problem" atraves do ".build()"
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
		
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
		
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	//Captura as exceptions quando uma propriedade recebe um valor diferente do esperado
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String pathField = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.ERRO_REQUISICAO;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. "
				+ "Corrija e informe um valor compatível com o tipo '%s'.", 
				pathField,ex.getValue(),ex.getTargetType().getSimpleName());
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	//Captura as exceptions quando são passadas propriedades que não existem ou estão sendo ignoradas pelo json
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String pathField = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.ERRO_REQUISICAO;
		String detail = String.format("A propriedade '%s' não existe. Corrija ou remova esta propriedade tente novamente.", 
				pathField);
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	//Captura as exceptions quando o Parametro da URL está de um tipo inválido
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException)ex, headers, status, request);
		}
		
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido."
				+ " Corrija e informe um valor compatível com o tipo '%s'.", 
				ex.getName(),ex.getValue(),ex.getRequiredType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	//Captura as exceptions quando o caminho da URL está inválido
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
	
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso '%s' que você tentou acessar, não existe.", 
				ex.getRequestURL());
		
		Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
//	Captura as exceptions de validação do BeanValidation (@Valid)
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return handleValidacaoInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
//	Captura erros de validacao da Atualizao Partical (PATCH)
	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<?> handleValidacaoException(ValidacaoException ex, WebRequest request){
		return handleValidacaoInternal(ex, ex.getBindingResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	public ResponseEntity<Object> handleValidacaoInternal(
			Exception ex, BindingResult bindingResult, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.ERRO_VALIDACAO;
		String detail = MSG_ERRO_DADOS_INVALIDOS;
		
		List<Problem.Field> problemFields = bindingResult.getAllErrors()
				.stream()
				.map(objectError -> {					
					String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
					
					String name = objectError.getObjectName();
					
					if (objectError instanceof FieldError) {
						name = ((FieldError) objectError).getField();
					}
					
					return Problem.Field.builder()
					.name(name)
					.userMessage(message)
					.build();
				})
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.fields(problemFields)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
//	Captura as exceptions de acesso negado
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(AccessDeniedException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.FORBIDDEN;
	    ProblemType problemType = ProblemType.ACESSO_NEGADO;
	    String detail = ex.getMessage();

	    Problem problem = createProblemBuilder(status, problemType, detail)
	            .userMessage(detail)
	            .userMessage("Você não possui permissão para executar essa operação.")
	            .build();

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
//	Captura as demais exceptions que não foram identificadas
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOthersExceptions(Exception ex, WebRequest request){
				
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;
		
//		ex.printStackTrace();
		log.error(ex.getMessage(), ex);

		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
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
					.title(status.getReasonPhrase())
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.timestamp(OffsetDateTime.now())
					.build();
		} 
		
		else if (body instanceof String) {
			body = Problem.builder()					
					.title((String) body)
					.status(status.value())
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.timestamp(OffsetDateTime.now())
					.build();
		}		
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
//	Este método é um "builder/fabrica" de "ProblemType", ele retorna um "builder"
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail)
				.timestamp(OffsetDateTime.now());
	}
	
	private String joinPath(List<Reference> list) {
		return list.stream()
				.map(reference -> reference.getFieldName())
				.collect(Collectors.joining("."));
	}
}
