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
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

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
		max_id = max_id.replace("tt", "");
		int new_id = Integer.parseInt(max_id) + 1;
		String new_movieId = "tt" + Integer.toString(new_id); 
		
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
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		
		String movie= request.getParameter("movie");	
		String year_i= request.getParameter("year");
		//int year = Integer.valueOf(year_i);
		String director = request.getParameter("director");
		String genre= request.getParameter("genre");
		
		
		boolean status = true;
		String error_message = "";
		
		response.setContentType("application/json"); // Response mime type
		
		// Retrieve parameter id from url request.
		PrintWriter out = response.getWriter();
		
		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			
			if (add.equals("0"))
			{
				System.out.println("ADDED STAR");
				
				String pre_query = "select max(id) as id from stars";					
				PreparedStatement statement = dbcon.prepareStatement(pre_query);
				
				String query2 = "";
				ResultSet rs = statement.executeQuery();
				
				while(rs.next())
				{
					String max_id = rs.getString("id");
					String new_id = generate_newstarId(max_id);
					System.out.println(new_id);
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
				System.out.println(query2);
				//statement = dbcon.prepareStatement(query2);
				statement.executeUpdate();
				statement.close();
			}
			
			// add a movie to database
			else if (add.equals("1"))
			{
				
				CallableStatement cStmt = dbcon.prepareCall("{call add_movie(?,?,?,?,?,?,?,?)}");
				String star_query = "select max(id) as id from stars";
				String movie_query = "select max(id) as id from movies";
				String genre_query = "select max(id) as id from genres";
				PreparedStatement star_statement = dbcon.prepareStatement(star_query);
				PreparedStatement movie_statement = dbcon.prepareStatement(movie_query);
				PreparedStatement genre_statement = dbcon.prepareStatement(genre_query);
				ResultSet star_rs = star_statement.executeQuery();
				ResultSet movie_rs = movie_statement.executeQuery();
				ResultSet genre_rs = genre_statement.executeQuery();
			
				String star_id="";
				String movie_id="";
				int genre_id=0;
				while(star_rs.next()) {
				
					String max_id = star_rs.getString("id");
			
					star_id = generate_newstarId(max_id);
				}
				while(movie_rs.next()) {
				
					String max_id = movie_rs.getString("id");
				
					movie_id = generate_newMovieId(max_id);
				}
				while(genre_rs.next()) {
				
					String max_id = genre_rs.getString("id");
		
					genre_id = generate_newGenreId(max_id);
				}
				star_rs.close();
				movie_rs.close();
				genre_rs.close();
				star_statement.close();
				movie_statement.close();
				genre_statement.close();
				
				cStmt.setString(1, movie_id);
				cStmt.setString(2, movie);
				int year = Integer.valueOf(year_i);
				cStmt.setInt(3, year);
				cStmt.setString(4, director);
				cStmt.setString(5, star_id);
				cStmt.setString(6, star);
				cStmt.setInt(7, genre_id);
				cStmt.setString(8, genre);
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
				System.out.print("message");
				System.out.print(error_message);
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
