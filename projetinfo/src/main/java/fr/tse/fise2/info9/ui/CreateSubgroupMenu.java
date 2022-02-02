package fr.tse.fise2.info9.ui;

import javax.swing.JFrame;

/**
 * Fenêtre de création d'un sous groupe au sein d'un groupe déjà existant.
 * 
 * Utilise la classe CreateGroupMenu.
 * 
 */
public class CreateSubgroupMenu extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CreateGroupMenu groupPanel; // menu de création de groupe appelé dans cette fenêtre
	private TabGroupes groupMenu; // fenêtre parente
	private CreateSubgroupMenu frame = this; //this
	
	/**
	 * Constructeur de la classe qui prend en paramètre la fenêtre parente et le groupe parent du sous groupe.
	 * 
	 * @param parentFrame : TabGroupes
	 * @param displayedGroup : Group
	 */
	public CreateSubgroupMenu(TabGroupes parentFrame,org.gitlab4j.api.models.Group displayedGroup) {
		
		groupMenu = parentFrame;
		
		//appel de la classe CreateGroupMenu
		groupPanel = new CreateGroupMenu(frame,displayedGroup);
		groupPanel.setBounds(0, 0, 614, 500);
		getContentPane().setLayout(null);
		setBounds(450, 135, 614, 530);
		getContentPane().add(groupPanel);
		setVisible(true);
	}

	/**
	 * Méthode qui raffraichis la fiche groupe et l'arborescence des groupes et qui ferme la fenêtre	
	 */
	public void closeWindow() {
		groupMenu.redrawJTree();
		groupMenu.redrawGroupSheet();
		dispose();
	}
}
