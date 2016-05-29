package handsome.is.joey.servlet;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class UserBean implements Serializable {
	public String option = new String();
	public String option2 = new String();
	public String option3 = new String();
	public String option5 = new String();
	
	public String getOption5() {
		changeJSON5(input);
		return option5;
	}

	public void setOption5(String option5) {
		this.option5 = option5;
	}

	public String getOption3() {
		changeJSON3(input);
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public static JSONObject input=new JSONObject();
	public UserBean() {
		super();
	}

	public String getOption() {
		changeJSON(input);
		return option;
	}

	public String getOption2() {
		changeJSON2(input);
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	public void changeJSON(JSONObject result){
		option=FormatJSONAndPutIntoBean.ParseField(result);
		
	}
	public void changeJSON2(JSONObject result){
		option2=FormatJSONAndPutIntoBean.ParseAuthor(result);
		
	}
	public void changeJSON3(JSONObject result){
		option3=FormatJSONAndPutIntoBean.ParseAff(result);
		
	}
	public void changeJSON5(JSONObject result){
		option5=FormatJSONAndPutIntoBean.ParseHotwords(result);
		
	}
}
