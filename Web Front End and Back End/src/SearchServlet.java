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
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
			String title =  request.getParameter("title");
			String year =  request.getParameter("year");
			String director =  request.getParameter("director");
			String star =  request.getParameter("star");

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("title", title);
			jsonObject.addProperty("year",year);
			jsonObject.addProperty("director",director);
			jsonObject.addProperty("star",star);

			out.write(jsonObject.toString());
			response.setStatus(200);
						
			out.close();
    }
		
}