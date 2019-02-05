import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "resultPageServlet", urlPatterns = "/api/resultPage")
public class resultPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

    /**
     * handles POST requests to store session information
     */


    /**
     * handles GET requests to add and show the item list information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String c_id = request.getParameter("c_id");
     
        response.setContentType("application/json");
        System.out.println(c_id);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        // get the previous items in a ArrayList
        //ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
         HashMap<String, Map.Entry<String, Integer>>previousItems = (HashMap<String,Map.Entry<String, Integer>>) session.getAttribute("previousItems");
      
         try {
        	 Connection dbcon = dataSource.getConnection();
        	 synchronized (previousItems) {
        		 System.out.println(previousItems.keySet().toString());
        		 int num = previousItems.values().size();
        		 System.out.println(num);
        		 Object[] key_a = previousItems.keySet().toArray();
        		 for(int i =0; i<num; ++i) {
        		 
        			 String query = "INSERT INTO sales(customerId,movieId,saleDate) VALUES(?,?,?)";
        			 PreparedStatement statement = dbcon.prepareStatement(query);
        			 statement.setString(1, c_id);
        			 statement.setString(2,(String) key_a[i]);
        			 long millis = System.currentTimeMillis();
        			 //DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
        			 Date date = new Date(millis);
        			 statement.setDate(3,date);
        			 System.out.println(statement.toString());
        			 statement.executeUpdate();
        			 
        		 }
        	 }
        	 //previousItems.clear();
        		 
 		}
        catch (Exception e) {
        	JsonObject jsonObject = new JsonObject();
        	jsonObject.addProperty("errorMessage", e.getMessage());
        	out.write(jsonObject.toString());
        				
        	response.setStatus(500);
        			}
        			
        			out.close();
        //System.out.println(jsonObject.toString());
        //response.getWriter().write(jsonObject.toString());
    }
}
