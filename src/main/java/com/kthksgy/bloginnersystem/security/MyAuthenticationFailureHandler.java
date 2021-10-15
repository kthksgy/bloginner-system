package com.kthksgy.bloginnersystem.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.kthksgy.bloginnersystem.structures.ErrorResponseJson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

	/* デフォルトや用意されている標準実装クラスは利用せず、HTTPステータス403とデフォルトのメッセージを返すだけのハンドラを実装します。 */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
//    	if(exception instanceof BadCredentialsException) {
//    		
//    	}
    	if (response.isCommitted()) {
			log.info("Response has already been committed.");
			return;
		}
    	response.resetBuffer();
    	ErrorResponseJson errorResponseJson = new ErrorResponseJson(
        		HttpStatus.FORBIDDEN.value(),
        		HttpStatus.FORBIDDEN.getReasonPhrase(),
        		request.getContextPath(),
        		response.getHeader("Bloginner-Error-Code") != null ? response.getHeader("Bloginner-Error-Code") : "A403",
        		"認証に失敗しました。",
        		"");
        errorResponseJson.writeToBody(response);
        response.flushBuffer();
    }

}
