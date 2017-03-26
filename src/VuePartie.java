
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

/** Cette classe permet de visualiser des éléments tels que le niveau courant 
* ou encore le nombre de pas effectués.
* Elle permet également de refaire les niveaux complétés
*/

public class VuePartie extends JPanel implements Observer {

	private Plateau p;
	private JLabel nbPas,nbPousses, lvlCourant;
	
	public VuePartie(Plateau p) {
		super(new GridLayout(1,3));
		this.p = p;
		nbPas = new JLabel(" Pas : "+p.getNbPas());
		nbPas.setHorizontalAlignment(SwingConstants.CENTER);
		add(nbPas);
		nbPousses = new JLabel(" Pousses : "+p.getNbPousses());
		nbPousses.setHorizontalAlignment(SwingConstants.CENTER);
		add(nbPousses);
		lvlCourant = new JLabel(" Niveau "+p.getPartie().getNiveauCourant()+" sur 100");
		lvlCourant.setHorizontalAlignment(SwingConstants.CENTER);
		add(lvlCourant);
		p.addObserver(this);
		
	}
	
	public void update(Observable o, Object a) {
		nbPas.setText(" Pas : "+p.getNbPas());
		nbPousses.setText(" Pousses : "+p.getNbPousses());
		lvlCourant.setText(" Niveau "+p.getPartie().getNiveauCourant()+" sur 100");
	}
}