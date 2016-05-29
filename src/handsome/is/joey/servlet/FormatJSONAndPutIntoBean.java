package handsome.is.joey.servlet;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class FormatJSONAndPutIntoBean {
	static JSONObject tempObject = null;
	
	static public String ParseField(JSONObject toParse){
		tempObject = toParse;
		
		JSONObject fieldFormatObject = new JSONObject();
		
		JSONObject tooltipObject = new JSONObject();
		tooltipObject.put("trigger", "'axis'");
		fieldFormatObject.put("tooltip", tooltipObject);
		
		JSONArray legendArray = new JSONArray();
		
		JSONObject gridObject = new JSONObject();
		gridObject.put("left", "'3%'");
		gridObject.put("right", "'4%'");
		gridObject.put("bottom", "'3%'");
		gridObject.put("containLabel", true);
		
		JSONObject xAxisObject = new JSONObject();
		xAxisObject.put("type",  "'category'");
		xAxisObject.put("boundaryGap",  false);
		JSONArray dataArray = new JSONArray();
		for(int i=2007;i<=2016;i++){
			String toadd = "'"+i+"'";
			dataArray.add(toadd);
		}
		xAxisObject.put("data",dataArray);
		fieldFormatObject.put("xAxis", xAxisObject);
		
		JSONObject yAxisObject = new JSONObject();
		yAxisObject.put("type","'value'");
		fieldFormatObject.put("yAxis", yAxisObject);
		
		JSONArray seriesArray = new JSONArray();
		JSONObject seriesObject = null;
		for (Object i: (JSONArray)(toParse.get("field"))){
			JSONObject tempJSONObject = new JSONObject();
			JSONObject currentJSONObject = (JSONObject)i;
			legendArray.add("'"+currentJSONObject.get("field")+"'");
			tempJSONObject.put("name", "'"+currentJSONObject.get("field")+"'");
			tempJSONObject.put("type", "'line'");
			
			tempJSONObject.put("data",currentJSONObject.get("recentTenYearsArticle"));
			seriesArray.add(tempJSONObject);
		}
		
		JSONObject legendObject = new JSONObject();
		legendObject.put("data", legendArray);
		fieldFormatObject.put("legend", legendObject);
		fieldFormatObject.put("series", seriesArray);
		String returnString = (fieldFormatObject.toString()).replace("\"", "");
		returnString.replace("\\", "");
		return returnString;
	}
	
	static public String ParseAuthor(JSONObject toParse){
		JSONObject fieldFormatObject = new JSONObject();
		
		JSONObject tooltipObject = new JSONObject();
		tooltipObject.put("trigger", "'axis'");
		fieldFormatObject.put("tooltip", tooltipObject);
		
		JSONArray legendArray = new JSONArray();
		
		JSONObject gridObject = new JSONObject();
		gridObject.put("left", "'3%'");
		gridObject.put("right", "'4%'");
		gridObject.put("bottom", "'3%'");
		gridObject.put("containLabel", true);
		
		JSONObject xAxisObject = new JSONObject();
		xAxisObject.put("type",  "'category'");
		xAxisObject.put("boundaryGap",  false);
		JSONArray dataArray = new JSONArray();
		for(int i=2007;i<=2016;i++){
			String toadd = "'"+i+"'";
			dataArray.add(toadd);
		}
		xAxisObject.put("data",dataArray);
		fieldFormatObject.put("xAxis", xAxisObject);
		
		JSONObject yAxisObject = new JSONObject();
		yAxisObject.put("type","'value'");
		fieldFormatObject.put("yAxis", yAxisObject);
		
		JSONArray seriesArray = new JSONArray();
		JSONObject seriesObject = null;
		for (Object i: (JSONArray)(toParse.get("author"))){
			JSONObject tempJSONObject = new JSONObject();
			JSONObject currentJSONObject = (JSONObject)i;
			legendArray.add("'"+currentJSONObject.get("authorName")+"'");
			tempJSONObject.put("name", "'"+currentJSONObject.get("authorName")+"'");
			tempJSONObject.put("type", "'line'");
			
			tempJSONObject.put("data",currentJSONObject.get("recentTenYearsArticle"));
			seriesArray.add(tempJSONObject);
		}
		
		JSONObject legendObject = new JSONObject();
		legendObject.put("data", legendArray);
		fieldFormatObject.put("legend", legendObject);
		fieldFormatObject.put("series", seriesArray);
		String returnString = (fieldFormatObject.toString()).replace("\"", "");
		returnString.replace("\\", "");
		return returnString;
	}
	static public String ParseAff(JSONObject toParse){
		JSONObject fieldFormatObject = new JSONObject();
		
		JSONObject tooltipObject = new JSONObject();
		tooltipObject.put("trigger", "'axis'");
		fieldFormatObject.put("tooltip", tooltipObject);
		
		JSONArray legendArray = new JSONArray();
		
		JSONObject gridObject = new JSONObject();
		gridObject.put("left", "'3%'");
		gridObject.put("right", "'4%'");
		gridObject.put("bottom", "'3%'");
		gridObject.put("containLabel", true);
		
		JSONObject xAxisObject = new JSONObject();
		xAxisObject.put("type",  "'category'");
		xAxisObject.put("boundaryGap",  false);
		JSONArray dataArray = new JSONArray();
		for(int i=2007;i<=2016;i++){
			String toadd = "'"+i+"'";
			dataArray.add(toadd);
		}
		xAxisObject.put("data",dataArray);
		fieldFormatObject.put("xAxis", xAxisObject);
		
		JSONObject yAxisObject = new JSONObject();
		yAxisObject.put("type","'value'");
		fieldFormatObject.put("yAxis", yAxisObject);
		
		JSONArray seriesArray = new JSONArray();
		JSONObject seriesObject = null;
		for (Object i: (JSONArray)(toParse.get("afflication"))){
			JSONObject tempJSONObject = new JSONObject();
			JSONObject currentJSONObject = (JSONObject)i;
			legendArray.add("'"+currentJSONObject.get("afflicationName")+"'");
			tempJSONObject.put("name", "'"+currentJSONObject.get("afflicationName")+"'");
			tempJSONObject.put("type", "'line'");
			
			tempJSONObject.put("data",currentJSONObject.get("recentTenYearsArticle"));
			seriesArray.add(tempJSONObject);
		}
		
		JSONObject legendObject = new JSONObject();
		legendObject.put("data", legendArray);
		fieldFormatObject.put("legend", legendObject);
		fieldFormatObject.put("series", seriesArray);
		String returnString = (fieldFormatObject.toString()).replace("\"", "");
		returnString.replace("\\", "");
		return returnString;
	}
	static public String ParseID(int count){
		JSONArray paperArray = (JSONArray) tempObject.get("Suggested Papers");
		JSONObject currentObject = (JSONObject)paperArray.get(count);
		String result = (String) currentObject.get("Title");
		return result;
	}
	
	static public String ParseURL(int count){
		JSONArray paperArray = (JSONArray) tempObject.get("Suggested Papers");
		JSONObject currentObject = (JSONObject)paperArray.get(count);
		String result = (String) currentObject.get("url");
		return result;
	}
	static public String ParseHotwords(JSONObject toParse){
		JSONArray result = new JSONArray(); 
		
		
		JSONArray hotwords = (JSONArray)toParse.get("Hot words");
		
		JSONObject oneResult = null;
		int sizeOfhotwords = hotwords.size();
		for(int i = 0;i<hotwords.size();i++){
			oneResult = new JSONObject();
			oneResult.put("text", hotwords.get(i));
			oneResult.put("weight",sizeOfhotwords--);
			result.add(oneResult);
		}
		
		
		return result.toJSONString();
	}
	
}
