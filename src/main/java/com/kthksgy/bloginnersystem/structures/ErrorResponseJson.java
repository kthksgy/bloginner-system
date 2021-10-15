package com.kthksgy.bloginnersystem.structures;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString @EqualsAndHashCode
public class ErrorResponseJson {
	/** エラー発生日時 */
	private String timestamp;
	/** ステータスコード (例: 404, 500) */
	private Integer status;
	/** ステータステキスト(例: 404 → Not Found.) */
	private String error;
	/** エラーが発生したコンテキストパス */
	private String path;
	
	/** アプリケーション独自のエラーコード */
	private String code;
	/** エラーメッセージ */
	private String message;
	/** データ(任意の構造体のJSON文字列) */
	private String data;
	
	public ErrorResponseJson(Integer status, String error, String path, String code, String message, String data) {
		this(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS), status, error, path, code, message, data);
	}
	
	public ErrorResponseJson(OffsetDateTime odt, Integer status, String error, String path, String code, String message, String data) {
		this(odt.toString(), status, error, path, code, message, data);
	}

	public ErrorResponseJson(String timestamp, Integer status, String error, String path, String code, String message,
			String data) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.path = path;
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public void writeToBody(ServletResponse response) throws IOException {
		writeToBody((HttpServletResponse) response);
	}
	
	public void writeToBody(HttpServletResponse response) throws IOException {
		if(response.isCommitted()) {
			return;
		}
		response.setStatus(this.status);
		this.code = response.getHeader("Bloginner-Error-Code") == null ? this.code : response.getHeader("Bloginner-Error-Code");
        response.setHeader("Bloginner-Error-Code", this.code);
		response.setContentType("application/json");
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(this));
	}
}
