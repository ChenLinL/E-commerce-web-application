
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet(name = "movieAutoServlet", urlPatterns = "/api/movieAuto")
public class movieAutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * populate the Super hero hash map.
	 * Key is hero ID. Value is hero name.
	 */
	public static HashMap<Integer, String> movieMap = new HashMap<>();
    
    public movieAutoServlet() {
        super();
    }
    
    private static String check_space(String query) {
    	String result = "";
    	String split[] = query.split(" ");
    	for(String s:split) {
    		result += "+"+s+"* ";
    	}
    	return result;
    }
  
	//@Resource(name = "jdbc/moviedb")
	//private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			
			// get the query string from parameter
			String query = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			//Connection dbcon = dataSource.getConnection();
			
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            
            int randomNumber = (int)(Math.random()*2);
            DataSource ds = null;
            
            // Look up data source
            // if randomNumber is 0, access master MySql database; otherwise access slave MySql database  
            if (randomNumber == 0)
            {
            	ds = (DataSource) envCtx.lookup("jdbc/masterdb");
            }
            
            else
            {
            	ds = (DataSource) envCtx.lookup("jdbc/slavedb");
            }
            
            // Connect to moviedb
            Connection dbcon = ds.getConnection();
			
			String search_query = "SELECT m.id, m.title FROM moviedb.movies as m, moviedb.ratings as r WHERE m.id = r.movieId and MATCH (title) AGAINST (? IN BOOLEAN MODE) LIMIT 10;";
			
			PreparedStatement statement = dbcon.prepareStatement(search_query);
			query = check_space(query);
			statement.setString(1, query);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("m.id");
				String title = rs.getString("m.title");
				jsonArray.add(generateJsonObject(id,title));
			}
			
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		}
	}
	
	private static JsonObject generateJsonObject(String id, String title) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", title);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("movieId", id);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}

