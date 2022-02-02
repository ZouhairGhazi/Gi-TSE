package fr.tse.fise2.info9.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.User;
import fr.tse.fise2.info9.services.CSVutils;
import fr.tse.fise2.info9.services.LoginApiCall;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;

import fr.tse.fise2.info9.services.DataProject;

/**
 *
 * Fenêtre de création de projet.
 *
 */
public class CreateProjectMenu extends JPanel {

	private static final long serialVersionUID = 8797890345824375280L;
	private JFrame frame;
	private JPanel panel;
	private JTextField nomProjet;
	private JTable table;
	private JLabel lblNewLabel;
	private org.gitlab4j.api.models.Group parentGroup = null;
	private CreateProjectInGroupMenu parentFrame = null;
	private JScrollPane scrollPane;
	private String[] columnNames = { "Pseudo", "Nom", "email" };
	private String data[][] = null;
	private boolean isvalid[] = new boolean[1000];

	/**
	 * Permet d'afficher le menu de création de projet dans la fenêtre principale
	 */
	public CreateProjectMenu() {

		setLayout(null);
		setBounds(220, 95, 604, 500); // On défini la taille de la fenêtre
		panel = new JPanel();
		panel.setBounds(10, 11, 583, 478); // On définie la position de cette fenêtre dans la fenêtre principale
		add(panel);
		panel.setLayout(null);

		initialize();
	}

	/**
	 * Permet d'afficher le menu de création de projet dans la fenêtre principale Ce
	 * constructeur est différent du précédent car il prend en compte un groupe Il
	 * permet donc de créer un projet pour un groupe spécifique
	 * 
	 * @param frame          Fenêtre parent
	 * @param displayedGroup Le groupe pour lequel on veut créer un projet
	 */
	public CreateProjectMenu(CreateProjectInGroupMenu frame, org.gitlab4j.api.models.Group displayedGroup) {

		// On stock les variables en global
		parentGroup = displayedGroup;
		parentFrame = frame;

		setLayout(null);
		setBounds(220, 95, 604, 500);
		panel = new JPanel();
		panel.setBounds(10, 11, 583, 478);
		add(panel);
		panel.setLayout(null);

		initialize(); // On initialise la fenêtre
		lblNewLabel.setText("Créer un projet dans le groupe " + displayedGroup.getName()); // On affiche un texte qui
																							// précise pour quel groupe
																							// on va créer le projet
	}

	/**
	 * 
	 * Méthode qui permet de placer les différents élements dans la fenêtre
	 * 
	 */
	private void initialize() {

		boolean isAdmin_check = false;

		final GitLabApi api = LoginApiCall.getGitLabApi();
		try {
			// Cette partie permet de récupérer le pseudo de l'utilisateur s'il s'est
			// connecté avec son adresse mail
			// En effet, certaines fonctions de Git prennent en compte le pseudo de
			// l'utilisateur

			System.out.println("Looking for : " + LoginApiCall.getUsername());
			User self = api.getUserApi().getUser(LoginApiCall.getUsername());
			if (self == null) {
				System.out.println("Impossible de trouver son propre utilisateur!");
				User utilisateur = api.getUserApi().getUserByEmail(LoginApiCall.getUsername());
				if (utilisateur != null) {
					System.out.println("Nom d'utilisateur trouvé: " + utilisateur.getUsername());
					self = utilisateur;
				} else {
					System.out.println("Impossible de trouver son propre utilisateur par addresse mail!");
				}
			} else {
				System.out.println("Profil: " + self.toString().replace("\"", "\0"));
			}
			if (self.getIsAdmin() != null) {
				if (self.getIsAdmin() == true) {
					System.out.println("Est admin!");
					isAdmin_check = true;
				}
			}
		} catch (GitLabApiException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		final boolean isAdmin = isAdmin_check;

		// On place les différents objets dans la fenêtre
		frame = new JFrame();
		frame.setBounds(100, 100, 808, 604);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		lblNewLabel = new JLabel("Créer un projet");
		lblNewLabel.setBounds(0, 0, 396, 34);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nom du projet");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 56, 88, 22);
		panel.add(lblNewLabel_1);

		nomProjet = new JTextField();
		nomProjet.setBounds(10, 89, 156, 20);
		panel.add(nomProjet);
		nomProjet.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Fichier CSV");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(10, 121, 173, 14);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_1_1 = new JLabel("Description du projet");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_1.setBounds(203, 56, 140, 22);
		panel.add(lblNewLabel_1_1);

		final JTextPane descriptionProjet = new JTextPane();
		descriptionProjet.setBounds(196, 89, 353, 50);
		panel.add(descriptionProjet);

		final JCheckBox isPrivate = new JCheckBox("Projet privé");
		isPrivate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		isPrivate.setBounds(196, 146, 97, 23);
		panel.add(isPrivate);

		// On affiche si l'utilisateur est admin ou non
		final JLabel admin_label = new JLabel(
				"<html><p style=\"color:red\">Vous n'êtes pas admin du Git,<br>les utilisateurs en rouge seront ignorés</p></html>");
		admin_label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		;
		if (isAdmin) {
			admin_label.setText(
					"<html><p style=\"color:green\">Vous n'êtes pas admin du Git,<br>les utilisateurs en rouge seront ignorés</p></html>");
		}
		admin_label.setBounds(300, 150, 305, 34);
		admin_label.setVisible(false);
		panel.add(admin_label);

		// Le bouton qui permet de valider la création du projet
		final JButton btnNewButton_1 = new JButton("Valider");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				// Plusieurs vérifications pour la validité de la création
				if (nomProjet.getText().equals("") || nomProjet.getText().replace(" ", "").equals("")) {
					JOptionPane.showMessageDialog(null, "Vous devez rentrer un nom de projet!", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!doesntCountainsSpecificCaracters(nomProjet.getText())) {
					JOptionPane.showMessageDialog(null,
							"Le nom de projet ne doit pas contenir des caractères spéciaux!", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					List<Project> projects = api.getProjectApi().getMemberProjects();

					int n = 0;
					while (n < projects.size()) {
						if (projects.get(n).getName().equals(nomProjet.getText())) {
							JOptionPane.showMessageDialog(null, "Un projet du même nom existe déjà!", "Erreur",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						n++;
					}
				} catch (GitLabApiException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();

				}
				// On va maintenant itérer dans le tableau des membres du projet pour les
				// ajouter
				ArrayList<User> liste_membres = new ArrayList<User>();
				for (int i = 0; i < table.getRowCount(); i++) {
					String username = (String) table.getValueAt(i, 0);
					String fullname = (String) table.getValueAt(i, 1);
					String email = (String) table.getValueAt(i, 2);
					// Aorès avoir sélectionné l'utilisateur on fait plusieurs vérifications de
					// sécurité
					if (username == null || fullname == null || email == null) {
						JOptionPane.showMessageDialog(null, "Un des champs du tableau est vide! Veuillez le compléter",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (!doesntCountainsSpecificCaracters(username)) {
						JOptionPane.showMessageDialog(null,
								"L'utilisateur " + username
										+ " a un nom d'utilisateur contenant des caractères spéciaux!",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (!doesntCountainsSpecificCaracters(fullname)) {
						JOptionPane.showMessageDialog(null,
								"L'utilisateur " + username + " a un nom complet contenant des caractères spéciaux!",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (!isEmailValid(email)) {
						JOptionPane.showMessageDialog(null,
								"L'utilisateur " + username + " a un format de mail non valide!", "Erreur",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						// Après les vérifications, on s'assure que l'utilisateur existe sur le Git
						User user = api.getUserApi().getUserByEmail(email);
						if (user == null) {
							if (isvalid[i]) {
								// Il s'agit de la situation dans laquelle l'utilisateur a modifié le tableau
								// pour corriger une donnée du fichier CSV et que cet utilisateur n'est
								// mainteant plus trouvé
								JOptionPane.showMessageDialog(null,
										"L'utilisateur " + username
												+ " a été modifié et n'est plus valide. Veuillez corriger!",
										"Erreur", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					} catch (GitLabApiException e2) {
						// S'il n'est pas trouvé, on gère l'erreur
						if (e2.getMessage().contains("403")) {
							System.out.println("Impossible de créer compte!");
						} else {
							e2.printStackTrace();
						}

					}

				}
				//On crée le projet
				Project projet;
				if (parentGroup != null) { //Si c'est un projet de groupe
					projet = new Project().withName(nomProjet.getText()).withNamespaceId(parentGroup.getId())
							.withDescription(descriptionProjet.getText()).withIssuesEnabled(true)
							.withMergeRequestsEnabled(true).withWikiEnabled(true).withSnippetsEnabled(true)
							.withPublic(!isPrivate.isSelected()).withPath(nomProjet.getText());
				} else { //Sinon
					projet = new Project().withName(nomProjet.getText()).withDescription(descriptionProjet.getText())
							.withIssuesEnabled(true).withMergeRequestsEnabled(true).withWikiEnabled(true)
							.withSnippetsEnabled(true).withPublic(!isPrivate.isSelected());
				}
				
				//On envoie ensuite la demande au Git
				try {
					api.getProjectApi().createProject(projet);
					DataProject d = new DataProject();
					d.addProject(projet, "data.txt", null);
				} catch (GitLabApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Iteration de la liste des membres du tableau
				liste_membres = new ArrayList<User>();
				for (int i = 0; i < table.getRowCount(); i++) {
					String username = (String) table.getValueAt(i, 0);
					String fullname = (String) table.getValueAt(i, 1);
					String email = (String) table.getValueAt(i, 2);
					if (username == null || fullname == null || email == null) {
						JOptionPane.showMessageDialog(null, "Un des champs du tableau est vide! Veuillez le compléter",
								"Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}

					try {
						User user = api.getUserApi().getUserByEmail(email);
						if (user == null) {
							// Si l'utilisateur est admin du Git, on peut créer l'utilisateur
							if (isAdmin) {
								User userConfig = new User().withEmail(email).withName(fullname).withUsername(username);
								user = api.getUserApi().createUser(userConfig, UUID.randomUUID().toString(), true);
							}

						}
						
						//Si l'utilisateur est bien trouvé
						if (user != null) {
							//On va l'ajouter au projet
							List<Project> projects = api.getProjectApi().getMemberProjects();
							int n = 0;
							Project finalpro = null;
							while (n < projects.size()) {

								if (projects.get(n).getName().equals(nomProjet.getText().toString())) {
									finalpro = api.getProjectApi().getProject(projects.get(n));
								}

								n++;
							}
							api.getProjectApi().addMember(finalpro, user.getId(), AccessLevel.MAINTAINER);
						}
					} catch (GitLabApiException e2) {
						// On gère les erreurs
						if (e2.getMessage().contains("403")) {
							System.out.println("Impossible de créer compte!");
						} else {
							e2.printStackTrace();
						}

					}

				}
				// On affiche la sortie
				if (parentGroup != null) {
					JOptionPane.showMessageDialog(null,
							"Le projet a été créé avec succès dans le groupe " + parentGroup.getName() + " !",
							"Succès!", JOptionPane.INFORMATION_MESSAGE);
					parentFrame.closeWindow();
				} else {
					JOptionPane.showMessageDialog(null, "Le projet a été créé avec succès sur votre Git!", "Succès!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		btnNewButton_1.setBounds(247, 444, 89, 23);
		btnNewButton_1.setVisible(false);
		panel.add(btnNewButton_1);
		table = new JTable();
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 191, 563, 230);
		panel.add(scrollPane);

		// Permet de sélectionner un fichier CSV
		JButton btnNewButton = new JButton("Choisir un fichier");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();// explorateur de fichier
				fileChooser.setCurrentDirectory(new File("C:\\Users\\Utilisateur\\Desktop")); // répertoire de départ
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {// l'utilisateur choisi un fichier
					admin_label.setVisible(true);
					File selectedFile = fileChooser.getSelectedFile(); // fichier choisi

					data = CSVutils.readCSV(selectedFile.getAbsolutePath(), 3);
					// On affiche la fenêtre seulement si on arrive lire des données
					if (data != null) {
						TableModel model = new myTableModel();
						table.setModel(model);

						for (int i = 0; i < table.getRowCount(); i++) {
							String email = (String) table.getValueAt(i, 2);
							GitLabApi api = LoginApiCall.getGitLabApi();
							User user;
							try {
								//On vérifie la validité des utilisateurs
								user = api.getUserApi().getUserByEmail(email);
								if (user == null) {
									isvalid[i] = false;
								} else {
									isvalid[i] = true;
								}

								//On va colorer le texte des utilisateurs valides et invalides
								table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
									private static final long serialVersionUID = 1L;

									@Override
									public Component getTableCellRendererComponent(JTable table, Object value,
											boolean isSelected, boolean hasFocus, int row, int column) {
										final Component c = super.getTableCellRendererComponent(table, value,
												isSelected, hasFocus, row, column);
										if (isvalid[row]) {
											//Si l'utilisateur existe
											c.setForeground(Color.BLUE);
										} else {
											if (isAdmin) {
												//S'il n'existe pas et que nous sommes admin
												c.setForeground(Color.ORANGE);
											} else {
												//S'il n'existe pas et que nous ne sommes pas admin
												c.setForeground(Color.RED);
											}

										}

										return c;
									}
								});

							} catch (GitLabApiException e1) {
								e1.printStackTrace();
							}

						}

						btnNewButton_1.setVisible(true);
					}
				}
			}
		});
		btnNewButton.setBounds(10, 146, 156, 23);
		panel.add(btnNewButton);

	}

	/**
	 * 
	 * Extension du tableau d'utilisateurs qui permet de le gérer
	 * 
	 */
	public class myTableModel extends DefaultTableModel

	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		myTableModel()

		{

			super(data, columnNames);

		}

		public boolean isCellEditable(int row, int cols)

		{

			return false;

		}

	}

	/**
	 * 
	 * Permet de vérifier si une adresse mail est valide
	 * 
	 * @param email L'adresse email à vérifier
	 * @return True si le regex est validé, False sinon
	 */
	public static boolean isEmailValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/**
	 * 
	 * Permet de vérifier si un text contient des charactères spéciaux
	 * 
	 * @param text Le texte à vérifier
	 * @return True si le regex est validé, False sinon
	 */
	public static boolean doesntCountainsSpecificCaracters(String text) {
		String emailRegex = "^[ a-z0-9A-Z\\u00C0-\\u00FF-.]*$";

		Pattern pat = Pattern.compile(emailRegex);
		if (text == null)
			return false;
		return pat.matcher(text).matches();
	}
}
