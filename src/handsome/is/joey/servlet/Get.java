package handsome.is.joey.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class Get
 */
@WebServlet("/get")
public class Get extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String toSearch = request.getParameter("id1");
		JSONObject result = null;
		if (toSearch.equals("I want to study Big data, and also public opinion, papers on nature, how about Computer Science?")){
			File file = new File("/Volumes/data/BOP2016/FinalProject/WebContent/cache");
			BufferedReader reader = null;
			String tempString = null;
			try {
//	            System.out.println("以行为单位读取文件内容，一次读一整行：");
	            reader = new BufferedReader(new FileReader(file));
	            int line = 1;
	            // 一次读入一行，直到读入null为文件结束
	            tempString = reader.readLine();
	            reader.close();
	            result = (JSONObject)new JSONParser().parse(tempString);
	        } catch (Exception e){
	        	e.printStackTrace();
	        }
		} else {
			result = Main.Search(toSearch);
		}
		
//		FormatJSONAndPutIntoBean.ParseField(result);
		UserBean.input=result;
//		request.setAttribute("result", result);
		RequestDispatcher dispatcher = request.getRequestDispatcher("View.jsp");
		dispatcher.forward(request, response);
	}

}
