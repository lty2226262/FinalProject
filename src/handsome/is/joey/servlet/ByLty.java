package handsome.is.joey.servlet;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;

public class ByLty {
	static public JSONObject process(JSONArray attributeSet){
		JSONObject result = new JSONObject();
		
		HashMap<String,Long> fHashMap = new HashMap<String,Long>();
		HashMap<String,Long> auHashMap = new HashMap<String,Long>();
		HashMap<String,Long> afHashMap = new HashMap<String,Long>();
		HashMap<String,long[]> tenYearFHashMap = new HashMap<String,long[]>();
		HashMap<String,long[]> tenYearAuHashMap = new HashMap<String,long[]>();
		HashMap<String,long[]> tenYearAfHashMap = new HashMap<String,long[]>();
		long[] tenYearNum = null;
		
		
		for(Object currentObject: attributeSet){
			JSONObject currentJSONObject = (JSONObject)currentObject;
			Long yearLong = (Long)currentJSONObject.get("Y");
			Long ccLong = (Long)currentJSONObject.get("CC");
			int year = yearLong.intValue();
			int cc = ccLong.intValue();
			JSONArray f = (JSONArray)currentJSONObject.get("F");
			if(f!=null){
				for(Object fN:f){
					JSONObject fNJSON = (JSONObject)fN;
					String fNString = (String)(fNJSON.get("FN"));
					if(fHashMap.containsKey(fNString)){
						fHashMap.put(fNString, fHashMap.get(fNString)+1);
						tenYearNum = tenYearFHashMap.get(fNString);
						tenYearNum[year-2007]++;
						tenYearFHashMap.put(fNString, tenYearNum);
					} else {
						fHashMap.put(fNString, (long) 1);
						tenYearNum = new long[10];
						tenYearNum[year-2007] = 1;
						tenYearFHashMap.put(fNString, tenYearNum);
					}
				}
			}
			
			JSONArray aa = (JSONArray)currentJSONObject.get("AA");
			for(Object aaObj:aa){
				String AfNString = (String)((JSONObject)aaObj).get("AfN");
				String AuNString = (String)((JSONObject)aaObj).get("AuN");
				if (AfNString != null){
					if(afHashMap.containsKey(AfNString)){
						afHashMap.put(AfNString, afHashMap.get(AfNString)+1);
						tenYearNum = tenYearAfHashMap.get(AfNString);
						tenYearNum[year-2007]++;
						tenYearAfHashMap.put(AfNString, tenYearNum);
					} else {
						afHashMap.put(AfNString, (long) 1);
						tenYearNum = new long[10];
						tenYearNum[year-2007] = 1;
						tenYearAfHashMap.put(AfNString, tenYearNum);
					}
				}
				if(auHashMap.containsKey(AuNString)){
					auHashMap.put(AuNString, auHashMap.get(AuNString)+cc);
					tenYearNum = tenYearAuHashMap.get(AuNString);
					tenYearNum[year-2007]++;
					tenYearAuHashMap.put(AuNString, tenYearNum);
				} else {
					auHashMap.put(AuNString, (long) cc);
					tenYearNum = new long[10];
					tenYearNum[year-2007] = 1;
					tenYearAuHashMap.put(AuNString, tenYearNum);
				}
			}
		}
		
		Map<String,Long> sortedfHashMap = sortByValue(fHashMap);
		Map<String,Long> sortedauHashMap = sortByValue(auHashMap);
		Map<String,Long> sortedafHashMap = sortByValue(afHashMap);
		int count = 0;
		JSONArray tenResult = new JSONArray();
		JSONObject tempResult = null;
		for(Entry<String, Long> entry : sortedfHashMap.entrySet()) {
//            System.out.println(entry.getValue() + " - " + entry.getKey());
            tempResult = new JSONObject();
            tempResult.put("field", entry.getKey());
            String value = (Arrays.toString(tenYearFHashMap.get(entry.getKey()))).replace(" ", "");
            tempResult.put("recentTenYearsArticle", value);
            tenResult.add(tempResult);
            count++;
            if(count == 10) break;
        }
		result.put("field",tenResult);
		tenResult = new JSONArray();
		count = 0;
		for(Entry<String, Long> entry : sortedauHashMap.entrySet()) {
			tempResult = new JSONObject();
            tempResult.put("authorName", entry.getKey());
            String value = (Arrays.toString(tenYearAuHashMap.get(entry.getKey()))).replace(" ", "");
            tempResult.put("recentTenYearsArticle", value);
            tempResult.put("citationCount", entry.getValue());
            tenResult.add(tempResult);
            count++;
            if(count == 10) break;
		}
		result.put("author", tenResult);
		tenResult = new JSONArray();
		count = 0;
		for(Entry<String, Long> entry : sortedafHashMap.entrySet()) {
			tempResult = new JSONObject();
            tempResult.put("afflicationName", entry.getKey());
            String value = (Arrays.toString(tenYearAfHashMap.get(entry.getKey()))).replace(" ", "");
            tempResult.put("recentTenYearsArticle", value);
            tempResult.put("totalCount", entry.getValue());
            tenResult.add(tempResult);
            count++;
            if(count == 10) break;
		}
		result.put("afflication", tenResult);
		return result;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o2.getValue()).compareTo( o1.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
	
}
