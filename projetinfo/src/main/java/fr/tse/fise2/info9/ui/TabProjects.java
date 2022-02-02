package fr.tse.fise2.info9.ui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import fr.tse.fise2.info9.services.LoginApiCall;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import fr.tse.fise2.info9.services.DataProject;

/**
 *
 * Affiche la liste des projets de l'utilisateur et permet d'afficher les
 * statistiques avec des boutons
 *
 */
public class TabProjects extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 8793090345824375280L;
	private JTable table;
	private JButton btnNewButton;
	private boolean isWatchingArchives = false;
	private JScrollPane scrollPane;
	private String selectedproject;
	Map<String, Project> projectlist = new HashMap<String, Project>();
	private JPanel tabPannel;
	private static String projectName;
	private static JButton btnDtailler = new JButton("Détailler");
	private DataProject dp;

	/**
	 * Constructeur
	 * 
	 * @param data Les données des différents projets téléchargés à la connexion
	 */
	TabProjects(DataProject data) {

		// Récupération des données des projets et initialisation des composants
		dp = data;
		projectlist = data.getProjectList();
		setLayout(null);
		setBounds(220, 95, 750, 500);
		tabPannel = new JPanel();
		tabPannel.setBounds(10, 11, 730, 478);

		add(tabPannel);
		tabPannel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Mes projets");
		lblNewLabel.setBounds(0, 0, 514, 34);
		tabPannel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 100, 710, 350);
		tabPannel.add(scrollPane);

		// Mise en place du bouton d'archivage
		btnNewButton = new JButton("Archiver");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Pop up demande archivage
				if (isWatchingArchives) {
					int result = JOptionPane.showConfirmDialog(null,
							"Voulez-vous désarchiver le projet " + selectedproject + " ?", "Archivage",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						// Si il valide la mise en archive
						GitLabApi git = LoginApiCall.getGitLabApi();

						// On archive
						projectlist.get(selectedproject).setDescription(
								projectlist.get(selectedproject).getDescription().replace("Archive - ", ""));
						try {
							git.getProjectApi().updateProject(projectlist.get(selectedproject));
						} catch (GitLabApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// On update nos listes en local
						dp.addProject(projectlist.get(selectedproject), "data.txt", "archives.txt");
						dp.removeProject(projectlist.get(selectedproject), "archives.txt");
						refreshTable(true);
						btnNewButton.setEnabled(false);
					}
				} else {
					// Pop up demande désarchivage
					int result = JOptionPane.showConfirmDialog(null,
							"Voulez-vous archiver le projet " + selectedproject + " ?", "Archivage",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						// Si il valide le désarchivage
						GitLabApi git = LoginApiCall.getGitLabApi();

						// On archive
						projectlist.get(selectedproject)
								.setDescription("Archive - " + projectlist.get(selectedproject).getDescription());
						try {
							git.getProjectApi().updateProject(projectlist.get(selectedproject));
						} catch (GitLabApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							if (e1.getMessage().contains("base")) {
								JOptionPane.showMessageDialog(null, "Impossible d'archiver un projet vide! ", "Erreur!",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							if (e1.getMessage().contains("403")) {
								JOptionPane.showMessageDialog(null, "Vous n'avez pas les permissions pour ce projet. ",
										"Erreur!", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
						// On update nos listes en local
						dp.addProject(projectlist.get(selectedproject), "archives.txt", "data.txt");
						dp.removeProject(projectlist.get(selectedproject), "data.txt");
						refreshTable(false);
						btnNewButton.setEnabled(false);
					}
				}
			}
		});
		btnNewButton.setEnabled(false);
		btnNewButton.setBounds(455, 54, 106, 24);
		tabPannel.add(btnNewButton);

		btnDtailler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setProjectName(table.getValueAt(table.getSelectedRow(), 0).toString());
				ProjectInfo frame = new ProjectInfo();
				frame.setVisible(true);
			}
		});
		btnDtailler.setBounds(339, 54, 106, 24);
		tabPannel.add(btnDtailler);

		refreshTable(false);

		// Bouton des archives
		final JButton btnNewButton_1 = new JButton("Voir les archives");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Permet de switcher entre les archives et les projets
				if (!isWatchingArchives) {
					isWatchingArchives = true;
					btnNewButton.setText("Désarchiver");
					btnNewButton_1.setText("Revenir aux projets");
					btnDtailler.setVisible(false);
					refreshTable(true);
				} else {
					isWatchingArchives = false;
					btnNewButton.setText("Archiver");
					btnNewButton_1.setText("Voir les archives");
					btnDtailler.setVisible(true);
					refreshTable(false);
				}
			}
		});

		// Mise en place du tableau
		btnNewButton_1.setBounds(571, 54, 153, 24);
		tabPannel.add(btnNewButton_1);

		table.getColumnModel().getColumn(0).setMinWidth(120);
		table.getColumnModel().getColumn(0).setMaxWidth(140);
		table.getColumnModel().getColumn(1).setMinWidth(140);
		table.getColumnModel().getColumn(1).setMaxWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(140);
		table.getColumnModel().getColumn(2).setMaxWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(70);
		table.getColumnModel().getColumn(3).setMaxWidth(90);
		table.getColumnModel().getColumn(4).setMinWidth(80);
		table.getColumnModel().getColumn(4).setMaxWidth(110);
		table.getColumnModel().getColumn(5).setMinWidth(80);
		table.getColumnModel().getColumn(5).setMaxWidth(110);
		table.setFont(new Font("Verdana", Font.PLAIN, 13));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 13));
		table.getTableHeader().setBackground(new Color(252, 109, 38));

		ListSelectionModel listModel = table.getSelectionModel();
		listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listModel.addListSelectionListener(this);

		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(252, 109, 38));

	}

	/**
	 * Permet de réactualiser le tableau
	 * 
	 * @param isArchive True: Nous regardons les archives, False: Nous ne regardons
	 *                  pas les archives
	 */
	public void refreshTable(boolean isArchive) {
		String data[][];

		// On récupre les donnes concernant les projets avec l'api
		if (!isArchive) {
			data = getProjects();
		} else {
			data = getProjectsArchiveAPI();
		}

		// On affiche la fenêtre seulement si on arrive lire des données
		if (data != null) {
			String[] columnNames = { "Projets", "Création", "Dernière activité", "Commits", "Merges",
					"Collaborateurs" };

			// On initialise la JTable
			table = new JTable(data, columnNames);

			table.setFont(new Font("Verdana", Font.PLAIN, 13));
			table.getTableHeader().setOpaque(false);
			table.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 13));
			table.getTableHeader().setBackground(new Color(252, 109, 38));

			DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setModel(tableModel);

		} else {
			System.out.println("error");
		}
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			//Permet de détecter quel projet est cliqué et de le stocker
			public void valueChanged(ListSelectionEvent event) {
				selectedproject = table.getValueAt(table.getSelectedRow(), 0).toString();
				btnNewButton.setEnabled(true);

			}
		});
		scrollPane.setViewportView(table);

	}

	/**
	 * Permet d'obtenir un tableau contenant les projets de l'utilisateur
	 * @return Les données des projets
	 */
	@SuppressWarnings("static-access")
	public String[][] getProjects() {
		String data[][] = dp.getDataTable("data.txt");
		return data;

	}

	/**
	 * Permet d'obtenir un tableau contenant les projets archivés de l'utilisateur
	 * @return Les données des projets archivés
	 */
	public String[][] getProjectsArchiveAPI() {
		String data[][] = dp.getDataTable("archives.txt");
		return data;
	}

	/**
	 *  Détectee le changement de valeur
	 */
	public void valueChanged(ListSelectionEvent e) {
		int[] ligne;
		if (!e.getValueIsAdjusting()) {
			ligne = table.getSelectedRows();
			if (ligne.length > 0) {
				for (int i = 0; i < 4; i++) {
					TableModel tm = table.getModel();
					tm.getValueAt(ligne[0], i);
				}
			}
		}
	}

	/**
	 * Récupère le nom d'un projet
	 * @return Le nom du projet
	 */
	public static String getProjectName() {
		return projectName;
	}
	
	/**
	 * Défini le nom d'un projet
	 * @param projectName Le nom du projet
	 */
	public static void setProjectName(String projectName) {
		TabProjects.projectName = projectName;
	}

	/**
	 * Génère le fichier des données
	 */
	public void createDataFiles() {
		dp = new DataProject();
		String data[][] = dp.getDataAPI();
		dp.dataFiles(data);
	}

}
