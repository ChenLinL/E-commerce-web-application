
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet(name = "/MovieServlet", urlPatterns = "/api/movies")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//Create a dataSource
	//@Resource(name = "jdbc/masterdb")
	//private DataSource ds;
	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    private static String check_space(String query) {
    	String result = "";
    	String split[] = query.split(" ");
    	for(String s:split) {
    		result += "+"+s+"* ";
    	}
    	return result;
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		long startTime = System.nanoTime();
		response.setContentType("application/json");	
		
		PrintWriter out = response.getWriter();
		
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String star = request.getParameter("star");
		String firstRecord = request.getParameter("firstRecord");
		String numRecord = request.getParameter("numRecord");
		String sortType = request.getParameter("sortType");
		String sortOrder = request.getParameter("sortOrder");
		String title_i = request.getParameter("title_i");
		String genre = request.getParameter("genre");
		String num = null;
		
		long elapsedjdbcTime = 0;
		
		try {
			//Connect to dataSource
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
            		
            long jdbcstartTime = System.nanoTime();	
            // Connect to moviedb
            Connection dbcon = ds.getConnection();
            
			int total = 0;
			int total_i = 0;
			ArrayList<String> sL = new ArrayList<String>(); 
			ArrayList<Integer> sL_i = new ArrayList<Integer>(); 
			boolean t = false;
			boolean g = false;
			
			String query = "SELECT movies.id, movies.title, movies.year, movies.director, genres.name, "
					+ "stars.name as Stars, stars.id, ratings.rating,genres.name as Genre "
					+ "FROM" + " (select movies.id, movies.title, movies.year, movies.director from movies,ratings";
			String n_query = "SELECT count(*) as num "+
				       "FROM" + " (select movies.id, movies.title, movies.year, movies.director from movies,ratings";
			
			
			if(!star.isEmpty()&& !star.equals("null")) {
				query += ",stars_in_movies as s_i_m, stars as s";
				n_query += ",stars_in_movies as s_i_m, stars as s";
			}
			
			if(!genre.isEmpty() && !genre.equals("null")) {
				query += ",genres, genres_in_movies as g_i_m";
				n_query += ",genres, genres_in_movies as g_i_m";
			}
			
			query += " where movies.id = ratings.movieId";
			n_query +=" where movies.id = ratings.movieId";
			
			if(!title.isEmpty() && !title.equals("null")) {
				
				query += " and MATCH (movies.title) AGAINST (? IN BOOLEAN MODE)";
				
				// without using Prepared Statements
				//query += " and MATCH (movies.title) AGAINST ("+ "'" + check_space(title) + "'" + "IN BOOLEAN MODE)";
				n_query += " and MATCH (movies.title) AGAINST (? IN BOOLEAN MODE)";
			    total += 1;
			    sL.add(title);		
			}
			
			if(!year.isEmpty()&& !year.equals("null")) {
				query += " and movies.year LIKE ?";
				n_query += " and movies.year LIKE ?";
				sL.add(year);
				total += 1;
			}
			
			if(!director.isEmpty()&& !director.equals("null")) {
				query += " and movies.director LIKE ?";
				n_query += " and movies.director LIKE ?";
				sL.add(director);
				total += 1;
			}
			
			if(!star.isEmpty()&& !star.equals("null")) {
				query += " and movies.id = s_i_m.movieId and s_i_m.movieId = ratings.movieId and s.id = s_i_m.starId and s.name LIKE ?";
				n_query += " and movies.id = s_i_m.movieId and s_i_m.movieId = ratings.movieId and s.id = s_i_m.starId and s.name LIKE ?";
				sL.add(star);
				total += 1;
			}
		
			if(!title_i.isEmpty()&& !title_i.equals("null")) {
				query += " and movies.title LIKE ?";
				t = true;
				n_query += " and movies.title LIKE ?";
			}
		
			if(!genre.isEmpty() && !genre.equals("null")) {
				query += " and genres.id = g_i_m.genreId and movies.id = g_i_m.movieId and genres.name = ?";
				g = true;
				n_query += " and genres.id = g_i_m.genreId and movies.id = g_i_m.movieId and genres.name = ?";
			}
			
			if(sortType.equals("rating")) {
				
				query += " order by ratings.rating "+sortOrder+" LIMIT ? OFFSET ? ) movies";
				total_i += 2;
				
				sL_i.add(Integer.valueOf(numRecord));
				sL_i.add(Integer.valueOf(firstRecord));
				
				//query += " order by ratings.rating "+sortOrder+" LIMIT " + numRecord + " OFFSET " + firstRecord + ") movies";		
			}
			
			if(sortType.isEmpty() || sortType.equals("null")) {
				
				query += " order by ratings.rating DESC LIMIT ? OFFSET ? ) movies";
				total_i += 2;				
				sL_i.add(1);
				sL_i.add(0);
				
			}
			
			if(sortType.equals("title"))  {
				
				query += " order by movies.title "+sortOrder+" LIMIT ? OFFSET ? ) movies";
				total_i += 2;
				sL_i.add(Integer.valueOf(numRecord));
				sL_i.add(Integer.valueOf(firstRecord));
				
				
				//query += " order by movies.title "+ sortOrder +" LIMIT " + numRecord + " OFFSET " + firstRecord + ") movies";
			}
			
			n_query +=")movies";
			query += ", ratings, genres, genres_in_movies, stars, stars_in_movies "
				     + "WHERE movies.id = ratings.movieId and genres.id = genres_in_movies.genreId and "
				     + "genres_in_movies.movieId = movies.id and stars_in_movies.movieId = movies.id and "
				     + "stars_in_movies.starId = stars.id" ;
			
			// Without Prepared Statement
//			Statement statement = dbcon.createStatement();
//			ResultSet rs = statement.executeQuery(query);
//			long jdbcendTime = System.nanoTime();
			
			// use for movie selection
	        PreparedStatement statement = dbcon.prepareStatement(query);   
	        
	        // use for pagination
	        PreparedStatement movie_num = dbcon.prepareStatement(n_query);
	        
	        
	        int i = 0;
	        int x = 1;
	        
	        String Title = check_space(sL.get(0));
	        
	        statement.setString(1, Title);
        	movie_num.setString(x++,Title);
        	
        	// substring search for director/star
	        for( i = 2; i<=total; ++i)
	        {
	        	statement.setString(i, "%"+sL.get(i-1)+"%");
	        	movie_num.setString(x++, "%"+sL.get(i-1)+"%");
	        
	        }
	        
	        // if browsing movie by first character of title
	        if(t)
	        {
	        	
	        	statement.setString(i++, title_i+"%");
	        	movie_num.setString(x++, title_i+"%");
	        }
	        
	        // if browsing movie by its genre
	        if(g)
	        {	        	
	        	statement.setString(i++, genre);
	        	movie_num.setString(x++, genre);	        	
	        }
	        
	        // set limit and offset
	        for(int j = 0; j < 2; ++j) {
	        	
				statement.setInt(i++, sL_i.get(j));
			}
	       	       
	                
			ResultSet rs = statement.executeQuery();
			long jdbcendTime = System.nanoTime();	   
			
			ResultSet n_rs = movie_num.executeQuery();		
			JsonArray jsonArray = new JsonArray();
			
			String current_movieId = "";
			String current_genre = "";
			String check_genre = "";
			
	        
			while (n_rs.next()) {
				
				int n_num = n_rs.getInt("num");
				String nnum = Integer.toString(n_num);
				num = nnum;
			}
			
			
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
				jsonObject.addProperty("mtitle", title);
				jsonObject.addProperty("myear", year);
				jsonObject.addProperty("mdirector", director);
				jsonObject.addProperty("mstar", star);
				jsonObject.addProperty("mfirstRecord", firstRecord);
				jsonObject.addProperty("mnumRecord", numRecord);
				jsonObject.addProperty("msortType", sortType);
				jsonObject.addProperty("msortOrder", sortOrder);
				jsonObject.addProperty("mtitle_i", title_i);
				jsonObject.addProperty("mgenre", genre);
				jsonObject.addProperty("num", num);
				jsonArray.add(jsonObject);
			}
	
			out.write(jsonArray.toString());
			response.setStatus(200);
			
			rs.close();
			statement.close();
			dbcon.close();
		   
		    elapsedjdbcTime = jdbcendTime - jdbcstartTime;
		    
			
		} catch (Exception e) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			
			response.setStatus(500);
		}
		
		out.close();
		long endTime = System.nanoTime();
		
		long elapsedTime = endTime - startTime;
		
		FileWriter fileWriter = new FileWriter("//home//ubuntu//Logfile.txt", true);
		
		fileWriter.write("TS: " + Long.toString(elapsedTime) + " TJ: " + Long.toString(elapsedjdbcTime));
		fileWriter.write(System.getProperty( "line.separator" ));
	 	fileWriter.close();
	}
	
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 String quantity = request.getParameter("quantity");
	 }
}