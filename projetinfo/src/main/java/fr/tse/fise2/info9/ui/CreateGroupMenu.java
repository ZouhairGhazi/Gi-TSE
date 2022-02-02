package fr.tse.fise2.info9.ui;

import javax.swing.*;
import javax.swing.table.*;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;
import org.gitlab4j.api.models.Visibility;
import fr.tse.fise2.info9.services.LoginApiCall;
import fr.tse.fise2.info9.services.OpenImage;
import fr.tse.fise2.info9.services.Group;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 
 * Menu de création d'un groupe unique.
 * 
 */
public class CreateGroupMenu extends JPanel{

	private static final long serialVersionUID = 8797789345824375280L; // id du panel
		public JScrollPane scrollPane; // scrollPane du tableau
		private JTable table; //tableau d'affichage de l'aperu des groupes 
		private JButton b1 = new JButton("Valider"); //bouton confirmer
		private final JTextField tf = new JTextField(); //input pour le nom du groupe
		private JLabel header = new JLabel("Création d'un nouveau groupe"); //titre de la fentre
		private JLabel l1 = new JLabel("Nom du groupe"); //indication pour l'input nom groupe
		private JLabel l2 = new JLabel("Fichier CSV"); //indication pour l'input nom groupe
		private JComboBox<String> comboBox; //combobox de choix du groupe parent
		private JButton b2 = new JButton("Rechercher un fichier"); //bouton de confirmation
		private String[] columnNames = { "Pseudo", "Nom", "Email" }; //format d'affichage de l'aperu des groupes 
		private String data[][] = null; //data  stocker dans la scrollpane
		private List<Integer> groupIdList = new ArrayList<Integer>();
		private Group groupe = null; //groupe cr
		private JPanel panel; // panel interne
		private org.gitlab4j.api.models.Group groupeParent = null; // groupe parent (null si on utilise le constructeur classique)
		private CreateSubgroupMenu parentFrame = null; //Fenêtre parente (nulle si on utilise le constructeur classique)
	
		/**
		 * Constructeur par défaut de menu de création de groupe.
		 * 
		 */
		public CreateGroupMenu() {
			//initialisation de la page et de la combobox
			init();
			initComboBox(comboBox,groupIdList,groupeParent);
			
		}
		
		/**
		 * Surcharge du constructeur de groupe à partir d'une autre fenêtre et avec un groupe parent.
		 * 
		 * @param frame : CreateSubgroupMenu
		 * @param displayedGroup : Group
		 */
		public CreateGroupMenu(CreateSubgroupMenu frame, org.gitlab4j.api.models.Group displayedGroup) {
			
			groupeParent = displayedGroup;
			parentFrame = frame;
			
			//initialisation de la page et de la combobox
			init();
			initComboBox(comboBox,groupIdList,groupeParent);
			//modification du titre de la page
			header.setText("Création d'un sous groupe de " + displayedGroup.getName());
		}
		
		/**
		 * Méthode d'initialisation du menu de création de groupe et des listeners.
		 * 
		 */
		@SuppressWarnings("unused")
		public void init() {
			setLayout(null);
			setBounds(220, 95, 604, 500);
			panel = new JPanel();
			panel.setBounds(10, 11, 583, 478);
			add(panel);
			panel.setLayout(null);
			
			
			Font headerFont = new Font(header.getFont().getName(),Font.BOLD,header.getFont().getSize()+3); //Police du header
			header.setBackground(new Color(240, 240, 240));
			header.setFont(new Font("Tahoma", Font.PLAIN, 20));
			header.setBounds(0,0,600,34);
			panel.add(header);
			
			b1.setFont(new Font("Tahoma", Font.PLAIN, 12));
			b1.setBounds(242,427,100,40);
			panel.add(b1);
			
			b2.setFont(new Font("Tahoma", Font.PLAIN, 12));
			b2.setBounds(120,150,179,20);
			panel.add(b2);
			
			tf.setBounds(120,71,179,20);
			panel.add(tf);
			
			l1.setFont(new Font("Tahoma", Font.PLAIN, 12));
			l1.setBounds(10,70,100,20);
			panel.add(l1);
			
			l2.setFont(new Font("Tahoma", Font.PLAIN, 12));
			l2.setBounds(10,150,90,20);
			panel.add(l2);
			
			table = new JTable();
			scrollPane = new JScrollPane(table);
			scrollPane.setBounds(10, 191, 563, 230);
			scrollPane.setBackground(new Color(252,109,38));
			panel.add(scrollPane);
			
			JLabel l3 = new JLabel("Groupe parent");
			l3.setFont(new Font("Tahoma", Font.PLAIN, 12));
			l3.setBounds(10, 110, 100, 20);
			panel.add(l3);
			
			JLabel help = new JLabel("");
			help.setBounds(553, 448, 30, 30);
			help.setIcon(new ImageIcon(OpenImage.openImage("help.png")));
			help.setToolTipText("<html>" + "Vous pouvez créer ici un nouveau groupe" + "<br>"+ "<br>" 
			+ "Veuillez commencer par choisir un nom pour votre groupe." + "<br>"+ "<br>"
			+ "L'API Gitlab bloque la création de nouveau groupe pour" + "<br>"
			+ "les comptes non admins, veuillez créer manuellement un" + "<br>"
			+ "groupe 'racine' puis renseignez le nom de ce groupe " + "<br>"
			+ "dans le champs correspondant" + "<br>"+ "<br>"
			+ "Veuillez terminer par importer un fichier CSV contenant" + "<br>"
			+ "le pseudo, nom/prénom et email des membres du groupe à " + "<br>"
			+ "ajouter séparés par des virgules puis cliquez sur valider" + "<br>"+ "<br>"
			+ "</html>");
			panel.add(help);
			
			comboBox = new JComboBox<String>();
			comboBox.setBounds(120, 111, 179, 20);
			panel.add(comboBox);
			
			panel.setBackground(UIManager.getColor("Button.background"));
			panel.setLayout(null);
			
			
			
			
			b2.addActionListener(new ActionListener() { //bouton d'explorateur de fichiers
				public void actionPerformed(ActionEvent e) {
					 if(tf.getText().isEmpty())
					    {
					    	JOptionPane.showMessageDialog(null, "Veuillez entrer un nom pour votre groupe");
					    }
					 else if(comboBox.getSelectedItem().toString().isEmpty()){
						 JOptionPane.showMessageDialog(null, "Veuillez entrer le nom du groupe parent"); 
					 }
					 else {
						 JFileChooser fileChooser = new JFileChooser();//explorateur de fichier
							fileChooser.setCurrentDirectory(new File("C:\\Users\\Utilisateur\\Desktop")); //répertoire de dpart
							int result = fileChooser.showOpenDialog(null);
							if (result == JFileChooser.APPROVE_OPTION) {// l'utilisateur choisi un fichier
								File selectedFile = fileChooser.getSelectedFile(); //fichier choisi
							   
							    //cration d'un objet groupe et ajout des donnes dans le tableau d'aperu
							    try {
							    	groupe = new Group(tf.getText(),selectedFile.getAbsolutePath());
								    if (groupe.getData() != null) {
								    	data = groupe.getData();
									   	TableModel model = new myTableModel();
										table.setModel(model);
										boolean emailsValid = true;
										
										//vérification de la validité des emails saisis et de l'existence du compte gitlab correspondant
										final boolean isvalid[] = new boolean[1000];
										for (int i = 0; i < table.getRowCount(); i++) {  // Loop through the rows
										        // Record the 5th column value (index 4)
										        String email = (String) table.getValueAt(i, 2);
										        GitLabApi api = LoginApiCall.getGitLabApi();
										        User user;
										        
										        try {
										        	if(isEmailValid(email)) {
										        		user = api.getUserApi().getUserByEmail(email);
										        	}else {
										        		user = null;
										        	}
										        	
													if (user == null) {
														//Code erreur
														isvalid[i]=false;
														emailsValid = false;
											        } else {
											        	isvalid[i]=true;
											        }
													
													//affichage des couleurs de police en fonction de la validité des emails
													table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
											            /**
														 * 
														 */
														private static final long serialVersionUID = 1L;

														@Override
											            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
											            	
											                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
											                
											                if (isvalid[row]) {
											                	c.setForeground(Color.BLUE);//emails valides
											                } else {
											                	c.setForeground(Color.RED);//emails invalides
											                }
											                
											                return c;
											            }
											        });
													
										        }catch(java.lang.IllegalArgumentException e2) {
													e2.printStackTrace();
												}catch (GitLabApiException e1) {
													e1.printStackTrace();
												}

										}
										if(emailsValid == false) {
											JOptionPane.showMessageDialog(null,"Certaines adresses email sont invalides ou ne correspondent \n à aucun compte Gitlab et ne seront pas ajoutées au groupe");
										}
								    }
								    else {
								    	JOptionPane.showMessageDialog(null,"Le fichier ouvert est vide");
								    }
							    }catch(java.lang.ArrayIndexOutOfBoundsException err){
							    	JOptionPane.showMessageDialog(null,"Le format du fichier CSV ouvert n'est pas valide");
							    }
							}
					 }
				}
			});
			
			b1.addActionListener(new ActionListener() { // bouton confirmer
				public void actionPerformed(ActionEvent e) {
					
					if(tf.getText().isEmpty() || comboBox.getSelectedItem().toString().isEmpty()|| data == null)
				    {
				    	JOptionPane.showMessageDialog(null, "Veuillez entrer un nom pour votre groupe, le nom du groupe parent et choisir un fichier CSV à importer");
				    }else {
				    	GitLabApi git = LoginApiCall.getGitLabApi(); //connexion  l'api
						try {
							
							int groupClicked = comboBox.getSelectedIndex();
							org.gitlab4j.api.models.Group parentGroup ;
							boolean comptesInexistant = false;
							
							if(groupeParent != null) {
								parentGroup = groupeParent;
							}else {
								parentGroup = git.getGroupApi().getGroup(groupIdList.get(groupClicked));
							}
							int parentId = parentGroup.getId();
							
							
							String childPath = "g1";
							//cration d'un path unique pour le nouveau subgroup
							List<org.gitlab4j.api.models.Group> subgroups = git.getGroupApi().getSubGroups(parentId);
							boolean correctPath = true;
							for(int k = 0; k<subgroups.size();k++) {
								for(int l = 0; l<subgroups.size(); l++) {
									if(subgroups.get(l).getPath().equals(childPath)) {
										correctPath = false;
									}
								}
								if (correctPath == false) {
									childPath = "g" + (Integer.parseInt(childPath.substring(1,childPath.length()))+1);
									correctPath = true;
								}
								else {
									break;
								}
							}
							
							//cration du nouveau groupe
							org.gitlab4j.api.models.Group NewGroup = git.getGroupApi().addGroup(groupe.getName(), childPath," ",Visibility.PRIVATE,false,false,parentId);
							//pour chaque membre, on l'joute au groupe nouvellement cr
							for(int i=0; i<groupe.getGroupe().size();i++) {
								
								try {
									//Rcupration de l'id de l'utilisateur puis ajout au groupe
									if(isEmailValid(groupe.getGroupe(i).getEmail())) {
										boolean userAlreadyAdded = false;
										List<Member> memberList = git.getGroupApi().getMembers(NewGroup.getId());
										
										for(int x=0; x<memberList.size();x++) {
											if(git.getUserApi().getUserByEmail(groupe.getGroupe(i).getEmail()).getUsername().equals(memberList.get(x).getUsername())) {
												userAlreadyAdded = true;
											}
										}
										if(!userAlreadyAdded) { // si le membre n'est pas dans le groupe, on l'ajoute
											int userId = git.getUserApi().getUserByEmail(groupe.getGroupe(i).getEmail()).getId();
											git.getGroupApi().addMember(NewGroup.getId(), userId, AccessLevel.DEVELOPER);
										}
										
									}
									
								}catch(java.lang.NullPointerException ex){
									//l'api renvoie une valeur nulle dans le cas ou l'utilisateur n'existe pas
									comptesInexistant = true;
								}
							}
							
							if(groupeParent != null) {
								 JOptionPane.showMessageDialog(null, "Le sous groupe a été créé avec succès dans le groupe "+ groupeParent.getName() + " !", "Succès!", JOptionPane.INFORMATION_MESSAGE);
								 if (comptesInexistant == true) {
										JOptionPane.showMessageDialog(null, "Certaines adresses email ne correspondent à aucun compte Gitlab n'ont pas pu être ajoutées au groupe");
									}
								 parentFrame.closeWindow();
							 }else {
								 JOptionPane.showMessageDialog(null, "Le groupe a été créé avec succès ", "Succès!", JOptionPane.INFORMATION_MESSAGE);
								 if (comptesInexistant == true) {
										JOptionPane.showMessageDialog(null, "Certaines adresses email ne correspondent à aucun compte Gitlab n'ont pas pu être ajoutées au groupe");
									}
							 }
							
							
						} catch (GitLabApiException er) {
							er.printStackTrace();
							JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la création du groupe, \n veuillez vérifier si le nom de votre groupe est correct");
							JOptionPane.showMessageDialog(null, "le nom de groupe ne peut être composé que de lettres, de chiffres, d'espaces ou de '_', \n Le nom ne peut pas être identique à un groupe déjà existant");
						}
				    }
					
					
					
				}
			});
		}
		

		/**
		 * Méthode d'initialisation de la comboBox contenant les groupes de l'utilisateur
		 * 
		 * @param comboBox : JComboBox
		 * @param parent : Group
		 */
		public static void initComboBox(JComboBox<String> comboBox,List<Integer> groupIdList, org.gitlab4j.api.models.Group parent) {
			if(parent != null) {
				comboBox.addItem(parent.getName());
			}else {
				GitLabApi git = LoginApiCall.getGitLabApi();
				try {
					List<org.gitlab4j.api.models.Group> GroupList = git.getGroupApi().getGroups("");
					for(int i=0;i<GroupList.size();i++) {
						comboBox.addItem(GroupList.get(i).getName());
						groupIdList.add(GroupList.get(i).getId());
					}
				}catch(GitLabApiException er) {
					er.printStackTrace();
				}
			}
		}
	
	public class myTableModel extends DefaultTableModel

	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Modèle de tableau utilisé dans les JTable.
		 * 
		 */
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
	 *  Méthode de vérification la validité d'un email via une regex.
	 * 
	 * @param email : String
	 * 
	 * @return un booléen indiquant si le mail est valide ou non.
	 */
	 public static boolean isEmailValid(String email)
	    {
	        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
	                            "[a-zA-Z0-9_+&*-]+)*@" +
	                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
	                            "A-Z]{2,7}$";
	                              
	        Pattern pat = Pattern.compile(emailRegex);
	        if (email == null)
	            return false;
	        return pat.matcher(email).matches();
	    }
	
}


