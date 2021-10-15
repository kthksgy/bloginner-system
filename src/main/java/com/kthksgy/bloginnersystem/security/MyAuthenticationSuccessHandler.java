package com.kthksgy.bloginnersystem.security;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kthksgy.bloginnersystem.entities.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final int TOKEN_LIFE_MINUTES = 30;

	private final Algorithm algorithm;

	public MyAuthenticationSuccessHandler(String passphrase) {
		Objects.requireNonNull(passphrase, "パスフレーズをnullには出来ません。");
		this.algorithm = Algorithm.HMAC256(passphrase);
	}

	/* デフォルトや用意されている標準実装クラスは利用せず、HTTPステータス200を返すだけのハンドラを実装します。 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		if (response.isCommitted()) {
			log.info("Response has already been committed.");
			return;
		}
		MyLoginUser loginUser = (MyLoginUser) auth.getPrincipal();
		setToken(response, generateToken(loginUser.getUser()));
		response.setStatus(HttpStatus.OK.value());

		response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	    response.getOutputStream().print(loginUser.toJsonString());
	    response.flushBuffer();
	    
	    clearAuthenticationAttributes(request);
	}

	private String generateToken(User user) {
		OffsetDateTime issuedAt = OffsetDateTime.now();
		OffsetDateTime notBefore = issuedAt;
		OffsetDateTime expiresAt = issuedAt.plus(Duration.ofMinutes(TOKEN_LIFE_MINUTES));

		String token = JWT.create()
				.withIssuedAt(Date.from(issuedAt.toInstant()))
				.withNotBefore(Date.from(notBefore.toInstant()))
				.withExpiresAt(Date.from(expiresAt.toInstant()))
				.withSubject(user.getUsername())
				.sign(algorithm);
		log.debug("トークンが生成されました。<{}>", token);
		return token;
	}

	private void setToken(HttpServletResponse response, String token) {
		response.setHeader("Authorization", String.format("Bearer %s", token));
	}
	
	/**
	 * Removes temporary authentication-related data which may have been stored in the
	 * session during the authentication process.
	 */
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}