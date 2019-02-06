import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "shopingCartServlet", urlPatterns = "/api/shopingCart")
public class shopingCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * handles POST requests to store session information
     */


    /**
     * handles GET requests to add and show the item list information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String item = request.getParameter("id");
        String func = request.getParameter("func");
        String id = request.getParameter("item");

        //System.out.println(id);
        //System.out.println(func);
        //System.out.println(item);
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        //ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        HashMap<String, Map.Entry<String, Integer>>previousItems = (HashMap<String,Map.Entry<String, Integer>>) session.getAttribute("previousItems");
        JsonArray mId = new JsonArray();
        Boolean empty = false;
        String message = "";
        if(func.equals("add")) {
        	empty = true;
        	message = "The"+id+"is add to your shopping cart!";
        	if (previousItems == null) {
        		previousItems = new HashMap<String,Map.Entry<String, Integer>>();
        		Map.Entry<String, Integer> value = new AbstractMap.SimpleEntry<>(id,1);
        		previousItems.put(item, value);
        		session.setAttribute("previousItems", previousItems);
        	} else {
        		// prevent corrupted states through sharing under multi-threads
        		// will only be executed by one thread at a time
        		synchronized (previousItems) {
        			if(previousItems.containsKey(item)) {
        				int oldV = previousItems.get(item).getValue();
        				int newV = oldV+1;
        				Map.Entry<String, Integer> oldValue = previousItems.get(item);
        				Map.Entry<String, Integer> newValue = new AbstractMap.SimpleEntry<>(id,newV);
        				previousItems.replace(item, oldValue, newValue);
        			}
        			else {
        				Map.Entry<String, Integer> newValue = new AbstractMap.SimpleEntry<>(id,1);
        				previousItems.put(item, newValue);
        			}
        		}
        	}
        }
        else {
        	if (previousItems != null) {
        		// prevent corrupted states through sharing under multi-threads
        		// will only be executed by one thread at a time
        		synchronized (previousItems) {
        			if(previousItems.containsKey(item)) {
        				int oldV = previousItems.get(item).getValue();
        				int newV = oldV-1;
        				if(newV == 0) {
        					previousItems.remove(item);
        				}
        				else {
        					Map.Entry<String, Integer> oldValue = previousItems.get(item);
            				Map.Entry<String, Integer> newValue = new AbstractMap.SimpleEntry<>(id,newV);
        					previousItems.replace(item, oldValue, newValue);
        				}
        			}
        		}
        	}
        	else {
        		//System.out.print("The shopping cart is empty");
        		previousItems = new HashMap<String,Map.Entry<String, Integer>>();
        		session.setAttribute("previousItems", previousItems);
        	}
        }
        JsonObject jsonObject = new JsonObject();
        JsonArray key = new JsonArray();
        JsonArray value = new JsonArray();
        JsonArray id_l = new JsonArray();
        for(Map.Entry<String, Map.Entry<String, Integer>> m : previousItems.entrySet()) {
        	key.add(m.getKey());
        	value.add(m.getValue().getValue());
        	id_l.add(m.getValue().getKey());
        }
        jsonObject.add("id",key);
        jsonObject.add("value",value);
        jsonObject.add("key", id_l);
       // System.out.println(jsonObject.toString());
        response.getWriter().write(jsonObject.toString());
    }
}
