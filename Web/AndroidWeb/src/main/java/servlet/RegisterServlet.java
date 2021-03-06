package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.User;

public class RegisterServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String require = request.getParameter("request");
		if (require == null) return;
		if (require.equals("register")) {
			// generate RSA Keys
			RSAKeyManager.generate();
			String msg = RSAKeyManager.getPublicKey();
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.flush();
			out.close();
			return;
		}
		
		String username = request.getParameter("username");
		String cipherPassword = request.getParameter("password");
		String password =  RSAKeyManager.decrypt(cipherPassword, RSAKeyManager.getPrivateKey());
		response.setCharacterEncoding("UTF-8");
		
		User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        String msg = "";
        UserDao userDao = new UserDao();
        User userJudgeExist = userDao.findUser(user.getUsername());
       
        boolean reg = false;
        if (userJudgeExist == null) {
        	reg = userDao.register(user);  
        }

        
        if (reg)
            msg = "Register Successful";
        else
        	msg = "Register Failed";

	    if (userJudgeExist != null) 
	   		 msg = "Username Already Exists";
	
        PrintWriter out = response.getWriter();
		out.println(msg);
		out.flush();
		out.close();

	}
}


