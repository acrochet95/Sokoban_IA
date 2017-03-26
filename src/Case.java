
import java.util.Observable;
/**Represente une case du plateau courant du Sokoban
*/

public class Case extends Observable {
	// Declarations de champs entier statiques pour chaque statuts possibles d'une case
	public static final int CASE_VIDE = 0;
	public static final int MUR = 1;
	public static final int CAISSE = 2;
	public static final int PERSO = 3;
	public static final int ZONE_RANGEMENT = 4;
	public static final int PERSO_SUR_ZONE = 5;
	public static final int CAISSE_SUR_ZONE = 6;
	
	/** Le statut courant de la case
	*/
	private int statut;
	
	/** Le plateau auquel appartient la case
	*/
	private Plateau plateau;
	
	/**Instancie une nouvelle case
	* @param statut Le statut de la case
	*/
    public Case(Plateau p,int statut) {
		this.plateau = p;
		this.statut = statut;

		}
  	/** Instancie une case à partir d'une autre
	*/	 
    public Case(Case c) {
		setStatut(c.getStatut());
    }
	
	/** Renvoie le statut courant de la case
	*/
	public int getStatut() {
		return statut;
	}
	
	/** Permet de modifier le statut courant de la case
	*/
	public void setStatut(int newStatut) {
		this.statut = newStatut;
		setChanged();
		notifyObservers();
	}
	
	/** Permet de récupérer le plateau
	*/
	public Plateau getPlateau() {
		return plateau;
	}
}
