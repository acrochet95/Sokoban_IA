
import java.util.*;
import java.io.*;
import java.net.URI;
import java.awt.event.KeyEvent;

/** Cette classe reprensente le plateau de jeu du Sokoban
*/
public class Plateau extends Observable {
	
	private Partie p;
	//Le plateau courant et ses parametres
	private Case[][] plateau = new Case[16][19];
	private int nbLignes, nbColonnes,nbPas, nbPousses;
	//Les donnees de la case ou se trouve le perso
	private int lignePerso, colonnePerso;
	private boolean surZone = false;
	//indicateurs pour la case de destination
	private boolean mur = false;
	private boolean zone = false;
	private boolean caisse = false;
	// Pour la fin de partie
	private boolean fini = false;
	private boolean reinit = false;
	// Pour les types d'affichage
	public static final int CLASSIC = 0;
	public static final int NEW = 1;
	
	
	public Plateau(Partie p)  {
		this.p = p;
		initialiserPlateau(p.getNiveauCourant());
		
	}//Plateau
	
	
	public void recommencer() {
		initialiserPlateau(p.getNiveauCourant());
		reinit = true;		
	}
	
	/** Appelle le LevelLoader pour le niveau donné
	* @param niveau Le numero du niveau à charger
	*/	
	public void initialiserPlateau(int niveau)  {
		try {
			 p.goToLevel(niveau);
		    nbPas = 0;
			 nbPousses = 0;
			 String levelFile = "./niveaux/lvl"+niveau+".sok";
			 LevelLoader ll = LevelLoader.getInstance();
			 ll.initLevel(this,levelFile);
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
			e.printStackTrace();
			}
	}//initilaiserPlateau
	
	/**Rappelé par le level loadeur le fichier parcouru
	*Instancie nbLignes et nbColonnes pour éviter des cases superflues selon le tableau chargé
	*/
	public void initialiserPlateau(Case[][] p) {
		nbLignes = 0;
		nbColonnes = 0;
		reinit = true;
		for(int i =0; i < 16; i++) {
			for(int j=0; j < 19;j++) {
			   if (p[i][j].getStatut() == Case.MUR) {
				nbLignes = i;
				nbColonnes = (j >= nbColonnes)?j:nbColonnes;
			    }//if
			    if (p[i][j].getStatut() == Case.PERSO || p[i][j].getStatut() == Case.PERSO_SUR_ZONE) {
				lignePerso = i; colonnePerso = j;
			    }
			    this.plateau[i][j] = new Case(p[i][j]);
			}//for
		}//for
		setChanged();
		notifyObservers();
		fini = false;
	}//initialiserPlateau
	//--------------------------------
	// Methodes d'observation ---------
	//--------------------------------
	
	/** Renvoie la partie associée
	*/
	public Partie getPartie() {
		return p;
	}
	
	/**Renvoie le nombre de pas effectués dans ce plateau
	*/
	public int getNbPas() {
		return nbPas;
	}
	
	/** Renvoie le nombre de pousses
	*/
	public int getNbPousses() {
		return nbPousses;
	}
	
	/** Renvoie la case située sur la ligne ligne et la colonne colonne
	* @param ligne La ligne de la case voulue
	* @param colonne La colonne de la case voulue
	*/
	public Case caseEn(int ligne,int colonne) {
		return plateau[ligne][colonne];
	}
	
	/**Renvoie le nombre de lignes du plateau courant
	*/
	public int getNbLignes() {
		return nbLignes;
	}
	/**Renvoie le nombre de colonnes du plateau courant
	*/
	public int getNbColonnes() {
		return nbColonnes;
	}
	/** Teste la victoire
	* On est gagnant si il n'y a plus de zone vides ou avec le perso dessus.
	*/
	public void testVictoire() {
		fini = true;
		for(int i = 1; i < nbLignes  && fini;i++) {
			for(int j = 1; j < nbColonnes  && fini; j++) {
				fini = fini & (plateau[i][j].getStatut() != Case.ZONE_RANGEMENT) && (plateau[i][j].getStatut() != Case.PERSO_SUR_ZONE);
			}
		}
		if (fini) {
		    p.goToNextLevel();
		    initialiserPlateau(p.getNiveauCourant());
		}
	}
	/** Renvoie le booleen instancié dans testVictoire()
	*/
	public boolean termine() { 
		return fini;
	}
	
	public boolean reinit() {
		return reinit;
	}
    //------------------------------
    // Gestion des deplacements ----
    //------------------------------
    
    /** Appelle une methode precise pour chaque direction
     */	
    public void bougerPerso(int direction) {
	fini = false;
	switch(direction) {
	case KeyEvent.VK_UP ://on monte
	    monter();
	    break;			
	case KeyEvent.VK_RIGHT ://on va a droite
	    aDroite();
	    break;
	case KeyEvent.VK_DOWN : //on descends
	    descendre();
	    break;
	case KeyEvent.VK_LEFT ://on va à gauche
	    aGauche();
	    break;
	}//switch
	setChanged();
	notifyObservers();
	testVictoire();
    }//bougerPerso
    
    /** Methode privée appelée quand on veut monter
     */
    private void monter() {
	
	//instanciation des indicateurs
	mur = plateau[lignePerso-1][colonnePerso].getStatut() == Case.MUR;
	zone = (plateau[lignePerso-1][colonnePerso].getStatut() == Case.ZONE_RANGEMENT) || (plateau[lignePerso-1][colonnePerso].getStatut() == Case.CAISSE_SUR_ZONE);
	caisse = (plateau[lignePerso-1][colonnePerso].getStatut() == Case.CAISSE_SUR_ZONE)|| (plateau[lignePerso-1][colonnePerso].getStatut() == Case.CAISSE);
	surZone =  plateau[lignePerso][colonnePerso].getStatut() == Case.PERSO_SUR_ZONE;
	if (!mur) {
	    if (!caisse) {
		plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		plateau[lignePerso-1][colonnePerso].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		lignePerso--;
		nbPas++;
	    }//if
	    else {
		int afterCaisse = plateau[lignePerso-2][colonnePerso].getStatut();
		switch(afterCaisse) {
		case Case.CASE_VIDE :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso-1][colonnePerso].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso-2][colonnePerso].setStatut(Case.CAISSE);
		    lignePerso--;
			 nbPas++;
			 nbPousses++;
		    break;
		case Case.ZONE_RANGEMENT :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso-1][colonnePerso].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso-2][colonnePerso].setStatut(Case.CAISSE_SUR_ZONE);
		    lignePerso--;
			 nbPas++;
			 nbPousses++;
		    break;
		default:
		    return;
		}//switch
	    }//else
	}//if
		
    }//monter
    
    /** Methode privée appelée quand on veut descendre
     */
    private void descendre() {
	
	//instanciation des indicateurs
	mur = plateau[lignePerso+1][colonnePerso].getStatut() == Case.MUR;
	zone = (plateau[lignePerso+1][colonnePerso].getStatut() == Case.ZONE_RANGEMENT) || (plateau[lignePerso+1][colonnePerso].getStatut() == Case.CAISSE_SUR_ZONE);
	caisse = (plateau[lignePerso+1][colonnePerso].getStatut() == Case.CAISSE_SUR_ZONE)|| (plateau[lignePerso+1][colonnePerso].getStatut() == Case.CAISSE);
	surZone =  plateau[lignePerso][colonnePerso].getStatut() == Case.PERSO_SUR_ZONE;
	if (!mur) {
	    if (!caisse) {
		plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		plateau[lignePerso+1][colonnePerso].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		lignePerso++;
		nbPas++;
	    }//if
	    else {
		int afterCaisse = plateau[lignePerso+2][colonnePerso].getStatut();
		switch(afterCaisse) {
		case Case.CASE_VIDE :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso+1][colonnePerso].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso+2][colonnePerso].setStatut(Case.CAISSE);
		    lignePerso++;
			 nbPas++;
			 nbPousses++;
		    break;
		case Case.ZONE_RANGEMENT :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso+1][colonnePerso].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso+2][colonnePerso].setStatut(Case.CAISSE_SUR_ZONE);
		    lignePerso++;
			 nbPas++;
			 nbPousses++;
		    break;
		default :
		    return;
		}//switch
	    }//else
	}//if	
	}//descendre()
	
    /** Methode privée appelée quand on veut aller à gauche
     */
    private void aGauche() {
	
	//instanciation des indicateurs
	mur = plateau[lignePerso][colonnePerso-1].getStatut() == Case.MUR;
	zone = (plateau[lignePerso][colonnePerso-1].getStatut() == Case.ZONE_RANGEMENT) || (plateau[lignePerso][colonnePerso-1].getStatut() == Case.CAISSE_SUR_ZONE);
	caisse = (plateau[lignePerso][colonnePerso-1].getStatut() == Case.CAISSE_SUR_ZONE)|| (plateau[lignePerso][colonnePerso-1].getStatut() == Case.CAISSE);
	surZone =  plateau[lignePerso][colonnePerso].getStatut() == Case.PERSO_SUR_ZONE;
	if (!mur) {
	    if (!caisse) {
		plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		plateau[lignePerso][colonnePerso-1].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		colonnePerso--;
		nbPas++;
	    }
	    else {
		int afterCaisse = plateau[lignePerso][colonnePerso-2].getStatut();
		switch(afterCaisse) {
		case Case.CASE_VIDE :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso][colonnePerso-1].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso][colonnePerso-2].setStatut(Case.CAISSE);
		    colonnePerso--;
			 nbPas++;
			 nbPousses++;
		    break;
		case Case.ZONE_RANGEMENT :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso][colonnePerso-1].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso][colonnePerso-2].setStatut(Case.CAISSE_SUR_ZONE);
		    colonnePerso--;
			 nbPas++;
			 nbPousses++;
		    break;
		default :
		    return;
		}//switch
	    }//else
	}//if
		
    }
    
    /** Methode privée appelée quand on veut aller à droite
     */
    private void aDroite() {
	
	//instanciation des indicateurs
	mur = plateau[lignePerso][colonnePerso+1].getStatut() == Case.MUR;
	zone = (plateau[lignePerso][colonnePerso+1].getStatut() == Case.ZONE_RANGEMENT) || (plateau[lignePerso][colonnePerso+1].getStatut() == Case.CAISSE_SUR_ZONE);
	caisse = (plateau[lignePerso][colonnePerso+1].getStatut() == Case.CAISSE_SUR_ZONE)|| (plateau[lignePerso][colonnePerso+1].getStatut() == Case.CAISSE);
	surZone =  plateau[lignePerso][colonnePerso].getStatut() == Case.PERSO_SUR_ZONE;
	if (!mur) {
	    if(!caisse) {
		plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		plateau[lignePerso][colonnePerso+1].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		colonnePerso++;
		nbPas++;
	    }
	    else {
		int afterCaisse = plateau[lignePerso][colonnePerso+2].getStatut();
		switch(afterCaisse) {
		case Case.CASE_VIDE :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso][colonnePerso+1].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso][colonnePerso+2].setStatut(Case.CAISSE);
		    colonnePerso++;
			 nbPas++;
			 nbPousses++;
		    break;
		case Case.ZONE_RANGEMENT :
		    plateau[lignePerso][colonnePerso].setStatut((surZone)?Case.ZONE_RANGEMENT:Case.CASE_VIDE);
		    plateau[lignePerso][colonnePerso+1].setStatut((zone)?Case.PERSO_SUR_ZONE:Case.PERSO);
		    plateau[lignePerso][colonnePerso+2].setStatut(Case.CAISSE_SUR_ZONE);
		    colonnePerso++;
			 nbPas++;
			 nbPousses++;
		    break;
		default :
		    return;
		}//switch
	    }
	}//if
	
    }//aDroite()
    
	 /** Methode de deboguage
	 * Permet d'afficher le statut de chaque case du plateau
	 */
    public void print() {
	for(int i=0;i<16;i++) {
	    for (int j = 0; j <19;j++) {
		System.out.print(plateau[i][j].getStatut());
	    }
	    System.out.print("\n");
	}
    }
}
