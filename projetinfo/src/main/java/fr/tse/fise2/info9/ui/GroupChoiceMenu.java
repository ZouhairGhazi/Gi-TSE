package fr.tse.fise2.info9.ui;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Menu de choix du mode de création de groupe (groupe unique ou lot de groupe).
 * 
 */
public class GroupChoiceMenu extends JPanel {
	
	private static final long serialVersionUID = 8797789345826765280L; // id du panel
	private JPanel panel; //panel interne
	
	/**
	 * Constructeur qui prend en paramètre le panel parent et la fenêtre du menu principal.
	 * Le constructeur déclare les composants du menu et ses listeners.
	 * 
	 * @param contentPane : JPanel
	 * @param frame : MainMenu
	 */
	public GroupChoiceMenu(final JPanel contentPane, final MainMenu frame) {
		
		setLayout(null);
		setBounds(220, 95, 604, 500);
		panel = new JPanel();
		panel.setBounds(10, 11, 583, 478);
		add(panel);
		panel.setLayout(null);
		
	
		
		JButton btnNewButton = new JButton("Créer un groupe");
		btnNewButton.setBounds(201, 180, 180, 40);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Créer un lot de groupes");
		btnNewButton_1.setBounds(201, 260, 180, 40);
		panel.add(btnNewButton_1);
		
		
		btnNewButton.addActionListener(new ActionListener() { // bouton de création de groupe unique
			public void actionPerformed(ActionEvent e) {
				contentPane.remove(frame.getComp_in_use());
				frame.setComp_in_use(contentPane.add(new CreateGroupMenu()));
				
				contentPane.revalidate();
				contentPane.repaint();
				
			}
		});
		
		btnNewButton_1.addActionListener(new ActionListener() { // bouton de création de lot de groupes
			public void actionPerformed(ActionEvent e) {
				contentPane.remove(frame.getComp_in_use());
				frame.setComp_in_use(contentPane.add(new CreateGroupLotMenu()));
				
				contentPane.revalidate();
				contentPane.repaint();
			}
		});
	}
	
}
