package fr.tse.fise2.info9.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import fr.tse.fise2.info9.services.Encrypter;
import fr.tse.fise2.info9.services.LoginApiCall;
import fr.tse.fise2.info9.services.OpenImage;
import fr.tse.fise2.info9.services.OpenLink;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

/**
 * 
 * Fenêtre de connexion
 * 
 */
public class LoginMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel_4;
	private JButton mdp;
	private JButton btnNewButton;
	private boolean remember_me = false;
	private JTextField ip_git;

	/**
	 * 
	 * Menu de connexion
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public LoginMenu() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {

		// Construction de la fenêtre et placement des différents éléments
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ImageIcon img = new ImageIcon(OpenImage.openImage("icon.png"));
		setIconImage(img.getImage());

		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setBounds(0, 0, 235, 372);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(25, 27, 184, 78);
		panel.add(lblNewLabel);
		lblNewLabel.setIcon(new ImageIcon(OpenImage.openImage("gitse.png")));

		//Permet de sélectionné l'ip de connexion du git
		ip_git = new JTextField();
		ip_git.setToolTipText("L'addresse ip de l'instance de git");
		ip_git.setText("https://code.telecomste.fr");
		ip_git.setBounds(372, 329, 185, 20);
		contentPane.add(ip_git);
		ip_git.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Nom d'utilisateur");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(290, 86, 198, 25);
		contentPane.add(lblNewLabel_1);

		textField = new JTextField();
		textField.setBounds(290, 121, 311, 19);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Mot de passe");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(290, 150, 125, 25);
		contentPane.add(lblNewLabel_2);

		textField_1 = new JPasswordField();
		textField_1.setBounds(290, 185, 311, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		lblNewLabel_4 = new JLabel("<HTML><U>Nom d'utilisateur ou mot de passe est incorrect</U></HTML>");
		lblNewLabel_4.setForeground(Color.RED);
		lblNewLabel_4.setBounds(290, 245, 311, 29);
		contentPane.add(lblNewLabel_4);
		lblNewLabel_4.setVisible(false);

		// Bouton pour se connecter
		btnNewButton = new JButton("Se connecter");
		btnNewButton.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					btnNewButton.setForeground(new Color(252, 163, 38));
				} else {
					btnNewButton.setForeground(new Color(252, 109, 38));
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code permettant la connexion au Git
				try {
					// Tentative de connexion
					LoginApiCall.setGitLabApi(LoginApiCall.gitlabConnection(textField.getText(), textField_1.getText(),
							ip_git.getText()));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// Si la connexion a réussi
				if (LoginApiCall.getGitLabApi() != null) {
					// Et si on a choisi de sauvegarder notre mot de passe
					if (remember_me) {
						// On chiffre le login, le mdp, et l'ip du git dans le fichier data.enc
						File file = new File("dateenc.txt");
						file.delete();
						File file_enc = new File("data.enc");
						file_enc.delete();
						try {
							file.createNewFile();
							PrintWriter writer = new PrintWriter("dateenc.txt", "UTF-8");
							writer.println(textField.getText());
							writer.println(textField_1.getText());
							writer.println(ip_git.getText());
							writer.close();
							Encrypter.encryptedFile(Encrypter.key, "dateenc.txt", "data.enc");
							file.delete();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvalidKeyException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchPaddingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalBlockSizeException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (BadPaddingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else {
						File file = new File("dateenc.txt");
						file.delete();
						File file_enc = new File("data.enc");
						file_enc.delete();
					}
					MainMenu newFrame = new MainMenu();
					newFrame.setLocationRelativeTo(null);
					newFrame.setVisible(true);
					lblNewLabel_4.setVisible(false);
					dispose();
				} else {
					lblNewLabel_4.setVisible(true);
					textField_1.setText("");
				}
			}
		});

		// Reste de la construction de la fenêtre
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setBounds(405, 284, 125, 21);
		btnNewButton.setForeground(new Color(252, 109, 38));
		contentPane.add(btnNewButton);
		btnNewButton.setForeground(new Color(252, 109, 38));

		JLabel lblNewLabel_3 = new JLabel("Bienvenue");
		lblNewLabel_3.setFont(new Font("MV Boli", Font.BOLD, 25));
		lblNewLabel_3.setBounds(405, 30, 132, 25);
		lblNewLabel_3.setForeground(new Color(252, 109, 38));
		contentPane.add(lblNewLabel_3);

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Se souvenir de moi");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxNewCheckBox.isSelected()) {
					remember_me = true;
				} else {
					remember_me = false;
				}
			}
		});
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chckbxNewCheckBox.setBounds(290, 218, 164, 21);
		contentPane.add(chckbxNewCheckBox);

		// Lors de l'ouverture de la fenêtre, si le fichier chiffré est déjà la, cela
		// veut dire qu'un login est déjà sauvegardé, on va donc le lire
		File file = new File("data.enc");
		if (file.exists()) {
			Encrypter.decryptedFile(Encrypter.key, "data.enc", "dateenc.txt");
			BufferedReader reader = new BufferedReader(new FileReader("dateenc.txt"));
			String line = reader.readLine();
			textField.setText(line);
			line = reader.readLine();
			textField_1.setText(line);
			line = reader.readLine();
			ip_git.setText(line);
			chckbxNewCheckBox.setSelected(true);
			remember_me = true;
			reader.close();
			File file_txt = new File("dateenc.txt");
			file_txt.delete();
		}

		mdp = new JButton("Mot de passe oublié ?");
		mdp.setBounds(445, 222, 176, 13);
		mdp.setForeground(new Color(252, 109, 38));
		mdp.setContentAreaFilled(false);
		mdp.setBorderPainted(false);
		mdp.setFocusPainted(false);
		mdp.setOpaque(true);
		mdp.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					mdp.setForeground(new Color(252, 163, 38));
				} else {
					mdp.setForeground(new Color(252, 109, 38));
				}
			}
		});

		mdp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//On met l'ip de gitlab.com pour les mots de passe oubliés
				String str = "https://gitlab.com/users/password/new";
				URI uri;
				try {
					uri = new URI(str);
					OpenLink.openWebpage(uri);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(mdp);
		getRootPane().setDefaultButton(btnNewButton);

	}
}
