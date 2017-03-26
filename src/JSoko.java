
import Controlleur.Joueur;
import Controlleur.NoeudRecherche;
import Modèle.PlateauIA;
import niveaux.*;

public class JSoko {
	
	public static void main(String[] args){
		//Format Plateau(nom_fichier, hauteur, largeur, poids_caisses, poids_joueur)
		//PlateauIA p1 = new PlateauIA("./plateau1_8_21.txt",8,21);
		//PlateauIA p2 = new PlateauIA("./plateau2_7_6.txt",7,6);
		//PlateauIA p3 = new PlateauIA("./lvl4.sok",9,9); //Best 1 0.1  1 50
		//PlateauIA p3 = new PlateauIA("./lvl5.sok",11,11); //Best 1 0.1  1 50
		
		final int lvl = 107;
		PlateauIA p3 = new PlateauIA("./src/niveaux/lvl"+Integer.toString(lvl)+".sok");
		
		
		//Coeff Eval puis Coeff Coups
		NoeudRecherche.setCoefs(1, 1);
		
		//Coeff Caisses puis Coeff Joueur
		PlateauIA.setCoeffs(1, 1); //Si ces coefficients sont <=1, on obtient une solution optimale

		Joueur j = new Joueur(false,p3);
		
		
		//Lvl 1 assez particulier solution optimale le joueur reste collé aux caisses donc on trouve plus vite la soluton avec coeff dist J = 0 
		//Lvl 2 si on laisse comme ça stack overflow, solution évidente ( 1 / 2 ) puis (1/50)
		//Lvl 3 distance très petites donc eval pas importante // coups, on baisse le coeff coups R(1/1) puis R(1/0.1)
		//Lvl 4 pas réussi à la résoudre
		//Lvl 5 carte assez petite 1/1/1/1 passe mais si on diminue importance coups, on réduit la oritente très vite la recherche
		//Même choses levels suivants
		
		
	
		try{
			j.Astar(10000);
			j.afficherSolution();

		}
		catch(Error e){
			System.err.println(e);
			System.out.println(j.getIterations());
		}
		

		Partie partie = new Partie(lvl);
		Plateau p = new Plateau(partie);
		VuePartie vp2 = new VuePartie(p);
		VuePlateau vp = new VuePlateau(p);
		
		javax.swing.JFrame f = new javax.swing.JFrame("JSoko");
		f.getContentPane().add(vp2,java.awt.BorderLayout.SOUTH);
		f.getContentPane().add(vp,java.awt.BorderLayout.CENTER);
		f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		f.addKeyListener(vp);
		f.pack();
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation((screenSize.width-f.getWidth())/2,(screenSize.height-f.getHeight())/2);
		f.setJMenuBar(new MenuSoko(p));
		f.setResizable(false);
		f.setVisible(true);
		
		vp.afficherSolution(j.getDirectionsSolution());
	}
}
