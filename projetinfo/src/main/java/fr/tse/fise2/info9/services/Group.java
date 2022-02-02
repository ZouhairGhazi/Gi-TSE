package fr.tse.fise2.info9.services;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe utilisée pour manipuler les données du fichier CSV contenant les infos du groupe avant sa création sur gitlab.
 *
 */
public class Group{
	private List<Person>  groupe = new ArrayList<Person>(); //liste de personnes qui compose le groupe
	private String name;  //nom du groupe
	private String[][] data;  //données du groupes sous forme de tableau
	
	
	
	
	/**
	 * Constructeur de groupe à partir de son nom et d'un lien vers un fichier csv
	 * Le constructeur crée le groupe  partir de son nom et d'un path et va chercher les infos dans le fichier csv correspondant
	 * 
	 * @param name : String
	 * @param path : String
	 */
	public Group(String name, String path) {
		super();
		this.name = name;
		
		//on lit les donnes contenues dans le fichier csv
		data=CSVutils.readCSV(path,3);
		
		
		//On crée de nouvelles personnes qu'on ajoute au groupe
		for(int i=0; i<data.length; i++)
		{
			Person temp = new Person(null,null,null);
			temp.setPseudo(data[i][0]);
			temp.setName(data[i][1]);
			temp.setEmail(data[i][2]);
			groupe.add(temp);
		}
	}
	
	
	/**
	 * Surcharge du constructeur qui prend en paramètre un nom et un tableau de données déjà rempli.
	 * 
	 * Utilisé lors de la création de lots de groupes.
	 * 
	 * @param name : String
	 * @param data : String[][]
	 */
	public Group(String name, String[][] data) {
		super();
		this.name = name;
		this.data = data;
		
		//On crée de nouvelles personnes qu'on ajoute au groupe
				for(int i=0; i<data.length; i++)
				{
					Person temp = new Person(null,null,null);
					temp.setPseudo(data[i][0]);
					temp.setName(data[i][1]);
					temp.setEmail(data[i][2]);
					groupe.add(temp);
				}
	}
	
	/**
	 * Méthode pour ajouter des membres dans un groupe déjà existant.
	 * 
	 * @param pseudo : String
	 * @param name : String
	 * @param email : String
	 * @param size : int
	 */
	public void addPerson(String pseudo,String name, String email,int size){
		
		//ajout de la personne à la liste de personnes
		this.getGroupe().add(new Person(pseudo,name,email));
		
		//ajout des données de la personne dans un tableau plus grand qui remplace le tableau précédent
		String[][] tempdata = new String[size+1][3];
		for(int i=0;i<tempdata.length-1;i++) {
			tempdata[i] = getData()[i];
		}
		String[] templine = {pseudo,name,email};
		tempdata[tempdata.length-1] = templine;
		setData(tempdata);
	}
	
	//getter - setters

	/**
	 * Retourne les data d'un groupe
	 * 
	 * @return data : String[][]
	 */
	public String[][] getData() {
		return data;
	}

	/**
	 * Définit les data d'un groupe
	 * 
	 * @param data : String[][]
	 */
	public void setData(String[][] data) {
		this.data = data;
	}

	/**
	 * Retourne la liste de membre d'un groupe
	 * 
	 * @return groupe : List< Person >
	 */
	public List<Person> getGroupe() {
		return groupe;
	}
	
	/**
	 * Retourne le membre du groupe d'index i
	 * 
	 * @param i : int
	 * @return member : Person
	 */
	public Person getGroupe(int i) {
		return groupe.get(i);
	}

	/**
	 * Définit la liste de membre d'un groupe
	 * 
	 * @param groupe : List< Person >
	 */
	public void setGroupe(List<Person> groupe) {
		this.groupe = groupe;
	}

	/**
	 * Retourne le nom d'un groupe
	 * 
	 * @returns name : String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Définit le nom d'un groupe
	 * 
	 * @param name : String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retroune le hashcode d'un objet Group
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupe == null) ? 0 : groupe.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Retourne un booléen qui définit si les deux objets sont égaux
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (groupe == null) {
			if (other.groupe != null)
				return false;
		} else if (!groupe.equals(other.groupe))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}	
