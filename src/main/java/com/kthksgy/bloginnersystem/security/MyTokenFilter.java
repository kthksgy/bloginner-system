package com.kthksgy.bloginnersystem.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kthksgy.bloginnersystem.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyTokenFilter extends GenericFilterBean {
	@Autowired
	private UserService userService;

	private final Algorithm algorithm;
	private final JWTVerifier verifier;

	public MyTokenFilter(final String passphrase) {
		Objects.requireNonNull(passphrase, "パスフレーズをnullには出来ません。");
		this.algorithm = Algorithm.HMAC256(passphrase);
		this.verifier = JWT.require(this.algorithm).build();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		//		filterChain.doFilter(request, response);
		String token = resolveToken(request);
		try {
			authentication(verifyToken(token));
			//		} catch (SignatureVerificationException e) {
			//		} catch (AlgorithmMismatchException e) {
			//		} catch (JWTDecodeException e) {
			//		} catch (TokenExpiredException e) {
			//		} catch (InvalidClaimException e) {
		} catch (JWTVerificationException e) {
			SecurityContextHolder.clearContext();
			// responseに書き込むと後から書き込まれるデフォルトのbodyと重複する
			// ErrorResponseJson errorResponseJson = new ErrorResponseJson(...);
			// errorResponseJson.writeToBody(response);

			// Spring Securityのデフォルトエラーハンドラーのエラーがどうしても消せないのでヘッダーでお茶を濁す
			// TODO 存在チェック
			((HttpServletResponse) response).setHeader("Bloginner-Error-Code", "JWT_ERROR");
		} catch (NullPointerException e) {
			SecurityContextHolder.clearContext();
		}
		// https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html
		filterChain.doFilter(request, response);
	}

	private String resolveToken(ServletRequest request) {
		String token = ((HttpServletRequest) request).getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7);
	}

	private DecodedJWT verifyToken(String token) {
		return this.verifier.verify(token);
	}

	private void authentication(DecodedJWT jwt) {
		String username = jwt.getSubject();
		userService
				.get(username)
				.map(u -> new MyLoginUser(u))
				.ifPresent(
						u -> SecurityContextHolder
								.getContext()
								.setAuthentication(
										new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities())));
	}

}
