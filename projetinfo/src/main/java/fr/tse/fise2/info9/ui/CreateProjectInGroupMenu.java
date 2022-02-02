package fr.tse.fise2.info9.ui;

import javax.swing.JFrame;

/**
 * 
 * Fenêtre de création de projet au sein d'un groupe déjà existant.
 * 
 * Utilise la classe CreateProjectMenu.
 * 
 */
public class CreateProjectInGroupMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CreateProjectMenu projectPanel; // menu de création de projet appelé dans cette fenêtre
	private TabGroupes groupMenu; // fenêtre parente
	private CreateProjectInGroupMenu frame = this; // this

	/**
	 * 
	 * @param parentFrame    La fenêtre parent ou afficher cette fenêtre
	 * @param displayedGroup Groupe parent du projet
	 */
	public CreateProjectInGroupMenu(TabGroupes parentFrame, org.gitlab4j.api.models.Group displayedGroup) {

		groupMenu = parentFrame;

		// Appel de la classe CreateProjectMenu
		projectPanel = new CreateProjectMenu(frame, displayedGroup);
		projectPanel.setBounds(0, 0, 614, 500);
		getContentPane().setLayout(null);
		setBounds(450, 135, 614, 530);
		getContentPane().add(projectPanel);
		setVisible(true);
	}

	/**
	 * Méthode qui raffraichs la fiche groupe et qui ferme cette fenêtre
	 */
	public void closeWindow() {
		groupMenu.redrawGroupSheet();
		dispose();

	}
}
