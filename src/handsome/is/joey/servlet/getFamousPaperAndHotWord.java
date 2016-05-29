package handsome.is.joey.servlet;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class getFamousPaperAndHotWord {
	static public HashMap<String, JSONArray> process(JSONArray attributeSet) throws ParseException{
		JSONArray resultSuggestedPapers = new JSONArray();
		JSONArray resultHotWords = new JSONArray();
		HashMap<String, JSONArray> result = new HashMap<String, JSONArray>();
		String text="";
		int count=2;
		for(Object i:attributeSet)
		{
			String valueOfE = (String)((JSONObject)i).get("E");
			String title = (String)((JSONObject)i).get("Ti");
			Long CitationCount =(Long)((JSONObject)i).get("CC");
			JSONObject myJsonObject1 = (JSONObject)new JSONParser().parse(valueOfE);
			JSONArray myJsonArray1 =(JSONArray)(myJsonObject1.get("S"));
			JSONObject myJsonObject2=(JSONObject)(myJsonArray1.get(0));
			String resultText = (String) myJsonObject2.get("U");
			String keyWordText =(String)myJsonObject1.get("D");
			JSONObject item = new JSONObject();
			item.put("Title", title);
			item.put("Citation Count", CitationCount.toString());
			item.put("url", resultText);
			resultSuggestedPapers.add(item);
			if(count!=0){
				count--;
				text=text+keyWordText;
			}
			
		}
		resultHotWords=IdTransfer.KeyPhrase(text,1);
		result.put("Suggested Papers", resultSuggestedPapers);
		result.put("Hot words", resultHotWords);
		return result;
		
	}
}
