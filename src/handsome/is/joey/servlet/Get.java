package handsome.is.joey.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Get
 */
@WebServlet("/get")
public class Get extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id1 = Long.parseLong(request.getParameter("id1"));
		long id2 = Long.parseLong(request.getParameter("id2"));
		ArrayList<ArrayList<Long>> result = IDTransfer.bfs(id1, id2);
		String beginEnd = "Results from "+id1+" to "+ id2 + ".";
		request.setAttribute("beginEnd", beginEnd);
		request.setAttribute("result", result);
		RequestDispatcher dispatcher = request.getRequestDispatcher("View.jsp");
		dispatcher.forward(request, response);	
	}

}
