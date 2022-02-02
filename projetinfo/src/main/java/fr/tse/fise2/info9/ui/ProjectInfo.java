package fr.tse.fise2.info9.ui;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.ProjectFetches;
import fr.tse.fise2.info9.services.ApiConnection;
import fr.tse.fise2.info9.services.DataProject;
import fr.tse.fise2.info9.services.LoginApiCall;
import fr.tse.fise2.info9.services.OpenImage;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;

/**
 * 
 * Classe ProjectInfo : JFrame  affichant les données du projet séléctionné dans tabProjet
 * Contient tableau de stats, tableau de membre, PieChart, SpiderChart et Histogrammes
 *
 */
public class ProjectInfo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel spiderPanel;
	private JPanel pieChartPanel;
	private JPanel histogramme;
	private DataProject dp;
	private List<MergeRequest> list_mergesList;
	private JTable tableSpider;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectInfo frame = new ProjectInfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructeur : create the frame.
	 */
	public ProjectInfo() {
		setResizable(false);
		setSize(1000, 800);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		ImageIcon img = new ImageIcon(OpenImage.openImage("icon.png"));
	    setIconImage(img.getImage());
	    
		try {
			String access_token = LoginApiCall.getGitLabApi().getAuthToken();
			
			Map<String, String> map = ApiConnection.getProjectInfo(access_token, TabProjects.getProjectName());
			List<Member> membersList = LoginApiCall.getGitLabApi().getProjectApi().getAllMembers(map.get("id"));
			ProjectFetches stats = LoginApiCall.getGitLabApi().getProjectApi().getProjectStatistics(map.get("id"));

			Project project = LoginApiCall.getGitLabApi().getProjectApi().getProject(map.get("id"), true);
			list_mergesList = LoginApiCall.getGitLabApi().getMergeRequestApi().getMergeRequests(map.get("id"));
			contentPane.setLayout(null);
			JTable projectTable = new JTable();
			projectTable.setEnabled(false);
			projectTable.setFillsViewportHeight(true);
			projectTable.setFont(new Font("Verdana", Font.PLAIN, 13));
			projectTable.getTableHeader().setOpaque(false);
			projectTable.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 20));
			projectTable.getTableHeader().setBackground(new Color(252, 109, 38));
			JScrollPane projectScrollPane = new JScrollPane(projectTable);
			projectScrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
			projectScrollPane.setBackground(Color.WHITE);
			projectScrollPane.setBounds(58, 11, 410, 197);
			getContentPane().add(projectScrollPane);
			String[][] data = new String[10][1];
			String[] columnName = { "Projet : "+ map.get("name") };

			//data[0][0] = "Nom : " + map.get("name");

			if (map.get("description").isEmpty())
				data[0][0] = "Pas de description";
			else
				data[0][0] = "Description : " + map.get("description");

			if (map.get("visibility") == "public")
				data[1][0] = "Visibilité : publique";
			else
				data[1][0] = "Visibilité : privée";

			data[2][0] = "Date de création : " + map.get("creation_date");

			if (map.get("devops") == "true")
				data[3][0] = "Auto-DevOps : activé";
			else
				data[3][0] = "Auto-DevOps : désactivé";

			data[4][0] = "Nombre de forks : " + map.get("forks_count");

			data[5][0] = "Nombre de stars : " + map.get("star_count");

			if (map.get("issues_enabled") == "true")
				data[6][0] = "Issues : activé";
			else
				data[6][0] = "Issues : désactivé";

			if (map.get("merge_requests_enabled") == "true")
				data[7][0] = "Merge requests : activé";
			else
				data[7][0] = "Merge requests : désactivé";

			data[8][0] = "Nombre de fetches : " + stats.getFetches().getTotal();
			data[9][0] = "Taille du projet : " + project.getStatistics().getRepositorySize() / 1000000d + " MB";

			TableModel model = new DefaultTableModel(data, columnName);
			projectTable.setModel(model);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			projectTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			

			JTable projectTable1 = new JTable();
			projectTable1.setEnabled(false);
			projectTable1.setFillsViewportHeight(true);
			projectTable1.setFont(new Font("Verdana", Font.PLAIN, 13));
			projectTable1.getTableHeader().setOpaque(false);
			projectTable1.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 18));
			projectTable1.getTableHeader().setBackground(new Color(252, 109, 38));
			JScrollPane projectScrollPane1 = new JScrollPane(projectTable1);
			projectScrollPane1.setBorder(new LineBorder(Color.DARK_GRAY));
			projectScrollPane1.setBackground(Color.WHITE);
			projectScrollPane1.setBounds(548, 11, 410, 197);
			getContentPane().add(projectScrollPane1);

			String[][] data1 = new String[11][2];
			String[] columnName1 = { "Membres du projet", "Nom d'utilisateurs" };
			Iterator<Member> iterator = membersList.iterator();
			int j = 0;
			while (iterator.hasNext() && j < 10) {
				Member member = iterator.next();
				
				data1[j][0] = member.getName();
				data1[j][1] = member.getUsername();
				j++;
			}
			TableModel model1 = new DefaultTableModel(data1, columnName1);
			projectTable1.setModel(model1);
			DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
			centerRenderer1.setHorizontalAlignment(JLabel.CENTER);
			projectTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer1);
			projectTable1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer1);


			dp = new DataProject();
			dp.newDataFile();
			String res[][] = dp.getDataSpider(TabProjects.getProjectName(), "data.txt");
			

			spiderPanel = new JPanel();
			spiderPanel.setBounds(548, 219, 410, 270);
			contentPane.add(spiderPanel);
			spiderPanel.add(SpiderChart.affichage(res));

			
			Map<String, Float> languages1 = new HashMap<String,Float>();
	  	    try {
				languages1 = LoginApiCall.getGitLabApi().getProjectApi().getProjectLanguages(map.get("id"));
				pieChartPanel = new JPanel();
				pieChartPanel.setBounds(58, 219, 410, 270);
				contentPane.add(pieChartPanel);
				pieChartPanel.add(PieChart_AWT.affichage("Langages utilisés", languages1));

				histogramme = new Histogramme(list_mergesList);

				histogramme.setBounds(58, 500, 650, 238);
				contentPane.add(histogramme);

				String[][] donnees = new String[5][4];
				donnees[1][0] = "Projet";
				donnees[0][1] = "Commits";
				donnees[0][2] = "Merges";
				donnees[0][3] = "Members";
				donnees[2][0] = "Maximum";
				donnees[3][0] = "Moyenne";
				donnees[4][0] = "Minimum";
				// Projet
				int m = 3;
				for (int n = 0; n < 3; n++) {
					donnees[1][n + 1] = res[m][n + 3] ;
				}
				// max
				m = 2;
				for (int n = 0; n < 3; n++) {
					donnees[2][n + 1] = res[m][n];
				}
				m = 0;
				for (int n = 0; n < 3; n++) {
					donnees[3][n + 1] = res[m][n];
				}
				m = 1;
				for (int n = 0; n < 3; n++) {
					donnees[4][n + 1] = res[m][n];
				}
				String[] columnNameSpider = { " ", "Commits", "Merge requests", "Membres" };
				TableModel modelSpider = new DefaultTableModel(donnees, columnNameSpider);
				tableSpider = new JTable();
				tableSpider.setModel(modelSpider);
				tableSpider.setEnabled(false);
				tableSpider.setFillsViewportHeight(true);
				tableSpider.setFont(new Font("Verdana", Font.PLAIN, 10));
				tableSpider.getTableHeader().setOpaque(false);
				tableSpider.getTableHeader().setFont(new Font("Verdana", Font.PLAIN, 18));
				tableSpider.getTableHeader().setBackground(new Color(252, 109, 38));
				for (int i = 0; i < 3; i++)
					tableSpider.getColumnModel().getColumn(i).setMaxWidth(60);
				tableSpider.setBounds(718, 562, 240, 115);
				contentPane.add(tableSpider);

				JLabel labelTabSpider = new JLabel("Données du SpiderChart");
				labelTabSpider.setBounds(718, 537, 155, 14);
				contentPane.add(labelTabSpider);

			} catch (GitLabApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException | GitLabApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public List<MergeRequest> getListMergeReq(){
		return list_mergesList;
	}
}