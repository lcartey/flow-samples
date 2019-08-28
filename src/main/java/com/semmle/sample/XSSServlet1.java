package com.semmle.sample;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class XSSServlet1 extends HttpServlet {
	private static final long serialVersionUID = -6066623665943096962L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nextServlet = "servlet/Servlet2";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextServlet);
		req.setAttribute("taint", req.getParameter("builds")); // Tainted value in attribute
		dispatcher.forward(req, resp);
	}
}
