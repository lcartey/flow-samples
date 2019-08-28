package com.semmle.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SQLInjection {

	private Connection getDBConnection() {
		return null;
	}

	protected void sqlExecuteLargerUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		PrintWriter out = response.getWriter();
		String user = request.getParameter("user");
		try (Connection connection = getDBConnection(); Statement st = connection.createStatement();) {
			String sql = "UPDATE COMPANY set SALARY =" + user + " where ID=1;";// No I18N

			long ret = st.executeLargeUpdate(sql);// Sql Injection
			out.println(ret);
		}
		out.println("Executed done!!!");
	}

	public void sqlPrepareCall(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String runSP = "{ call get_employee_by_id('" + request.getParameter("id") + "',?,?,?) }";
		try (Connection conn = getDBConnection();
				Statement statement = conn.createStatement();
				CallableStatement callableStatement = conn.prepareCall(runSP)) // Sql Injection
		{
			/* ... */}
	}
}
