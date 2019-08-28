package com.semmle.sample;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("servlet/Servlet2")
public class XSSServlet2 extends HttpServlet {

	private static final long serialVersionUID = -3742201881936674151L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final PrintWriter writer = response.getWriter();
		String attribute = (String) request.getAttribute("taint");
		writer.print(attribute); // Reflected XSS, tainted value from attribute
	}
}
