
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class castParser {

    Document dom;
    Hashtable<String,ArrayList<String>> h;
    public castParser() {
        //create a list to hold the employee objects
    	h = new Hashtable<String, ArrayList<String>>();
    }
    
    public Hashtable<String,ArrayList<String>> getHash(){
    	return h;
    }
    public void runExample() {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

        //Iterate through the list and print the data
//        printData();

    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("casts124.xml");

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
        NodeList nl = docEle.getElementsByTagName("dirfilms");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);
                //System.out.println(el);
                //get the Employee object
                getEmployee(el);

                //add it to list
                //myEmpls.add(e);
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
    private void getEmployee(Element empEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
    	 NodeList nl = empEl.getElementsByTagName("filmc");
    	 //ArrayList<String> start_list = new ArrayList<String>();
    	 
    	 if (nl != null && nl.getLength() > 0) {
    		 //System.out.println(nl.getLength());
             for (int i = 0; i <nl.getLength(); i++) {
            	 String id = "";
            	 ArrayList<String> start_list = new ArrayList<String>();
            	 Element el = (Element) nl.item(i);
            	 NodeList nl_i = el.getElementsByTagName("m");
            	 if (nl_i != null && nl_i.getLength() > 0) {
            		 //System.out.println(nl_i.getLength());
                     for (int j = 0; j < nl_i.getLength(); j++) {
                    	 Element el_i = (Element) nl_i.item(j);
                    	 id = getTextValue(el_i, "f");
                    	 String a = getTextValue(el_i, "a");
                    	 start_list.add(a);
//                  	 	 System.out.println(id);
//                  	 	 System.out.println(a);
                    	 
                     }
            	 }
            	 h.put(id,start_list);
             }
             //System.out.println(id);
             //System.out.println(start_list);
             //h.put(id,start_list);
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
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            //System.out.println(el.getFirstChild());
            if(el.getFirstChild() == null) {
            	textVal = "null";
            }
            else {
            	textVal = el.getFirstChild().getNodeValue();
            }
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
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
   

   

}
