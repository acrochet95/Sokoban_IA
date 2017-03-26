
import javax.swing.*;
import java.awt.event.*;

public class MenuSoko extends JMenuBar {
	
	private Plateau p;
	private String help = "Ceci est une version du Sokoban faite en Java\n"+
		"Le but est de placer toutes les caisses sur les zones de rangement\n"+
		"en faisant le moins de pas/pousses possible.";
	private String aPropos = "JSoko Version 1.0.1 \n"+
		"Les nivaux fournis ont été créé par\nKevin B. Reilly et sont sous copyright.";
	
	public MenuSoko(Plateau p) {
		this.p = p;
		JMenu partie = new JMenu("Partie");
		JMenu options = new JMenu("Options de jeu");
		
		JMenuItem first = new JMenuItem("Aller au premier niveau");
		first.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuSoko.this.p.initialiserPlateau(1);
			}
		});
		options.add(first);
		JMenuItem previous = new JMenuItem("Aller au niveau precedant");
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuSoko.this.p.getPartie().goToPreviousLevel();
				MenuSoko.this.p.initialiserPlateau(MenuSoko.this.p.getPartie().getNiveauCourant());
			}
		});
		options.add(previous);
		JMenuItem recommencer = new JMenuItem("Recommencer le niveau courant");
		recommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuSoko.this.p.recommencer();
			}
		});
		options.add(recommencer);
		JMenuItem next = new JMenuItem("Aller au niveau suivant");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int suivant = MenuSoko.this.p.getPartie().getNiveauCourant() +1;
				int max = MenuSoko.this.p.getPartie().getNiveauMaxAtteint();
				if (suivant <= max)	MenuSoko.this.p.getPartie().goToNextLevel();
				MenuSoko.this.p.initialiserPlateau(MenuSoko.this.p.getPartie().getNiveauCourant());
			}
		});
		options.add(next);
		JMenuItem last = new JMenuItem("Aller au dernier niveau atteint");
		last.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuSoko.this.p.initialiserPlateau(MenuSoko.this.p.getPartie().getNiveauMaxAtteint());
			}
		});
		options.add(last);
		partie.add(options);
		JMenuItem quit = new JMenuItem("Quitter");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		partie.add(quit);
		this.add(partie);
		JMenu aide = new JMenu("?");
		JMenuItem help = new JMenuItem("Aide");
		
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,MenuSoko.this.help,"Aide de JSoko",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aide.add(help);
		JMenuItem apd = new JMenuItem("A propos de ...");
		apd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,MenuSoko.this.aPropos,"A propos de ...",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aide.add(apd);
		this.add(aide);
	}
	
}