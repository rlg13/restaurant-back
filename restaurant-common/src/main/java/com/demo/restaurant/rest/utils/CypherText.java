package com.demo.restaurant.rest.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CypherText {

	private CypherText() {
	}

	public static String sha256(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
