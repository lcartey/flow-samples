package com.semmle.sample;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XSS {

	public void sample1(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream pout = response.getOutputStream();

		String string = Arrays.toString(request.getParameterValues("MyParam2"));
		pout.println("Param values " + string); // Reflected XSS
	}

	public void sample2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream pout = response.getOutputStream();

		List<String> asList = Arrays.asList(request.getParameterValues("MyParam2"));
		pout.println("Param values " + asList); // Reflected XSS

		for (String string2 : asList) {
			pout.println("Param values " + string2); // Reflected XSS
		}
	}

	public void sample3(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream fis = null;
		try {
			fis = request.getInputStream();
			int bytes;
			while ((bytes = fis.read()) != -1) {
				response.getOutputStream().write(bytes);// Reflected XSS
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sample4(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream pout = response.getOutputStream();
		Set<String> set = new HashSet<String>(Arrays.asList(request.getParameterValues("MyParam2")));
		pout.println("<BR>Using tainted list :: " + set); // Reflected XSS
		set.stream().forEach(s -> {
			try {
				pout.print(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}); // Reflected XSS
		set.parallelStream().forEach(s -> {
			try {
				pout.print(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}); // Reflected XSS
		set.stream().forEach(arg0 -> {
			try {
				pout.println(arg0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}); // Reflected XSS
		pout.print("a" + set.stream().map(s -> "Param value " + s).collect(Collectors.toList())); // Reflected XSS
	}

	private static class ParamContainer {
		private String name;
		private String value;

		ParamContainer(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getParamName() {
			return name;
		}

		public String getParamValue() {
			return value;
		}
	}

	public void sample5(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream pout = response.getOutputStream();
		Map<String, ParamContainer> pMap = new HashMap<>();
		Enumeration<String> itr = request.getParameterNames();
		while (itr.hasMoreElements()) {
			String p = itr.nextElement();
			pMap.put(p, new ParamContainer(p, request.getParameter(p)));
		}
		pout.println("<BR>Using tainted container map :: " + pMap); // Reflected XSS
		pout.println("<BR>Using tainted container object toString :: " + pMap.get("MyParam1")); // Reflected XSS
		pout.println("<BR>Using tainted container object getParamName :: " + pMap.get("MyParam1").getParamName()); // Reflected
																													// XSS
		pout.println("<BR>Using tainted container object getParamValue :: " + pMap.get("MyParam1").getParamValue()); // Reflected
																														// XSS
	}

	public void sample6(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream pout = response.getOutputStream();
		Map<String, String> tMap = new HashMap<>();
		String p = "Myparam";
		tMap.put(p, request.getParameter(p));
		pout.println("<BR>Using tainted value :: " + tMap.get("p")); // This is not an issue, but it was captured. Note : Key is hard coded string of "p" and not a "MyParam"
	}
}
