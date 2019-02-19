import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
class insertParser{
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        	//create an instance
		  
        	castParser dpe = new castParser();
        	dpe.runExample();
        	Hashtable<String,ArrayList<String>> h = dpe.getHash();
        	movieParser mp = new movieParser();
        	mp.runExample();
        	Hashtable<String,ArrayList<ArrayList<String>> > h_i = mp.getHash();
        	Set<String> keys = h_i.keySet();
        	Connection conn = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
            try {
                conn = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            CallableStatement movie_insert = null;
            String sql_insert = null;
            int[] iNoRows = null;
            sql_insert = "{call add_new_movie(?,?,?,?,?)}";
        	try {
        		conn.setAutoCommit(false);
        		movie_insert = conn.prepareCall(sql_insert);
        		for(String key:keys) {
        			if(h.containsKey(key)) {
        				String genre_name;
        				ArrayList<String>genres =h_i.get(key).get(1);
        				while(genres.contains(null)) {
        					genres.remove(null);
        				}
        				if(genres.isEmpty()) {
        					genre_name = "null";
        					ArrayList<String> m_i = h_i.get(key).get(0);
        					ArrayList<String>stars = h.get(key);
        					String m_title = m_i.get(1);
    						String m_director = m_i.get(0);
    						int m_year = Integer.valueOf(m_i.get(2));
        					for(int i = 0; i<stars.size(); i++) {
        						String star = stars.get(i);
        	        			movie_insert.setString(1, m_title);
        	        			movie_insert.setInt(2, m_year);
        	        			movie_insert.setString(3, m_director);
        	        			movie_insert.setString(4, star);
        	        			movie_insert.setString(5, genre_name);
        	        			System.out.println(movie_insert.toString());
        	        			movie_insert.addBatch();;
            				}
        				}
        				else {
        					ArrayList<String> m_i = h_i.get(key).get(0);
        					ArrayList<String>stars = h.get(key);
        					String m_title = m_i.get(1);
    						String m_director = m_i.get(0);
    						int m_year = Integer.valueOf(m_i.get(2));
        					for(int i = 0; i<genres.size(); i++) {
        						genre_name = genres.get(i);
        						for(int j = 0; j<stars.size(); ++j) {
        							String star = stars.get(j);
            	        			movie_insert.setString(1, m_title);
            	        			movie_insert.setInt(2, m_year);
            	        			movie_insert.setString(3, m_director);
            	        			movie_insert.setString(4, star);
            	        			movie_insert.setString(5, genre_name);
            	        			System.out.println(movie_insert.toString());
            	        			movie_insert.addBatch();
        							
        						}
        					}
        			
        				}
        		
        			}
        		}
        		iNoRows=movie_insert.executeBatch();
        		conn.commit();
        		System.out.println(iNoRows.length);
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        	try {
        		if(movie_insert!=null)movie_insert.close();
        		if(conn!=null) conn.close();
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
	}
}
