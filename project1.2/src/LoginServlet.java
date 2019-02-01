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
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    //Create a dataSource
  	@Resource(name = "jdbc/moviedb")
  	private DataSource dataSource;
  	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	PrintWriter out = response.getWriter();
    	
    	try {
			Connection dbcon = dataSource.getConnection();
			
			String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        
	        String query = "Select customers.email, customers.password " + "From customers Where customers.email = '" + username + "'";
	        
	        PreparedStatement statement = dbcon.prepareStatement(query);
	        
	        // Perform the query
	        ResultSet rs = statement.executeQuery();
	     	String check_password = "";
	     	
	     	if (!rs.next())
	     	{
				JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "User " + username + " Doesn't Exist. Please Try Again.");
	            response.getWriter().write(responseJsonObject.toString());
	     	}
	     	
	     	else
	     	{
	     		check_password = rs.getString("customers.password");
				if (!password.equals(check_password))
				{
					JsonObject responseJsonObject = new JsonObject();
		            responseJsonObject.addProperty("status", "fail");
		            
		            responseJsonObject.addProperty("message", "Incorrect Password. Please Try Again.");
		            response.getWriter().write(responseJsonObject.toString());
				}
				else
				{
					// login success
					String sessionId = ((HttpServletRequest) request).getSession().getId();
		            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
		            request.getSession().setAttribute("user", new User(username));

		            JsonObject responseJsonObject = new JsonObject();
		            responseJsonObject.addProperty("status", "success");
		            responseJsonObject.addProperty("message", "success");

		            response.getWriter().write(responseJsonObject.toString());
				}
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
