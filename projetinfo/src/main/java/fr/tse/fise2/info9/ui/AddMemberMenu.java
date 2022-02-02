package fr.tse.fise2.info9.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;
import fr.tse.fise2.info9.services.LoginApiCall;


/**
 * 
 * Fenêtre d'ajout d'un membre à un groupe.
 * 
 */
public class AddMemberMenu extends JFrame {
	
	private static final long serialVersionUID = 879778934582438671L; //id du du composant dans le menu principal
	private JPanel panel; // panel dans lequel son placé les autres composants
	private JScrollPane scrollPane; //scrollPane ou est affiché le tableau des membres à ajouter
	private JLabel header; // titre de la fenêtre
	private JTable table;  //tableau ou sont affichés les membres à ajouter
	private JTextField textField;  // input ou l'utilisateur saisi les mails des membres à ajouter
	private JButton btnNewButton; // boutton d'ajout d'un membre au tableau
	private JButton btnNewButton_1; //bouton d'ajout des membres saisis dans le tableau vers le groupe
	private String[][] members; //liste des membres à ajouter (version utilisée pour le tableau)
	private List<User> memberList = new ArrayList<User>(); // liste des membres à ajouter
	private org.gitlab4j.api.models.Group group; // le groupe ou l'on veut ajouter les membres
	private TabGroupes groupMenu; //le panel parent pour pouvoir ensuite actualiser l'affichage après l'ajout des membres
	
	/**
	 * Constructeur du menu à partir du panel parent et du groupe ciblé.
	 * Le constructeur déclare tous les éléments graphiques de la fenêtre et les Listeners des boutons.
	 * 
	 * @param parentFrame : TabGroupes
	 * @param displayedGroup : Group
	 * 
	 */
	public AddMemberMenu(TabGroupes parentFrame,org.gitlab4j.api.models.Group displayedGroup) {
			
		group = displayedGroup;
		groupMenu = parentFrame;
		
		getContentPane().setLayout(null);
		setBounds(450, 135, 400, 360);
		panel = new JPanel();
		panel.setBounds(10, 10, 365, 310);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		header = new JLabel("Ajouter des membres");
		header.setBounds(87, 11, 191, 34);
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(header);
		
		table = new JTable();
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(20, 70, 325, 160);
		panel.add(scrollPane);
		
		textField = new JTextField();
		textField.setText("saisir le mail du membre à ajouter");
		textField.setBounds(20, 241, 280, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		btnNewButton = new JButton("+");
		btnNewButton.setBounds(300, 241, 45, 20);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(btnNewButton);
		
		btnNewButton_1 = new JButton("Ajouter les membres");
		btnNewButton_1.setBounds(107, 272, 151, 23);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setEnabled(false);
		panel.add(btnNewButton_1);
		setVisible(true);
		
		btnNewButton.addActionListener(new ActionListener() { //actionListener de saisie d'un membre dans le tableau
			public void actionPerformed(ActionEvent e) {
				GitLabApi git = LoginApiCall.getGitLabApi();
				try {
					String email = textField.getText(); //récupération de l'email saisi
					if(isEmailValid(email)) {
						try {
							User user = git.getUserApi().getUserByEmail(email); //récupération de l'utilisateur gitlab via son email
							boolean userAlreadyAdded = false;
							
							for(int i=0; i<memberList.size();i++) {// on vérifie si l'utilisateur est déjà ajouté dans le tableau
								if(user.getUsername().equals(  memberList.get(i).getUsername())) {
									userAlreadyAdded = true;
								}
							}
							
							if(!userAlreadyAdded) {//si l'utilisateur n'est pas dans le tableau, on continue
								//ajout de l'utilisateur à la liste des membres à ajouter
								memberList.add(user);
								
								//création d'un nouveau tableau contenant le nouveau membre
								members = new String[memberList.size()][1];
								String[] columnName = {"membres à ajouter " + " (" + memberList.size()  + ")"};
								for(int i=0;i<memberList.size();i++) {
									members[i][0] = memberList.get(i).getUsername();
								}
								
								//actualisation du model pour raffraichir le tableau
								TableModel model = new myTableModel(members,columnName);
								table.setModel(model);
								
								textField.setText("");//remise à zéro du textField
								if(!btnNewButton_1.isEnabled()) {// on dégrise le bouton d'ajout des membres au groupe si ce n'est pas déjà fait
									btnNewButton_1.setEnabled(true);
								}
							}else {
								JOptionPane.showMessageDialog(null,"Vous avez déjà saisi cet utilisateur");
							}
							
						}catch(java.lang.NullPointerException ex) { //erreur renvoyée si le compte n'existe pas
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null,"Le mail saisi ne correspond à aucun compte gitlab");
						}
					}else {
						JOptionPane.showMessageDialog(null,"Le mail saisi est invalide");
					}
				}catch(GitLabApiException er) {
					er.printStackTrace();
				}
			}
		});
		
		btnNewButton_1.addActionListener(new ActionListener() {// ActionListener d'ajout du tableau de membre au groupe
			public void actionPerformed(ActionEvent e) {
				GitLabApi git = LoginApiCall.getGitLabApi();
				
				try {
					// récupération des anciens membres du groupe
					List<Member>  groupList =git.getGroupApi().getMembers(group.getId());
					
					for(int i=0; i<memberList.size();i++) {// pour chaque membre à ajouter, on vérifie si le membre est déjà dans le groupe ou non
						boolean userAlreadyAdded = false;
						for(int j=0; j<groupList.size();j++) {
							if(groupList.get(j).getUsername().equals(memberList.get(i).getUsername())) {
								userAlreadyAdded = true;
							}
						}
						if(!userAlreadyAdded) { // si le membre n'est pas dans le groupe, on l'ajoute
							git.getGroupApi().addMember(group.getId(), memberList.get(i).getId(),AccessLevel.DEVELOPER);
						}
					}
					//raffraichissement de la fiche groupe pour afficher les nouveaux membres et fermeture de la fenêtre
					groupMenu.redrawGroupSheet();
					dispose();
				}catch(GitLabApiException er) {
					er.printStackTrace();
				}
			}
		});
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
		 * @param data : String[][]
		 * @param columName : String[] 
		 */
		myTableModel(String[][] data,String[] columnName)
		{
			super(data, columnName);
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
