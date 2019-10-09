////
////import java.io.IOException;
////import java.sql.CallableStatement;
////import java.sql.Connection;
////import java.sql.DriverManager;
////import java.sql.SQLException;
////import java.util.ArrayList;
////import java.util.Hashtable;
////import java.util.Iterator;
////import java.util.List;
////import java.util.Map;
////import java.util.Set;
////
////import javax.xml.parsers.DocumentBuilder;
////import javax.xml.parsers.DocumentBuilderFactory;
////import javax.xml.parsers.ParserConfigurationException;
////
////import org.w3c.dom.Document;
////import org.w3c.dom.Element;
////import org.w3c.dom.NodeList;
////import org.xml.sax.SAXException;
////
////public class movieParser {
////
////    Document dom;
////    //List<Movie> myMovie;
////    Hashtable<String,ArrayList<ArrayList<String>> > movies_table;
////    public movieParser() {
////    	movies_table = new Hashtable<String,ArrayList<ArrayList<String>> >();
////    }
////    
////    public Hashtable<String,ArrayList<ArrayList<String>> > getHash() {
////    	return movies_table;
////    }
////    
////    public void runExample() {
////
////        //parse the xml file and get the dom object
////        parseXmlFile();
////
////        //get each employee element and create a Employee object
////        parseDocument();
////
////    }
////
////    private void parseXmlFile() {
////        //get the factory
////        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
////
////        try {
////
////            //Using factory get an instance of document builder
////            DocumentBuilder db = dbf.newDocumentBuilder();
////
////            //parse using builder to get DOM representation of the XML file
////            dom = db.parse("mains243.xml");
////           
////
////        } catch (ParserConfigurationException pce) {
////            pce.printStackTrace();
////        } catch (SAXException se) {
////            se.printStackTrace();
////        } catch (IOException ioe) {
////            ioe.printStackTrace();
////        }
////    }
////
////    private void parseDocument() {
////        //get the root elememt
////    	
////        Element docEle = dom.getDocumentElement();
////       
////
////        //get a nodelist of <employee> elements
////        NodeList nl = docEle.getElementsByTagName("directorfilms");
////        
////        if (nl != null && nl.getLength() > 0) {
////            for (int i = 0; i < 5; i++) {
////
////                //get the employee element
////                Element el = (Element) nl.item(i);
////         
////                //get the Employee object
////                getMovie(el);
////                
////
////            }
////        }
////    }
////
////    /**
////     * I take an employee element and read the values in, create
////     * an Employee object and return it
////     * 
////     * @param empEl
////     * @return
////     */
////    private void getMovie(Element empEl) {
////    	
////    	 get_director(empEl);
////    	
////    	System.out.println("-------------------------------------------------");
////  
////    }
////    
////    public void get_director(Element empEl)   
////    {
////    	String director = "";
////    	NodeList nl = empEl.getElementsByTagName("director");
////    	
////    	String title = "";
////    	String movie_Id = "";
////    	String year = "";
////    	String genre = "";
////    	NodeList titlenl = empEl.getElementsByTagName("film");
////    	for (int i = 0; i < nl.getLength(); i++) {
////    		Element el = (Element) nl.item(i);
////    		director = getTextValue(el, "dirname");
////    		
////    		System.out.println(director);
////    		for (int j = 0; j < titlenl.getLength(); j++)
////        	{
////    			ArrayList<String> movie_info = new ArrayList<String>();
////        		Element ele1 = (Element) titlenl.item(j);
////        		//movie_Id = getTextValue(ele1, "fid");
////        		if((getTextValue(ele1, "fid") != null)&&(getTextValue(ele1, "t")!=null)&&(getTextValue(ele1, "year")!=null)&&(director!=null)) {
////        			
////        		
////        			movie_Id = getTextValue(ele1, "fid");
////        			title = getTextValue(ele1, "t");
////        			year = getTextValue(ele1, "year");
////        			movie_info.add(director);
////        			movie_info.add(title);
////        			movie_info.add(year);
////        			NodeList catsnl = ele1.getElementsByTagName("cats");
////        			ArrayList<String> genre_info = new ArrayList<String>();
////        			for (int z = 0; z < catsnl.getLength(); z++)
////        			{
////        				Element cat = (Element) catsnl.item(z);
////        				genre = getTextValue(cat, "cat");
////        				if(genre != null) {
////        					genre_info.add(genre);
////        					System.out.println(genre);
////        				}
////        			}
////        			ArrayList<ArrayList<String>> m_info = new ArrayList<ArrayList<String>>();
////        			m_info.add(movie_info);
////        			m_info.add(genre_info);
////        			movies_table.put(movie_Id, m_info);
////        			System.out.println(movie_Id + " " + title + " " +  year);
////        			System.out.println("------------------------------------------------------");
////        		}
////        	}
////    	}
////    
////    }
////    
////
////    /**
////     * I take a xml element and the tag name, look for the tag and get
////     * the text content
////     * i.e for <employee><name>John</name></employee> xml snippet if
////     * the Element points to employee node and tagName is name I will return John
////     * 
////     * @param ele
////     * @param tagName
////     * @return
////     */
////    private String getTextValue(Element ele, String tagName) {
////        String textVal = null;
////        
////        NodeList nl = ele.getElementsByTagName(tagName);
////        
////        try {
////        	 if (nl != null && nl.getLength() > 0) {
////             	
////                 Element el = (Element) nl.item(0);
////                 
////                 textVal = el.getFirstChild().getNodeValue();
////             }
////        }
////        catch(Exception e)
////        {
////        	return textVal;
////        }
////       
////
////        return textVal;
////    }
////    
////    /**
////     * Calls getTextValue and returns a int value
////     * 
////     * @param ele
////     * @param tagName
////     * @return
////     */
////    private int getIntValue(Element ele, String tagName) {
////        //in production application you would catch the exception
////    	int year = 0;
////    	
////    	try {
////    		year = Integer.parseInt(getTextValue(ele, tagName));
////    	}
////    	catch(Exception e)
////    	{
////    		return 0;
////    	}
////        return year;
////    }
////    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
////    	//create an instance
////	  
////    	movieParser mp = new movieParser();
////    	mp.runExample();
////    	Hashtable<String,ArrayList<ArrayList<String>> > h_i = mp.getHash();
////    	Set<String> keys = h_i.keySet();
////    	Connection conn = null;
////        Class.forName("com.mysql.jdbc.Driver").newInstance();
////        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
////        try {
////            conn = DriverManager.getConnection(jdbcURL,"root", "lcl960410");
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////
////        CallableStatement movie_insert = null;
////        String sql_insert = null;
////        int[] iNoRows = null;
////        
////        sql_insert = "{call add_movie_parser(?,?,?,?,?)}";
////    	try {
////    		conn.setAutoCommit(false);
////    		movie_insert = conn.prepareCall(sql_insert);
////    		for(String key:keys) {
////    			
////    				String star = "empty";
////    				ArrayList<String>genres =h_i.get(key).get(1);
////    				ArrayList<String> m_i = h_i.get(key).get(0);
////    				String m_title = m_i.get(1);
////					String m_director = m_i.get(0);
////					int m_year = Integer.valueOf(m_i.get(2));
////    				for(int i = 0; i<genres.size(); i++) {
////    					String genre_name = genres.get(i);
////        	        	movie_insert.setString(1, m_title);
////        	        	movie_insert.setInt(2, m_year);
////        	        	movie_insert.setString(3, m_director);
////        	        	movie_insert.setString(4, star);
////        	        	movie_insert.setString(5, genre_name);
////        	        	System.out.println(movie_insert.toString());
////        	        	movie_insert.addBatch();
////    							
////    						}
////    					}
////    			
////    		
////    
////    		iNoRows=movie_insert.executeBatch();
////    		conn.commit();
////    		System.out.println(iNoRows.length);
////    	}catch (SQLException e) {
////    		e.printStackTrace();
////    	}
////    	try {
////    		if(movie_insert!=null)movie_insert.close();
////    		if(conn!=null) conn.close();
////    	}catch(Exception e) {
////    		e.printStackTrace();
////    	}
////}
////}
////
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import java.sql.CallableStatement;
//
//public class movieParser {
//
//    Document dom;
//    static ArrayList<Movie> myMovie = new ArrayList<Movie>();
//
//    public movieParser() {
//    	
//    }
//
//    public void runExample() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//
//        //parse the xml file and get the dom object
//        parseXmlFile();
//
//        //get each employee element and create a Employee object
//        parseDocument();
//
//    }
//
//    private void parseXmlFile() {
//        //get the factory
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//
//        try {
//
//            //Using factory get an instance of document builder
//            DocumentBuilder db = dbf.newDocumentBuilder();
//
//            //parse using builder to get DOM representation of the XML file
//            dom = db.parse("mains243.xml");
//           
//
//        } catch (ParserConfigurationException pce) {
//            pce.printStackTrace();
//        } catch (SAXException se) {
//            se.printStackTrace();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
//
//    private void parseDocument() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//        //get the root elememt
//    	
//        Element docEle = dom.getDocumentElement();
//       
//
//        //get a nodelist of <employee> elements
//        NodeList nl = docEle.getElementsByTagName("directorfilms");
//        
//        if (nl != null && nl.getLength() > 0) {
//        	
//            for (int i = 0; i < nl.getLength(); i++) {
//
//                //get the employee element
//                Element el = (Element) nl.item(i);
//         
//                getMovie(el);
//            }
//        }
//    }
//
//    /**
//     * I take an employee element and read the values in, create
//     * an Employee object and return it
//     * 
//     * @param empEl
//     * @return
//     * @throws ClassNotFoundException 
//     * @throws IllegalAccessException 
//     * @throws InstantiationException 
//     * @throws SQLException 
//     */
//    private void getMovie(Element empEl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//    	
//    	get_director(empEl);
//    	
//    	System.out.println("-------------------------------------------------");
//    }
//    
//    public void get_director(Element empEl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException   
//    {   
//    	String director = "";
//    	NodeList nl = empEl.getElementsByTagName("director");
//    	
//    	String movie_Id = "";
//    	String title = ""; 	
//    	String year = "";
//    	String genre = "";
//    	String actor = "empty";
//    	
//    	
//    	NodeList titlenl = empEl.getElementsByTagName("film");
//    	
//    	for (int i = 0; i < nl.getLength(); i++) {
//    		Movie movie = new Movie();
//    		
//    		Element el = (Element) nl.item(i);
//    		director = getTextValue(el, "dirname");
//    		movie.setName(director);
//    		
//    		for (int j = 0; j < titlenl.getLength(); j++)
//        	{
//        		Element ele1 = (Element) titlenl.item(j);
//        		movie_Id = getTextValue(ele1, "fid");
//        		title = getTextValue(ele1, "t");
//        		year = getTextValue(ele1, "year");
//        		movie.setId(movie_Id);
//        		movie.setTitle(title);
//        		movie.setYear(year);
//        		movie.setActor(actor);
//        		
//        		
//        		if (movie.getId() == null||movie.getId().equals(null))
//        		{
//        			System.out.println(movie.getTitle() + " cannot be added because movie id is null");
//        		}
//        		
//        		else if (movie.getDirector() == null||movie.getDirector().equals(null))
//        		{
//        			System.out.println(movie.getTitle() + " cannot be added because director is null");
//        		}
//        		
//        		else if (movie.getTitle() == null||movie.getTitle().equals(null))
//        		{
//        			System.out.println(movie.getTitle() + " cannot be added because title is null");
//        		}
//        		
//        		else if (movie.getReleaseYear() == null||movie.getReleaseYear().equals(null))
//        		{
//        			System.out.println(movie.getTitle() + " cannot be added because year is null");
//        		}
//        		
//        		else 
//        		{
//        			boolean check = false;
//        			try
//        			{
//        				Integer.valueOf(movie.getReleaseYear());
//        				check = true;
//        			}
//        			catch(Exception e)
//        			{
//        				
//        			}
//        			if (check)
//        			{
//        				movie.setYear(movie.getReleaseYear().replaceAll("\\s+",""));
//        				ArrayList<String> added_genre = new ArrayList<>();
//                		
//                		NodeList catsnl = ele1.getElementsByTagName("cats");
//                		for (int z = 0; z < catsnl.getLength(); z++)
//                		{
//
//                			Element cat = (Element) catsnl.item(z);
//                			genre = getTextValue(cat, "cat");
//                			if(genre == null) {
//                				genre = "null";
//                			}
//                			System.out.println(genre);
//                			added_genre.add(genre);
//                		}
//                		
//                		movie.setGenre(added_genre);
//                		myMovie.add(movie);
//                		System.out.println(movie);        				
//        			}
//        			
//        			else
//        			{
//        				System.out.println("Movie cannot be added due to inconsistency");
//        			}
//        		}
//        		
//        		
//        		System.out.println("------------------------------------------------------");
//        	}
//    	}
//    	
//    }
//    
//    /**
//     * I take a xml element and the tag name, look for the tag and get
//     * the text content
//     * i.e for <employee><name>John</name></employee> xml snippet if
//     * the Element points to employee node and tagName is name I will return John
//     * 
//     * @param ele
//     * @param tagName
//     * @return
//     */
//    private String getTextValue(Element ele, String tagName) {
//        String textVal = null;
//        
//        NodeList nl = ele.getElementsByTagName(tagName);
//        
//        try {
//        	 if (nl != null && nl.getLength() > 0) {
//             	
//                 Element el = (Element) nl.item(0);
//                 
//                 textVal = el.getFirstChild().getNodeValue();
//             }
//        }
//        catch(Exception e)
//        {
//        	return textVal;
//        }
//       
//
//        return textVal;
//    }
//    
//    /**
//     * Calls getTextValue and returns a int value
//     * 
//     * @param ele
//     * @param tagName
//     * @return
//     * @throws ClassNotFoundException 
//     * @throws IllegalAccessException 
//     * @throws InstantiationException 
//     * @throws SQLException 
//     */
//    public static int generate_newGenreId(int max_id, int n)
//	 {	
//			int new_id = max_id+ n;
//			int new_starId = new_id; 
//			
//			return new_starId;
//	 	
//	 }
//    
//    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//        //create an instance
//    	movieParser dpe = new movieParser();
//
//        //call run example
//        dpe.runExample();
//        System.out.println(myMovie.size());
//        
//        Connection conn = null;
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
//        try {
//            conn = DriverManager.getConnection(jdbcURL,"root", "lcl960410");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        PreparedStatement movie_insert = null;
//        PreparedStatement genre_insert = null;
//        PreparedStatement genre_movie_insert = null;
//        String sql_movie_insert = null;
//        String sql_genre_insert = null;
//        String sql_genre_movie_insert = null;
//        int[] iNoRows = null;
//        sql_movie_insert = "insert into movies values(?,?,?,?)";
//        sql_genre_insert = "insert into genres values(?,?)";
//        sql_genre_movie_insert = "insert into genres_in_movies values(?,?)";
//
//        try {
//        	String movie_query = "select id, title, year,director from stars";
//    		String max_genre_id = "select max(id) as id from genres";
//    		String genre_query = "select id, name from genres";
//    		String genre_in_movie = "select g.genreId, g.movieId from genres_in_movies as g";
//        	Hashtable<String,ArrayList<String>> movie_info = new Hashtable<String,ArrayList<String>>();//<id,<title,year,director>
//        	Hashtable<String, Integer> genre_id = new Hashtable<String, Integer> ();//<id, name>
//        	HashMap<Integer, ArrayList<String>> genres_in_movies = new HashMap<Integer, ArrayList<String>> ();//<movie_id,<genres_id>>
//        	
//        	PreparedStatement statement = conn.prepareStatement(movie_query);
//            ResultSet rs = statement.executeQuery();
//            while(rs.next()) {
//            	String m_id = rs.getString("id");
//            	String m_title = rs.getString("title");
//            	int year = rs.getInt("year");
//            	String m_year = Integer.toString(year);
//            	String m_director = rs.getString("director");
//            	ArrayList<String> movie_l = new ArrayList<String>();
//            	movie_l.add(m_title);
//            	movie_l.add(m_year);
//            	movie_l.add(m_director);
//            	if(!movie_info.contains(m_id)) {
//            		movie_info.put(m_id,movie_l);
//            	}
//            }
//            rs.close();
//            statement.close();
//            
//            PreparedStatement genre_statement = conn.prepareStatement(genre_query);
//            ResultSet genre_rs = genre_statement.executeQuery();
//            while(genre_rs.next()) {
//           	 int g_id = genre_rs.getInt("id");
//           	 String g_name = genre_rs.getString("name");
//           	 genre_id.put(g_name,g_id);
//            }
//            genre_rs.close();
//            genre_statement.close();
//        	
//            int genre_max_id = 0;
//            PreparedStatement genre_max = conn.prepareStatement(max_genre_id);
//            ResultSet genre_max_rs = genre_max.executeQuery();
//            while(genre_max_rs.next()) {
//            	genre_max_id = genre_max_rs.getInt("id");
//            }
//            genre_max_rs.close();
//            genre_max.close();
//            
//            PreparedStatement genre_m_statement = conn.prepareStatement(genre_in_movie);
//            ResultSet star_m_rs = genre_m_statement.executeQuery();
//            while(star_m_rs.next()) {
//           	 int g_id = star_m_rs.getInt("s.genreId");
//           	 String m_id  = star_m_rs.getString("s.movieId");
//           	 if(genres_in_movies.containsKey(g_id)) {
//           		 genres_in_movies.get(g_id).add(m_id);
//           	 }
//           	 else {
//           		 ArrayList<String> movie_ids = new ArrayList<String>();
//           		 movie_ids.add(m_id);
//           		 genres_in_movies.put(g_id,movie_ids);
//           	 }
//           	
//            }
//        	
//        	
//        	conn.setAutoCommit(false);
//    		movie_insert = conn.prepareCall(sql_insert);
//    		
//    		
//    		for (int i = 0; i < myMovie.size(); i++)
//    	    {
//    			boolean check = false;
//    			try
//    			{
//    				Integer.valueOf(myMovie.get(i).getReleaseYear());
//    				check = true;
//    				if(myMovie.get(i).getTitle()==null||myMovie.get(i).getTitle().equals(null)) {
//   					 	check = false;
//   				 	}
//    				if(myMovie.get(i).getId()==null||myMovie.get(i).getId().equals(null)) {
//   					 	check = false;
//   				 	}
//    				if(myMovie.get(i).getDirector()==null||myMovie.get(i).getDirector().equals(null)) {
//   					 	check = false;
//   				 	}
//    				
//    			}
//    			catch(Exception e)
//    			{
//    				
//    			}
//    			if (check)
//    			{
//    				if(myMovie.get(i).getTitle()==null||myMovie.get(i).getTitle().equals(null)) {
//   					 	System.out.println("??????????????????????????????????????????????????????????????????????");
//   				 	}
//    			
//    				for (int j = 0; j < myMovie.get(i).getGenre().size(); j++)
//        			{
//        				movie_insert.setString(1, myMovie.get(i).getId());
//            			movie_insert.setString(2, myMovie.get(i).getTitle());
//            			movie_insert.setInt(3, Integer.valueOf(myMovie.get(i).getReleaseYear()));
//            			movie_insert.setString(4, myMovie.get(i).getDirector());
//            			movie_insert.setString(5, myMovie.get(i).getActor());
//            			movie_insert.setString(6, myMovie.get(i).getGenre().get(j));
//            			//System.out.println(movie_insert.toString());
//            			movie_insert.addBatch();
//        			}
//    			}
//    			
//    	    }
//    		
//    		iNoRows=movie_insert.executeBatch();
//    		conn.commit();
//    		System.out.println("Successfully Parsed Movie");
//        }
//        catch (Exception e)
//        {
//        	e.printStackTrace();
//        }
//       
//        try {
//            if(conn!=null) 
//            	conn.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }      
//    }
//}
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.sql.CallableStatement;

public class MovieParser {

    Document dom;
    static Hashtable<String,Movie> myMovie = new Hashtable<String,Movie>();
    static ArrayList<String> my_id = new ArrayList<String>();

    public MovieParser() {
    	
    }

    public void runExample() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("mains243.xml");
           

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        //get the root elememt
    	
        Element docEle = dom.getDocumentElement();
       

        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("directorfilms");
        
        if (nl != null && nl.getLength() > 0) {
        	
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);
         
                getMovie(el);
            }
        }
    }

    /**
     * I take an employee element and read the values in, create
     * an Employee object and return it
     * 
     * @param empEl
     * @return
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws SQLException 
     */
    private void getMovie(Element empEl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
    	
    	get_director(empEl);
    }
    
    public void get_director(Element empEl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException   
    {   
    	String director = "";
    	NodeList nl = empEl.getElementsByTagName("director");
    	
    	String movie_Id = "";
    	String title = ""; 	
    	String year = "";
    	String genre = "";
    	String actor = "empty";
    	    	
    	NodeList movienl = empEl.getElementsByTagName("film");
    	
    	for (int i = 0; i < nl.getLength(); i++) {
    		Element el = (Element) nl.item(i);
    		director = getTextValue(el, "dirname");
    		
    		
    		for (int j = 0; j < movienl.getLength(); j++)
        	{
    			Movie movie = new Movie();
    			movie.setName(director);
    			
        		Element ele1 = (Element) movienl.item(j);
        		movie_Id = getTextValue(ele1, "fid");
        		
        		
        		
        		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + movie_Id);
        		
        		title = getTextValue(ele1, "t");
        		year = getTextValue(ele1, "year");
        		
        		
        		
        		movie.setId(movie_Id);
        		movie.setTitle(title);
        		movie.setYear(year);
        		movie.setActor(actor);
        		
        		
        		boolean check = false;
        		try
        		{
        			Integer.valueOf(movie.getReleaseYear());
        			check = true;
        		}
        		catch(Exception e)
        		{
        				
        		}
        		
        		if (check)
        		{   
        			      				
        			ArrayList<String> added_genre = new ArrayList<>();
                		
                	NodeList catsnl = ele1.getElementsByTagName("cats");
                		
                	for (int z = 0; z < catsnl.getLength(); z++)
                	{
                		Element cat = (Element) catsnl.item(z);
                		genre = getTextValue(cat, "cat");
                		if(genre == null || genre.equals(null) || genre.equals("")) {
                				genre = "null";
                		}
                			
                		genre = genre.trim();
                		
                		added_genre.add(genre);
                	}
                	movie.setGenre(added_genre);
                	
                	if (!my_id.contains(movie.getId()))
                	{	               		
                		my_id.add(movie.getId());
                		if (movie.getId() != null && !movie.getId().equals(null)) {
                			myMovie.put(movie.getId(),movie);
                		}
                				
                	}
                	else
                	{
                		System.out.println(movie.getTitle() + " cannot be added due to duplicate id");
                	}
                	
        		}
        		
        		else
        		{
        			System.out.println(movie.getTitle() + " cannot be added due to inconsistency (year value)");
        		}
        	}
        }
    }
    
    
    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        
        NodeList nl = ele.getElementsByTagName(tagName);
        
        try {
        	 if (nl != null && nl.getLength() > 0) {
             	
                 Element el = (Element) nl.item(0);
                 
                 textVal = el.getFirstChild().getNodeValue();
             }
        }
        catch(Exception e)
        {
        	return textVal;
        }
       

        return textVal;
    }
    
    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws SQLException 
     */
     public static int generate_newGenreId(int max_id, int n)
	 {	
			int new_id = max_id+ n;
			int new_starId = new_id; 
			
			return new_starId;
	 	
	 }
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        //create an instance
    	MovieParser dpe = new MovieParser();

        //call run example
        dpe.runExample();
        
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
        
        //CallableStatement movie_insert = null;
        
        int[] iNoRows = null;
        //String sql_insert = "{call add_movie_parser(?,?,?,?,?,?)}";
        
        try {
            conn = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
        } catch (SQLException e) {
            e.printStackTrace();
        }    	
          PreparedStatement movie_insert = null;
          PreparedStatement genre_insert = null;
          PreparedStatement genre_movie_insert = null;
          PreparedStatement rating_insert = null;
          String sql_movie_insert = null;
          String sql_genre_insert = null;
          String sql_genre_movie_insert = null;
          String sql_rating_insert = null;
          sql_movie_insert = "insert into movies values(?,?,?,?)";
          sql_genre_insert = "insert into genres values(?,?)";
          sql_genre_movie_insert = "insert into genres_in_movies values(?,?)";
          sql_rating_insert = "insert into ratings values(?,0,0)";
  
          try {
          	String movie_query = "select id, title, year,director from movies";
      		String max_genre_id = "select max(id) as id from genres";
      		String genre_query = "select id, name from genres";
      		String genre_in_movie = "select g.genreId, g.movieId from genres_in_movies as g";
          	Hashtable<String,ArrayList<String>> movie_info = new Hashtable<String,ArrayList<String>>();//<id,<title,year,director>
          	Hashtable<String, Integer> genre_id = new Hashtable<String, Integer> ();//<id, name>
          	HashMap<String, ArrayList<Integer>> genres_in_movies = new HashMap<String, ArrayList<Integer>> ();//<movie_id,<genres_id>>
          	
          	PreparedStatement statement = conn.prepareStatement(movie_query);
            ResultSet rs = statement.executeQuery();
              while(rs.next()) {
              	String m_id = rs.getString("id");
              	String m_title = rs.getString("title");
              	int year = rs.getInt("year");
              	String m_year = Integer.toString(year);
              	String m_director = rs.getString("director");
              	ArrayList<String> movie_l = new ArrayList<String>();
              	movie_l.add(m_title);
              	movie_l.add(m_year);
              	movie_l.add(m_director);
              	if(!movie_info.contains(m_id)) {
              		movie_info.put(m_id,movie_l);
              	}
              }
              rs.close();
              statement.close();
              
              PreparedStatement genre_statement = conn.prepareStatement(genre_query);
              ResultSet genre_rs = genre_statement.executeQuery();
              while(genre_rs.next()) {
             	 int g_id = genre_rs.getInt("id");
             	 String g_name = genre_rs.getString("name");
             	 genre_id.put(g_name,g_id);
              }
              genre_rs.close();
              genre_statement.close();
          	
              int genre_max_id = 0;
              PreparedStatement genre_max = conn.prepareStatement(max_genre_id);
              ResultSet genre_max_rs = genre_max.executeQuery();
              while(genre_max_rs.next()) {
              	genre_max_id = genre_max_rs.getInt("id");
              }
              genre_max_rs.close();
              genre_max.close();
              
              PreparedStatement genre_m_statement = conn.prepareStatement(genre_in_movie);
              ResultSet star_m_rs = genre_m_statement.executeQuery();
              while(star_m_rs.next()) {
             	 int g_id = star_m_rs.getInt("g.genreId");
             	 String m_id  = star_m_rs.getString("g.movieId");
             	 if(genres_in_movies.containsKey(m_id)) {
             		 genres_in_movies.get(m_id).add(g_id);
             	 }
             	 else {
             		 ArrayList<Integer> genre_ids = new ArrayList<Integer>();
             		 genre_ids.add(g_id);
             		 genres_in_movies.put(m_id,genre_ids);
             	 }
             	
              }
          	
        	conn.setAutoCommit(false);
        	
    		//movie_insert = conn.prepareCall(sql_insert);
        	genre_insert = conn.prepareCall(sql_genre_insert);
        	Set<String> keys = myMovie.keySet();
        	int count = 0;
    		for (String key:keys)
    		{   			
    			boolean check = true;
    			
    			try
    			{
    				Integer.valueOf(myMovie.get(key).getReleaseYear());
    				if(myMovie.get(key).getTitle() == null || myMovie.get(key).getTitle().equals(null)) {
    					//System.out.println("Movie cannot be added because title cannot be null");
   					 	check = false;
   				 	}
    				
    				if (myMovie.get(key).getId() == null || myMovie.get(key).getId().equals(null))
    				{
    					//System.out.println("Movie cannot be added because id cannot be null");
   					 	check = false;
    				}
    				
    				if(myMovie.get(key).getDirector()==null||myMovie.get(key).getDirector().equals(null)) {
    					//System.out.println(myMovie.get(i).getTitle()+ " cannot be added because director is null");
   					 	check = false;
   				 	}    				
    			}
    			catch (Exception e)
    			{
    				check = false;
    			}
    			
    			if (check)
    			{
    				for (int j = 0; j < myMovie.get(key).getGenre().size(); j++)
        			{
//        				movie_insert.setString(1, myMovie.get(i).getId());
//            			movie_insert.setString(2, myMovie.get(i).getTitle());
//            			movie_insert.setInt(3, Integer.valueOf(myMovie.get(i).getReleaseYear()));
//            			movie_insert.setString(4, myMovie.get(i).getDirector());
//            			movie_insert.setString(5, myMovie.get(i).getActor());
//            			movie_insert.setString(6, myMovie.get(i).getGenre().get(j));     
    					if(!genre_id.containsKey(myMovie.get(key).getGenre().get(j))) {
    						++count;
    						int new_id = generate_newGenreId(genre_max_id,count);
    						String name = myMovie.get(key).getGenre().get(j);
    						genre_id.put(name,new_id);
    						genre_insert.setInt(1, new_id);
    						genre_insert.setString(2,name);
    						genre_insert.addBatch();
    					}
            		
        			}
			 	    		
    			}  			
    		}
    		iNoRows=genre_insert.executeBatch();
    		conn.commit();	
    		movie_insert = conn.prepareCall(sql_movie_insert);
    		rating_insert = conn.prepareCall(sql_rating_insert);
    		for (String key:keys)
    		{   			
    			boolean check = true;
    			
    			try
    			{
    				Integer.valueOf(myMovie.get(key).getReleaseYear());
    				if(myMovie.get(key).getTitle() == null || myMovie.get(key).getTitle().equals(null)) {
    					//System.out.println("Movie cannot be added because title cannot be null");
   					 	check = false;
   				 	}
    				
    				if (myMovie.get(key).getId() == null || myMovie.get(key).getId().equals(null))
    				{
    					//System.out.println("Movie cannot be added because id cannot be null");
   					 	check = false;
    				}
    				
    				if(myMovie.get(key).getDirector()==null||myMovie.get(key).getDirector().equals(null)) {
    					//System.out.println(myMovie.get(i).getTitle()+ " cannot be added because director is null");
   					 	check = false;
   				 	}    				
    			}
    			catch (Exception e)
    			{
    				check = false;
    			}
    			
    			if (check)
    			{
    				if(!movie_info.containsKey(myMovie.get(key).getId())) {
    					String m_id = myMovie.get(key).getId();
    					String m_title = myMovie.get(key).getTitle();
    					int m_year = Integer.valueOf(myMovie.get(key).getReleaseYear());
    					String m_director = myMovie.get(key).getDirector();
    					ArrayList<String> movie_l = new ArrayList<String>();
    	              	movie_l.add(m_title);
    	              	movie_l.add(Integer.toString(m_year));
    	              	movie_l.add(m_director);
    	              	movie_info.put(m_id, movie_l);
    	              	movie_insert.setString(1, m_id);
    	              	movie_insert.setString(2, m_title);
    	              	movie_insert.setInt(3,m_year);
    	              	movie_insert.setString(4,m_director);
    	              	movie_insert.addBatch();
    	              	rating_insert.setString(1,m_id);
    	              	rating_insert.addBatch();
    				}
    			}
    		}
    		iNoRows=movie_insert.executeBatch();
    		iNoRows = rating_insert.executeBatch();
    		conn.commit();	
    		
    			
    		genre_movie_insert = conn.prepareCall(sql_genre_movie_insert);
    		for (String key:keys)
    		{   			
    			boolean check = true;
    			
    			try
    			{
    				Integer.valueOf(myMovie.get(key).getReleaseYear());
    				if(myMovie.get(key).getTitle() == null || myMovie.get(key).getTitle().equals(null)) {
    					//System.out.println("Movie cannot be added because title cannot be null");
   					 	check = false;
   				 	}
    				
    				if (myMovie.get(key).getId() == null || myMovie.get(key).getId().equals(null))
    				{
    					//System.out.println("Movie cannot be added because id cannot be null");
   					 	check = false;
    				}
    				
    				if(myMovie.get(key).getDirector()==null||myMovie.get(key).getDirector().equals(null)) {
    					//System.out.println(myMovie.get(i).getTitle()+ " cannot be added because director is null");
   					 	check = false;
   				 	}    				
    			}
    			catch (Exception e)
    			{
    				check = false;
    			}
    			
    			if (check)
    			{
    			
    				ArrayList<String> genres = myMovie.get(key).getGenre();
    				for (int j = 0; j < genres.size(); j++) {
    					String genre_name = genres.get(j);
    					if(!genres_in_movies.containsKey(key)) {
    						if(genre_id.containsKey(genre_name)) {
    							ArrayList<Integer> new_genres = new ArrayList<Integer>();
    							int new_genre_id = genre_id.get(genre_name);
    							new_genres.add(new_genre_id);
    							genres_in_movies.put(key, new_genres);
    						
    							genre_movie_insert.setInt(1, new_genre_id);
    							genre_movie_insert.setString(2, key);
    							genre_movie_insert.addBatch();
    						}
    					}
    					else {
    						int new_genre_id = genre_id.get(genre_name);
    						if(!genres_in_movies.get(key).contains(new_genre_id)) {
    							
    							genres_in_movies.get(key).add(new_genre_id);
        						genre_movie_insert.setInt(1, new_genre_id);
        						genre_movie_insert.setString(2, key);
        						genre_movie_insert.addBatch();
    						}
    					}
    					
    				}
    			}
    		}
    		iNoRows=genre_movie_insert.executeBatch();
    		conn.commit();	


    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		System.out.println("Successfully Parsed Movie");
        }
        catch (Exception e)
        {
        	 e.printStackTrace();
        }
      
        try {
        	if(genre_insert != null) genre_insert.close();
        	if (movie_insert != null)movie_insert.close();
        	if(rating_insert != null)rating_insert.close();
        	if(genre_movie_insert != null)genre_movie_insert.close();
        	if(conn!=null) conn.close();
        	
        } catch(Exception e) {
        	e.printStackTrace();
        }   
    }
}