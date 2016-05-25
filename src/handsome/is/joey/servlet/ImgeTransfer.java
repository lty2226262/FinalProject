//package handsome.is.joey.servlet;
//
//////This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
//import java.net.URI;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//public class ImgeTransfer {
//	public static void main(String[] args) 
//	 {
//	     HttpClient httpclient = HttpClients.createDefault();
//
//	     try
//	     {
//	         URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/face/v1.0/detect");
//
//	         builder.setParameter("returnFaceId", "true");
//	         builder.setParameter("returnFaceLandmarks", "false");
//	         builder.setParameter("returnFaceAttributes", "{string}");
//
//	         URI uri = builder.build();
//	         HttpPost request = new HttpPost(uri);
//	         request.setHeader("Content-Type", "application/json");
//	         request.setHeader("Ocp-Apim-Subscription-Key", "{subscription key}");
//
//
//	         // Request body
//	         StringEntity reqEntity = new StringEntity("{body}");
//	         request.setEntity(reqEntity);
//
//	         HttpResponse response = httpclient.execute(request);
//	         HttpEntity entity = response.getEntity();
//
//	         if (entity != null) 
//	         {
//	             System.out.println(EntityUtils.toString(entity));
//	         }
//	     }
//	     catch (Exception e)
//	     {
//	         System.out.println(e.getMessage());
//	     }
//	 }
//}
