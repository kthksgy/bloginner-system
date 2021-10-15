package com.kthksgy.bloginnersystem.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.kthksgy.bloginnersystem.structures.ErrorResponseJson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/*
	 * デフォルトや用意されている標準実装クラスは利用せず、HTTPステータス401とデフォルトのメッセージを返すだけの処理を実装する。
	 * 
	 * AuthenticationExceptionのサブクラスでより詳細な例外理由を知ることができる場合があります。
	 * 
	 * - BadCredentialsException
	 * 認証情報が無効な場合にスローされる
	 * - LockedException
	 * アカウントがロックされている場合にスローされる
	 * - DisabledException
	 * アカウントが無効な場合にスローされる
	 * - AccountExpiredException
	 * アカウントの有効期限が切れている場合にスローされる
	 * - CredentialsExpiredException
	 * 認証情報の有効期限が切れている場合にスローされる
	 * - SessionAuthenticationException
	 * 同一ユーザーによる最大セッション数が設定を超えた場合にスローされる
	 */
	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (response.isCommitted()) {
			log.info("Response has already been committed.");
			return;
		}
		response.resetBuffer();
		ErrorResponseJson errorResponseJson = new ErrorResponseJson(
        		HttpStatus.UNAUTHORIZED.value(),
        		HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        		request.getContextPath(),
        		"A401",
        		"認証されていません。",
        		"");
		errorResponseJson.writeToBody(response);
		response.flushBuffer();
	}
}
