package handsome.is.joey.servlet;

//import java.io.EOFException;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
//import javax.swing.JOptionPane;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
public class IDTransfer implements IDTypeDefInterface{
	static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	static{		
		cm.setMaxTotal(1000);        		
	}
	static CloseableHttpClient httpclient = HttpClients.custom()
            .setConnectionManager(cm)
            .build();
    static class GetThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        public JSONArray resultArray;
        public GetThread(CloseableHttpClient httpClient, HttpGet httpget) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
        }
        public GetThread(CloseableHttpClient httpClient,int aType,long aId, int bType) throws URISyntaxException {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext(); 
            String testCompare;
            if (aType == AUID && bType == ID) {
            	testCompare = "Composite(" + TYPE[aType] + "=" + aId + ")";
            	URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");          
                builder.setParameter("expr", testCompare);
                builder.setParameter("count", "999999");
                builder.setParameter("attributes", TYPE[bType]);
                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
                this.httpget = request;
            }
            else if (aType == AUID ){
            	testCompare = "Composite(" + TYPE[aType] + "=" + aId + ")";
            	URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");          
                builder.setParameter("expr", testCompare);
                builder.setParameter("count", "999999");
                builder.setParameter("attributes", TYPE[bType]+","+TYPE[aType]);
                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
                this.httpget = request;
            } else{
            	testCompare = "" + TYPE[aType] + "=" + aId;
            	URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");          
                builder.setParameter("expr", testCompare);
                builder.setParameter("count", "999999");
                builder.setParameter("attributes", TYPE[bType]);
                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
                this.httpget = request;
            }
        }
        public GetThread(CloseableHttpClient httpClient,int aType,long aId, int bType,int cType) throws URISyntaxException {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();       
            String testCompare;
            if(aType == ID|| aType == RID){
    			testCompare = "" + TYPE[aType] + "=" + aId;
    		} else {
    			testCompare = "Composite(" + TYPE[aType] + "=" + aId + ")";
    		}
			URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");          
            builder.setParameter("expr", testCompare);
            builder.setParameter("count", "999999");
            builder.setParameter("attributes", TYPE[bType]+","+TYPE[cType]);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
            this.httpget = request;        
        }      
        @Override
        public void run() {
            try {
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                    	resultArray = (JSONArray) ((JSONObject)new JSONParser().parse(EntityUtils.toString(entity))).get("entities");
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println("124行" + e);
            }
        }
    }	
    static class GetThreadForSet extends Thread{
    	private GetThread[] threads;
    	public JSONArray[] resultArray;
    	public GetThreadForSet(int aType, HashSet<Long> aSet, int bType) throws URISyntaxException {
    		if (aType == AUID){
    			ArrayList<String> tmpCompares = new ArrayList<String>();
    			int firstFlag = 1;
    			int stringNum = 0;
    			for (long i : aSet){
    				if (firstFlag == 1){
    					tmpCompares.add("Composite(AA.AuId="+i+")");
    					stringNum = tmpCompares.size() - 1;
    					firstFlag = 0;
    				} else {
    					if (tmpCompares.get(stringNum).length() < EXPRSTRINGLENGTHFORSET){
    						tmpCompares.set(stringNum, "Or(Composite(AA.AuId="+i+"),"+tmpCompares.get(stringNum)+")");
    					} else{
    						tmpCompares.set(stringNum, "Or(Composite(AA.AuId="+i+"),"+tmpCompares.get(stringNum)+")");
    						firstFlag = 1;
    					}
    				}
    			}
    			this.threads = new GetThread[tmpCompares.size()];
				int tempI = 0;
				URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");
				builder.setParameter("count", "999999");
				builder.setParameter("attributes", TYPE[AUID]+","+TYPE[AFID]);
				int sizeOftmoCompares = tmpCompares.size();
				for (int i = 0;i < sizeOftmoCompares;i++){
					String testCompare = tmpCompares.get(i);
					builder.setParameter("expr", testCompare);
					HttpGet request = new HttpGet(builder.build());
					request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
					this.threads[tempI++] = new GetThread(httpclient,request);
				}
    		}
    		else {
    			ArrayList<String> tmpCompares = new ArrayList<String>();
    			int firstFlag = 1;
    			int stringNum = 0;
    			for (long i : aSet){
    				if (firstFlag == 1){
    					tmpCompares.add("Id="+i);
    					stringNum = tmpCompares.size() - 1;
    					firstFlag = 0;
    				} else {
    					if (tmpCompares.get(stringNum).length() < EXPRSTRINGLENGTHFORSET){
    						tmpCompares.set(stringNum, "Or(Id="+i+","+tmpCompares.get(stringNum)+")");
    					} else{
    						tmpCompares.set(stringNum, "Or(Id="+i+","+tmpCompares.get(stringNum)+")");
    						firstFlag = 1;
    					}
    				}
    			}
    			this.threads = new GetThread[tmpCompares.size()];
				int tempI = 0;
				URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");
				builder.setParameter("count", "999999");
				builder.setParameter("attributes", TYPE[aType]+","+TYPE[bType]);
				int sizeOftmpCompares = tmpCompares.size();
				for (int i = 0;i < sizeOftmpCompares;i++){
					String testCompare = tmpCompares.get(i);
					builder.setParameter("expr", testCompare);
					HttpGet request = new HttpGet(builder.build());
					request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
					this.threads[tempI++] = new GetThread(httpclient,request);
				}
    		}
        }
    	public GetThreadForSet(int startType, HashMap<String,HashSet<Long>> startSet,int endType,long end,int targetType) throws URISyntaxException{
    		if (startType == AUIDFIDCIDJIDRID){
    			ArrayList<String> tmpCompares = new ArrayList<String>();
    			int firstFlag = 1;
    			int stringNum = 0;
    			for (Entry<String,HashSet<Long>> entry : startSet.entrySet()){
    				String Type = entry.getKey();
    				for (long i : entry.getValue()){
    					if (firstFlag == 1){
    						if (Type.equals("RId")){
    							tmpCompares.add("RId="+i);
    						} else {
    							tmpCompares.add("Composite("+Type+"="+i+")");
    						}
        					stringNum = tmpCompares.size() - 1;
        					firstFlag = 0;
        				} else {
        					if (tmpCompares.get(stringNum).length() < EXPRSTRINGLENGTHFORSET){
        						if (Type.equals("RId")){
        							tmpCompares.set(stringNum, "Or(RId="+i+","+tmpCompares.get(stringNum)+")");
        						} else {
        							tmpCompares.set(stringNum, "Or(Composite("+Type+"="+i+"),"+tmpCompares.get(stringNum)+")");
        						}	
        					} else{
        						if (Type.equals("RId")){
        							tmpCompares.set(stringNum, "Or(RId="+i+","+tmpCompares.get(stringNum)+")");
        						} else {
        							tmpCompares.set(stringNum, "Or(Composite("+Type+"="+i+"),"+tmpCompares.get(stringNum)+")");
        						}
        						firstFlag = 1;
        					}
        				}
    				}		
    			}
    			int sizeOftmoCompares = tmpCompares.size();
    			for (int i = 0; i < sizeOftmoCompares;i++){
    				tmpCompares.set(i, "And(Composite(AA.AuId="+end+"),"+tmpCompares.get(i)+")");
    			}
    			this.threads = new GetThread[tmpCompares.size()];
				int tempI = 0;
				URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");
				builder.setParameter("count", "999999");
				builder.setParameter("attributes", TYPE[ID]+","+TYPE[AUIDFIDCIDJIDRID]);

				for (int i = 0;i < sizeOftmoCompares;i++){
					String testCompare = tmpCompares.get(i);
					builder.setParameter("expr", testCompare);
					HttpGet request = new HttpGet(builder.build());
					request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
					this.threads[tempI++] = new GetThread(httpclient,request);
				}
    		}
    		else if(endType == AUID){
    			ArrayList<String> tmpCompares = new ArrayList<String>();
    			int firstFlag = 1;
    			int stringNum = 0;
    			for (Entry<String,HashSet<Long>> entry : startSet.entrySet()){
    				String Type = entry.getKey();
    				for (long i : entry.getValue()){
    					if (firstFlag == 1){
    						tmpCompares.add("Composite("+Type+"="+i+")");
        					stringNum = tmpCompares.size() - 1;
        					firstFlag = 0;
        				} else {
        					if (tmpCompares.get(stringNum).length() < EXPRSTRINGLENGTHFORSET){
        						tmpCompares.set(stringNum, "Or(Composite("+Type+"="+i+"),"+tmpCompares.get(stringNum)+")");
        					} else{
        						tmpCompares.set(stringNum, "Or(Composite("+Type+"="+i+"),"+tmpCompares.get(stringNum)+")");
        						firstFlag = 1;
        					}
        				}
    				}		
    			}
    			int sizeOftmoCompares = tmpCompares.size();
    			for (int i = 0; i < sizeOftmoCompares;i++){
    				tmpCompares.set(i, "And(Composite(AA.AuId="+end+"),"+tmpCompares.get(i)+")");
    			}
    			this.threads = new GetThread[tmpCompares.size()];
				int tempI = 0;
				URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");
				builder.setParameter("count", "999999");
				builder.setParameter("attributes", TYPE[ID]+","+TYPE[AUIDFIDCIDJID]);
				for (int i = 0;i <sizeOftmoCompares;i++){
					String testCompare = tmpCompares.get(i);
					builder.setParameter("expr", testCompare);
					HttpGet request = new HttpGet(builder.build());
					request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
					this.threads[tempI++] = new GetThread(httpclient,request);
				}
    		}
    		else{
    			ArrayList<String> tmpCompares = new ArrayList<String>();
    			int firstFlag = 1;
    			int stringNum = 0;
    			for (Entry<String,HashSet<Long>> entry : startSet.entrySet()){
    				String Type = entry.getKey();
    				for (long i : entry.getValue()){
    					if (firstFlag == 1){
    						tmpCompares.add("Composite("+Type+"="+i+")");
        					stringNum = tmpCompares.size() - 1;
        					firstFlag = 0;
        				} else {
        					if (tmpCompares.get(stringNum).length() < EXPRSTRINGLENGTH){
        						tmpCompares.set(stringNum, "Or(Composite("+Type+"="+i+"),"+tmpCompares.get(stringNum)+")");
        					} else{
        						tmpCompares.add("Composite("+Type+"="+i+")");
            					stringNum = tmpCompares.size() - 1;
        					}
        				}
    				}		
    			}
    			int sizeOftmoCompares = tmpCompares.size();
    			for (int i = 0; i < sizeOftmoCompares;i++){
    				tmpCompares.set(i, "And(RId="+end+","+tmpCompares.get(i)+")");
    			}
    			this.threads = new GetThread[sizeOftmoCompares*2];
				int tempI = 0;
				URIBuilder builder = new URIBuilder("https://oxfordhk.azure-api.net/academic/v1.0/evaluate");
				builder.setParameter("count", "30000");
				builder.setParameter("attributes", TYPE[ID]+","+TYPE[AUIDFIDCIDJID]);
				for (int i = 0;i < sizeOftmoCompares;i++){
					for (int ii = 0; ii < 60000; ii+= 30000){
						String testCompare = tmpCompares.get(i);
						builder.setParameter("expr", testCompare);
						if(ii == 30000) {
							builder.setParameter("count", "999999");
						}
						builder.setParameter("offset", ""+ii);
						HttpGet request = new HttpGet(builder.build());
						request.setHeader("Ocp-Apim-Subscription-Key", "f7cc29509a8443c5b3a5e56b0e38b5a6");
						this.threads[tempI++] = new GetThread(httpclient,request);
					}
				}
    		}
    	}	
    	@Override
        public void run() {
    		int sizeOfthreads = threads.length;
    		resultArray = new JSONArray[sizeOfthreads];
    		try {
	    		for (int i = 0; i < sizeOfthreads;i++){
					threads[i].start();
				}
	    		for (int i = 0; i < sizeOfthreads;i++){
						threads[i].join();
	    		}
	    		for (int i = 0; i < sizeOfthreads;i++){
					this.resultArray[i] = threads[i].resultArray;
				}
    		}
    		catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    static public ArrayList<ArrayList<Long>> bfs(long start,long end){
    	ArrayList<ArrayList<Long>> res = new ArrayList<ArrayList<Long>>();
    	try{
    		GetThread threads000 = new GetThread(httpclient,AUID,start,AFIDANDIDANDRID);
    		GetThread threads001 = new GetThread(httpclient,AUID,end,AFIDANDID);
    		GetThread threads002 = new GetThread(httpclient,ID,start,AUIDFIDCIDJIDRID);
    		GetThread threads003 = new GetThread(httpclient,ID,end,AUIDFIDCIDJID);
    		GetThread threads004 = new GetThread(httpclient,RID,end,ID);
    		threads000.start();
    		threads001.start();
    		threads002.start();
    		threads003.start();
    		threads004.start();
    		threads000.join();
    		int startType = (threads000.resultArray.size()>0)? AUID:ID;
    		threads001.join();
    		int endType = (threads001.resultArray.size()>0)? AUID:ID;
    		if (startType == ID && endType == ID){
				threads002.join();
				HashSet<Long> threads1ReturnValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashMap<String,HashSet<Long>> threads1ReturnTagValue = new HashMap<String,HashSet<Long>>(HASHMAPSIZESMALL);
				HashSet<Long> threads1ReturnAllValue = new HashSet<Long>(HASHMAPSIZESMALL);
				GetFromIdToIdThreads002(threads1ReturnValue, threads1ReturnTagValue, threads1ReturnAllValue, threads002.resultArray);
				GetThreadForSet threads5 = new GetThreadForSet(ID,threads1ReturnValue,AUIDFIDCIDJIDRID);
				threads5.start();
				GetThreadForSet threads4 = new GetThreadForSet(AUIDFIDCIDJID, threads1ReturnTagValue,RID,end,ID);
				threads4.start();
				if(threads1ReturnAllValue.contains(end)){
					ArrayList<Long> oneRes = new ArrayList<Long>(2);
					oneRes.add(start);
					oneRes.add(end);
					res.add(oneRes);
				}
				threads003.join(); 
				HashSet<Long> thread2ReturnValue = GetBFromA(ID,end,AUIDFIDCIDJID,threads003.resultArray);
				threads004.join();
				HashSet<Long> thread3ReturnValue = GetBFromA(RID,end,ID,threads004.resultArray);
				for(long secondNode:threads1ReturnAllValue){
					if(thread2ReturnValue.contains(secondNode) || thread3ReturnValue.contains(secondNode)){
						ArrayList<Long> oneRes = new ArrayList<Long>(3);
						oneRes.add(start);
						oneRes.add(secondNode);
						oneRes.add(end);
						res.add(oneRes);
					}
				}
				thread3ReturnValue.addAll(thread2ReturnValue);		
				threads4.join();
				GetBFromResultIDSetAndAddSomeResult(AUIDFIDCIDJID, threads4.resultArray, start, end, threads1ReturnAllValue ,res);
				threads5.join();
				GetBFromASetForMultiThreadsAndAddSomeResult(ID,thread3ReturnValue,AUIDFIDCIDJIDRID,threads5.resultArray, null, start, end, res);
    		} 
    		else if(startType == ID) {
    			//threads1 == threads001,threads2 == threads002
				threads002.join();
				HashSet<Long> thread2ReturnValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashMap<String,HashSet<Long>> thread2ReturnTagValue = new HashMap<String,HashSet<Long>>(HASHMAPSIZESMALL);
				HashSet<Long> thread2ReturnIdValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashSet<Long> thread2ReturnNoTagValue = new HashSet<Long>(HASHMAPSIZESMALL);
				GetFromIdToAuIdThreads002(thread2ReturnValue, thread2ReturnTagValue, thread2ReturnIdValue,thread2ReturnNoTagValue,threads002.resultArray);
				GetThreadForSet threads3 = new GetThreadForSet(AUID,thread2ReturnValue,AFID);
				threads3.start();
				GetThreadForSet threads4 = new GetThreadForSet(AUIDFIDCIDJID,thread2ReturnTagValue,AUID,end,ID);
				threads4.start();
				GetThreadForSet threads5 = new GetThreadForSet(ID,thread2ReturnIdValue,RID);
				threads5.start();
				HashSet<Long> thread1ReturnValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashSet<Long> thread1ReturnIdValue = new HashSet<Long>(HASHMAPSIZESMALL);
				GetFromIdToAuIdThreads001(thread1ReturnValue, thread1ReturnIdValue,end,threads001.resultArray);
				if(thread2ReturnValue.contains(end)){
					ArrayList<Long> oneRes = new ArrayList<Long>(2);
	            	oneRes.add(start);
            		oneRes.add(end);
            		res.add(oneRes);
				}
				ArrayList<Long> oneRes = null;
				for(long secondNode: thread2ReturnIdValue){
					if(thread1ReturnIdValue.contains(secondNode)){
						oneRes = new ArrayList<Long>(3);
		            	oneRes.add(start);
		            	oneRes.add(secondNode);
	            		oneRes.add(end);
	            		res.add(oneRes);
					}
				}
				threads3.join();
				GetBFromASetForMultiThreadsAndAddSomeResult(AUID,thread2ReturnValue,AFID,threads3.resultArray, thread1ReturnValue, start,end, res);
				threads4.join();
				GetBFromResultIDSetAndAddSomeResult(AUIDFIDCIDJID,threads4.resultArray,start,end,thread2ReturnNoTagValue,res);
				threads5.join();
				GetBFromASetForMultiThreadsAndAddSomeResult(ID,thread1ReturnIdValue,RID,threads5.resultArray, null, start, end, res);
    		} 
    		else if(endType == ID){
				threads003.join();
				HashSet<Long> thread2ReturnValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashMap<String,HashSet<Long>> thread2ReturnTagValue = new HashMap<String,HashSet<Long>>(HASHMAPSIZESMALL);
				HashSet<Long> thread2ReturnNoTagValue = new HashSet<Long>(HASHMAPSIZESMALL);
				GetFromBacktoFrontAuIdtoId(end, threads003.resultArray, thread2ReturnValue, thread2ReturnTagValue, thread2ReturnNoTagValue);
				GetThreadForSet threads4 = new GetThreadForSet(AUID,thread2ReturnValue,AFID);
				threads4.start();
				GetThreadForSet threads5 = new GetThreadForSet(AUIDFIDCIDJID,thread2ReturnTagValue,AUID,start,ID);
				threads5.start();
				HashSet<Long> thread1ReturnIdValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashSet<Long> thread1ReturnValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashMap<Long,HashSet<Long>> ridMap = new HashMap<Long,HashSet<Long>>(HASHMAPSIZESMALL);
				GetFromFronttoEndAuIdtoAuId(start, threads000.resultArray, thread1ReturnIdValue, thread1ReturnValue, ridMap);
				threads004.join();
				GetBFromAAndAddSomeResult(RID,end,ID,threads004.resultArray, start, ridMap, thread1ReturnIdValue, res);
				if(thread1ReturnIdValue.contains(end)){
					ArrayList<Long> oneRes = new ArrayList<Long>(2);
	            	oneRes.add(start);
            		oneRes.add(end);
            		res.add(oneRes);
				} 
				threads4.join();
				GetBFromASetForMultiThreadsAndAddSomeResultReverse(AUID,thread2ReturnValue,AFID, threads4.resultArray,thread1ReturnValue,start,end,res);
				threads5.join();
				GetBFromResultIDSetAndAddSomeResultReverse(AUIDFIDCIDJID,threads5.resultArray, start, end, thread2ReturnNoTagValue, res);
    		}
    		else {
				HashSet<Long> threads1ReturnValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashSet<Long> threads1ReturnAfidValue = new HashSet<Long>(HASHMAPSIZESMALL);
				GetFromBacktoFrontAuIdtoAuId( end, threads001.resultArray, threads1ReturnValue, threads1ReturnAfidValue);
				HashMap<Long,HashSet<Long>> threads3ReturnValuemap = new HashMap<Long,HashSet<Long>>(HASHMAPSIZESMALL);
				HashSet<Long> threads3ReturnAfidValue = new HashSet<Long>(HASHMAPSIZESMALL);
				HashSet<Long> threads3ReturnIdValue = new HashSet<Long>(HASHMAPSIZESMALL);
				GetFromFronttoEndAuIdtoAuId(start, threads000.resultArray, threads3ReturnIdValue, threads3ReturnAfidValue, threads3ReturnValuemap);
				ArrayList<Long> oneRes = null;
				for (long secondNode:threads1ReturnAfidValue){
					if(threads3ReturnAfidValue.contains(secondNode)){
						oneRes = new ArrayList<Long>(3);
						oneRes.add(start);
						oneRes.add(secondNode);
						oneRes.add(end);
						res.add(oneRes);
					} 
				}
				for (long i:threads1ReturnValue){
					if (threads3ReturnValuemap.containsKey(i)){
						//这个得到hashmap里对应的值，然后遍历就是结果啦
						for (long ii : threads3ReturnValuemap.get(i)){
							oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(ii);
							oneRes.add(i);
							oneRes.add(end);
							res.add(oneRes);
						}
					} 
					if (threads3ReturnIdValue.contains(i)){
						oneRes = new ArrayList<Long>(3);
						oneRes.add(start);
						oneRes.add(i);
						oneRes.add(end);
						res.add(oneRes);
					}
				}	
    		}
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	return res;
    }
	static private void GetFromBacktoFrontAuIdtoId(long aId,JSONArray resultArray, HashSet<Long> AUIDres,HashMap<String,HashSet<Long>> res, HashSet<Long> res2){
		int sizeOfresultArray = resultArray.size();
		for(int iii = 0;iii<sizeOfresultArray;iii++){
    		JSONObject imformation = new JSONObject();
    		imformation = (JSONObject)(resultArray.get(iii));
			JSONArray aa = (JSONArray)imformation.get("AA");
			int sizeOfaa = aa.size();
			for(int i = 0;i < sizeOfaa;i++){
    			long auid = (long)((JSONObject)aa.get(i)).get("AuId");
    			AUIDres.add(auid);
    			res2.add(auid);
    			if (res.containsKey("AA.AuId")){ 					
					HashSet<Long> tmpKeyValue = res.get("AA.AuId");
					tmpKeyValue.add(auid);
					res.put("AA.AuId", tmpKeyValue);
				} else{
					HashSet<Long> oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
					oneKeyValue.add(auid);
					res.put("AA.AuId", oneKeyValue);
				}
    		}
			JSONArray f = (JSONArray)imformation.get("F");
			if (f!=null){	
    			int sizeOff = f.size();
    			for(int i = 0;i < sizeOff;i++){
        			long fid = (long)((JSONObject)f.get(i)).get("FId");
        			res2.add(fid);
        			if (res.containsKey("F.FId")){ 					
    					HashSet<Long> tmpKeyValue = res.get("F.FId");
    					tmpKeyValue.add(fid);
    					res.put("F.FId", tmpKeyValue);
    				} else{
    					HashSet<Long> oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
    					oneKeyValue.add(fid);
    					res.put("F.FId", oneKeyValue);
    				}
        		}
    		} 
    		if (imformation.get("C")!=null){
    			JSONObject c = (JSONObject)imformation.get("C");
    			long cid = (long)c.get("CId");
    			res2.add(cid);
				HashSet<Long> oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
				oneKeyValue.add(cid);
				res.put("C.CId", oneKeyValue);
    		}
    		if (imformation.get("J")!=null){
    			JSONObject j = (JSONObject)imformation.get("J");
    			long jid = (long)j.get("JId");
    			res2.add(jid);
				HashSet<Long> oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
				oneKeyValue.add(jid);
				res.put("J.JId", oneKeyValue);
    		} 
    	}
    }
	static private void GetFromBacktoFrontAuIdtoAuId(long aId,JSONArray resultArray,HashSet<Long> IDres,HashSet<Long> AFIDres){
		int sizeOfresultArray = resultArray.size();
		for(int i = 0;i < sizeOfresultArray;i++){
    		JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(i))).get("AA");
    		IDres.add((long) ((JSONObject)resultArray.get(i)).get("Id"));
    		
    		int sizeOfaa = aa.size();
    		for(int ii = 0;ii < sizeOfaa;ii++){

    			long id = (long)((JSONObject)aa.get(ii)).get("AuId");
    			if (id == aId && ((JSONObject)aa.get(ii)).get("AfId")!=null){
    				AFIDres.add((long)((JSONObject)aa.get(ii)).get("AfId"));
    			}
    		}
    	}
	}
	static private void GetFromFronttoEndAuIdtoAuId(long aId,JSONArray resultArray,HashSet<Long> IDres,HashSet<Long> AFIDres,HashMap<Long,HashSet<Long>> returnValueMap){
		int sizeOfresultArray = resultArray.size();
		for(int i = 0;i < sizeOfresultArray;i++){
			JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(i))).get("AA");
			IDres.add((long) ((JSONObject)resultArray.get(i)).get("Id"));
			
			int sizeOfaa = aa.size();
			for(int ii = 0;ii < sizeOfaa;ii++){
				
				long id = (long)((JSONObject)aa.get(ii)).get("AuId");
				
				if (id == aId && ((JSONObject)aa.get(ii)).get("AfId")!=null){
					AFIDres.add((long)((JSONObject)aa.get(ii)).get("AfId"));
				}
			}
			long id =(long) ((JSONObject)resultArray.get(i)).get("Id");
    		if (((JSONObject)resultArray.get(i)).get("RId") != null) {
    			JSONArray ridJSONArray = (JSONArray) ((JSONObject)resultArray.get(i)).get("RId");
        		for(Object ii : ridJSONArray){
        			long rid = (long)ii;
        			if (returnValueMap.containsKey(rid)){
        				HashSet<Long> tmpKeyValue = returnValueMap.get(rid);
        				tmpKeyValue.add(id);
        				returnValueMap.put(rid, tmpKeyValue);
        			} else{
        				HashSet<Long> oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
        				oneKeyValue.add(id);
        				returnValueMap.put(rid, oneKeyValue);
        			}
        		}
    		}
		}
	}
	static private HashSet<Long> GetBFromA (int aType,long aId,int bType,JSONArray resultArray){
		HashSet<Long> res = new HashSet<Long>(HASHMAPSIZEBIG);
		if (aType == RID){
        	int sizeOfResultArray = resultArray.size();
			for(int i = 0;i<sizeOfResultArray;i++){
        		res.add((long) ((JSONObject)resultArray.get(i)).get("Id"));
        	}
		}
		else{
			int sizeOfResultArray = resultArray.size();
			for(int i = 0;i<sizeOfResultArray;i++){
				JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(i))).get("AA");
        		for(int ii = 0;ii < aa.size();ii++){
        			res.add((long)((JSONObject)aa.get(ii)).get("AuId"));
        		}
        		JSONArray f = (JSONArray)((JSONObject)(resultArray.get(i))).get("F");
        		if (f!=null){
        			int sizeOff = f.size();
        			for(int ii = 0;ii < sizeOff;ii++){
        				res.add((long)((JSONObject)f.get(ii)).get("FId"));
        			}
        		}
        		JSONObject c = (JSONObject)(((JSONObject)(resultArray.get(i))).get("C"));
        		if(c!=null){
        			res.add((long)c.get("CId"));
        		}
        		JSONObject j = (JSONObject)((JSONObject)(resultArray.get(i))).get("J");
        		if(j!=null){
        			res.add((long)j.get("JId"));
        		}
        	}
		}
		return res;
	}
	private static void GetFromIdToIdThreads002(HashSet<Long> threads1ReturnValue,
			HashMap<String, HashSet<Long>> threads1ReturnTagValue, HashSet<Long> threads1ReturnAllValue, 
			JSONArray resultArray) {
		int sizeOfresultArray = resultArray.size();
		HashSet<Long> oneKeyValue = null;
		for(int i = 0;i<sizeOfresultArray;i++){
			JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(i))).get("AA");
    		int sizeOfaa = aa.size();
			for(int ii = 0;ii < sizeOfaa;ii++){
    			long auid = (long)((JSONObject)aa.get(ii)).get("AuId");
    			threads1ReturnAllValue.add(auid);
    			if (threads1ReturnTagValue.containsKey("AA.AuId")){ 					
					HashSet<Long> tmpKeyValue = threads1ReturnTagValue.get("AA.AuId");
					tmpKeyValue.add(auid);
					threads1ReturnTagValue.put("AA.AuId", tmpKeyValue);
				} else{
					oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
					oneKeyValue.add(auid);
					threads1ReturnTagValue.put("AA.AuId", oneKeyValue);
				}
    		}
    		JSONArray f = (JSONArray)((JSONObject)(resultArray.get(i))).get("F");
    		if (f!=null){
    			int sizeOff = f.size();
    			for(int ii = 0;ii < sizeOff;ii++){
    				long fid = (long)((JSONObject)f.get(ii)).get("FId");
    				threads1ReturnAllValue.add(fid);
    				if (threads1ReturnTagValue.containsKey("F.FId")){ 					
    					HashSet<Long> tmpKeyValue = threads1ReturnTagValue.get("F.FId");
    					tmpKeyValue.add(fid);
    					threads1ReturnTagValue.put("F.FId", tmpKeyValue);
    				} else{
    					oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
    					oneKeyValue.add(fid);
    					threads1ReturnTagValue.put("F.FId", oneKeyValue);
    				}
    			}		
    		}
    		JSONObject c = (JSONObject)(((JSONObject)(resultArray.get(i))).get("C"));
    		if(c!=null){
    			long cid = (long)c.get("CId");
    			threads1ReturnAllValue.add(cid);
    			oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
				oneKeyValue.add(cid);
				threads1ReturnTagValue.put("C.CId", oneKeyValue);
    		}
    		JSONObject j = (JSONObject)((JSONObject)(resultArray.get(i))).get("J");
    		if(j!=null){
    			long jid = (long)j.get("JId");
    			threads1ReturnAllValue.add(jid);
    			oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
				oneKeyValue.add(jid);
				threads1ReturnTagValue.put("J.JId", oneKeyValue);
    		}
    		JSONArray rid = (JSONArray)((JSONObject)(resultArray.get(i))).get("RId");
    		if(rid!=null)
    		{
    			for(Object ii:rid){
    				threads1ReturnValue.add((long)ii);
    				threads1ReturnAllValue.add((long)ii);
        		}
    		}
    	}
	}
	private static void GetFromIdToAuIdThreads002(HashSet<Long> thread2ReturnValue,
			HashMap<String, HashSet<Long>> thread2ReturnTagValue, HashSet<Long> thread2ReturnIdValue, 
			HashSet<Long> thread2ReturnNoTagValue, JSONArray resultArray) {
		int sizeOfresultArray = resultArray.size();
		HashSet<Long> oneKeyValue = null;
		for(int i = 0;i<sizeOfresultArray;i++){
			JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(i))).get("AA");
    		int sizeOfaa = aa.size();
			for(int ii = 0;ii < sizeOfaa;ii++){
    			long auid = (long)((JSONObject)aa.get(ii)).get("AuId");
    			thread2ReturnValue.add(auid);
    			thread2ReturnNoTagValue.add(auid);
    			if (thread2ReturnTagValue.containsKey("AA.AuId")){ 					
					HashSet<Long> tmpKeyValue = thread2ReturnTagValue.get("AA.AuId");
					tmpKeyValue.add(auid);
					thread2ReturnTagValue.put("AA.AuId", tmpKeyValue);
				} else{
					oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
					oneKeyValue.add(auid);
					thread2ReturnTagValue.put("AA.AuId", oneKeyValue);
				}
    		}
    		JSONArray f = (JSONArray)((JSONObject)(resultArray.get(i))).get("F");
    		if (f!=null){	
    			int sizeOff = f.size();
    			for(int ii = 0;ii < sizeOff;ii++){
    				long fid = (long)((JSONObject)f.get(ii)).get("FId");
    				thread2ReturnNoTagValue.add(fid);
    				if (thread2ReturnTagValue.containsKey("F.FId")){ 					
    					HashSet<Long> tmpKeyValue = thread2ReturnTagValue.get("F.FId");
    					tmpKeyValue.add(fid);
    					thread2ReturnTagValue.put("F.FId", tmpKeyValue);
    				} else{
    					oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
    					oneKeyValue.add(fid);
    					thread2ReturnTagValue.put("F.FId", oneKeyValue);
    				}
    			}		
    		}
    		JSONObject c = (JSONObject)(((JSONObject)(resultArray.get(i))).get("C"));
    		if(c!=null){
    			long cid = (long)c.get("CId");
    			thread2ReturnNoTagValue.add(cid);
    			oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
				oneKeyValue.add(cid);
				thread2ReturnTagValue.put("C.CId", oneKeyValue);
    		}
    		
    		JSONObject j = (JSONObject)((JSONObject)(resultArray.get(i))).get("J");
    		if(j!=null){
    			long jid = (long)j.get("JId");
    			thread2ReturnNoTagValue.add(jid);
    			oneKeyValue = new HashSet<Long>(HASHMAPSIZESMALL);
				oneKeyValue.add(jid);
				thread2ReturnTagValue.put("J.JId", oneKeyValue);
    		}
    		JSONArray rid = (JSONArray)((JSONObject)(resultArray.get(i))).get("RId");
    		if(rid!=null)
    		{
    			for(Object ii:rid){
    				thread2ReturnIdValue.add((long)ii);
    				thread2ReturnNoTagValue.add((long)ii);
        		}
    		}
    	}
	}
	private static void GetFromIdToAuIdThreads001(HashSet<Long> thread1ReturnValue, HashSet<Long> thread1ReturnIdValue, long end, 
			JSONArray resultArray) {
		int sizeOfresultArray = resultArray.size();
		for(int i = 0;i < sizeOfresultArray;i++){
			thread1ReturnIdValue.add((long) ((JSONObject)resultArray.get(i)).get("Id"));
    		JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(i))).get("AA");
    		int sizeOfaa = aa.size();
    		for(int ii = 0;ii < sizeOfaa;ii++){
    			long auid = (long)((JSONObject)aa.get(ii)).get("AuId");
    			if (auid == end && ((JSONObject)aa.get(ii)).get("AfId")!=null){
    				thread1ReturnValue.add((long)((JSONObject)aa.get(ii)).get("AfId"));
    			}
    		}
    	}
	}
	private static void GetBFromResultIDSetAndAddSomeResult(int bType, JSONArray[] resultArrays, long start,
			long end, HashSet<Long> thread2ReturnNoTagValue, ArrayList<ArrayList<Long>> res) {	
		HashSet<ArrayList<Long>> localRes = new HashSet<ArrayList<Long>>(HASHMAPSIZEBIG);
		int sizeOfresultArrays = resultArrays.length;
		ArrayList<Long> oneRes = null;
		for (int m = 0; m <sizeOfresultArrays;m++){
			JSONArray resultArray = resultArrays[m];
			int sizeOfresultArray = resultArray.size();
			for(int n = 0;n<sizeOfresultArray;n++){
	    		JSONObject imformation = (JSONObject)(resultArray.get(n));
	    		long id =(long) imformation.get("Id");
	    		JSONArray aa = (JSONArray)imformation.get("AA");
	    		int sizeOfaa = aa.size();
	    		for(int i = 0;i < sizeOfaa;i++){
	    			long auid = (long)((JSONObject)aa.get(i)).get("AuId");
	    			if (thread2ReturnNoTagValue.contains(auid)){
	    				oneRes = new ArrayList<Long>(4);
						oneRes.add(start);
						oneRes.add(auid);
						oneRes.add(id);
						oneRes.add(end);
						localRes.add(oneRes);
	    			}
	    		}
	    		if (imformation.get("F")!=null){
	    			JSONArray f = (JSONArray)imformation.get("F");
	    			int sizeOff = f.size();
	    			for(int i = 0;i < sizeOff;i++){
	        			long fid = (long)((JSONObject)f.get(i)).get("FId");		
	        			if (thread2ReturnNoTagValue.contains(fid)){
		    				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(fid);
							oneRes.add(id);
							oneRes.add(end);
							localRes.add(oneRes);
		    			}
	        		}
	    		}
	    		if (imformation.get("C")!=null){
	    			JSONObject c = (JSONObject)imformation.get("C");
	    			long cid = (long)c.get("CId");
	    			if (thread2ReturnNoTagValue.contains(cid)){
	    				oneRes = new ArrayList<Long>(4);
						oneRes.add(start);
						oneRes.add(cid);
						oneRes.add(id);
						oneRes.add(end);
						localRes.add(oneRes);
	    			}
	    		}
	    		if (imformation.get("J")!=null){
	    			JSONObject j = (JSONObject)imformation.get("J");
	    			long jid = (long)j.get("JId");
	    			if (thread2ReturnNoTagValue.contains(jid)){
	    				oneRes = new ArrayList<Long>(4);
						oneRes.add(start);
						oneRes.add(jid);
						oneRes.add(id);
						oneRes.add(end);
						localRes.add(oneRes);
	    			}
	    		}
	    	}
		}
		res.addAll(localRes);	
	}
	
	private static void GetBFromResultIDSetAndAddSomeResultReverse(int bType, JSONArray[] resultArrays, long start,
			long end, HashSet<Long> thread2ReturnNoTagValue, ArrayList<ArrayList<Long>> res) {
		HashSet<ArrayList<Long>> localRes = new HashSet<ArrayList<Long>>(HASHMAPSIZEBIG);
		int sizeOfresultArrays =  resultArrays.length;
		ArrayList<Long> oneRes = null;
		for (int m = 0; m <sizeOfresultArrays;m++){
			JSONArray resultArray = resultArrays[m];
			int sizeOfreslutArray = resultArray.size();
			for(int n = 0;n<sizeOfreslutArray;n++){
	    		JSONObject imformation = (JSONObject)(resultArray.get(n));
	    		long id =(long) imformation.get("Id");
	    		JSONArray aa = (JSONArray)imformation.get("AA");
	    		int sizeOfaa = aa.size();
	    		for(int i = 0;i < sizeOfaa;i++){
	    			long auid = (long)((JSONObject)aa.get(i)).get("AuId");
	    			if (thread2ReturnNoTagValue.contains(auid)){
	    				oneRes = new ArrayList<Long>(4);
						oneRes.add(start);
						oneRes.add(id);
						oneRes.add(auid);
						oneRes.add(end);
						localRes.add(oneRes);
	    			}
	    		}
	    		if (imformation.get("F")!=null){
	    			JSONArray f = (JSONArray)imformation.get("F");
	    			int sizeOff = f.size();
	    			for(int i = 0;i < sizeOff;i++){
	        			long fid = (long)((JSONObject)f.get(i)).get("FId");		
	        			if (thread2ReturnNoTagValue.contains(fid)){
		    				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(id);
							oneRes.add(fid);
							oneRes.add(end);
							localRes.add(oneRes);
		    			}
	        		}
	    		}
	    		if (imformation.get("C")!=null){
	    			JSONObject c = (JSONObject)imformation.get("C");
	    			long cid = (long)c.get("CId");
	    			if (thread2ReturnNoTagValue.contains(cid)){
	    				oneRes = new ArrayList<Long>(4);
						oneRes.add(start);
						oneRes.add(id);
						oneRes.add(cid);
						oneRes.add(end);
						localRes.add(oneRes);
	    			}
	    		}
	    		if (imformation.get("J")!=null){
	    			JSONObject j = (JSONObject)imformation.get("J");
	    			long jid = (long)j.get("JId");
	    			if (thread2ReturnNoTagValue.contains(jid)){
	    				oneRes = new ArrayList<Long>(4);
						oneRes.add(start);
						oneRes.add(id);
						oneRes.add(jid);
						oneRes.add(end);
						localRes.add(oneRes);
	    			}
	    		}
	    	}
		}
		res.addAll(localRes);
	}
	private static void GetBFromASetForMultiThreadsAndAddSomeResult(int aType, HashSet<Long> aSet,
			int bType, JSONArray[] resultArrays,HashSet<Long> bSet, long start, long end,
			ArrayList<ArrayList<Long>> globalRes) {
		HashSet<ArrayList<Long>> res = new HashSet<ArrayList<Long>>(HASHMAPSIZEBIG);
		if (bType== AUIDFIDCIDJIDRID){//这个不太正常，JsonArray是第二个的，对的aset是第四个的
			int sizeOfresultArrays = resultArrays.length;
			ArrayList<Long> oneRes = null;
			for (int i = 0; i < sizeOfresultArrays; i++){
				JSONArray resultArray = (JSONArray) resultArrays[i];
				int sizeOfresultArray = resultArray.size();
				for(int ii = 0;ii < sizeOfresultArray;ii++){
	        		JSONObject imformation = (JSONObject)(resultArray.get(ii));
					JSONArray ridArray = (JSONArray)(imformation).get("RId");
	        		long id = (long) imformation.get("Id");
	        		for (Object ridObj:ridArray){
	        			long rid = (long)ridObj;
	        			if(aSet.contains(rid)){
	        				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(id);
							oneRes.add(rid);
							oneRes.add(end);
							res.add(oneRes);
	        			}
	        		}
		    		JSONArray aa = (JSONArray)imformation.get("AA");
		    		int sizeOfaa = aa.size();
		    		for(int i3 = 0;i3 < sizeOfaa;i3++){
		    			long auid = (long)((JSONObject)aa.get(i3)).get("AuId");
		    			if(aSet.contains(auid)){
		    				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(id);
							oneRes.add(auid);
							oneRes.add(end);
							res.add(oneRes);
		    			}
		    		}
		    		JSONArray f = (JSONArray)imformation.get("F");
		    		if (f!=null){
		    			int sizeOff = f.size();
		    			for(int i3 = 0;i3 < sizeOff;i3++){
		        			long fid = (long)((JSONObject)f.get(i3)).get("FId");
		        			if(aSet.contains(fid)){
		        				oneRes = new ArrayList<Long>(4);
								oneRes.add(start);
								oneRes.add(id);
								oneRes.add(fid);
								oneRes.add(end);
								res.add(oneRes);
		        			} 
		        		}
		    		}
		    		JSONObject c = (JSONObject)imformation.get("C");
		    		if (c!=null){
		    			long cid = (long)c.get("CId");
		    			if(aSet.contains(cid)){
		    				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(id);
							oneRes.add(cid);
							oneRes.add(end);
							res.add(oneRes);
		    			} 
		    		} 
		    		JSONObject j = (JSONObject)imformation.get("J");
		    		if (j!=null){
		    			long jid = (long)j.get("JId");
		    			if(aSet.contains(jid)){
		    				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(id);
							oneRes.add(jid);
							oneRes.add(end);
							res.add(oneRes);
		    			}
		    		} 
	        	}
			}
		} else if (aType == AUID){
			int sizeOfresultArrays = resultArrays.length;
			ArrayList<Long> oneRes = null;
			for (int i = 0; i < sizeOfresultArrays; i++){
				JSONArray resultArray = (JSONArray) resultArrays[i];
				int sizeOfresultArray = resultArray.size();
				for(int ii = 0;ii < sizeOfresultArray;ii++){
	        		JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(ii))).get("AA");
	        		int sizeOfaa = aa.size();
	        		for(int iii = 0;iii < sizeOfaa;iii++){
	        			long auid = (long)((JSONObject)aa.get(iii)).get("AuId");
	        			if (aSet.contains(auid) && ((JSONObject)aa.get(iii)).get("AfId")!=null){
	        				long afid = (long) ((JSONObject)aa.get(iii)).get("AfId");
	        				if(bSet.contains(afid)){
	        					oneRes = new ArrayList<Long>(4);
								oneRes.add(start);
								oneRes.add(auid);
								oneRes.add(afid);
								oneRes.add(end);
								res.add(oneRes);
	        				}	
	        			}
	        		}
	        	}
			}
		} else {
			int sizeOfresultArrays = resultArrays.length;
			ArrayList<Long> oneRes = null;
			for (int i = 0; i < sizeOfresultArrays; i++){
				JSONArray resultArray = (JSONArray) resultArrays[i];
				int sizeOfresultArray = resultArray.size();
				for(int ii = 0;ii < sizeOfresultArray;ii++){
	        		JSONObject currentObject = (JSONObject)(resultArray.get(ii));
					JSONArray ridArray = (JSONArray)(currentObject).get("RId");
	        		long id = (long) currentObject.get("Id");
	        		for (Object ridObj:ridArray){
	        			long rid = (long)ridObj;
	        			if(aSet.contains(rid)){
	        				oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(id);
							oneRes.add(rid);
							oneRes.add(end);
							res.add(oneRes);
	        			}
	        		}
	        	}
			}
		}
		globalRes.addAll(res);
	}
	private static void GetBFromAAndAddSomeResult(int aType, long end, int bType, JSONArray resultArray, long start,
			HashMap<Long, HashSet<Long>> ridMap, HashSet<Long> thread1ReturnIdValue, ArrayList<ArrayList<Long>> globalRes) {
		HashSet<ArrayList<Long>> res = new HashSet<ArrayList<Long>>(HASHMAPSIZEBIG);
		ArrayList<Long> oneRes = new ArrayList<Long>();
    	int sizeOfresultArray = resultArray.size();
		for(int i = 0;i<sizeOfresultArray;i++){
    		long id = (long) ((JSONObject)resultArray.get(i)).get("Id");
    		if (ridMap.containsKey(id)){
    			HashSet<Long> secondNodeSet = ridMap.get(id);
    			for(long secondNode : secondNodeSet){
					oneRes = new ArrayList<Long>(4);
					oneRes.add(start);
					oneRes.add(secondNode);
					oneRes.add(id);
					oneRes.add(end);
					res.add(oneRes);
				}
    		}
    		if(thread1ReturnIdValue.contains(id)){
    			oneRes = new ArrayList<Long>(3);
    			oneRes.add(start);
            	oneRes.add(id);
        		oneRes.add(end);
        		res.add(oneRes);
    		}
    	}
		globalRes.addAll(res);
	}
	private static void GetBFromASetForMultiThreadsAndAddSomeResultReverse(int aType, HashSet<Long> aSet,
			int bType, JSONArray[] resultArrays,HashSet<Long> bSet, long start, long end,
			ArrayList<ArrayList<Long>> globalRes) {
		HashSet<ArrayList<Long>> res = new HashSet<ArrayList<Long>>(HASHMAPSIZEBIG);
		ArrayList<Long> oneRes = null;
			int sizeOfresultArrays = resultArrays.length;
			for (int i = 0; i < sizeOfresultArrays; i++){
			JSONArray resultArray = (JSONArray) resultArrays[i];
			int sizeOfresultArray = resultArray.size();;
			for(int ii = 0;ii < sizeOfresultArray;ii++){
        		JSONArray aa = (JSONArray)((JSONObject)(resultArray.get(ii))).get("AA");
        		int sizeOfaa = aa.size();
        		for(int iii = 0;iii < sizeOfaa;iii++){
        			long auid = (long)((JSONObject)aa.get(iii)).get("AuId");
        			if (aSet.contains(auid) && ((JSONObject)aa.get(iii)).get("AfId")!=null){
        				long afid = (long) ((JSONObject)aa.get(iii)).get("AfId");
        				if(bSet.contains(afid)){
        					oneRes = new ArrayList<Long>(4);
							oneRes.add(start);
							oneRes.add(afid);
							oneRes.add(auid);
							oneRes.add(end);
							res.add(oneRes);
        				}
        			}
        		}
        	}
		}
		globalRes.addAll(res);
	}
	static public void main (String[] args){
//		long a = System.currentTimeMillis();
//		System.out.println(bfs(2107710616,2128635872).size());
//		long b = System.currentTimeMillis();
//		System.out.println(b-a);
//		System.out.println(bfs(2292217923L,2100837269));//null	pass!
//		System.out.println(bfs(2020842694,2093390569).size());//Id->Id:2020842694->2093390569	pass!
//		System.out.println(bfs(2020842694,1971666241).size());//Id->Id->Id:2020842694->2093390569->1971666241	pass!
//		System.out.println(bfs(2020842694,2020842694).size());//Id->F/C/Au->Id:2020842694->65965080/1140684652/2139109562->2020842694	pass!
//		System.out.println(bfs(2020842694,237088).size());//Id->Id->Id->Id	[2020842694, 2093390569, 1971666241, 237088]	pass!
//		System.out.println(bfs(1880262756,2020842694).size());//Id->Id->F/C/Au->Id[1880262756, 2020842694, 65965080, 2020842694] [1880262756, 2020842694, 1140684652, 2020842694] [1880262756, 2020842694, 2139109562, 2020842694]	pass!
//		System.out.println(bfs(2162915993L,1880262756L).size());//Id->Id->J->Id[2162915993, 1880262756, 118988714, 1880262756]	pass!
//		System.out.println(bfs(2020842694L,2093390569L).size());//Id->F/C/Au->Id->Id [2020842694, 65965080, 2020842694, 2093390569] [2020842694, 1140684652, 2020842694, 2093390569] [2020842694, 2139109562, 2020842694, 2093390569] pass!
//		System.out.println(bfs(1880262756L,41216786).size());//Id->J->Id->Id [1880262756, 118988714, 1880262756, 41216786] pass!
//		System.out.println(bfs(1880262756L,2289542319L).size());//Id-AuId [1880262756, 2289542319] pass!
//		System.out.println(bfs(2097726431L,2289542319L).size());//Id->Id->AuId [2097726431, 1880262756, 2289542319]pass!
//		System.out.println(bfs(1880262756L,2289542319L).size());//Id->AuId->AfId->AuId [1880262756, 2289542319, 20089843, 2289542319] pass!
//		System.out.println(bfs(2020842694L,2139109562L).size());//Id->F/C/Au->Id->AuId[2020842694, 65965080, 2020842694, 2139109562] [2020842694, 1140684652, 2020842694, 2139109562] [2020842694, 2139109562, 2020842694, 2139109562] pass!
//		System.out.println(bfs(1880262756L,2139109562L).size());//Id->J->Id->AuId[1880262756, 118988714, 1880262756, 2139109562] pass!
//		System.out.println(bfs(2020842694L,103064199L).size());//Id->Id->Id->AuId[2020842694, 2093390569, 1971666241, 103064199]	pass!
//		System.out.println(bfs(2289542319L,2289542319L).size());//AuId->Id/AfId->AuId[2289542319, 95457486, 2289542319] [2289542319, 1880262756, 2289542319] pass!
//		System.out.println(bfs(2289542319L,2004554093L).size());//AuId->Id->Id->AuId[2289542319, 1880262756, 2147152072, 2004554093] pass!
//		System.out.println(bfs(2289542319L,1880262756L).size());//AuId->Id [2289542319, 1880262756] pass!
//		System.out.println(bfs(2289542319L,2147152072L).size());//AuId->Id->Id[2289542319, 1880262756, 2147152072] pass!
//		System.out.println(bfs(2289542319L,1880262756L).size());//AuId->AfId->AuId->Id [2289542319, 20089843, 2289542319, 1880262756] pass!
//		System.out.println(bfs(2289542319L,2000215628L).size());//AuId->Id->Id->Id[2289542319, 1880262756, 2147152072, 2000215628] pass!
//		System.out.println(bfs(2139109562,2020842694).size());//AuId->Id->F/C/Au->Id[2139109562, 2020842694, 65965080, 2020842694] [2139109562, 2020842694, 1140684652, 2020842694] [2139109562, 2020842694, 2139109562, 2020842694]	pass!
//		System.out.println(bfs(2139109562L,1880262756L).size());//AuId->Id->J->Id[2139109562, 1880262756, 118988714, 1880262756] pass!
//		ArrayList<ArrayList<ArrayList<Long>>> result = new ArrayList<ArrayList<ArrayList<Long>>>();
//		result.add(bfs(2333,6666666));
//		result.add(bfs(2020842694,2093390569));
//		result.add(bfs(2020842694,1971666241));
//		result.add(bfs(2020842694,2020842694));
//		result.add(bfs(1880262756,1880262756));
//		result.add(bfs(2020842694,237088));
//		result.add(bfs(1880262756,2020842694));
//		result.add(bfs(2162915993L,1880262756L));
//		result.add(bfs(2020842694L,2093390569L));
//		result.add(bfs(1880262756L,41216786));
//		result.add(bfs(1880262756L,2289542319L));
//		result.add(bfs(2097726431L,2289542319L));
//		result.add(bfs(1880262756L,2289542319L));
//		result.add(bfs(2020842694L,2139109562L));
//		result.add(bfs(1880262756L,2139109562L));
//		result.add(bfs(2020842694L,103064199L));
//		result.add(bfs(2289542319L,2289542319L));
//		result.add(bfs(2289542319L,2004554093L));
//		result.add(bfs(2289542319L,1880262756L));
//		result.add(bfs(2289542319L,2147152072L));
//		result.add(bfs(2289542319L,1880262756L));
//		result.add(bfs(2289542319L,2000215628L));
//		result.add(bfs(2139109562,2020842694));
//		result.add(bfs(2139109562L,1880262756L));
//		ArrayList<resultList> resultSerlializable = new ArrayList<resultList>();
//		for(ArrayList<ArrayList<Long>> i:result){
//			resultList a = new resultList();
//			copyFromArryaListToResultList(i, a);
//			resultSerlializable.add(a);
//		}
//		//write to file resultList
////		try{
////			FileOutputStream fw = new FileOutputStream("resultList");
////			ObjectOutputStream bw = new ObjectOutputStream(fw);
////			
////			for (resultList p: resultSerlializable) {
////				bw.writeObject(p);
////			}
////			bw.close();
////		}
////		catch (IOException ex){
////			JOptionPane.showMessageDialog(null, "Failed to save", "Warning", JOptionPane.ERROR_MESSAGE);
////		}
////		read from file resultList
//		ArrayList<resultList> fetchList = new ArrayList<resultList>();
//		try{
//			FileInputStream fi = new FileInputStream("resultList");
//			ObjectInputStream oi = new ObjectInputStream(fi);
//			while(true){
//				try{
//					 fetchList.add((resultList)oi.readObject());
//					
//				}catch (EOFException ee) {
//					break;
//				}
//				catch (ClassNotFoundException e1){
//					JOptionPane.showMessageDialog(null, "Failed to load", "Warning", JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		}
//		catch (IOException ex){
//			JOptionPane.showMessageDialog(null, "Failed to read", "Warning", JOptionPane.ERROR_MESSAGE);
//		}
//		//compare data from internet with old version program
//		if(resultSerlializable.size()!=fetchList.size())
//		{
//			System.err.println("Not same");
//		}
//		else {
//			for(int i=0;i<resultSerlializable.size();i++)
//			{	
//				boolean flag=true;
//				if (resultSerlializable.get(i).size() != fetchList.get(i).size()){
//					System.err.println("Not same");
//					flag=false;
//				}
//				for(ArrayList<Long> ii: resultSerlializable.get(i)){
//					if(fetchList.get(i).contains(ii)==false)
//					{
////						System.err.println("Not same");
////						System.out.println(ii);
//						flag=false;
//					}			
//				}
//				if(flag)
//					System.out.println((i+1)+": pass");
//			}	
//		}	
//	}
//	public static void copyFromArryaListToResultList(ArrayList<ArrayList<Long>> a, resultList b){
//		for(ArrayList<Long> i:a){
//			b.add(i);
//		}
	}
}
//class resultList extends ArrayList<ArrayList<Long>> implements Serializable {
//	public boolean add(ArrayList<Long> o){return super.add(o);}
//	public boolean remove(ArrayList<Long> o){return super.remove(o);}
//	public int size(){return super.size();}
//}