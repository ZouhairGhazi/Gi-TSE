package fr.tse.fise2.info9.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import fr.tse.fise2.info9.services.OpenImage;
import fr.tse.fise2.info9.services.DataProject;

/**
 * Cette classe est l'interface graphique principale du projet, hérite de JFrame
 * et contient le menu qui donne accès aux fonctionnalités de l'application.
 */

public class MainMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnNewButton;
	JButton btnNewButton_1;
	JButton btnNewButton_2;
	JButton btnNewButton_3;
	JButton btnNewButton_4;
	private Component comp_in_use = null;
	private MainMenu frame = this;

	private DataProject dp;
	// private Map<String, Project> map;

	/**
	 * La fonction main qui lancer cette fenetre.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Le constructeur qui créé la fenetre de type MainMenu
	 */
	public MainMenu() {

		// Des fonctions affectant la frame principale, le "this" ici n'est pas
		// nécessaire car on est dans le constructeur
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 645);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Le logo de gitlab est mis comme favicon de l'interface
		ImageIcon img = new ImageIcon(OpenImage.openImage("icon.png"));
		setIconImage(img.getImage());

		JPanel panel = new JPanel();
		panel.setBounds(0, 83, 210, 534);
		panel.setBackground(new Color(252, 109, 38));
		contentPane.add(panel);
		panel.setLayout(null);

		// La panel du bienvenue qui s'écrase une fois l'utilisateur passe sur une autre
		// frame.
		final JPanel panel_bienvenue = new JPanel();
		panel_bienvenue.setBounds(371, 221, 453, 275);
		contentPane.add(panel_bienvenue);

		JLabel bienvenueLabel2 = new JLabel("Bienvenue");
		bienvenueLabel2.setFont(new Font("MV Boli", Font.BOLD, 25));
		bienvenueLabel2.setBounds(401, 204, 160, 37);
		panel_bienvenue.add(bienvenueLabel2);
		JLabel bienvenueLabel = new JLabel();
		bienvenueLabel.setIcon(new ImageIcon(OpenImage.openImage("gitse black.png")));
		panel_bienvenue.add(bienvenueLabel);

		// Le bouton pour lancer la création d'un groupe
		btnNewButton = new JButton("Créer groupe");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(0, 370, 210, 61);
		btnNewButton.setBackground(new Color(252, 109, 38));
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setOpaque(true);
		panel.add(btnNewButton);
		btnNewButton.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					btnNewButton.setFont(btnNewButton.getFont().deriveFont(Font.BOLD));
				} else {
					btnNewButton.setFont(btnNewButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.remove(panel_bienvenue);
				if (comp_in_use != null) {
					contentPane.remove(comp_in_use);
					if (comp_in_use.getClass().toString().contains("CreateProject")) {
						createDataFiles();
					}
				}

				comp_in_use = contentPane.add(new GroupChoiceMenu(contentPane, frame));

				contentPane.revalidate();
				contentPane.repaint();
			}
		});

		// Le bouton pour lancer la visualisation des groupes
		btnNewButton_1 = new JButton("Voir groupes");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_1.setBounds(0, 170, 210, 61);
		btnNewButton_1.setBackground(new Color(252, 109, 38));
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setOpaque(true);
		panel.add(btnNewButton_1);

		// Un listener tres utilisé dans le projet, appliquant des effets quand
		// l'utilisateur hover sur un bouton.
		btnNewButton_1.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					btnNewButton_1.setFont(btnNewButton.getFont().deriveFont(Font.BOLD));
				} else {
					btnNewButton_1.setFont(btnNewButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				contentPane.remove(panel_bienvenue);
				if (comp_in_use != null) {
					contentPane.remove(comp_in_use);
					if (comp_in_use.getClass().toString().contains("CreateProject")) {
						createDataFiles();
					}
				}

				comp_in_use = contentPane.add(new TabGroupes(frame));

				contentPane.revalidate();
				contentPane.repaint();
			}
		});

		// Le bouton pour lancer la visualisation des projets
		btnNewButton_2 = new JButton("Voir projets");
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_2.setBounds(0, 70, 210, 61);
		btnNewButton_2.setBackground(new Color(252, 109, 38));
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setBorderPainted(false);
		btnNewButton_2.setFocusPainted(false);
		btnNewButton_2.setOpaque(true);
		panel.add(btnNewButton_2);
		createDataFiles();

		btnNewButton_2.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					btnNewButton_2.setFont(btnNewButton.getFont().deriveFont(Font.BOLD));
				} else {
					btnNewButton_2.setFont(btnNewButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});

		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				contentPane.remove(panel_bienvenue);

				if (comp_in_use != null) {
					contentPane.remove(comp_in_use);
					if (comp_in_use.getClass().toString().contains("CreateProject")) {
						createDataFiles();
					}
				}

				comp_in_use = contentPane.add(new TabProjects(dp));

				contentPane.revalidate();
				contentPane.repaint();

			}
		});

		// Le bouton pour lancer la création d'un projet
		btnNewButton_3 = new JButton("Créer projet");
		btnNewButton_3.setBounds(0, 270, 210, 61);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_3.setBackground(new Color(252, 109, 38));
		btnNewButton_3.setContentAreaFilled(false);
		btnNewButton_3.setBorderPainted(false);
		btnNewButton_3.setFocusPainted(false);
		btnNewButton_3.setOpaque(true);
		panel.add(btnNewButton_3);

		btnNewButton_3.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					btnNewButton_3.setFont(btnNewButton.getFont().deriveFont(Font.BOLD));
				} else {
					btnNewButton_3.setFont(btnNewButton.getFont().deriveFont(Font.PLAIN));
				}
			}
		});

		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				contentPane.remove(panel_bienvenue);
				if (comp_in_use != null) {
					contentPane.remove(comp_in_use);
					if (comp_in_use.getClass().toString().contains("CreateProject")) {
						createDataFiles();
					}
				}

				comp_in_use = contentPane.add(new CreateProjectMenu());

				contentPane.revalidate();
				contentPane.repaint();

			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.BLACK);
		panel_1.setBounds(0, 0, 996, 84);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(77, 5, 118, 74);
		lblNewLabel.setIcon(new ImageIcon(OpenImage.openImage("icon.png")));
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 26));

		// Le bouton de déconnexion, en renvoyant l'utilisateur au menu du login.
		btnNewButton_4 = new JButton("");
		btnNewButton_4.setIcon(new ImageIcon(OpenImage.openImage("logout4.png")));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginMenu frame;
				try {
					frame = new LoginMenu();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (InvalidKeyException e1) {
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				dispose();
			}
		});
		btnNewButton_4.setForeground(new Color(252, 109, 38));
		btnNewButton_4.setBounds(858, 10, 128, 61);
		panel_1.add(btnNewButton_4);
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_4.setBackground(Color.BLACK);
		btnNewButton_4.setContentAreaFilled(false);
		btnNewButton_4.setBorderPainted(false);
		btnNewButton_4.setFocusPainted(false);
		btnNewButton_4.setOpaque(true);

		btnNewButton_4.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					btnNewButton_4.setFont(btnNewButton_4.getFont().deriveFont(Font.BOLD));
				} else {
					btnNewButton_4.setFont(btnNewButton_4.getFont().deriveFont(Font.PLAIN));
				}
			}
		});

	}

	// Les fonctions reponsables de la gestion des panel qui sont visibles sur le
	// main menu.
	public Component getComp_in_use() {
		return comp_in_use;
	}

	public void setComp_in_use(Component comp_in_use) {
		this.comp_in_use = comp_in_use;
	}

	public void callGroupCreator() {
		if (comp_in_use != null) {
			contentPane.remove(comp_in_use);
			if (comp_in_use.getClass().toString().contains("CreateProject")) {
				createDataFiles();
			}
		}

		comp_in_use = contentPane.add(new GroupChoiceMenu(contentPane, frame));

		contentPane.revalidate();
		contentPane.repaint();
	}

	public void callProjectCreator() {
		if (comp_in_use != null) {
			contentPane.remove(comp_in_use);
			if (comp_in_use.getClass().toString().contains("CreateProject")) {
				createDataFiles();
			}
		}

		comp_in_use = contentPane.add(new CreateProjectMenu());

		contentPane.revalidate();
		contentPane.repaint();
	}

	public void createDataFiles() {
		dp = new DataProject();
		String data[][] = dp.getDataAPI();
		dp.dataFiles(data);
		// map=dp.getProjectList();
	}
}
