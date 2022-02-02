package fr.tse.fise2.info9.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * Classe utilisée pour manipuler les données du fichier CSV contenant les infos d'un lot de groupes avant leur création sur gitlab.
 *
 */
public class GroupLot{
	private List<Group>  groupes = new ArrayList<Group>(); //liste de groupes du lot de groupe
	private String[][] data; //données du lot de groupe sous forme de tableau
	
	
	/**
	 * Constructeur de lot groupes à partir d'un lien vers un CSV
	 * 
	 * @param path : String
	 */
	public GroupLot(String path) {
		super();
		//lecture du fichier CSV pour récupérer les données
		data = CSVutils.readCSV(path,4);
		
		
		
		boolean newGroup = true; //variable booléenne qui indique si l'on doit créér un nouveau groupe on non
		int existingGroupIndex = -1;  //index du nouveau groupe
		
		//pour chaque ligne du tableau, on cherche à savoir si la personne correspond à un groupe existant dans la liste ou si l'on en crée un nouveau
		//et en fonction du résultat on crée le nouveau groupe ou ajoute simplement la personne au groupe existant
		for(int i=0; i<data.length;i++) {
			for(int j=0;j<groupes.size();j++) {
				if(groupes.get(j).getName() == null) {//si le nom du groupe ne correspond pas à un groupe existant dans la liste
					newGroup = true;
				}
				else if(data[i][0].equals(groupes.get(j).getName())) {//si le nom du groupe correspond à un groupe existant dans la liste
					newGroup = false;
					existingGroupIndex = j;
				}
			}
			
			if (newGroup) { //ajout d'un nouveau groupe à la lise
				String[][] tempdata = new String[1][3];
				String[] templine = {data[i][1],data[i][2],data[i][3]};
				tempdata[0] = templine;
				Group tempGroup = new Group(data[i][0],tempdata);
				groupes.add(tempGroup);
				
			}else{  //ajout de la personne au groupe existant
				groupes.get(existingGroupIndex).addPerson(data[i][1],data[i][2],data[i][3],groupes.get(existingGroupIndex).getData().length);
			}
			newGroup = true;
			existingGroupIndex = -1;
		}
		
	}
	
	// getters - setters

	/**
	 * Retourne la liste des groupes
	 * 
	 * @return groupes : List < Group >
	 */
	public List<Group> getGroupes() {
		return groupes;
	}

	/**
	 * Définit la liste des groupes 
	 * 
	 * @param groupes : List < Group >
	 */
	public void setGroupes(List<Group> groupes) {
		this.groupes = groupes;
	}

	/**
	 * Retourne les data d'un lot de groupe
	 * 
	 * @return data : String[][]
	 */
	public String[][] getData() {
		return data;
	}

	/**
	 * Définit les data d'un lot de groupe
	 * 
	 * @param data : String[][]
	 */
	public void setData(String[][] data) {
		this.data = data;
	}

	/**
	 * Retroune le hashcode d'un objet Groupe
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(data);
		result = prime * result + ((groupes == null) ? 0 : groupes.hashCode());
		return result;
	}

	/**
	 * Retroune un booléen qui définit si deux lots de groupes sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupLot other = (GroupLot) obj;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		if (groupes == null) {
			if (other.groupes != null)
				return false;
		} else if (!groupes.equals(other.groupes))
			return false;
		return true;
	}
	
	
	

	
	
	
	
}	

