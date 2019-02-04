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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
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

		response.setContentType("application/json"); // Response mime type

		// Retrieve parameter id from url request.
		String id = request.getParameter("id");

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		String mtitle = request.getParameter("title");
		//System.out.println(title);
		String myear = request.getParameter("year");
		//System.out.println(year);
		String mdirector = request.getParameter("director");
		//System.out.println(director);
		String mstar = request.getParameter("star");
		String mfirstRecord = request.getParameter("firstRecord");
		String mnumRecord = request.getParameter("numRecord");
		String msortType = request.getParameter("sortType");
		String msortOrder = request.getParameter("sortOrder");

		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			
			String query = "SELECT movies.id, movies.title, movies.year, movies.director, genres.name as Genre, stars.name as Stars, stars.id, ratings.rating "
					+ "FROM movies, ratings, genres, genres_in_movies, stars, stars_in_movies "
					+ "WHERE movies.id = ratings.movieId and genres.id = genres_in_movies.genreId and genres_in_movies.movieId = movies.id and "
					+ "stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id and movies.id = ? ";
				
			
			System.out.println("RUNNING SINGLEMOVIE SERVLET");
			
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, id);

			// Perform the query
			ResultSet rs = statement.executeQuery();
			
			JsonArray jsonArray = new JsonArray();
			
			List<String> genre_list = new ArrayList<String>();
			List<String> actor_list = new ArrayList<String>();
			List<String> actor_idlist = new ArrayList<String>();
			
			while (rs.next())
			{	
				String movie_id = rs.getString("movies.id");
				
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
				genre_list.add(movie_genre);
				
				String movie_star = rs.getString("Stars");
				actor_list.add(movie_star);
				
				String movie_rating = rs.getString("ratings.rating");
				String star_id = rs.getString("stars.id");
				actor_idlist.add(star_id);
				
				while(rs.next())
				{
					String next_genre = rs.getString("Genre");
					String next_star = rs.getString("Stars");
					String next_id = rs.getString("stars.id");
					
					if (!genre_list.contains(next_genre))
					{
						genre_list.add(next_genre);
					}
					
					if (!actor_list.contains(next_star))
					{
						actor_list.add(next_star);
					}
					
					if (!actor_idlist.contains(next_id))
					{
						actor_idlist.add(next_id);
					}
				}
				
				for (int i = 0; i < genre_list.size(); i++)
				{
					movie_genreArray.add(genre_list.get(i));
				}
				
				for (int i = 0; i < actor_list.size(); i++)
				{
					movie_starArray.add(actor_list.get(i));
				}
				
				for (int i = 0; i < actor_idlist.size(); i++)
				{
					starIdArray.add(actor_idlist.get(i));
				}
				
				String stars_num = Integer.toString(movie_starArray.size());
				jsonObject.addProperty("movie_id", movie_id);
				jsonObject.addProperty("movie_title", movie_title);
				jsonObject.addProperty("movie_year", movie_year);
				jsonObject.addProperty("movie_director", movie_director);
				jsonObject.add("movie_genre", movie_genreArray);
				jsonObject.add("movie_star", movie_starArray);
				jsonObject.add("star_id", starIdArray);
				jsonObject.addProperty("movie_rating", movie_rating);
				jsonObject.addProperty("stars_num",stars_num);
				jsonObject.addProperty("mtitle", mtitle);
				jsonObject.addProperty("myear", myear);
				jsonObject.addProperty("mdirector", mdirector);
				jsonObject.addProperty("mstar", mstar);
				jsonObject.addProperty("mfirstRecord", mfirstRecord);
				jsonObject.addProperty("mnumRecord", mnumRecord);
				jsonObject.addProperty("msortType", msortType);
				jsonObject.addProperty("msortOrder", msortOrder);
				//System.out.print(jsonObject.toString());
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