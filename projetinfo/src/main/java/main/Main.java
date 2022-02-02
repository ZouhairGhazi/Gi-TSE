package main;

import java.awt.EventQueue;
import fr.tse.fise2.info9.ui.WelcomeMenu;

/**
 * 
 * Classe principale qui lance le programme
 * 
 */
public class Main {
	/**
	 * Classe principal qui appelle le menu de chargement
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//On lance le menu de bienvenue
					WelcomeMenu window = new WelcomeMenu();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
