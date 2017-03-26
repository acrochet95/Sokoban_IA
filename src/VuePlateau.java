
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;

public class VuePlateau extends JPanel implements KeyListener, Observer {
	
	private Plateau p;
	private VueCase[][] cases = new VueCase[16][19];
	private int nbLignes;
	private int nbColonnes;
	
	public VuePlateau(Plateau p) {
		nbLignes = 16;
		nbColonnes = 19;
		setLayout(new GridLayout(nbLignes,nbColonnes));
		this.p = p;
		for(int i = 0; i < nbLignes; i++) {
			for(int j = 0; j < nbColonnes; j++) {
				Case c = p.caseEn(i,j);
				VueCase vc = new VueCase(c);
				cases[i][j] = vc;
				add(vc);
			}//for
		}//for
		p.addObserver(this);
	}//Vueplateau
	
	public void update(Observable o, Object obj) {
		if (p.termine() || p.reinit()) {
			if (p.termine()) JOptionPane.showMessageDialog(this,"Félicitations, vous avez terminé le niveau","Niveau terminé",JOptionPane.INFORMATION_MESSAGE);
			for(int i = 0; i < nbLignes; i++) {
		   	 for(int j = 0; j < nbColonnes; j++) {
				cases[i][j].setCase(p.caseEn(i,j));
		   	 }//for
			}//for
		}//if
	}//update()
	
	public void afficherSolution(ArrayList<Integer> dir){
		for(int direction : dir){
			//System.out.println(direction);
			p.bougerPerso(direction);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void keyPressed(KeyEvent e) {
		p.bougerPerso(e.getKeyCode());
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
}//class
