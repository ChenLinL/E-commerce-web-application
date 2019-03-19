import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
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
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    //Create a dataSource
  	//@Resource(name = "jdbc/moviedb")
  	//private DataSource dataSource;
  	
  	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        String userAgent = request.getHeader("User-Agent");
        
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        
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
			
			String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        
	        String query = "Select customers.email, customers.password From customers Where customers.email = ?";
	        
	        PreparedStatement statement = dbcon.prepareStatement(query);
	        statement.setString(1, username);
	        
	        // Perform the query
	        ResultSet rs = statement.executeQuery();
	     	
	     	if (!rs.next())
	     	{
				JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            responseJsonObject.addProperty("message", "User " + username + " Doesn't Exist. Please Try Again.");
	            response.getWriter().write(responseJsonObject.toString());
	     	}
	     	
	     	else
	     	{
	     		String encryptedPassword = rs.getString("customers.password");
	     		
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
					boolean check = false;
					try {
						 if (userAgent != null && !userAgent.contains("Android")) { 
							 //RecaptchaVerifyUtils.verify(gRecaptchaResponse);
							 check = true;
						 }
						 else {
							 check = true;
						 }
					}
					catch(Exception e)
					{
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject .addProperty("status", "fail");
						
						responseJsonObject.addProperty("message", "Recaptcha Verification Failed");
						response.getWriter().write(responseJsonObject.toString());
					}
					if (check)
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
  	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	doGet(request, response);	
   }
}
