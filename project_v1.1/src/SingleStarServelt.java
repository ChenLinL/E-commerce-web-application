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
@WebServlet(name = "SingleStarServlet", urlPatterns = "/api/single-star")
public class SingleStarServelt extends HttpServlet {
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
		//System.out.println(id);
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			//Connect to dataSource
			Connection dbcon = dataSource.getConnection();
			
			String query = "SELECT stars.id, stars.name, stars.birthYear, movies.id, movies.title "
			+ "FROM movies, stars, stars_in_movies "
			+ "WHERE stars.id = ? and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id";


			//System.out.print("ahfdoahk");	
			//System.out.print(query);
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, id);
			//System.out.println(statement);
			// Perform the query
			//System.out.println("465");
			ResultSet rs = statement.executeQuery();
			//System.out.println(rs);
			//System.out.println(rs.getString("stars.name"));
			//System.out.println("46514545156165315631653");
			
			JsonArray jsonArray = new JsonArray();
			
			String current_starId = "";
			
			while (rs.next())
			{	
				String star_id = rs.getString("stars.id");
				//System.out.println(star_id);
				
				if (current_starId.equals(""))
				{
					 current_starId= star_id;
				}
				
				// Create a JsonObject to store all the data
				JsonObject jsonObject = new JsonObject();
				
				// Create a JsonArray to store the list of genres
				JsonArray movie_array = new JsonArray();
				JsonArray movie_id = new JsonArray();
				
				
				String star_name = rs.getString("stars.name");
				//DateFormat dateFormat = new SimpleDateFormat("yyyy");
				String star_dob = rs.getString("stars.birthYear");
				System.out.print(star_dob);

				String mId = rs.getString("movies.id");
				String movie_title = rs.getString("movies.title");
			
				movie_array.add(movie_title);
				movie_id.add(mId);
				while(rs.next())
				{
					String next_starId = rs.getString("stars.id");
					
					if (current_starId.equals(next_starId))
					{
						String next_movieTitle = rs.getString("movies.title");
						String next_mId = rs.getString("movies.id");
						
						movie_array.add(next_movieTitle);
						movie_id.add(next_mId);
							
					}
					else
					{
						current_starId = next_starId;
						rs.previous();
						break;
					}
				}
				String movies_num = Integer.toString(movie_array.size());
				jsonObject.addProperty("star_name", star_name);
				jsonObject.addProperty("star_dob", star_dob);
				jsonObject.add("movies", movie_array);
				jsonObject.add("moviesId", movie_id);
				jsonObject.addProperty("movies_num", movies_num);
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
