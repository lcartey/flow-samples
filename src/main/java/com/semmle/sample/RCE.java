package com.semmle.sample;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RCE {

	private static class RuntimeProcessUtil {
		public static void dump(Process p, PrintWriter s) {

		}
	}

	public void sample1(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String cmd = request.getParameter("cmd");
		RuntimeProcessUtil.dump(new ProcessBuilder("javac", cmd).start(), response.getWriter()); // RCE Not captured
		String[] strings = new String[] { "javac", cmd };
		Process p = new ProcessBuilder(strings).start(); // RCE Not captured
	}

	public void sample2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cmd = request.getParameter("cmd");
		String[] command = new String[]{"javac", cmd};
		Runtime.getRuntime().exec(command); // RCE Not captured
	}
}
