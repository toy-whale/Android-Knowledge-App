package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.User;

public class InfoServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String require = request.getParameter("request");
		if (require == null) return;
		if (require.equals("update")) {
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
		
		String oldusername = request.getParameter("oldusername");
		String newusername = request.getParameter("newusername");
		String cipherPassword = request.getParameter("newpassword");
		String newpassword =  RSAKeyManager.decrypt(cipherPassword, RSAKeyManager.getPrivateKey());
		response.setCharacterEncoding("UTF-8");
		
		// reload
		UserDao userDao = new UserDao();
		User userOld = userDao.findUser(oldusername);
		User userNew = new User();
        userNew.setUsername(newusername);
        userNew.setPassword(newpassword);

        String msg = "";
        
        User userJudgeExist = userDao.findUser(userNew.getUsername());
       
        boolean reg = false;
        if (userJudgeExist == null) {
        	// new username fits
        	reg = userDao.updateUser(userOld.getId(), userNew);
        }
        else if (userJudgeExist.getUsername().equals(oldusername)) {
        	reg = userDao.updateUser(userOld.getId(), userNew);
        }

        
        if (reg)
            msg = "Info Change Successful";
        else
        	msg = "Info Change Failed";

	    if (userJudgeExist != null) {
	    	if (oldusername.equals(newusername))
	    		msg = "Info Change Successful";
	    	else
	    		msg = "Username Already Exists";
	    }
	
        PrintWriter out = response.getWriter();
		out.println(msg);
		out.flush();
		out.close();

	}
}


