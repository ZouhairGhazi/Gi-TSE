package fr.tse.fise2.info9.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import com.google.gson.Gson;

/**
 * 
 * Classe DataProject 
 * Rôle : gérer les données concernant les projets,
 * concerne l'onglet visionner projet
 *
 */
public class DataProject {
	private int nbChamps;
	private static float nbActifs;
	private static float commitMoy;
	private static float mergeMoy;
	private static float membersMoy;
	private static float commitMin;
	private static float mergeMin;
	private static float membersMin;
	private static float commitMax;
	private static float mergeMax;
	private static float membersMax;
	private static String data[][];
	private static Map<String, Project> liste;

	/**
	 * Constructeur
	 */
	public DataProject() {
		liste = new HashMap<String, Project>();
		nbChamps = 6;
		nbActifs = 0;
		commitMoy = 0;
		mergeMoy = 0;
		membersMoy = 0;
		commitMin = Integer.MAX_VALUE;
		mergeMin = Integer.MAX_VALUE;
		membersMin = Integer.MAX_VALUE;
		commitMax = 0;
		mergeMax = 0;
		membersMax = 0;
	}

	/**
	 * Méthode getDataAPI récupérant les données issues de l'API gitlab4j
	 * @return Tableau à deux dimensions de string
	 */
	public String[][] getDataAPI() {
		//interface permettant d'intéragir avec gitlab
		GitLabApi git = LoginApiCall.getGitLabApi();

		try {
			// Liste de projets dont l'utilisateur est membre
			List<Project> projects = git.getProjectApi().getMemberProjects();
			

			int n = 0;	// pour parcourir l'ensemble des projets
			int m = 0;
			// Le tableau qui contiendra l'ensemble des données
			data = new String[projects.size()][nbChamps + 1];

			// Pour chaque projet, on extraie les données recherchées
			while (n < projects.size()) {
				// liste est une liste composée d'un ensemble de noms (de projet permettant de l'identifier) et de projets
				liste.put(projects.get(n).getName(), projects.get(n));
				
				// Le nom
				data[m][0] = projects.get(n).getName();

				// La date de création du projet, on utilise la classe Calendar
				Calendar date = Calendar.getInstance();
				date.setTime(projects.get(n).getCreatedAt());
				int month=date.get(Calendar.MONTH)+1;
				StringBuilder createDateString=new StringBuilder() ;
				createDateString.append((date.get(Calendar.DATE) < 10 ? "0" + date.get(Calendar.DATE)
				: date.get(Calendar.DATE)) + "/"
				+ (month < 10 ? "0" + month : month)
				+ "/" + date.get(Calendar.YEAR) + " "
				+ (date.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + date.get(Calendar.HOUR_OF_DAY) : date.get(Calendar.HOUR_OF_DAY)) + ":"
				+ (date.get(Calendar.MINUTE) < 10 ? "0" + date.get(Calendar.MINUTE) : date.get(Calendar.MINUTE))+ " ");

				data[m][1] = createDateString.toString();
				date.setTime(projects.get(n).getLastActivityAt());
				month=date.get(Calendar.MONTH)+1;
				StringBuilder dateString=new StringBuilder() ;
				dateString.append ((date.get(Calendar.DATE) < 10 ? "0" + date.get(Calendar.DATE)
						: date.get(Calendar.DATE)) + "/"
						+ (month < 10 ? "0" + month : month)
						+ "/" + date.get(Calendar.YEAR) + " "
						+ (date.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + date.get(Calendar.HOUR_OF_DAY) : date.get(Calendar.HOUR_OF_DAY)) + ":"
						+ (date.get(Calendar.MINUTE) < 10 ? "0" + date.get(Calendar.MINUTE) : date.get(Calendar.MINUTE))+ " ");

				data[m][2] = dateString.toString();
				// Le nombre de commits (en r�cup�rant la taille de la liste de commits)
				data[m][3] = Integer.toString(git.getCommitsApi().getCommits(projects.get(n)).size());

				// Le nombre de mergees (en r�cup�rant la taille de la liste de mergees)
				data[m][4] = Integer.toString(git.getMergeRequestApi().getMergeRequests(projects.get(n)).size());// getmergees(projects.get(n)).size());

				// Le nombre de membres (en r�cup�rant la taille de la liste de membres)
				data[m][5] = Integer.toString(git.getProjectApi().getAllMembers(projects.get(n)).size());

				if (!projects.get(n).getDescription().contains("Archive -")) {
					// TODO ne plus avoir besoin de cette classe sauf pour remplir le fichier cache
					commitMoy += Integer.parseInt(data[m][3]);
					if (Integer.parseInt(data[m][3]) < commitMin)
						commitMin = Integer.parseInt(data[m][3]);
					if (Integer.parseInt(data[m][3]) > commitMax)
						commitMax = Integer.parseInt(data[m][3]);
					mergeMoy += Integer.parseInt(data[m][4]);
					if (Integer.parseInt(data[m][4]) < mergeMin)
						mergeMin = Integer.parseInt(data[m][4]);
					if (Integer.parseInt(data[m][4]) > mergeMax)
						mergeMax = Integer.parseInt(data[m][4]);
					membersMoy += Integer.parseInt(data[m][5]);
					if (Integer.parseInt(data[m][5]) < membersMin)
						membersMin = Integer.parseInt(data[m][5]);
					if (Integer.parseInt(data[m][5]) > membersMax)
						membersMax = Integer.parseInt(data[m][5]);
					nbActifs++;
				}

				data[m][6] = projects.get(n).getDescription();
				m++;

				n++;

			}
			data = Arrays.copyOf(data, m);

			commitMoy = commitMoy / nbActifs;
			mergeMoy = mergeMoy / nbActifs;
			membersMoy /= nbActifs;

			return data;

		} catch (GitLabApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}

	}

	
	/**
	 * Mise en place d'une méthode pour générer des fichiers json lors de la récupération des données des projets
	 * non utilisées dans le code
	 */
	public void newDataFile() { 
		String jsonString = new String();
		String access_token = LoginApiCall.getGitLabApi().getAuthToken();
		try {
			Map<String, String> mapInfo = ApiConnection.getProjectsInfo(access_token);
			Gson gson = new Gson();
			jsonString = gson.toJson(mapInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			File fichier = new File("info.txt");
			// créer le fichier s'il n'existe pas
			if (!fichier.exists()) {
				fichier.createNewFile();
			}

			FileWriter fw = new FileWriter(fichier.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(jsonString);
			bw.close();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode dataFiles : gère la création des fichiers caches data.txt et archives.txt contenant les données des projets pour la vue d'ensemble
	 * @param d : tableau à deux dimensions de String
	 */
	public void dataFiles(String d[][]) {
		String nomFichier = new String("data.txt");
		String nomArchives = new String("archives.txt");
		StringBuilder chaine = new StringBuilder();
		StringBuilder chaineA = new StringBuilder();
		chaine.append(commitMoy + ";" + mergeMoy + ";" + membersMoy + "\n");
		chaine.append(commitMin + ";" + mergeMin + ";" + membersMin + "\n");
		chaine.append(commitMax + ";" + mergeMax + ";" + membersMax + "\n");
		chaineA.append(commitMoy + ";" + mergeMoy + ";" + membersMoy + "\n");
		chaineA.append(commitMin + ";" + mergeMin + ";" + membersMin + "\n");
		chaineA.append(commitMax + ";" + mergeMax + ";" + membersMax + "\n");
		for (int i = 0; i < d.length; i++) {
			if (d[i][6].contains("Archive -")) {
				for (int j = 0; j < nbChamps; j++) {
					if (j == nbChamps - 1)		//dernier champs sans point virgule
						chaineA.append(d[i][j]);
					else
						chaineA.append(d[i][j] + ";");	//point virgule entre les champs
				}
				chaineA.append("\n"); 	//retour à la ligne
			} else {
				for (int j = 0; j < nbChamps; j++) {
					if (j == nbChamps - 1)
						chaine.append(d[i][j]);
					else
						chaine.append(d[i][j] + ";");
				}
				chaine.append("\n");
			}

		}
		try {
			File fichier = new File(nomFichier);
			// créer le fichier s'il n'existe pas
			if (!fichier.exists()) {
				fichier.createNewFile();
			}

			FileWriter fw = new FileWriter(fichier.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(chaine.toString());
			bw.close();

			fichier = new File(nomArchives);
			// créer le fichier s'il n'existe pas
			if (!fichier.exists()) {
				fichier.createNewFile();
			}
			fw = new FileWriter(fichier.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(chaineA.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode addProject : permet d'ajouter les infos d'un projet dans le fichier passer en paramètres (possibilité d'extraire les données d'un autre fichier)
	 * @param projet
	 * @param filename
	 * @param fromfilename
	 */
	public void addProject(Project projet,String filename,String fromfilename) {
		File fichier = new File(filename);
		// créer le fichier s'il n'existe pas
		if ((fichier.exists()) &&(fromfilename!=null)){
			try {		
				String d[][]=getDataTable(fromfilename);
				String data[]=new String[5];
				boolean trouve=true;
				int i=0;
				while((d.length>i)&&(trouve)) {
					if (d[i][0].equals(projet.getName())) {
						for(int j=0;j<5;j++) {
							data[j]=d[i][j+1];
						}
					}
					i++;
				}
				FileWriter fw = new FileWriter(fichier.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(projet.getName() + ';' + data[0]+";" +data[1] + ";"+data[2]+";"+data[3]+";"+data[4]+"\n");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Méthode removeProject permettant d'enlever un projet d'un fichier (passé en paramètre)
	 * @param projet
	 * @param filename
	 */
	@SuppressWarnings("resource")
	public void removeProject(Project projet, String filename) {
		File fichier = new File(filename);
		StringBuilder liste = new StringBuilder();
		String strCurrentLine;
		// créer le fichier s'il n'existe pas
		if (fichier.exists()) {
			try {
				//fichier.createNewFile();
				BufferedReader objReader = new BufferedReader(new FileReader(filename));
				while ((strCurrentLine = objReader.readLine()) != null) {
					if(!strCurrentLine.contains(projet.getName()))
						liste.append(strCurrentLine+"\n");
				}

				FileWriter fw = new FileWriter(fichier.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(liste.toString());
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Méthode retournant le tableau de données
	 * @return
	 */
	public String[][] getTabData() {
		return data;
	}

	/**
	 * Méthode getDataTable retournant les données contenues dans le fichier (en paramètre) sous la forme d'un tableau de String
	 * @param nomFichier
	 * @return
	 */
	@SuppressWarnings("unused")
	public String[][] getDataTable(String nomFichier) {
		// a modif si on ajoute des champs
		// int nbChamps = 7;

		// FileReader file;
		String table[][] = null;
		int nblines = 0;

		boolean fin = true;
		int index = 0;
		BufferedReader objReader = null;

		if (new File(nomFichier).exists()) {
			try {
				LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(nomFichier));
				lineNumberReader.skip(Long.MAX_VALUE);
				nblines = lineNumberReader.getLineNumber();
				lineNumberReader.close();
				table = new String[nblines - 3][nbChamps];

				String strCurrentLine;
				objReader = new BufferedReader(new FileReader(nomFichier));

				while ((strCurrentLine = objReader.readLine()) != null) {
					if (index > 2) {
						table[index - 3] = strCurrentLine.split(";");
					}

					

					index++;
				}

			} catch (

			IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (objReader != null)
						objReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return table;
	}

	/**
	 * Méthode getDataSpider : retourne les données nécessaires à la création du spider chart d'un projet
	 * @param nameProj
	 * @param nomFichier
	 * @return
	 */
	@SuppressWarnings("unused")
	public String[][] getDataSpider(String nameProj, String nomFichier) {
		int nblines = 0;
		String returned[] = { "name" };
		String moyenne[] = { "moy" };
		String minimum[] = { "moy" };
		String maximum[] = { "moy" };
		String valfin[][] = new String[4][nbChamps];
		boolean fin = true;
		int index = 0;

		BufferedReader objReader = null;
		try {
			LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(nomFichier));
			lineNumberReader.skip(Long.MAX_VALUE);
			nblines = lineNumberReader.getLineNumber();
			lineNumberReader.close();

			String strCurrentLine;
			objReader = new BufferedReader(new FileReader(nomFichier));

			while (((strCurrentLine = objReader.readLine()) != null) && (fin)) {


				returned = strCurrentLine.split(";");
				if (index == 0) {
					moyenne = returned;
				}
				if (index == 1) {
					minimum = returned;
				}
				if (index == 2) {
					maximum = returned;
				}
				if (nameProj.equals(returned[0])) {
					fin = false;
				}
				index++;

			}
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (objReader != null)
					objReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < moyenne.length; i++)
			valfin[0][i] = moyenne[i];
		for (int i = 0; i < minimum.length; i++)
			valfin[1][i] = minimum[i];
		for (int i = 0; i < maximum.length; i++)
			valfin[2][i] = maximum[i];
		for (int i = 0; i < returned.length; i++)
			valfin[3][i] = returned[i];
		return valfin;
	}

	/**
	 * Méthode getProjectList : renvoie la liste des projets sous forme : nom, projet
	 * @return
	 */
	public Map<String, Project> getProjectList() {
		return liste;

	}

}
