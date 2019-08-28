package com.semmle.sample;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class WeakAlgorithms {

	public void sample1() throws NoSuchAlgorithmException, NoSuchPaddingException {
		MessageDigest.getInstance("MD5"); // Weak algorithm
		MessageDigest.getInstance("SHA-1");// Weak algorithm
		Cipher.getInstance("DES");
	}
}
