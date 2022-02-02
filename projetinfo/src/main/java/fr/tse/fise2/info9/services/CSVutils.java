package fr.tse.fise2.info9.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * CSVutils : Classe utilisée dans les classes CreateProjectMenu, Group et
 * GroupLot Rôle : fournir une méthode permettant de lire un fichier .csv
 *
 */
public class CSVutils {
	/**
	 * Méthode pour lire les fichiers csv
	 * @param pathFile : chemin pù se trouve le fichier
	 * @param nbCol : indice de la colonne qui nous intéresse
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String[][] readCSV(String pathFile, int nbCol) {
		String line = " "; // String pour récupérer les données ligne par ligne les données
		String sep = "[,;]"; // Séparateurs autorisés : virgule et point-virgule

		ArrayList<String[]> arrayData = new ArrayList<String[]>(); // contiendra les données

		// au cas où le fichier serait introuvable (amélioration possible car fonction
		// fileFound ci-dessous)
		try {

			// Buffer pour lire le fichier
			BufferedReader br = new BufferedReader(new FileReader(pathFile));

			// tant qu'on ne parcourt pas tout le fichier
			while ((line = br.readLine()) != null) {
				String[] dataline = line.split(sep); // d�coupe les lignes avec les s�parateurs
				arrayData.add(dataline);// remplit l'arraylist
			}

			// les données doivent être sous la forme String [][]
			String data[][] = new String[arrayData.size()][nbCol];
			for (int j = 0; j < arrayData.size(); j++) {
				data[j] = arrayData.get(j);
			}

			return data;

		} catch (FileNotFoundException e) {
			e.printStackTrace(); // exception fichier non trouvé
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/** 
	 * Fonction renvoyant true si le fichier est trouvé à l'adresse indiquée et false sinon
	 * @param path
	 * @return
	 */
	@SuppressWarnings({ "unused", "resource" })
	public static Boolean fileFound(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		// readCSV("C:\\Users\\Utilisateur\\Desktop\\fichiers_test\\fichierCorrect.csv",3);
		// readCSV("C:\\Users\\Utilisateur\\Desktop\\fichiers_test\\test.csv",4);
	}
}