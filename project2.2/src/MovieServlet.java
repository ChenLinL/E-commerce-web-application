
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
import java.sql.PreparedStatement;
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
		//System.out.println(request.getRequestURL());
		String title = request.getParameter("title");
		//System.out.println(title);
		String year = request.getParameter("year");
		//System.out.println(year);
		String director = request.getParameter("director");
		System.out.println(director);
		String star = request.getParameter("star");
		String firstRecord = request.getParameter("firstRecord");
		String numRecord = request.getParameter("numRecord");
		String sortType = request.getParameter("sortType");
		String sortOrder = request.getParameter("sortOrder");
		//String title_i = request.getParameter("title_i");
		//String genre = request.getParameter("genre");
		//System.out.println(star);
		//System.out.println(firstRecord);
		//System.out.println(numRecord);
		//System.out.println(sortType);
		//System.out.println(sortOrder);
		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			Statement statement = dbcon.createStatement();
			Statement movie_num = dbcon.createStatement();
			String num_query = "";
			String query = "SELECT movies.id, movies.title, movies.year, movies.director, genres.name, stars.name as Stars, stars.id, ratings.rating,genres.name as Genre "
					       + "FROM";
			//System.out.println(query);
			if(!title.isEmpty()) {
				query += " (select movies.id, movies.title, movies.year, movies.director from movies, ratings where movies.id = ratings.movieId and movies.title LIKE "+"'%"+title+"%'";
				num_query += "select count(*) as movie_num from movies, ratings where movies.id = ratings.movieId and movies.title LIKE "+"'%"+title+"%'";
				if(sortType.equals("rating")) {
					query += " order by ratings.rating "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
				}
				else {
					query += " order by movies.title "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
					
				}
				//System.out.println("t");
			}
			if(!year.isEmpty()) {
				query += "(select movies.id, movies.title, movies.year, movies.director from movies, ratings where movies.id = ratings.movieId and movies.year = "+year;
				num_query += "select count(*) as movie_num from movies, ratings where movies.id = ratings.movieId and movies.year = "+year;
				if(sortType.equals("rating")) {
					query += " order by ratings.rating "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
					
				}
				else {
					query += " order by movies.title "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
				
				}
				//System.out.println("y");
			}
			if(!director.isEmpty()) {
				query += " (select movies.id, movies.title, movies.year, movies.director from movies, ratings where movies.id = ratings.movieId and movies.director = " +"'"+director+"'";
				num_query += "select count(*) as movie_num from movies, ratings where movies.id = ratings.movieId and movies.director = " +"'"+director+"'";
				if(sortType.equals("rating")) {
					query += " order by ratings.rating "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
					
				}
				else {
					query += " order by movies.title "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
				
				}
				//System.out.println("d");
			}
			if(!star.isEmpty()) {
				query += " (select movies.id, movies.title, movies.year, movies.director From movies, stars_in_movies as s_i_m, stars as s, ratings where movies.id = s_i_m.movieId and s_i_m.movieId = ratings.movieId and s.id = s_i_m.starId and s.name = "+"'"+star+"'";
				num_query += "select count(*) as movie_num From movies, stars_in_movies as s_i_m, stars as s, ratings where movies.id = s_i_m.movieId and s_i_m.movieId = ratings.movieId and s.id = s_i_m.starId and s.name = "+"'"+star+"'";
				if(sortType.equals("rating")) {
					query += " order by ratings.rating "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
				}
				else {
					query += " order by movies.title "+sortOrder+" LIMIT "+ numRecord + " OFFSET "+ firstRecord+" ) movies";
			
				}
				//System.out.println("s");
			}
			//if(!title_i.isEmpty()) {
				//query += 
			//}
			query += ", ratings, genres, genres_in_movies, stars, stars_in_movies "
				     + "WHERE movies.id = ratings.movieId and genres.id = genres_in_movies.genreId and genres_in_movies.movieId = movies.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id" ;
			System.out.println(num_query);
			ResultSet rs = statement.executeQuery(query);
			ResultSet num_rs = movie_num.executeQuery(num_query);
			//System.out.println(num_rs.getInt(1));
			//System.out.println(rs.getString(1));
			//System.out.println(num_rs.getObject("movie_num"));
			System.out.println("ooooookkkk");
			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			//statement.setString(1, title);
			//System.out.print(statement);
			//ResultSet rs = statement.executeQuery();
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
				//String num_movie = num_rs.getString(0);
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
				jsonObject.addProperty("mtitle", title);
				jsonObject.addProperty("myear", year);
				jsonObject.addProperty("mdirector", director);
				jsonObject.addProperty("mstar", star);
				jsonObject.addProperty("mfirstRecord", firstRecord);
				jsonObject.addProperty("mnumRecord", numRecord);
				jsonObject.addProperty("msortType", sortType);
				jsonObject.addProperty("msortOrder", sortOrder);
				//jsonObject.addProperty("num_movie", num_movie);
				jsonArray.add(jsonObject);
			}
			
			
			
			out.write(jsonArray.toString());
			//System.out.println(jsonArray.toString());
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