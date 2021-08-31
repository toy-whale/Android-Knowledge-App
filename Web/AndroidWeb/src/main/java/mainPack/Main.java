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
    	if(flag == false) { //获取登录状态码
    		try {
				id = Login.get("14759265980", "Ee123456");
			} catch (Exception e) {}
    		flag = true;
    	}
    	
    	String code = request.getParameter("code");
    	if (code == null) return;
    	
    	String answer = "";
    	if(code.equals("2")) { //实体搜索
    		//sort_type可选，1为按长度排序，2为按拼音排序
    		//word可选，为必须包含的关键词，如有多个以空格隔开
    		String course = request.getParameter("course");
    		String name = request.getParameter("name");
    		String sort_type = request.getParameter("sort_type");
    		String word = request.getParameter("word");
    		if (sort_type == null) sort_type = "0";
    		try {
				answer = InstanceList.get(course, name, id, sort_type, word);
			} catch (Exception e) {}
    	}
    	else if(code.equals("3")) { //实体详情
    		String course = request.getParameter("course");
    		String name = request.getParameter("name");
    		try {
				answer = InfoByInstanceName.get(course, name, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("4")) { //问答
    		String course = request.getParameter("course");
    		String inputQuestion = request.getParameter("inputQuestion");
    		try {
				answer = InputQuestion.get(course, inputQuestion, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("5")) { //知识链接
    		String course = request.getParameter("course");
    		String context = request.getParameter("context");
    		try {
				answer = LinkInstance.get(course, context, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("6")) { //相关习题
    		String name = request.getParameter("name");
    		try {
				answer = QuestionListByUriName.get(name, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("7")) { //知识关联
    		String course = request.getParameter("course");
    		String name = request.getParameter("name");
    		try {
				answer = Relatedsubject.get(course, name, id);
			} catch (Exception e) {}
    	}
    	else if(code.equals("8")) { //专项测试
    		try {
    			String input = request.getParameter("points");
    			String str = request.getParameter("number");
    			String points[] = input.split("\\|");
    			int number = 0;
    			try {
    			    number = Integer.valueOf(str).intValue();
    			} catch (NumberFormatException e) {
    			    number = -1;
    			}
				answer = TestQuestion.get(points, number, id);
			} catch (Exception e) {}
    	}
    	response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(answer);
        response.getWriter().flush();
        response.getWriter().close();
        super.doGet(request, response);
    }
}