import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
	//@Resource(name = "jdbc/moviedb")
	//private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	public String generate_newstarId(String max_id)
	{	
		max_id = max_id.replace("n", "");
		max_id = max_id.replaceAll("m", "");
		int new_id = Integer.parseInt(max_id) + 1;
		String new_starId = "nm" + Integer.toString(new_id); 
		
		return new_starId;
		
	}
	public String generate_newMovieId(String max_id)
	{	
		String s1 = "";
		int count = 0;
		
		for (int i = 0; i < max_id.length(); i++)
		{			
			if (Character.isLetter(max_id.charAt(i)))
			{
				s1 += i;
				++count;
			}
		}
		max_id = max_id.substring(count, max_id.length());
		
		int new_id = Integer.parseInt(max_id) + 1;
		String new_movieId = s1 + Integer.toString(new_id); 
		
		return new_movieId;
		
	}
	public Integer generate_newGenreId(String max_id)
	{	
		int new_id = Integer.valueOf(max_id) + 1;
		return new_id;
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String star= request.getParameter("star");		
		String bod_star= request.getParameter("bod_star");		
		String add = request.getParameter("add");		
		String movie= request.getParameter("movie");	
		String year_i= request.getParameter("year");
		String director = request.getParameter("director");
		String genre= request.getParameter("genre");
				
		boolean status = true;
		String error_message = "";
		
		response.setContentType("application/json"); // Response mime type
		
		// Retrieve parameter id from url request.
		PrintWriter out = response.getWriter();
		
		try {
			//Connect to dataSource
			//Connection dbcon = dataSource.getConnection();
			
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/masterdb");
            
            // Connect to moviedb
            Connection dbcon = ds.getConnection();
			
			if (add.equals("0"))
			{				
				String pre_query = "select max(id) as id from stars";					
				PreparedStatement statement = dbcon.prepareStatement(pre_query);
				
				String query2 = "";
				ResultSet rs = statement.executeQuery();
				
				while(rs.next())
				{
					String max_id = rs.getString("id");
					String new_id = generate_newstarId(max_id);
					
					// insert star when birth year is indicated
					if (!bod_star.equals(""))
					{
						int bod_result = Integer.parseInt(bod_star);
						query2 = "INSERT INTO stars (id, name, birthYear) VALUES (?,?,?)";
						statement = dbcon.prepareStatement(query2);
						statement.setString(1, new_id);
						statement.setString(2, star);
						statement.setInt(3, Integer.valueOf(bod_star));
					}
					// insert star when birth year is not indicated
					else
					{
						query2 = "INSERT INTO stars (id, name) VALUES (?,?)";
						statement = dbcon.prepareStatement(query2);
						statement.setString(1, new_id);
						statement.setString(2, star);
					}
				}

				rs.close();			
				
				statement.executeUpdate();
				statement.close();
			}
			
			// add a movie to database
			else if (add.equals("1"))
			{				
				CallableStatement cStmt = dbcon.prepareCall("{call add_movie(?,?,?,?,?)}");
			
				String star_id="";
				String movie_id="";
				int genre_id=0;
							
				cStmt.setString(1, movie);
				int year = Integer.valueOf(year_i);
				cStmt.setInt(2, year);
				cStmt.setString(3, director);
				cStmt.setString(4, star);
				cStmt.setString(5, genre);
				boolean hadResult = cStmt.execute();
				if(hadResult) {
					ResultSet cstmtRs = cStmt.getResultSet();
					while(cstmtRs.next()) {
						status = false;
						error_message = cstmtRs.getString("answer");
						
					}
					cstmtRs.close();
				}
				cStmt.close();
			}
			
			String query = "show tables";
			PreparedStatement statement = dbcon.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			
			JsonObject result = new JsonObject();
			JsonArray tables = new JsonArray();
			
			while (rs.next())
			{	
				JsonObject table = new JsonObject();
				String table_name = rs.getString("Tables_in_moviedb");
	
				String query_info = "describe "+ table_name;			
				PreparedStatement table_info = dbcon.prepareStatement(query_info);
				
				// Perform the query
				ResultSet info_rs = table_info.executeQuery();
			
				JsonArray fileds = new JsonArray();
				JsonArray types = new JsonArray();
				JsonArray Nulls = new JsonArray();
				
				while (info_rs.next()) {
					String filed = info_rs.getString("field");
					String type = info_rs.getString("type");
					String Null = info_rs.getString("null");
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
