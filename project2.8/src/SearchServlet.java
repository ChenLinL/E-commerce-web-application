import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
    
  	
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
			String title =  request.getParameter("title");
			String year =  request.getParameter("year");
			String director =  request.getParameter("director");
			String star =  request.getParameter("star");
//			System.out.println(title.isEmpty());
//			System.out.println(year.isEmpty());
//			System.out.println(director.isEmpty());
//			System.out.println(star.isEmpty());
			Connection dbcon;
			try {
				dbcon = dataSource.getConnection();
		
				Statement statement = dbcon.createStatement();
				String query = "SELECT count(*) as num "
					       + "FROM" + " (select movies.id, movies.title, movies.year, movies.director from movies,ratings";
				//System.out.println(query);
				if(!star.isEmpty()&& !star.equals("null")) {
					query += ",stars_in_movies as s_i_m, stars as s";
				}
				query += " where movies.id = ratings.movieId";
				if(!title.isEmpty() && !title.equals("null")) {
				    query += " and movies.title LIKE "+"'%"+title+"%'";
				}
				if(!year.isEmpty()&& !year.equals("null")) {
					query += " and movies.year = "+year;
				}
				if(!director.isEmpty()&& !director.equals("null")) {
					query += " and movies.director = " +"'"+director+"'";
				}
				if(!star.isEmpty()&& !star.equals("null")) {
					query += " and movies.id = s_i_m.movieId and s_i_m.movieId = ratings.movieId and s.id = s_i_m.starId and s.name = "+"'"+star+"'";
				}
				query +=")movies";
				System.out.println(query);
				ResultSet rs = statement.executeQuery(query);
				//System.out.println(rs.next());
				System.out.println("1");
				JsonObject jsonObject = new JsonObject();
				while(rs.next()) {
					int num = rs.getInt("num");
					System.out.println("2");
				//String m_num = rs.getString("num");
					System.out.println(num);
					String m_num = Integer.toString(num);
					System.out.println(m_num);
					jsonObject.addProperty("title", title);
					jsonObject.addProperty("year",year);
					jsonObject.addProperty("director",director);
					jsonObject.addProperty("star",star);
					jsonObject.addProperty("m_num",m_num);
				}
				System.out.println(jsonObject.toString());
				out.write(jsonObject.toString());
				response.setStatus(200);
				
				rs.close();
				statement.close();
				dbcon.close();
			}
			catch (Exception e) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("errorMessage", e.getMessage());
				out.write(jsonObject.toString());
				
				response.setStatus(500);
			}
			
			out.close();
    }
		
}
