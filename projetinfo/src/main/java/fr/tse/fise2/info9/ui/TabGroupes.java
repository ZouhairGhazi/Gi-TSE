package fr.tse.fise2.info9.ui;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import java.util.List;
import java.util.Dictionary;
import java.util.Hashtable;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import fr.tse.fise2.info9.services.LoginApiCall;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.SystemColor;
import java.awt.Color;

/**
 * 
 * Menu de visualisation des groupes de l'utilisateur.
 * 
 * Contient la liste des groupes sous forme d'arborescence ainsi qu'une fiche
 * détaillée du groupe sélectionné.
 * 
 */
public class TabGroupes extends JPanel {

	private static final long serialVersionUID = 8793090345824375280L; // id du panel
	private JPanel panel; // panel interne
	private JLabel lblNewLabel; // label du titre du menu
	private JPanel groupPanel; // panel qui contient la fiche d'information du groupe sélectionné
	private JScrollPane jtScrollPane; // scrollPane qui contient l'arborescence des groupes
	private List<org.gitlab4j.api.models.Group> groupList = getGroups(); // liste des groupes auxquel l'utilisateur
																			// appartient
	private Dictionary<DefaultMutableTreeNode, Integer> dic = new Hashtable<DefaultMutableTreeNode, Integer>(); // table
																												// de
																												// correspondance
																												// des
																												// noeuds
																												// de
																												// l'arborescence
																												// avec
																												// l'id
																												// du
																												// groupe
	private org.gitlab4j.api.models.Group displayedGroup = null; // groupe affiché
	private JTree jt; // arbre qui contient les groupes de l'utilisateur
	private TabGroupes frame = this; // this

	/**
	 * 
	 * Constructeur du tabgroupe
	 * 
	 * @param frame La frame de l'interface principale
	 */
	public TabGroupes(MainMenu frame) {
		setLayout(null);
		setBounds(220, 95, 750, 500);
		panel = new JPanel();
		panel.setBounds(10, 11, 730, 478);
		add(panel);
		panel.setLayout(null);

		lblNewLabel = new JLabel("Mes groupes");
		lblNewLabel.setBounds(0, 0, 500, 34);
		panel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

		groupPanel = new JPanel();
		groupPanel.setBorder(new LineBorder(SystemColor.DARK_GRAY));
		groupPanel.setBackground(new Color(227, 227, 227));
		groupPanel.setForeground(Color.BLACK);
		groupPanel.setBounds(220, 52, 500, 425);
		groupPanel.setLayout(null);
		panel.add(groupPanel);

		// affichage de l'arborescence des groupes
		drawJTree();

	}

	/**
	 *
	 * Fonction qui gère l'affichage de la fiche groupe et qui génère tous ses
	 * composants à partir du groupe sélectionné
	 * 
	 * @param p Le panel ou affiche le groupe
	 * @param g Le groupe sélectionné
	 */
	public void displayGroupSheet(JPanel p, org.gitlab4j.api.models.Group g) {

		// récupération des informations du groupe
		GitLabApi git = LoginApiCall.getGitLabApi();
		List<org.gitlab4j.api.models.Project> projectList = new ArrayList<Project>();
		List<Member> membersList = new ArrayList<Member>();
		String groupVisibility = g.getVisibility().toString();
		String description = g.getDescription();

		// remplissage des listes de membres et de projets du groupe
		try {
			projectList = git.getGroupApi().getProjects(g.getId());
			membersList = git.getGroupApi().getMembers(g.getId());
		} catch (GitLabApiException er) {
			er.printStackTrace();
		}

		// On affiche les différents éléments
		JLabel groupLabel = new JLabel(g.getName());
		groupLabel.setBounds(10, 0, 500, 34);
		groupLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		p.add(groupLabel);

		JTable projectTable = new JTable();
		JScrollPane projectScrollPane = new JScrollPane(projectTable);
		projectScrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		projectScrollPane.setBackground(Color.WHITE);
		projectScrollPane.setBounds(10, 40, 200, 180);
		p.add(projectScrollPane);

		JTable membersTable = new JTable();
		JScrollPane memberScrollPane = new JScrollPane(membersTable);
		memberScrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		memberScrollPane.setBackground(Color.WHITE);
		memberScrollPane.setBounds(10, 235, 200, 180);
		p.add(memberScrollPane);

		JLabel dateLabel = new JLabel("Date de création");
		dateLabel.setBounds(230, 185, 500, 34);
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		p.add(dateLabel);

		JLabel date = new JLabel(g.getCreatedAt().toString());
		date.setBounds(250, 215, 500, 34);
		date.setFont(new Font("Tahoma", Font.PLAIN, 14));
		p.add(date);

		JLabel visibilityLabel = new JLabel("Visibilité");
		visibilityLabel.setBounds(230, 255, 500, 34);
		visibilityLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		p.add(visibilityLabel);

		JLabel visibility = new JLabel(groupVisibility);
		visibility.setBounds(250, 285, 500, 34);
		visibility.setFont(new Font("Tahoma", Font.PLAIN, 14));
		p.add(visibility);

		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(230, 325, 500, 34);
		descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		p.add(descriptionLabel);

		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setText(description);
		textPane.setBounds(230, 355, 250, 60);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		p.add(textPane);

		JLabel line = new JLabel("");
		line.setBorder(new LineBorder(Color.LIGHT_GRAY));
		line.setBounds(240, 165, 240, 1);
		p.add(line);

		// bouton pour ajouter un projet dans le groupe
		final JButton addProjetctButton = new JButton("Ajouter un projet");
		addProjetctButton.setBounds(230, 20, 250, 40);
		addProjetctButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addProjetctButton.setBackground(SystemColor.controlHighlight);
		addProjetctButton.setContentAreaFilled(false);
		addProjetctButton.setBorderPainted(false);
		addProjetctButton.setFocusPainted(false);
		addProjetctButton.setOpaque(true);
		addProjetctButton.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					addProjetctButton.setFont(addProjetctButton.getFont().deriveFont(Font.BOLD));
				} else {
					addProjetctButton.setFont(addProjetctButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});
		p.add(addProjetctButton);

		// bouton pour ajouter un sous groupe au sein du groupe sélectionné
		final JButton addsubGroupButton = new JButton("Ajouter un sous groupe");
		addsubGroupButton.setBounds(230, 55, 250, 40);
		addsubGroupButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addsubGroupButton.setBackground(SystemColor.controlHighlight);
		addsubGroupButton.setContentAreaFilled(false);
		addsubGroupButton.setBorderPainted(false);
		addsubGroupButton.setFocusPainted(false);
		addsubGroupButton.setOpaque(true);
		addsubGroupButton.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					addsubGroupButton.setFont(addsubGroupButton.getFont().deriveFont(Font.BOLD));
				} else {
					addsubGroupButton.setFont(addsubGroupButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});
		p.add(addsubGroupButton);

		// bouton pour ajouter un membre dans le groupe sélectionné
		final JButton addMemberButton = new JButton("Ajouter un membre");
		addMemberButton.setBounds(230, 90, 250, 40);
		addMemberButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addMemberButton.setBackground(SystemColor.controlHighlight);
		addMemberButton.setContentAreaFilled(false);
		addMemberButton.setBorderPainted(false);
		addMemberButton.setFocusPainted(false);
		addMemberButton.setOpaque(true);
		addMemberButton.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					addMemberButton.setFont(addMemberButton.getFont().deriveFont(Font.BOLD));
				} else {
					addMemberButton.setFont(addMemberButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});
		p.add(addMemberButton);

		// bouton pour supprimer un membre dans le groupe sélectionné
		final JButton deleteMemberButton = new JButton("Supprimer un membre");
		deleteMemberButton.setBounds(230, 125, 250, 40);
		deleteMemberButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deleteMemberButton.setBackground(SystemColor.controlHighlight);
		deleteMemberButton.setContentAreaFilled(false);
		deleteMemberButton.setBorderPainted(false);
		deleteMemberButton.setFocusPainted(false);
		deleteMemberButton.setOpaque(true);
		deleteMemberButton.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					deleteMemberButton.setFont(deleteMemberButton.getFont().deriveFont(Font.BOLD));
				} else {
					deleteMemberButton.setFont(deleteMemberButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});
		p.add(deleteMemberButton);

		addProjetctButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CreateProjectInGroupMenu(frame, displayedGroup);
			}
		});
		addsubGroupButton.addActionListener(new ActionListener() {
			// appel de la fenêtre de création de sous groupe
			public void actionPerformed(ActionEvent e) {
				new CreateSubgroupMenu(frame, displayedGroup);
			}
		});
		addMemberButton.addActionListener(new ActionListener() {
			// appel de la fenêtre d'ajout de membre dans un groupe
			public void actionPerformed(ActionEvent e) {
				new AddMemberMenu(frame, displayedGroup);
			}
		});
		deleteMemberButton.addActionListener(new ActionListener() {
			// appel de la fenêtre d'ajout de membre dans un groupe
			public void actionPerformed(ActionEvent e) {
				new DeleteMemberMenu(frame, displayedGroup);
			}
		});

		// remplissage du tableau des projets
		if (projectList != null) {
			String[][] data = new String[projectList.size()][1];
			String[] columnName = { "Projets du groupe " + g.getName() + " (" + projectList.size() + ")" };
			for (int i = 0; i < projectList.size(); i++) {
				data[i][0] = projectList.get(i).getName();
			}
			TableModel model = new myTableModel(data, columnName);
			projectTable.setModel(model);
		}

		// remplissage du tableau des membres du groupe
		if (membersList != null) {
			String[][] data = new String[membersList.size()][1];
			String[] columnName = { "membres du groupe " + g.getName() + " (" + membersList.size() + ")" };
			for (int i = 0; i < membersList.size(); i++) {
				data[i][0] = membersList.get(i).getName();
			}
			TableModel model = new myTableModel(data, columnName);
			membersTable.setModel(model);
		}
	}

	/**
	 * Méthode de suppression de la fiche groupe
	 * 
	 * @param p Le panel a nettoyer
	 */
	public void popGroupSheet(JPanel p) {
		p.removeAll();
		p.revalidate();
		p.repaint();
	}

	/**
	 * Méthode pour redessiner la fiche groupe
	 */
	public void redrawGroupSheet() {
		redrawJTree();
		popGroupSheet(groupPanel);
		displayGroupSheet(groupPanel, displayedGroup);
	}

	/**
	 * Méthode pour dessiner l'arborescence des projets
	 */
	public void drawJTree() {
		// On récupère les groupes de l'utilisateur
		groupList = getGroups();
		// Création du noeud racine
		DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Mes groupes");

		// Pour chaque groupe de la liste qui se situe à la racine des groupes de
		// l'utilisateur, on crée un noeud et on va afficher récursivement ses noeuds
		// enfants
		for (int i = 0; i < groupList.size(); i++) {

			// si le groupe ne possède pas de parent == se situe à la racine, alors on va
			// chercher les noeuds dont il est le parent
			if (groupList.get(i).getParentId() == null) {
				DefaultMutableTreeNode noeudRacine = new DefaultMutableTreeNode(groupList.get(i).getName());
				racine.add(noeudRacine);
				dic.put(noeudRacine, i);
				displayChildren(noeudRacine, groupList.get(i));
			}
		}

		// création de l'arbre à partir de l'arborescence de noeuds et déploiement des
		// branches de l'arbre
		jt = new JTree(racine);
		for (int i = 0; i < jt.getRowCount(); i++) {
			jt.expandRow(i);
		}

		// Listener qui appelle la fonction nodeSelected lorsque l'on clique sur un
		// noeud
		jt.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				nodeSelected(e);
			}
		});

		jtScrollPane = new JScrollPane(jt);
		jtScrollPane.setBounds(10, 52, 200, 425);
		panel.add(jtScrollPane);
	}

	/**
	 * Rafraichissement de l'arbre
	 */
	public void redrawJTree() {
		panel.remove(jtScrollPane);
		drawJTree();
	}

	/**
	 * Méthode qui appelle la fiche groupe correspondant au groupe sélectionné
	 * 
	 * @param e Evenement de sélection d'un élement dans l'arbre
	 */
	public void nodeSelected(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();
		try {
			if (node.getParent() != null) {
				displayedGroup = groupList.get(dic.get(node));
				popGroupSheet(groupPanel);
				displayGroupSheet(groupPanel, displayedGroup);
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 
	 * Méthode récursive qui ajoute un noeud à l'arbre et qui se rappelle elle même
	 * pour les noeuds enfants de celui ajouté
	 * 
	 * @param node Noeud
	 * @param group Groupe sélectionné
	 */
	public void displayChildren(DefaultMutableTreeNode node, org.gitlab4j.api.models.Group group) {
		for (int i = 0; i < groupList.size(); i++) {
			if (groupList.get(i).getParentId() != null) {
				if (groupList.get(i).getParentId().equals(group.getId())) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(groupList.get(i).getName());
					node.add(newNode);
					dic.put(newNode, i);
					displayChildren(newNode, groupList.get(i));
				}
			}
		}
	}

	/**
	 * Méthode de récupération des groupes gitlab de l'utilisateur
	 * @return Une liste des groupe
	 */
	public List<org.gitlab4j.api.models.Group> getGroups() {

		GitLabApi git = LoginApiCall.getGitLabApi();
		List<org.gitlab4j.api.models.Group> groupList = null;
		try {
			groupList = git.getGroupApi().getGroups("");
		} catch (GitLabApiException er) {
			er.printStackTrace();
		}
		return groupList;
	}

	/**
	 * 
	 * Modèle du tableau
	 *
	 */
	public class myTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructeur du tableau
		 * @param data
		 * @param columnName
		 */
		myTableModel(String[][] data, String[] columnName) {
			super(data, columnName);
		}

		public boolean isCellEditable(int row, int cols) {
			return false;
		}
	}
}
