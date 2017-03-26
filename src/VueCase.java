
import javax.swing.*;
import java.util.*;
/** Cette classe permet de visualiser une case du plateau
*/

public class VueCase extends JLabel implements Observer {
	
	private Case caseVisu;
	private ImageIcon[] images = {
		new ImageIcon(getClass().getResource("ressources/vide.gif")),
		new ImageIcon(getClass().getResource("ressources/mur.gif")),
		new ImageIcon(getClass().getResource("ressources/caisse.gif")),
		new ImageIcon(getClass().getResource("ressources/perso.gif")),
		new ImageIcon(getClass().getResource("ressources/zone.gif")),
		new ImageIcon(getClass().getResource("ressources/persoZone.gif")),
		new ImageIcon(getClass().getResource("ressources/caisseOK.gif")),
		};
	
	public VueCase(Case c) {
		super();
		this.caseVisu = c;
		this.setIcon(images[caseVisu.getStatut()]);
		caseVisu.addObserver(this);
	}
	
	public void setCase(Case c) {
		this.caseVisu = c;
		this.setIcon(images[caseVisu.getStatut()]);
		caseVisu.addObserver(this);
	}
	
	public void update(Observable obs, Object o) {
		this.setIcon(images[caseVisu.getStatut()]);
	}
	
}
