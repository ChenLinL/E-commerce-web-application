
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
		String title="";
		String year="";
		String director="";
		String star ="";
		
		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			Statement statement = dbcon.createStatement();
			
			String query = "SELECT movies.id, movies.title, movies.year, movies.director, stars.name as Stars, stars.id, ratings.rating, genres.name as Genre "
				       + "FROM movies, (select * from ratings order by rating desc limit 3) ratings, genres, genres_in_movies, stars, stars_in_movies "
				       + "WHERE movies.id = ratings.movieId and genres.id = genres_in_movies.genreId and genres_in_movies.movieId = movies.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id " 
				       + "ORDER BY ratings.rating DESC";
			
			String current_id = "";
			String current_genre = "";
			String check_genre = "";
			
			JsonArray jsonArray = new JsonArray();
			JsonObject jsonObject = new JsonObject();
			
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next())
			{
				String movie_id = rs.getString("movies.id");
				
				if (current_id.equals(""))
				{
					current_id = movie_id;
				}
				
				JsonObject topjsonObject = new JsonObject();
				
				// Create a JsonArray to store the list of genres
				JsonArray topmovie_genreArray = new JsonArray();
				
				// Create a JsonArray to store the list of stars
				JsonArray topmovie_starArray = new JsonArray();
				
				JsonArray topstarIdArray = new JsonArray();
				
				String movie_title = rs.getString("movies.title");
				String movie_year = rs.getString("movies.year");
				String movie_director = rs.getString("movies.director");
				String movie_genre = rs.getString("Genre");
				String movie_star = rs.getString("Stars");
				String movie_rating = rs.getString("ratings.rating");
				
				if (check_genre.equals(""))
				{
					check_genre = movie_genre;
					current_genre = movie_genre;
				}
				
				String star_id = rs.getString("stars.id");
				topstarIdArray.add(star_id);
				
				topmovie_genreArray.add(movie_genre);
				topmovie_starArray.add(movie_star);
				
				while (rs.next())
				{
					String next_movieId = rs.getString("movies.id");
					String next_genre = rs.getString("Genre");
					
					if (current_id.equals(next_movieId))
					{
						String next_star = rs.getString("Stars");
						String next_id = rs.getString("stars.id");
						
						if (check_genre.equals(next_genre))
						{
							topmovie_starArray.add(next_star);
							topstarIdArray.add(next_id);
						}
						
						if (!current_genre.equals(next_genre))
						{	
							current_genre = next_genre;
							topmovie_genreArray.add(next_genre);
						}	
					}
					
					else
					{
						check_genre = "";
						current_id = next_movieId;
						current_genre = next_genre;
						rs.previous();
						break;
					}					
				}
				topjsonObject.addProperty("top_movie_id", movie_id);
				topjsonObject.addProperty("top_movie_title", movie_title);
				topjsonObject.addProperty("top_movie_year", movie_year);
				topjsonObject.addProperty("top_movie_director", movie_director);
				
				topjsonObject.add("top_movie_genre", topmovie_genreArray);
				topjsonObject.add("top_movie_star", topmovie_starArray);
				topjsonObject.add("top_star_id", topstarIdArray);
				topjsonObject.addProperty("top_movie_rating", movie_rating);
				jsonArray.add(topjsonObject);
			}
			
			ArrayList<String> movie_genre = new ArrayList<String>();
			movie_genre = get_genre(statement);
			
			ArrayList<String> movie_title = new ArrayList<String>();
			movie_title = get_title(statement);
			
			JsonArray movie_genreArray = new JsonArray();
			JsonArray movie_titleArray = new JsonArray();		
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
			jsonObject.addProperty("selected_title", title);
			jsonObject.addProperty("selected_year", year);
			jsonObject.addProperty("selected_director", director);
			jsonObject.addProperty("selected_star", star);
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