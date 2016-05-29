package handsome.is.joey.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class Main {

	public static JSONObject Search(String toSearch)  {

		JSONObject result = new JSONObject();
//		String aString="The Text Analytics API is a suite of text analytics web services built with Azure Machine Learning. The API can be used to analyze unstructured text for tasks such as sentiment analysis, key phrase extraction and language detection. ";
//		String bString="I want to study Big data, and also public opinion, papers on nature, how about Computer Science?";
		
		
		try {
			JSONArray KeyWords =IdTransfer.KeyPhrase(toSearch,0);
			ArrayList<String> medium = new ArrayList<String>();
			
			for(Object i:KeyWords)
	      	{
				medium.add(IdTransfer.interpret(i.toString()));
	      	}
			
			
			String finalComposite = "And(Y=[2007,2016],"+requestString(medium)+")";
			JSONArray attributeSet = IdTransfer.GetAcademicEvaluate(finalComposite, "F.FN,Y,CC,AA.AuN,AA.AfN");
			result = ByLty.process(attributeSet);
//			System.out.println("1");
			attributeSet = IdTransfer.GetFamousPaper(requestString(medium));      //requestString(medium)用户的请求被解释成api请求
			HashMap<String, JSONArray> afterProcess;
			afterProcess = getFamousPaperAndHotWord.process(attributeSet);
			for(Entry<String, JSONArray> entry : afterProcess.entrySet()) {
	            result.put(entry.getKey(),entry.getValue());
	            result.put(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
//		System.out.println(requestString(medium));
		return result;
	}
	
	
	public static String requestString(ArrayList<String> aSet){
		ArrayList<String> tmpCompares = new ArrayList<String>();
		int firstFlag = 1;
		int stringNum = 0;
		for (String i : aSet){
			if (firstFlag == 1){
				tmpCompares.add(i);
				stringNum = tmpCompares.size() - 1;
				firstFlag = 0;
			} else {
				if (tmpCompares.get(stringNum).length() < 1200){
					tmpCompares.set(stringNum, "Or("+i+","+tmpCompares.get(stringNum)+")");
				} else{
					tmpCompares.set(stringNum, "Or("+i+","+tmpCompares.get(stringNum)+")");
					firstFlag = 1;
				}
			}
		}
		return tmpCompares.get(0);
	}
	
	public static void main(String[] args){
//		String aString="The Text Analytics API is a suite of text analytics web services built with Azure Machine Learning. The API can be used to analyze unstructured text for tasks such as sentiment analysis, key phrase extraction and language detection. ";
		String bString="I want to study Big data, and also public opinion, papers on nature, how about Computer Science?";
		Search(bString);
	}
	
}
