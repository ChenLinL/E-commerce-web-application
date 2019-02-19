
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class movieParser {

    Document dom;
    //List<Movie> myMovie;
    Hashtable<String,ArrayList<ArrayList<String>> > movies_table;
    public movieParser() {
    	movies_table = new Hashtable<String,ArrayList<ArrayList<String>> >();
    }
    
    public Hashtable<String,ArrayList<ArrayList<String>> > getHash() {
    	return movies_table;
    }
    
    public void runExample() {

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

    private void parseDocument() {
        //get the root elememt
    	
        Element docEle = dom.getDocumentElement();
       

        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("directorfilms");
        
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < 5; i++) {

                //get the employee element
                Element el = (Element) nl.item(i);
         
                //get the Employee object
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
     */
    private void getMovie(Element empEl) {
    	
    	 get_director(empEl);
    	
    	System.out.println("-------------------------------------------------");
  
    }
    
    public void get_director(Element empEl)   
    {
    	String director = "";
    	NodeList nl = empEl.getElementsByTagName("director");
    	
    	String title = "";
    	String movie_Id = "";
    	String year = "";
    	String genre = "";
    	NodeList titlenl = empEl.getElementsByTagName("film");
    	for (int i = 0; i < nl.getLength(); i++) {
    		Element el = (Element) nl.item(i);
    		director = getTextValue(el, "dirname");
    		
    		System.out.println(director);
    		for (int j = 0; j < titlenl.getLength(); j++)
        	{
    			ArrayList<String> movie_info = new ArrayList<String>();
        		Element ele1 = (Element) titlenl.item(j);
        		movie_Id = getTextValue(ele1, "fid");
        		title = getTextValue(ele1, "t");
        		year = getTextValue(ele1, "year");
        		movie_info.add(director);
        		movie_info.add(title);
        		movie_info.add(year);
        		NodeList catsnl = ele1.getElementsByTagName("cats");
        		ArrayList<String> genre_info = new ArrayList<String>();
        		for (int z = 0; z < catsnl.getLength(); z++)
        		{
        			Element cat = (Element) catsnl.item(z);
        			genre = getTextValue(cat, "cat");
        			genre_info.add(genre);
        			System.out.println(genre);
        		}
        		ArrayList<ArrayList<String>> m_info = new ArrayList<ArrayList<String>>();
        		m_info.add(movie_info);
        		m_info.add(genre_info);
        		movies_table.put(movie_Id, m_info);
        		System.out.println(movie_Id + " " + title + " " +  year);
        		System.out.println("------------------------------------------------------");
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
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
    	int year = 0;
    	
    	try {
    		year = Integer.parseInt(getTextValue(ele, tagName));
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
        return year;
    }


}
