package fr.tse.fise2.info9.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import fr.tse.fise2.info9.services.LoginApiCall;
import javax.swing.SwingConstants;


/**
 * Fenêtre de suppression d'un membre à un groupe.
 * 
 */
public class DeleteMemberMenu extends JFrame {
	
	private static final long serialVersionUID = 879778934582438671L; //id du du composant dans le menu principal
	private JPanel panel; // panel dans lequel son placé les autres composants
	private JScrollPane scrollPane; //scrollPane ou est affiché le tableau des membres à supprimer
	private JLabel header; // titre de la fenêtre
	private JLabel lblGroupe; // nom du groupe
	private JTable table;  //tableau ou sont affichés les membres à supprimer
	private JButton btnNewButton_1; //bouton de suppression du membre sélectionné dans le tableau
	private String[][] members; //liste des membres du groupe (version utilisée pour le tableau)
	private List<Member> memberList = new ArrayList<Member>(); // liste des membres du groupe
	private org.gitlab4j.api.models.Group group; // le groupe ou l'on veut supprimer les membres
	private TabGroupes groupMenu; //le panel parent pour pouvoir ensuite actualiser l'affichage après la suppression des membres
	
	/**
	 * Constructeur du menu à partir du panel parent et du groupe ciblé.
	 * 
	 * @param parentFrame : TabGroupes
	 * @param displayedGroup : Group
	 */
	public DeleteMemberMenu(TabGroupes parentFrame,org.gitlab4j.api.models.Group displayedGroup) {
			
		group = displayedGroup;
		groupMenu = parentFrame;
		
		getContentPane().setLayout(null);
		setBounds(450, 135, 400, 340);
		panel = new JPanel();
		panel.setBounds(10, 10, 365, 290);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		header = new JLabel("Supprimer un membre du groupe");
		header.setBounds(28, 0, 308, 34);
		header.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(header);
		
		table = new JTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				btnNewButton_1.setEnabled(true);
			}
			
		});
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(20, 70, 325, 160);
		panel.add(scrollPane);
		
		btnNewButton_1 = new JButton("Supprimer");
		btnNewButton_1.setBounds(107, 240, 151, 23);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setEnabled(false);
		panel.add(btnNewButton_1);
		
		lblGroupe = new JLabel(group.getName());
		lblGroupe.setHorizontalAlignment(SwingConstants.CENTER);
		lblGroupe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGroupe.setBounds(28, 33, 308, 34);
		panel.add(lblGroupe);
		setVisible(true);
		
		btnNewButton_1.addActionListener(new ActionListener() {// ActionListener d'ajout du tableau de membre au groupe
			public void actionPerformed(ActionEvent e) {
				GitLabApi git = LoginApiCall.getGitLabApi();
				
				try {
					git.getGroupApi().removeMember(group.getId(),memberList.get(table.getSelectedRow()).getId() );
					JOptionPane.showMessageDialog(null,memberList.get(table.getSelectedRow()).getUsername() + " a bien été retiré du groupe", "Succès!", JOptionPane.INFORMATION_MESSAGE);
					groupMenu.redrawGroupSheet();
					dispose();
				}catch(GitLabApiException er) {
					er.printStackTrace();
				}
			}
		});
		
		initTable();
	}
	
	/**
	 * Méthode pour initialiser le tableau avec les membres du groupe.
	 */
	public void initTable(){
		GitLabApi git = LoginApiCall.getGitLabApi();
		
		try {
			memberList = git.getGroupApi().getMembers(group.getId());
			
			//création d'un nouveau tableau contenant le nouveau membre
			members = new String[memberList.size()][1];
			String[] columnName = {"membres du groupe " + " (" + memberList.size()  + ")"};
			for(int i=0;i<memberList.size();i++) {
				members[i][0] = memberList.get(i).getUsername();
			}
			
			//actualisation du model pour raffraichir le tableau
			TableModel model = new myTableModel(members,columnName);
			table.setModel(model);
			
		}catch(GitLabApiException er) {
			er.printStackTrace();
		}
	}
	
	/**
	 * Modèle de tableau utilisé dans les JTable.
	 * 
	 * @param data : String[][]
	 * @param columName : String[] 
	 */
	public class myTableModel extends DefaultTableModel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
