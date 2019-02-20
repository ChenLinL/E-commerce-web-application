//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Vector;
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
//public class starParser {
//
//   // static Vector<Actor> myActor;
//    
//    Document dom;
//
//    public starParser() {
//        //create a list to hold the actor objects
////        myActor = new Vector<Actor>();
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
//    public String generate_newstarId(String max_id)
//	{	
//		max_id = max_id.replace("n", "");
//		max_id = max_id.replaceAll("m", "");
//		int new_id = Integer.parseInt(max_id) + 1;
//		String new_starId = "nm" + Integer.toString(new_id); 
//		
//		return new_starId;
//		
//	}
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
//            dom = db.parse("actors63.xml");
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
//    private void parseDocument() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
//        //get the root elememt
//    	
//    	Connection conn = null;
//
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
//        
//        try {
//            conn = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//                
//        Element docEle = dom.getDocumentElement();
//        
//        //get a nodelist of <employee> elements
//        NodeList nl = docEle.getElementsByTagName("actor");
//        
//        if (nl != null && nl.getLength() > 0) 
//        {
//        	int[] iNoRows=null;
//        	
//            for (int i = 0; i < nl.getLength(); i++) {
//
//                //get the employee element
//                Element el = (Element) nl.item(i);
//
//                //get the Actor object
//                Actor a = getActor(el);
//                
//                try {
//                	 conn.setAutoCommit(false);
//                	 
//                	 String pre_query = "select max(id) as id from stars";					
//             		 PreparedStatement statement = conn.prepareStatement(pre_query);
//             		 ResultSet rs = statement.executeQuery();
//             		 
//             		 String query2 = "";
//            		
//            		 while(rs.next())
//            		 {
//            			String max_id = rs.getString("id");
//            			String new_id = generate_newstarId(max_id);
//            			System.out.println(new_id);
//            			
//            			// if birthYear is indicated
//                		if (a.getBirthYear() != 0)
//                		{
//                			query2 = "INSERT INTO stars (id, name, birthYear) VALUES (?,?,?)";
//                			statement = conn.prepareStatement(query2);
//        					statement.setString(1, new_id);
//        					statement.setString(2, a.getName());
//        					statement.setInt(3, Integer.valueOf(a.getBirthYear()));
//        					statement.addBatch();
//                		}
//                		
//                		else
//                		{
//                			query2 = "INSERT INTO stars (id, name) VALUES (?,?)";
//    						statement = conn.prepareStatement(query2);
//    						statement.setString(1, new_id);
//    						statement.setString(2, a.getName());
//    						statement.addBatch();
//                		}
//            			
//            		}
//            		 
//            		rs.close();          		
//             		iNoRows=statement.executeBatch();
//             		statement.close();
//        			conn.commit();  		
//                }
//                catch(Exception e)
//                {
//                	e.printStackTrace();
//                }
//            }
//        }
//        
//        try {
//            if(conn!=null) 
//            	conn.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * I take an employee element and read the values in, create
//     * an Employee object and return it
//     * 
//     * @param empEl
//     * @return
//     */
//    private Actor getActor(Element actorEl) {
//
//        //for each <actor> element get text or int values of 
//        //name , and bod
//        String name = getTextValue(actorEl, "stagename");
//        int bod = getIntValue(actorEl, "dob");
//        
//        //Create a new Employee with the value read from the xml nodes
//        Actor a = new Actor(name, bod);
//
//        return a;
//    }
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
//        NodeList nl = ele.getElementsByTagName(tagName);
//        if (nl != null && nl.getLength() > 0) {
//            Element el = (Element) nl.item(0);
//            textVal = el.getFirstChild().getNodeValue();
//        }
//
//        return textVal;
//    }
//    /**
//     * Calls getTextValue and returns a int value
//     * 
//     * @param ele
//     * @param tagName
//     * @return
//     */
//    private int getIntValue(Element ele, String tagName) {
//        //in production application you would catch the exception
//    	int dob = 0;
//    	
//    	try {
//    		dob = Integer.parseInt(getTextValue(ele, tagName));
//    	}
//    	catch(Exception e)
//    	{
//    		return 0;
//    	}
//        return dob;
//    }
//
//
//    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//        //create an instance
//        starParser dpe = new starParser();
//
//        //call run example
//        dpe.runExample();
//        System.out.println("Successfully Parse");
//    }
//
//}
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StarParser {

    static Vector<Actor> myActor;
    
    Document dom;

    public StarParser() {
        //create a list to hold the actor objects
        myActor = new Vector<Actor>();
    }

    public void runExample() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

    }
    
    public String generate_newstarId(String max_id)
	{	
		max_id = max_id.replace("n", "");
		max_id = max_id.replaceAll("m", "");
		int new_id = Integer.parseInt(max_id) + 1;
		String new_starId = "nm" + Integer.toString(new_id); 
		
		return new_starId;
		
	}

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("actors63.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //get the root elememt
    	
    	Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
        
        try {
            conn = DriverManager.getConnection(jdbcURL,"root", "lcl960410");
        } catch (SQLException e) {
            e.printStackTrace();
        }
                
        Element docEle = dom.getDocumentElement();
        
        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("actor");
        
        if (nl != null && nl.getLength() > 0) 
        {
        	int[] iNoRows=null;
        	
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Actor object
                Actor a = getActor(el);
                
                try {
                	 conn.setAutoCommit(false);
                	 
                	 String pre_query = "select max(id) as id from stars";					
             		 PreparedStatement statement = conn.prepareStatement(pre_query);
             		 ResultSet rs = statement.executeQuery();
             		 
             		 String query2 = "";
            		
            		 while(rs.next())
            		 {
            			String max_id = rs.getString("id");
            			String new_id = generate_newstarId(max_id);
            			System.out.println(new_id);
            			
            			// if birthYear is indicated
                		if (a.getBirthYear() != 0)
                		{
                			query2 = "INSERT INTO stars (id, name, birthYear) VALUES (?,?,?)";
                			statement = conn.prepareStatement(query2);
        					statement.setString(1, new_id);
        					statement.setString(2, a.getName());
        					statement.setInt(3, Integer.valueOf(a.getBirthYear()));
        					statement.addBatch();
                		}
                		
                		else
                		{
                			query2 = "INSERT INTO stars (id, name) VALUES (?,?)";
    						statement = conn.prepareStatement(query2);
    						statement.setString(1, new_id);
    						statement.setString(2, a.getName());
    						statement.addBatch();
                		}
            			
            		}
            		 
            		rs.close();          		
             		iNoRows=statement.executeBatch();
             		statement.close();
        			conn.commit();  		
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
        }
        
        try {
            if(conn!=null) 
            	conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * I take an employee element and read the values in, create
     * an Employee object and return it
     * 
     * @param empEl
     * @return
     */
    private Actor getActor(Element actorEl) {

        //for each <actor> element get text or int values of 
        //name , and bod
        String name = getTextValue(actorEl, "stagename");
        int bod = getIntValue(actorEl, "dob");
        
        //Create a new Employee with the value read from the xml nodes
        Actor a = new Actor(name, bod);

        return a;
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
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }
    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
    	int dob = 0;
    	
    	try {
    		dob = Integer.parseInt(getTextValue(ele, tagName));
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
        return dob;
    }


    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        //create an instance
        StarParser dpe = new StarParser();

        //call run example
        dpe.runExample();
        System.out.println("Successfully Parse");
    }

}