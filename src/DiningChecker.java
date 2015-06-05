import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiningChecker {

    public static void print(HashMap<String, HashMap<String, HashSet<String>>> data) {
        for (String place : data.keySet()) {
            System.out.println(place);
            HashMap<String, HashSet<String>> mealTimes = data.get(place);
            for (String time : mealTimes.keySet()) {
                System.out.println(time);
                HashSet<String> items = mealTimes.get(time);
                if (items.isEmpty()) {
                    System.out.println("CLOSED NIGGA!\n");
                    continue;
                }
                for (String item : items) {
                    System.out.println(item);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        HashMap<String, String> diningCommons = new HashMap<String, String>();
        diningCommons.put("CROSSROADS", "Crossroads");
        diningCommons.put("CAFE+3", "Cafe 3");
        diningCommons.put("FOOTHILL", "Foothill");
        diningCommons.put("CLARK+KERR", "Clark Kerr");
        
        String[] times = { "Breakfast", "Lunch", "Dinner" };
        
        /* Dining Hall -> Time -> Food */
        HashMap<String, HashMap<String, HashSet<String>>> data = new HashMap<String, HashMap<String, HashSet<String>>>();
        
        data.put("Crossroads", new HashMap<String, HashSet<String>>());
        data.put("Cafe 3", new HashMap<String, HashSet<String>>());
        data.put("Foothill", new HashMap<String, HashSet<String>>());
        data.put("Clark Kerr", new HashMap<String, HashSet<String>>());
        
        for (String diningCommon : diningCommons.values()) {
            for (String time : times) {
                data.get(diningCommon).put(time, new HashSet<String>());
            }
        }
        
        data.get("Cafe 3").remove("Breakfast");
        data.get("Cafe 3").remove("Lunch");
        data.get("Cafe 3").put("Lunch/Brunch", new HashSet<String>());
        
        
        try {
            url = new URL("http://services.housing.berkeley.edu/FoodPro/dining/static/todaysentrees.asp");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));                
            line = br.readLine(); 
            String mealTime = "";
            
            while (line != null) {
                
                /* Getting a meal time */
                if (line.contains("<b>")) {
                    int begin = line.lastIndexOf("<b>") + 3;
                    int end = line.lastIndexOf("</b>");
                    mealTime = line.substring(begin, end);
                }
                
                
                /* Parsing a meal item */
                if (line.contains("<a href='label.asp")) {
                    
                    /* Get the place */
                    String place = "";
                    for (String diningCommon : diningCommons.keySet()) {
                        if (line.contains(diningCommon)) {
                            place = diningCommons.get(diningCommon);
                        }
                    }
                    
                    /* Get the food */
                    Pattern regex = Pattern.compile(".*\\w'>(.*?)</.*");
                    Matcher m = regex.matcher(line);
                    if (m.matches()) {
                        String 
                        
                        item = m.group(1);
                        data.get(place).get(mealTime).add(item);

                    }                    
                }
                
                line = br.readLine();
                               
            }          
                
            DiningChecker.print(data);

        } catch (MalformedURLException mue) {
             mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
            }
        }
    }

}
