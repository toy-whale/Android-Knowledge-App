package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		doPost(request, response);
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password =  request.getParameter("password");
		response.setCharacterEncoding("UTF-8");
		
		UserDao dao = new UserDao();
		boolean login = dao.login(username, password);
		
		String msg = "";
		if (login)
			msg = "Login Successful";
		else
			msg = "Login Failed";
		
		PrintWriter out = response.getWriter();
		out.println(msg);
		out.flush();
		out.close();
	}
}