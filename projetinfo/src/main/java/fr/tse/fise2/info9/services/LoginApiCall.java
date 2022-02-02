package fr.tse.fise2.info9.services;

import java.io.IOException;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;

/**
 * Cette classe contient tous les éléments statiques de chaque connexion : l'instance gitlabapi par la biblio gitlab4j et
 * les identifiants de l'utilisateur connecté.
 */

public class LoginApiCall {
	private static GitLabApi gitLabApi;
	private static String own_username;
	private static String own_ip;
	private static String own_password;
	
	/**
	 * Cette fonction statique renvoie l'instance statique de gitlabapi pour que toutes les autres classes peuvent l'utiliser sans conflits.
	 * 
	 * @param username
	 * @param password
	 * @param ip
	 * @return GitLabApi
	 * @throws IOException
	 */
	public static GitLabApi gitlabConnection(String username, String password, String ip) throws IOException {
		try {
			own_ip = ip;
			setGitLabApi(GitLabApi.oauth2Login(ip + "/", username, password));
			setUsername(username);
			setPassword(password);
			return getGitLabApi();
		} catch (GitLabApiException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Tous les getters et setters des variables de la classe.
	public static GitLabApi getGitLabApi() {
		return gitLabApi;
	}
	public static void setGitLabApi(GitLabApi gitLabApi) {
		LoginApiCall.gitLabApi = gitLabApi;
	}
	
	public static String getUsername() {
		return own_username;
	}
	
	public static String getIP() {
		return own_ip;
	}
	
	public static void setUsername(String username) {
		LoginApiCall.own_username = username;
	}
	public static String getPassword() {
		return own_password;
	}
	public static void setPassword(String own_password) {
		LoginApiCall.own_password = own_password;
	}
	
}