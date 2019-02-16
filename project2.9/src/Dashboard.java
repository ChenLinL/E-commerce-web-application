import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
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
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "Dashboard", urlPatterns = "/api/dashBoard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String movie= request.getParameter("movie");
		String year= request.getParameter("year");
		String director = request.getParameter("director");
		String star= request.getParameter("star");
		String bod_star= request.getParameter("bod_star");
		String genre= request.getParameter("genre");
		System.out.print(director);
		boolean status = true;
		String error_message = "";
		response.setContentType("application/json"); // Response mime type
		//System.out.println("run single star");
		// Retrieve parameter id from url request.
		PrintWriter out = response.getWriter();
		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			
			String query = "show tables";

			PreparedStatement statement = dbcon.prepareStatement(query);

			ResultSet rs = statement.executeQuery();
			//System.out.print(rs.toString());
			JsonObject result = new JsonObject();
			JsonArray tables = new JsonArray();
			//System.out.println(rs.toString());
			
			while (rs.next())
			{	
				JsonObject table = new JsonObject();
				String table_name = rs.getString("Tables_in_moviedb");
				//System.out.println(table_name);
				String query_info = "describe "+table_name;
			
				PreparedStatement table_info = dbcon.prepareStatement(query_info);
				//System.out.println(table_info.toString());
				// Perform the query
				ResultSet info_rs = table_info.executeQuery();
			
				JsonArray fileds = new JsonArray();
				JsonArray types = new JsonArray();
				JsonArray Nulls = new JsonArray();
				while (info_rs.next()) {
					String filed = info_rs.getString("field");
					String type = info_rs.getString("type");
					String Null = info_rs.getString("null");
					//System.out.println(filed);
					//System.out.println(type);
					fileds.add(filed);
					types.add(type);
					Nulls.add(Null);
				}
				table.addProperty("name", table_name);
				table.add("fileds", fileds);
				table.add("types", types);
				table.add("Null", Nulls);
				tables.add(table);
				
			}
			//System.out.print(tables.toString());
			if(status) {
				result.addProperty("status", "success");
				result.add("tables", tables);
			}
			else {
				result.addProperty("status", "fail");
				result.add("tables", tables);
				result.addProperty("error_message", error_message);
			}
			out.write(result.toString());
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
