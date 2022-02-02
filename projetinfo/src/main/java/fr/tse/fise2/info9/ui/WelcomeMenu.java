package fr.tse.fise2.info9.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;
import fr.tse.fise2.info9.services.OpenImage;

/**
 * Cette classe représente le welcomemenu, qui hérite de JWindows, et donc cette fenetre se ferme apres un intervalle de temps donné.
 * Elle sert à afficher une loading screen avant que le projet soit pret pour utilisation. 
 */
public class WelcomeMenu extends JWindow {

	JPanel panel = new JPanel();
	JProgressBar bar = new JProgressBar();
	
	private static final long serialVersionUID = 1L;
	/**
	 * Create the window.
	 * @throws SQLException 
	 */
	public WelcomeMenu() {
		setSize(700, 400);
		setLocationRelativeTo(null);
		panel.setLayout(null);
		bar.setBounds(0, 384, 700, 16);
		panel.add(bar);
		panel.setBackground(Color.WHITE);
		bar.setStringPainted(true);
		bar.setForeground(new Color(252, 109, 38));
		bar.setBackground(Color.white);
		bar.setFont(new Font("MV Boli", Font.BOLD, 15));
		bar.setBorder(BorderFactory.createLineBorder(new Color(252, 109, 38)));
		getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(165, 10, 368, 384);
		lblNewLabel.setIcon(new ImageIcon(OpenImage.openImage("gitse black.png")));
		panel.add(lblNewLabel);
		
		// Le listener qui gere l'affichage du chargement en fonction de l'intervalle du temps donné
		Timer timer = new Timer(100, new ActionListener() {
			int progress = 0;

			public void actionPerformed(ActionEvent e) {
				bar.setValue(progress);
				progress += 5;
				panel.repaint();
				if (progress == 100) {
					((Timer) e.getSource()).stop();
					dispose();
					//DatabaseConnection dbConn = new DatabaseConnection();
					//ResultSet result = dbConn.fetchCurrentUser();
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
					
						
					
				}
			}
			
		});
		
		timer.start();
	}

}
