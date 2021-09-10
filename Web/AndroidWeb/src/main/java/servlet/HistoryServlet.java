package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dao.User;
import dao.TitleDao;
import dao.Title;
import dao.EntityDao;
import dao.Entity;

public class HistoryServlet extends HttpServlet {
	
	private List<Title> load_title;
	private List<Entity> load_entity;
	private int num_title;
	private int num_entity;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String require = request.getParameter("request");
		if (require == null) return;
		
		TitleDao titleDao = new TitleDao();
		EntityDao entityDao = new EntityDao();
		
		// load into android
		if (require.equals("load")) {
			String step = request.getParameter("step");
			// send amount
			if (step.equals("num")) {
				String username = request.getParameter("username");
				// get List
				load_title = titleDao.findAllTitle(username);
				load_entity = entityDao.findAllEntity(username);
				num_title = load_title.size();
				num_entity = load_entity.size();
				String msg = "";
				msg = Integer.toString(num_title) + " " + Integer.toString(num_entity);
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println(msg);
				out.flush();
				out.close();
				return;
			}
			else if (step.equals("data")) {
				// load title or entity
				String type = request.getParameter("type");
				int order = Integer.parseInt(request.getParameter("order"));
				String msg = "";
				if (type.equals("title")) {
					Title tmp = load_title.get(order);
					String msg_title = "";
					msg_title = tmp.getTitle() + " " + tmp.getSubject();
					response.setContentType("text/html;charset=utf-8");
					PrintWriter out = response.getWriter();
					out.println(msg_title);
					out.flush();
					out.close();
					return;
				}
				else if (type.equals("entity")) {
					Entity tmp = load_entity.get(order);
					String msg_entity = "";
					msg_entity = tmp.getName() + " " + tmp.getSubject() + " "
							+ tmp.getDescription() + " " + tmp.getProperty() + " "
							+ tmp.getRelative() + " " + tmp.getQuestion() + " "
							+ tmp.getImage();
					response.setContentType("text/html;charset=utf-8");
					PrintWriter out = response.getWriter();
					out.println(msg_entity);
					out.flush();
					out.close();
					return;
				}						
			}
		}
		
		// upgrade local DB
		else if (require.equals("upgrade")) {
			String username = request.getParameter("username");
			int num_title = Integer.parseInt(request.getParameter("titleNum"));
			int num_entity = Integer.parseInt(request.getParameter("entityNum"));
			
			List<Title> upgrade_title = new ArrayList<Title>();
			List<Entity> upgrade_entity = new ArrayList<Entity>();
			
			for (int i = 0; i < num_title; i++) {
				String getTitle = "title" + Integer.toString(i) + "title";
				String getSubject = "title" + Integer.toString(i) + "subject";
				Title tmp = new Title(request.getParameter(getTitle), request.getParameter(getSubject));
				upgrade_title.add(tmp);
			}
			for (int j = 0; j < num_entity; j++) {
				String getName = "entity" + Integer.toString(j) + "name";
				String getSubject = "entity" + Integer.toString(j) + "subject";
				String getDescription = "entity" + Integer.toString(j) + "description";
				String getProperty = "entity" + Integer.toString(j) + "property";
				String getRelative = "entity" + Integer.toString(j) + "relative";
				String getQuestion = "entity" + Integer.toString(j) + "question";
				String getImage = "entity" + Integer.toString(j) + "image";
				Entity tmp = new Entity(request.getParameter(getName), request.getParameter(getSubject),
						request.getParameter(getDescription), request.getParameter(getProperty),
						request.getParameter(getRelative), request.getParameter(getQuestion),
						request.getParameter(getImage));
				upgrade_entity.add(tmp);
			}
			titleDao.insertAllTitle(username, upgrade_title);
			entityDao.insertAllEntity(username, upgrade_entity);
			
			String msg = "upgrade already";
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.flush();
			out.close();
			return;
		}
		else if (require.equals("change")) {
			String oldName = request.getParameter("oldusername");
			String newName = request.getParameter("newusername");
			List<Title> oldTitle = titleDao.findAllTitle(oldName);
			List<Entity> oldEntity = entityDao.findAllEntity(oldName);
			titleDao.deleteAllTitle(oldName);
			entityDao.deleteAllEntity(oldName);
			titleDao.insertAllTitle(newName, oldTitle);
			entityDao.insertAllEntity(newName, oldEntity);
		}
	}
}


