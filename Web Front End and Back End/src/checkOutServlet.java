import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "checkOutServlet", urlPatterns = "/api/checkOut")
public class checkOutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    //Create a dataSource
  	//@Resource(name = "jdbc/moviedb")
  	//private DataSource dataSource;
  	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	PrintWriter out = response.getWriter();
    	
    	try {
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
			
			String id = request.getParameter("id");
	        String first = request.getParameter("firstName");
	        String last = request.getParameter("lastName");
	        String date_r = request.getParameter("date");
	        Date date = Date.valueOf(date_r);
	        String query = "Select cus.id from creditcards as c, customers as cus where c.id = cus.ccId and c.id =? and c.firstName=? and c.lastName=? and c.expiration=?";
	        
	        PreparedStatement statement = dbcon.prepareStatement(query);
	        statement.setString(1, id);
	        statement.setString(2, first);
	        statement.setString(3, last);
	        statement.setDate(4, date);
	        
	        // Perform the query
	        ResultSet rs = statement.executeQuery();
	       
	     	String check_password = "";
	     	JsonObject responseJsonObject = new JsonObject();
	     	if (!rs.next())
	     	{
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "creditCard information incorrect, Please try again");
	            response.getWriter().write(responseJsonObject.toString());
	     	}
	     	
	     	else
	     	{
					// login success
					String sessionId = ((HttpServletRequest) request).getSession().getId();
		            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();		      		        
		            responseJsonObject.addProperty("status", "success");
		            responseJsonObject.addProperty("message", "success");
		            responseJsonObject.addProperty("customerId", rs.getString("cus.id"));
		            response.getWriter().write(responseJsonObject.toString());
				
	     	}
	     	
			response.setStatus(200);
			
			rs.close();
			statement.close();
			dbcon.close();
		} catch (Exception e) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			
			response.setStatus(500);
		}
    	out.close();
   }
}
