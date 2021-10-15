package com.kthksgy.bloginnersystem.helpers;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public interface PasswordHelper {
	public static final String ENCODED_PASSWORD_PREFIX = "^\\{.+\\}.*";
	public static final int MIN_PASSWORD_LENGTH= 8;
	public static final int MAX_PASSWORD_LENGTH = 32;
	public static final char[] PASSWORD_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.?/-".toCharArray();
	public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9.?/-]*$";
	
	public static String generatePassword(int length) {
		return generatePassword(length, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
	}
	
	public static String generatePassword(int length, final Integer min, final Integer max) {
		if(min != null) {
			length = Math.max(length, min);
		}
		if(max != null) {
			length = Math.min(length, max);
		}
		StringBuffer sb = new StringBuffer(length);
		Random r;
		try {
			r = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			r = new Random();
		}
		for(int i = 0; i < length; i++) {
			sb.append(PASSWORD_CHARS[r.nextInt(PASSWORD_CHARS.length)]);
		}
		return sb.toString();
	}
}
