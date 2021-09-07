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
			String username = request.getParameter("username");
			// get List
			List<Title> load_title = titleDao.findAllTitle(username);
			List<Entity> load_entity = entityDao.findAllEntity(username);
			int num_title = load_title.size();
			int num_entity = load_entity.size();
			String msg_title = "";
			String msg_entity = "";
			String split = " ";
			
			for (int i = 0; i < num_title; i++) {
				msg_title = msg_title + load_title.get(i).getTitle() + split
						+ load_title.get(i).getSubject() + split;
			}
			for (int j = 0; j < num_entity; j++) {
				msg_entity = msg_entity + load_entity.get(j).getName() + split
						+ load_entity.get(j).getSubject() + split
						+ load_entity.get(j).getDescription() + split
						+ load_entity.get(j).getProperty() + split
						+ load_entity.get(j).getRelative() + split
						+ load_entity.get(j).getQuestion() + split;
			}
			
			String msg = Integer.toString(num_title) + split + Integer.toString(num_entity) + split + msg_title + msg_entity;
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.flush();
			out.close();
			return;
		}
		
		// upgrade local DB
		else if (require.equals("upgrade")) {
			String username = request.getParameter("username");
			int num_title = Integer.parseInt(request.getParameter("titleNum"));
			int num_entity = Integer.parseInt(request.getParameter("entityNum"));
			String titles = request.getParameter("titles");
			String entities = request.getParameter("entities");
			String[] data_title = titles.split(" ");
			String[] data_entity = entities.split(" ");
			List<Title> upgrade_title = new ArrayList<Title>();
			List<Entity> upgrade_entity = new ArrayList<Entity>();
			
			for (int i = 0; i < num_title; i++) {
				int index = i * 2;
				Title tmp = new Title(data_title[index], data_title[index+1]);
				upgrade_title.add(tmp);
			}
			for (int j = 0; j < num_entity; j++) {
				int index = j * 6;
				Entity tmp = new Entity(data_entity[index], data_entity[index+1],
						data_entity[index+2], data_entity[index+3],
						data_entity[index+4], data_entity[index+5]);
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
		
	}
}


