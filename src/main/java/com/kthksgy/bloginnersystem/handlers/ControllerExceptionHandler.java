package com.kthksgy.bloginnersystem.handlers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kthksgy.bloginnersystem.exceptions.DummyException;
import com.kthksgy.bloginnersystem.exceptions.NotPermittedException;
import com.kthksgy.bloginnersystem.exceptions.ResourceAlreadyExistsException;
import com.kthksgy.bloginnersystem.exceptions.ResourceNotFoundException;
import com.kthksgy.bloginnersystem.structures.ErrorResponseJson;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (!(body instanceof ErrorResponseJson)) {
			body = new ErrorResponseJson(
					status.value(),
					status.getReasonPhrase(),
					request.getContextPath(),
					String.format("HTTP%d", status.value()),
					ex.getMessage(),
					"");
		}
		return new ResponseEntity<>(body, headers, status);
	}

	/**
	 * リソースが見つからなかった場合に呼び出されます。
	 *
	 * @param ex      throwされたException
	 * @param request the current request
	 * @return エラーレスポンス
	 */
	@ExceptionHandler({ResourceNotFoundException.class})
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
	    return this.handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, request);
	}
	
	/**
	 * 新規に追加しようとしたエンティティが既に存在していた場合に呼び出されます。
	 *
	 * @param ex      throwされたException
	 * @param request the current request
	 * @return エラーレスポンス
	 */
	@ExceptionHandler({ResourceAlreadyExistsException.class})
	public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
	    return this.handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DummyException.class})
	public ResponseEntity<Object> handleDummyException(DummyException ex, WebRequest request) {
	    return this.handleExceptionInternal(ex, null, null, ex.getStatus(), request);
	}
	
	@ExceptionHandler({NoSuchElementException.class})
	public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
	    return this.handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({NotPermittedException.class})
	public ResponseEntity<Object> handleNotPermittedException(NotPermittedException ex, WebRequest request) {
	    return this.handleExceptionInternal(ex, null, null, HttpStatus.FORBIDDEN, request);
	}
	
	
	// TODO 実装する
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//	    // validationに失敗したフィールドのリストを取得
//	    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//	    // レスポンスの"Detail"に格納するために、validationに失敗したフィールドと失敗理由を連結
//	    StringBuilder sb = new StringBuilder();
//	    sb.append("{");
//	    fieldErrors.forEach(fieldError ->
//	    	sb
//	    	.append("\"")
//	    	.append(fieldError.getField())
//	                    .append(": ")
//	                    .append(fieldError.getDefaultMessage())
//	                    .append("; ")
//	    );
//
//	    ErrorResponseJson body = new ErrorResponseJson("Bad Request", errorDetailStr.toString(), "");
//	    body = new ErrorResponseJson(
//				status.value(),
//				status.getReasonPhrase(),
//				request.getContextPath(),
//				String.format("H%d", status.value()),
//				body instanceof Exception ? ((Exception) body).getMessage() : status.getReasonPhrase(),
//				"");
//
//	    return this.handleExceptionInternal(ex, body, headers, status, request);
//	}
	@Override
	/**
	 * Customize the response for BindException.
	 * <p>This method delegates to {@link #handleExceptionInternal}.
	 * @param ex the exception
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return a {@code ResponseEntity} instance
	 */
	protected ResponseEntity<Object> handleBindException(
			BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getFieldErrors();
		StringBuilder message = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		detail.append("{");
	    fieldErrors.forEach(fieldError -> {
	    	if(message.length() > 0) {
	    		message.append('\n');
	    	}
	    	message
	    	.append(fieldError.getField())
	    	.append(" : ")
	    	.append(fieldError.getDefaultMessage());
	    	detail
	    	.append("\"")
	    	.append(fieldError.getField())
	    	.append("\"")
	        .append(": ")
	        .append("\"")
	        .append(fieldError.getDefaultMessage())
	        .append("\"")
	        .append(", ");
	    }
	    );
	    detail.append("}");
		Object body = new ErrorResponseJson(
				status.value(),
				status.getReasonPhrase(),
				request.getContextPath(),
				String.format("VALIDATION_ERROR", status.value()),
				message.toString(),
				"");
		return handleExceptionInternal(ex, body, headers, status, request);
	}
}
