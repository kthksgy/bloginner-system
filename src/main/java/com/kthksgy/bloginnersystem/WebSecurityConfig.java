package com.kthksgy.bloginnersystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.GenericFilterBean;

import com.kthksgy.bloginnersystem.security.MyAccessDeniedHandler;
import com.kthksgy.bloginnersystem.security.MyAuthenticationEntryPoint;
import com.kthksgy.bloginnersystem.security.MyAuthenticationFailureHandler;
import com.kthksgy.bloginnersystem.security.MyAuthenticationSuccessHandler;
import com.kthksgy.bloginnersystem.security.MyTokenFilter;
import com.kthksgy.bloginnersystem.security.MyUserDetailsService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Autowired
	BloginnerProperties bloginnerProperties;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				/* 認可(権限に応じてリクエストの許可／拒否を制御) */
				.authorizeRequests()
				/*
				 * !! 指定した順番にマッチングされる
				 * !! 権限は上書き不可能
				 * !! "/{text}"や"/**"は"/hello"を含む
				 * !! 範囲が狭い順に指定(直値→ワイルドカード(階層が深い順))
				 * !! 権限が高い順に指定
				 * */

				/* ユーザー関連 */
				.mvcMatchers("/api/me", "/api/my-authorities", "/api/user/change-password")
				.hasAnyRole("ADMINISTRATOR", "CONTRIBUTOR")
				.mvcMatchers("/api/users", "/api/user/register", "/api/user/change-restriction", "/api/user/remove")
				.hasAnyRole("ADMINISTRATOR")
				/* ユーザー(ワイルドカード) */
				.mvcMatchers("/api/user/**")
				.hasAnyRole("ADMINISTRATOR")

				/* 記事 */
				.mvcMatchers("/api/article/post", "/api/article/update", "/api/article/remove")
				.hasRole("CONTRIBUTOR")
				.mvcMatchers("/api/articles")
				.permitAll()
				/* 記事(ワイルドカード) */
				.mvcMatchers("/api/article/**")
				.permitAll()

				/* コメント */
				.mvcMatchers("/api/comments", "/api/comment/is-published", "/api/comment/delete")
				.hasRole("CONTRIBUTOR")
				.mvcMatchers("/api/article-comments", "/api/comment/post")
				.permitAll()

				/* サイト */
				.mvcMatchers("/api/site-information")
				.permitAll()

				/* テスト */
				.mvcMatchers("/api/test/**")
				.permitAll()
				
				.mvcMatchers("/**")
				.permitAll()
//				.mvcMatchers("/admin/")
//				.permitAll()
//				.mvcMatchers("/resources/**")
//				.permitAll()

				/* 上記に記されていないパスへのアクセスを禁止 */
				.anyRequest()
				.denyAll()

				.and()
				// EXCEPTION
				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler())
				.and()
				// LOGIN
				.formLogin()
				.loginProcessingUrl("/api/login").permitAll()
				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
				.and()
				// LOGOUT
				.logout()
				.logoutUrl("/api/logout")
				//				.invalidateHttpSession(true)
				//				.deleteCookies("JSESSIONID")
				.logoutSuccessHandler(logoutSuccessHandler())
				//.addLogoutHandler(new CookieClearingLogoutHandler())
				.and()
				/* CSRF対策の無効化(JWTはAuthorizationヘッダーで送受信するため) */
				.csrf().disable()
				/* JWTで認証を行うフィルター */
				.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class)
				/* セッション管理 */
				.sessionManagement()
				/* ステートレス */
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//				.ignoringAntMatchers("/login")
		//				.csrfTokenRepository(new CookieCsrfTokenRepository());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth,
			PasswordEncoder passwordEncoder) throws Exception {
		auth.eraseCredentials(true)
				.userDetailsService(myUserDetailsService)
				.passwordEncoder(passwordEncoder);
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	AuthenticationEntryPoint authenticationEntryPoint() {
		return new MyAuthenticationEntryPoint();
	}

	AccessDeniedHandler accessDeniedHandler() {
		return new MyAccessDeniedHandler();
	}

	AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new MyAuthenticationSuccessHandler(bloginnerProperties.getPassphrase());
	}

	AuthenticationFailureHandler authenticationFailureHandler() {
		return new MyAuthenticationFailureHandler();
	}

	/** 
	 * ログアウトが正常終了した時の処理を実装したハンドラを設定します。
	 * HTTPステータスを返すだけのSpring Securityの標準実装クラスHttpStatusReturningLogoutSuccessHandlerがあるのでこれを利用しました。
	 * ログアウト時に行うセッション破棄やクッキー削除はコンフィグレーションで行うので実装は不要です。
	 */
	LogoutSuccessHandler logoutSuccessHandler() {
		return new HttpStatusReturningLogoutSuccessHandler();
	}

	@Bean /* @Beanを指定する事でUserRepositoryを注入できる */
	GenericFilterBean tokenFilter() {
		return new MyTokenFilter(bloginnerProperties.getPassphrase());
	}

}
