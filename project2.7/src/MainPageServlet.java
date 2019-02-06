
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet(name = "/MainPageServlet", urlPatterns = "/api/main-page")
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//Create a dataSource
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		
		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			Statement statement = dbcon.createStatement();
			
			ArrayList<String> movie_genre = new ArrayList<String>();
			movie_genre = get_genre(statement);
			
			ArrayList<String> movie_title = new ArrayList<String>();
			movie_title = get_title(statement);
//			String query = "SELECT movies.id, movies.title, movies.year, movies.director, genres.name, stars.name as Stars, stars.id, ratings.rating,genres.name as Genre "
//				       + "FROM" + " (select movies.id, movies.title, movies.year, movies.director from movies,ratings";
			JsonArray jsonArray = new JsonArray();
			
			JsonArray movie_genreArray = new JsonArray();
			JsonArray movie_titleArray = new JsonArray();
			
			JsonObject jsonObject = new JsonObject();
			
			for (String str : movie_genre)
			{
				movie_genreArray.add(str);
			}
			
			for (String str: movie_title)
			{
				movie_titleArray.add(str);
			}
			
			jsonObject.add("movie_genre", movie_genreArray);
			jsonObject.add("movie_title", movie_titleArray);
			jsonArray.add(jsonObject);
			
			out.write(jsonArray.toString());
			response.setStatus(200);
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
	
	public ArrayList<String> get_genre(Statement statement) throws SQLException
	{
		ArrayList<String> movie_genre = new ArrayList<String>();
		String query = "Select distinct genres.name From genres";
		ResultSet rs = statement.executeQuery(query);
		while (rs.next())
		{	
			String genre = rs.getString("genres.name");
			movie_genre.add(genre);		
		}
		rs.close();
		return(movie_genre);
	}
	
	public ArrayList<String> get_title(Statement statement) throws SQLException
	{
		ArrayList<String> movie_title = new ArrayList<String>();
		String query = "Select distinct substring(movies.title, 1, 1) as Titles From movies "
				+ "Where movies.title Regexp '^[a-z0-9]*$' Order by Titles";
		ResultSet rs = statement.executeQuery(query);
		while (rs.next())
		{	
			String title = rs.getString("Titles");
			movie_title.add(title);		
		}
		rs.close();
		return(movie_title);
	}
}