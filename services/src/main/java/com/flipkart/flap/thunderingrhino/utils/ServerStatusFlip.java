package com.flipkart.flap.thunderingrhino.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by suchi.sethi on 01/10/15.
 */

public class ServerStatusFlip implements Runnable{

	private String dataJSONFile;
	private Object obj;


	public ServerStatusFlip(String dataJSONFile) {
		this.dataJSONFile=dataJSONFile;
		System.out.println(this.dataJSONFile);
		JSONParser parser = new JSONParser();


		try {
			obj = parser.parse(new FileReader(this.dataJSONFile));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			sendHttpRequest((JSONObject) this.obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Function sendHttpRequest creates JSON object and json arrays required for
	// all the clusters and calls executeRequest for all the clusters provided in the data_json
	// file
	private void sendHttpRequest(JSONObject obj) throws Exception {
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray clusters = (JSONArray) jsonObject.get("clusters");
		JSONObject appsLocal = (JSONObject) clusters.get(0);

		System.out.println("Send the healthcheck");


		for ( Object appIdLocal : appsLocal.keySet() ) {

			String IaasUrl = "http://10.47.3.204/services/requestjson?appId=" + appIdLocal;
			JSONArray instanceLocal = (JSONArray) appsLocal.get(appIdLocal);
			JSONObject instanceGrpLocal = (JSONObject) instanceLocal.get(0);


			//For each of the instance gropus available locally in the json file
			//call Gethttps which will return the array of ip addresses which belongs to that
			//Instance group
			for ( Object instanceGp : instanceGrpLocal.keySet() ) {
				//Value of port will be fetched for the selected group of a app
				JSONArray portUrlValues = (JSONArray) instanceGrpLocal.get(instanceGp);
				JSONObject configValues = (JSONObject) portUrlValues.get(0);
				ArrayList<String> ipAddress =  retrieveIPAdd(IaasUrl, instanceGp);
				healthStatus(ipAddress, configValues.get("url"),configValues.get("port"), configValues.get("threshold"), instanceGp, appIdLocal);
			}
		}



	}

	// function executeRequest calls the send get method for each server given
	// in a cluster.
	// and checks if the no of system down is greater than or equal to the
	// threshold value.
	// then it sends mail in the slack to the oncall channel
	private void healthStatus(ArrayList listIp,  Object healthUrlLocal, Object healthPortLocal, Object thresholdLocal, Object instGrp, Object appID) throws Exception {
		int system_down = 0;
		for (int i = 0; i < listIp.size(); i++) {
			String healthURl = "http://" + listIp.get(i) + ':'+ healthPortLocal + '/' + healthUrlLocal ;
			System.out.println("healthUrl " + healthURl);
			int Response = sendGethttp(healthURl);
			if (Response != 200) {
				system_down ++;
			}

		}
		System.out.println("system_down" + system_down);

		if (system_down > (long)thresholdLocal) {
			System.out.println("Sending Mail");
			String mail_msg = "https://skadoosh-flipkart.slack.com/api/chat.postMessage?username==Zeng&token=xoxp-2549515818-2980580417-3232076475-743b4f&channel=C02HB4BRZ&text="
					+ system_down + "servers+of+AppID+" + appID + "+with+InstanceGrp+" + instGrp + "+are+down,+More+Info:+http://10.47.3.204/";
			sendGethttp(mail_msg);
		}

	}

	// Function:Gethttps sends the https request for checking if any build is running on
	// the server returns the response Message.

	@SuppressWarnings("finally")
	ArrayList<String> retrieveIPAdd(String url, Object grpLocal) throws Exception {

		String responseString = null;
		ArrayList<String> listIpAddress = new ArrayList<String>();

		try {

			URL obj = new URL(url);

			BufferedReader in = new BufferedReader(new InputStreamReader(obj.openStream()));
			String inputLine;


			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			responseString = response.toString();
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(responseString);
			if (resultObject instanceof JSONArray) {
				JSONArray array = (JSONArray) resultObject;
				for (Object object : array) {
					JSONObject iaasResponse = (JSONObject) object;
					if( iaasResponse.containsKey("instance_group")) {

						String instanceGrp = (String) iaasResponse.get("instance_group");
						String instanceGrpLocal = (String) grpLocal;

						if (instanceGrp.contains(instanceGrpLocal)) {
							listIpAddress.add((String) iaasResponse.get("primary_ip"));
						}
					}


				}
			}


		} catch (java.io.FileNotFoundException e) {
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			return listIpAddress;
		}

	}

	// Function:sendGethttp sends the http request of checking the health of the
	// servers and  return the response code.
	// HTTP GET request
	@SuppressWarnings("finally")
	int sendGethttp(String url) throws Exception {
		int responseCode = 0;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			responseCode = con.getResponseCode();

		} catch (java.io.FileNotFoundException e) {
		} finally {
			return responseCode;
		}

	}


}
