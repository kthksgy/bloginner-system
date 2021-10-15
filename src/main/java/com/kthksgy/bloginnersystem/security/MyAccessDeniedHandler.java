package com.kthksgy.bloginnersystem.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.kthksgy.bloginnersystem.structures.ErrorResponseJson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	
	/*
	 * デフォルトや用意されている標準実装クラスは利用せず、HTTPステータス403とデフォルトのメッセージを返すだけのハンドラを実装する。
	 * 
	 * AccessDeniedExceptionのサブクラスでより詳細な例外理由を知ることができます。
	 * - AuthorizationServiceException
	 * AccessDecisionManagerの実装に問題があった場合にスローされることがあります
	 * - org.springframework.security.web.server.csrf.CsrfException
	 * CSRFトークンが無効な場合にスローされます
	 * - org.springframework.security.web.csrf.CsrfException
	 * CSRFトークンが無効な場合にスローされます
	 *   - InvalidCsrfTokenException
	 *   リクエストのCSRFトークンと一致しない場合にスローされます
	 *   - MissingCsrfTokenException
	 *   リクエストにCSRFトークンが見つからない場合にスローされます
	 */
	@Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
		if (response.isCommitted()) {
			log.info("Response has already been committed.");
			return;
		}
		response.resetBuffer();
        ErrorResponseJson errorResponseJson = new ErrorResponseJson(
        		HttpStatus.FORBIDDEN.value(),
        		HttpStatus.FORBIDDEN.getReasonPhrase(),
        		request.getContextPath(),
        		"A403",
        		"アクセスは禁止されています。",
        		"");
        errorResponseJson.writeToBody(response);
        response.flushBuffer();
    }
}
