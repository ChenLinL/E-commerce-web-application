import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
  	private DataSource dataSource;
  	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
			String title =  request.getParameter("title");
			String year =  request.getParameter("year");
			String director =  request.getParameter("director");
			String star =  request.getParameter("star");
			System.out.println(title.isEmpty());
			System.out.println(year.isEmpty());
			System.out.println(director.isEmpty());
			System.out.println(star.isEmpty());

            JsonObject jsonObject = new JsonObject();
//            if(title.isEmpty())
//            {
//            	jsonObject.addProperty("title","Chief Zabu");
//            }
//            else {
//            	jsonObject.addProperty("title",title);
//            }
//            if(year.isEmpty())
//            {
//            	jsonObject.addProperty("year","null");
//            }
//            else {
//            	jsonObject.addProperty("year",year);
//            }
//            if(director.isEmpty())
//            {
//            	jsonObject.addProperty("title","null");
//            }
//            else {
//            	jsonObject.addProperty("director",director);
//            }
//            if(name_of_star.isEmpty()) {
//            	jsonObject.addProperty("name_of_star","null");
//            }
//            else {
//            	jsonObject.addProperty("name_of_star",name_of_star);
//            }
            System.out.println(title);
            System.out.println(year);
            System.out.println(director);
            System.out.println(star);
            jsonObject.addProperty("title", title);
            jsonObject.addProperty("year",year);
            jsonObject.addProperty("director",director);
            jsonObject.addProperty("star",star);
            response.getWriter().write(jsonObject.toString());
            System.out.println(jsonObject.toString());
			response.setStatus(200);
			response.getWriter().close();
    }
		
}
