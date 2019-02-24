import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
class insertParser{
	 public static String generate_newstarId(String max_id, int n)
	 {	
			max_id = max_id.replace("n", "");
			max_id = max_id.replaceAll("m", "");
			int new_id = Integer.parseInt(max_id) + n;
			String new_starId = "nm" + Integer.toString(new_id); 
			
			return new_starId;
	 	
	 }
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        	//create an instance
		  
        	castParser dpe = new castParser();
        	dpe.runExample();
        	Hashtable<String,ArrayList<String>> h = dpe.getHash();
        	Hashtable<String,ArrayList<String>> movie_info = new Hashtable<String,ArrayList<String>>();
        	Hashtable<String, String> star_id = new Hashtable<String, String> ();//<id, name>
        	HashMap<String, ArrayList<String>> stars_in_movies = new HashMap<String, ArrayList<String>> ();
        	//Set<String> keys = h.keySet();
        	Connection conn = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
            try {
                conn = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
            } catch (SQLException e) {
                e.printStackTrace();
            }
       
            PreparedStatement star_insert = null;
            PreparedStatement star_movie_insert = null;
            String sql_star_insert = null;
            String sql_star_movie_insert = null;
            int[] iNoRows = null;
            sql_star_insert = "insert into stars values(?,?,null)";
            sql_star_movie_insert = "insert into stars_in_movies values(?,?)";
        	try {
        		String star_query = "select id, name from stars";
        		String max_star_id = "select max(id) as id from stars";
        		String query = "select m.id from movies as m";
        		String star_in_movie = "select s.starId, s.movieId from stars_in_movies as s";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                	String m_id = rs.getString("m.id");
                	if(h.get(m_id)!=null) {
                		//System.out.println(m_id);
                		movie_info.put(m_id, h.get(m_id));
                	}
                	else {
                		//System.out.println(m_id);
                	}
                }
                rs.close();
                statement.close();
                
                PreparedStatement star_statement = conn.prepareStatement(star_query);
                ResultSet star_rs = star_statement.executeQuery();
                while(star_rs.next()) {
               	 String s_id = star_rs.getString("id");
               	 String s_name = star_rs.getString("name");
               	 star_id.put(s_name,s_id);
                }
                star_rs.close();
                star_statement.close();
                
                String star_max_id = "";
                PreparedStatement star_max = conn.prepareStatement(max_star_id);
                ResultSet star_max_rs = star_max.executeQuery();
                while(star_max_rs.next()) {
                	star_max_id = star_max_rs.getString("id");
                }
                star_max_rs.close();
                star_max.close();
                
                PreparedStatement star_m_statement = conn.prepareStatement(star_in_movie);
                ResultSet star_m_rs = star_m_statement.executeQuery();
                while(star_m_rs.next()) {
               	 String s_id = star_m_rs.getString("s.starId");
               	 String m_id  = star_m_rs.getString("s.movieId");
               	 if(stars_in_movies.containsKey(s_id)) {
               		 stars_in_movies.get(s_id).add(m_id);
               	 }
               	 else {
               		 ArrayList<String> movie_ids = new ArrayList<String>();
               		 movie_ids.add(m_id);
               		 stars_in_movies.put(s_id,movie_ids);
               	 }
               	
                }
                Set<String> keys = movie_info.keySet();
                star_m_rs.close();
                star_m_statement.close();
                int count = 0;
                conn.setAutoCommit(false);
        		star_insert = conn.prepareCall(sql_star_insert);
                for(String key:keys) {
        			if(key.equals(null)) {
        				ArrayList<String>stars = movie_info.get(key);
        				System.out.print("the key of");
        				System.out.print(stars);
        				System.out.print(" so it cannot be add to the database");
        			}
        			else {
        				ArrayList<String>stars = h.get(key);	
        				for(int i = 0; i<stars.size(); i++) {
        						String star = stars.get(i);
        						if(!star_id.containsKey(star)) {
        							++count;
        							String new_id = generate_newstarId(star_max_id, count);
        							star_id.put(star,new_id);
        							star_insert.setString(1, new_id);
        							star_insert.setString(2, star);
        							//System.out.println(star_insert.toString());
        							star_insert.addBatch();
        						}
        				}
        			}
                }
                iNoRows=star_insert.executeBatch();
        		conn.commit();				
                if(stars_in_movies.containsKey("nm9433910")) {
                	System.out.println("YES");
                	System.out.println(stars_in_movies.get("nm9433910"));
                }
        		conn.setAutoCommit(false);
        		star_movie_insert = conn.prepareCall(sql_star_movie_insert);
        		for(String key:keys) {
        			if(key.equals(null)) {
        				ArrayList<String>stars = movie_info.get(key);
        				System.out.print("the key of");
        				System.out.print(stars);
        				System.out.print(" so it cannot be add to the database");
        			}
        			else {
        				ArrayList<String>stars = h.get(key);	
        				for(int i = 0; i<stars.size(); i++) {
        						String star = stars.get(i);
        						if(star.equals(null)) {
        							System.out.print("one null show");
        						}
        						else {
        							if(!stars_in_movies.containsKey(star_id.get(star))) {
        								star_movie_insert.setString(1, star_id.get(star));
    									star_movie_insert.setString(2, key);
    									star_movie_insert.addBatch();
    									ArrayList<String> movies = new ArrayList<String>();
    									movies.add(key);
    									stars_in_movies.put(star_id.get(star), movies);
        							}
        							else {
        								ArrayList<String> movies = stars_in_movies.get(star_id.get(star));
        								if(!movies.contains(key)) {
        									star_movie_insert.setString(1, star_id.get(star));
        									star_movie_insert.setString(2, key);
        									star_movie_insert.addBatch();
        									stars_in_movies.get(star_id.get(star)).add(key);
        								}
        							}
        						}
            				}
        				}
        			//iNoRows=star_insert.executeBatch();
        		}
        		 if(stars_in_movies.containsKey("nm9433910")) {
                 	System.out.println("YES");
                 	System.out.println(stars_in_movies.get("nm9433910"));
                 }
        		iNoRows=star_movie_insert.executeBatch();
        		conn.commit();
        		System.out.println("DONE");
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        	try {
        		if(star_insert!=null)star_insert.close();
        		if(star_movie_insert!=null)star_movie_insert.close();
        		if(conn!=null) conn.close();
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
	}
}
