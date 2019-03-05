import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.google.gson.JsonObject;
/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "DashboardLogin", urlPatterns = "/api/dashLogin")
public class DashboardLogin extends HttpServlet {
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
			
			String username = request.getParameter("e_name");
	        String password = request.getParameter("e_password");
	        
	        String query = "Select e.email, e.password From employees as e Where e.email = ?";
	        
	        PreparedStatement statement = dbcon.prepareStatement(query);
	        statement.setString(1, username);
	        // Perform the query
	        ResultSet rs = statement.executeQuery();
	     
	     	if (!rs.next())
	     	{
				JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "Employee " + username + " Doesn't Exist. Please Try Again.");
	            response.getWriter().write(responseJsonObject.toString());
	     	}
	     	
	     	else
	     	{
	     		String encryptedPassword = rs.getString("e.password");
	     		
	     		System.out.println(encryptedPassword);
	     		
	     		boolean success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
	     		
				if (!success)
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

