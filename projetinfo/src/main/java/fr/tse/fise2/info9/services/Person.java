package fr.tse.fise2.info9.services;


/**
 * Classe utilisée pour manipuler les utilisateurs d'un groupe.
 *
 */
public class Person {
private String pseudo; //pseudo du compte gitlab
private String name;  //nom de la personne
private String email; // email du compte gitlab

/**
 * Constructeur de personne
 * 
 * @param pseudo : String
 * @param name : String
 * @param email : String
 */
public Person(String pseudo, String name, String email) {
	super();
	this.pseudo = pseudo;
	this.name = name;
	this.email = email;
}

/**
 * Retourne le pseudo d'une personne
 * 
 * @return pseudo : String
 */
public String getPseudo() {
	return pseudo;
}


/**
 * Définit le pseudo d'une personne 
 * 
 * @param pseudo : String
 */
public void setPseudo(String pseudo) {
	this.pseudo = pseudo;
}

/**
 * Retourne le nom d'une personne
 * 
 * @return name : String
 */
public String getName() {
	return name;
}


/**
 * Définit le nom d'une personne 
 * 
 * @param name : String
 */
public void setName(String name) {
	this.name = name;
}

/**
 * Retourne le mail d'une personne
 * 
 * @return email : String
 */
public String getEmail() {
	return email;
}

/**
 * Définit le mail d'une personne 
 * 
 * @param email : String
 */
public void setEmail(String email) {
	this.email = email;
}

/**
 * Retroune le hashcode d'un objet Person
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
	return result;
}

/**
 * Retourne un booléen qui définit si les deux objets sont égaux
 * 
 */
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Person other = (Person) obj;
	if (email == null) {
		if (other.email != null)
			return false;
	} else if (!email.equals(other.email))
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (pseudo == null) {
		if (other.pseudo != null)
			return false;
	} else if (!pseudo.equals(other.pseudo))
		return false;
	return true;
}


}
