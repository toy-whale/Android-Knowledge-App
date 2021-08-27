package mainPack;
import java.io.IOException;
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
public class Main extends HttpServlet {
    private static final long serialVersionUID = 382354197754278695L;
    private static boolean flag = false;
    private static String id = "";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
    	if(flag == false) {
    		try {
				id = Login.get("14759265980", "Ee123456");
			} catch (Exception e) {}
    		flag = true;
    	}
    	
    	String code = request.getParameter("code");
    	
    	// code can't be null
    	if (code == null) return;
    	
    	String answer = "";
    	if(code.equals("2")) { //ʵ������
    		String course = request.getParameter("course");
    		String name = request.getParameter("name");
    		try {
				answer = InstanceList.get(course, name, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("3")) { //ʵ������
    		String course = request.getParameter("course");
    		String name = request.getParameter("name");
    		try {
				answer = InfoByInstanceName.get(course, name, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("4")) { //�ʴ�
    		String course = request.getParameter("course");
    		String inputQuestion = request.getParameter("inputQuestion");
    		try {
				answer = InputQuestion.get(course, inputQuestion, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("5")) { //֪ʶ����
    		String course = request.getParameter("course");
    		String context = request.getParameter("context");
    		try {
				answer = LinkInstance.get(course, context, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("6")) { //���ϰ��
    		String name = request.getParameter("name");
    		try {
				answer = QuestionListByUriName.get(name, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("7")) { //֪ʶ����
    		String course = request.getParameter("course");
    		String name = request.getParameter("name");
    		try {
				answer = Relatedsubject.get(course, name, id);
			} catch (Exception e) {}
    	}
    	response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(answer);
        response.getWriter().flush();
        response.getWriter().close();
        super.doGet(request, response);
    }
}