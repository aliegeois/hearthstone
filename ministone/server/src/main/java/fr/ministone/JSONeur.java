package fr.ministone;

import java.util.Iterator;
import java.util.Map;

public class JSONeur {
    public static String toJSON(Map<String,String> obj) {
        String result = "{";
        Iterator<Map.Entry<String,String>> i = obj.entrySet().iterator();

		while(i.hasNext()) {
            Map.Entry<String,String> current = i.next();
            result += "\"" + current.getKey() + "\":\"" + current.getValue() + "\"";
            if(i.hasNext())
                result += ",";
        }
        
        return result + "}";
    }
}