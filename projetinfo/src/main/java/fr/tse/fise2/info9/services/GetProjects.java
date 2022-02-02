package fr.tse.fise2.info9.services;

import java.util.List;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

/**
 * Classe GetProjects permettant d'intéragir avec les projets (récupérer la liste des projets)
 */
public class GetProjects {
	public static List<Project> memberProjects(GitLabApi gitLabApi){
		try {
			List<Project> projectList = LoginApiCall.getGitLabApi().getProjectApi().getMemberProjects();
			
			return projectList;
		} catch (GitLabApiException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
