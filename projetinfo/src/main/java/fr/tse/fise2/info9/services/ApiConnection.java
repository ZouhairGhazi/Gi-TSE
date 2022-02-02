package fr.tse.fise2.info9.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Cette classe établie une connection directe avec le GitLab api en utilisant une variable de type HttpURLConnection
 * pour génerer un access_token de la connection et récuperer les informations d'un projet
 */

public class ApiConnection {

	// Variable static pour représenter la connection à établir
	private static HttpURLConnection connection;

	/**
	 * Cette fonction s'occupe de générer un access_token pour une connection en se basant sur les identifiants fournis par l'utilisateur
	 * 
	 * @param nomPrenom
	 * @param mdp
	 * @return String
	 * @throws IOException
	 */
	public static String generateAccessToken(String nomPrenom, String mdp) throws IOException {
		String access_token = "";
		URL url = new URL(LoginApiCall.getIP() + "/oauth/token");
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("POST");
		String jsonInputString = "{ \"grant_type\"	:\"password\",	\"username\" :\" " + nomPrenom
				+ "\",	\"password\"	:\"" + mdp + "\"}";

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("UTF-8");
			os.write(input, 0, input.length);
		}
		connection.setConnectTimeout(10000);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			JSONObject obj = new JSONObject(response.toString());
			access_token = obj.getString("access_token");
			br.close();
		}
		return access_token;

	}
	
	/**
	 * Cette fonction récupere des informations/statistiques sur un projet par son nom.
	 * 	 
	 * @param access_token
	 * @param name
	 * @return Map<String, String>
	 * @throws IOException
	 */

	public static Map<String, String> getProjectInfo(String access_token, String name) throws IOException {

		Map<String, String> map = new HashMap<>();
		URL url = new URL(LoginApiCall.getIP() + "/api/v4/projects?access_token=" + access_token
				+ "&membership=true&statistics=true");
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");

		connection.setConnectTimeout(10000);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			br.close();
			JSONArray arr = new JSONArray(response.toString());
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				if (obj.get("name").toString().equals(name)) {
					map.put("id", obj.get("id").toString());
					map.put("name", obj.get("name").toString());
					map.put("visibility", obj.get("visibility").toString());
					map.put("description", obj.get("description").toString());
					map.put("archived", obj.get("archived").toString());
					map.put("creation_date", obj.get("created_at").toString().substring(0, 10));
					map.put("devops", obj.get("auto_devops_enabled").toString());
					map.put("forks_count", obj.get("forks_count").toString());
					map.put("star_count", obj.get("star_count").toString());
					map.put("issues_enabled", obj.get("issues_enabled").toString());
					map.put("merge_requests_enabled", obj.get("merge_requests_enabled").toString());
				}
			}
		}
		return map;
	}

	public static Map<String, String> getProjectsInfo(String access_token) throws IOException {

		Map<String, String> map = new HashMap<>();
		//Map<String, Projet> mapProj = new HashMap<>();
		URL url = new URL(LoginApiCall.getIP() + "/api/v4/projects?access_token=" + access_token
				+ "&membership=true&statistics=true");
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(10000);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
				// System.out.println(responseLine.trim()+"\n");
			}
			br.close();

			JSONArray arr = new JSONArray(response.toString());
			
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				map.put("id", obj.get("id").toString());
				map.put("name", obj.get("name").toString());
				map.put("visibility", obj.get("visibility").toString());
				map.put("description", obj.get("description").toString());
				map.put("archived", obj.get("archived").toString());
				map.put("creation_date", obj.get("created_at").toString().substring(0, 10));
				map.put("devops", obj.get("auto_devops_enabled").toString());
				map.put("forks_count", obj.get("forks_count").toString());
				map.put("star_count", obj.get("star_count").toString());
				map.put("issues_enabled", obj.get("issues_enabled").toString());
				map.put("merge_requests_enabled", obj.get("merge_requests_enabled").toString());
			}

		}
		return map;
	}
}