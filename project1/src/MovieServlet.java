
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
import java.sql.Statement;
import java.util.Enumeration;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet(name = "/MovieServlet", urlPatterns = "/api/movies")
public class MovieServlet extends HttpServlet {
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
			
			String query = "SELECT movies.id, movies.title, movies.year, movies.director, genres.name, stars.name as Stars, stars.id, ratings.rating,genres.name as Genre "
					       + "FROM movies, (select * from ratings order by rating desc limit 20) ratings, genres, genres_in_movies, stars, stars_in_movies "
					       + "WHERE movies.id = ratings.movieId and genres.id = genres_in_movies.genreId and genres_in_movies.movieId = movies.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id " 
					       + "ORDER BY ratings.rating DESC";
			
			ResultSet rs = statement.executeQuery(query);
			
			JsonArray jsonArray = new JsonArray();
			
			String current_movieId = "";
			String current_genre = "";
			String check_genre = "";
			
			while (rs.next())
			{	
				String movie_id = rs.getString("movies.id");
				
				if (current_movieId.equals(""))
				{
					current_movieId = movie_id;
				}
				
				// Create a JsonObject to store all the data
				JsonObject jsonObject = new JsonObject();
				
				// Create a JsonArray to store the list of genres
				JsonArray movie_genreArray = new JsonArray();
				
				// Create a JsonArray to store the list of stars
				JsonArray movie_starArray = new JsonArray();
				JsonArray starIdArray = new JsonArray();
				
				String movie_title = rs.getString("movies.title");
				String movie_year = rs.getString("movies.year");
				String movie_director = rs.getString("movies.director");
				String movie_genre = rs.getString("Genre");
				if (check_genre.equals(""))
				{
					check_genre = movie_genre;
				}
				
				if (current_genre.equals(""))
				{
					current_genre = movie_genre;
				}
				
				String movie_star = rs.getString("Stars");
				String movie_rating = rs.getString("ratings.rating");
				String star_id = rs.getString("stars.id");
				starIdArray.add(star_id);
				
				movie_genreArray.add(movie_genre);
				movie_starArray.add(movie_star);
				
				while(rs.next())
				{
					String next_movieId = rs.getString("movies.id");
					String next_genre = rs.getString("Genre");
					
					if (current_movieId.equals(next_movieId))
					{
						String next_star = rs.getString("Stars");
						String next_id = rs.getString("stars.id");
						
						if (check_genre.equals(next_genre))
						{
							movie_starArray.add(next_star);
							starIdArray.add(next_id);
						}
						
						if (!current_genre.equals(next_genre))
						{	
							current_genre = next_genre;
							movie_genreArray.add(next_genre);
						}	
					}
					else
					{
						check_genre = "";
						current_movieId = next_movieId;
						current_genre = next_genre;
						rs.previous();
						break;
					}
				}
				String star_num = Integer.toString(movie_starArray.size());
				jsonObject.addProperty("movie_id", movie_id);
				jsonObject.addProperty("movie_title", movie_title);
				jsonObject.addProperty("movie_year", movie_year);
				jsonObject.addProperty("movie_director", movie_director);
				jsonObject.add("movie_genre", movie_genreArray);
				jsonObject.add("movie_star", movie_starArray);
				jsonObject.add("star_id", starIdArray);
				jsonObject.addProperty("movie_rating", movie_rating);
				jsonObject.addProperty("stars_num",star_num);
				jsonArray.add(jsonObject);
			}
			
			out.write(jsonArray.toString());
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