package Mod�le;


import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Controlleur.Joueur;
import Controlleur.NoeudRecherche;

public class PlateauIA {

	private int x;
	private int y;
	private int sousJoueur; //Rappel de la case sous le joueur
	private ArrayList<int[]> objectifs; //Rappel des positions finales des caisses
	private ArrayList<int[]> caisses; //Rappel position caisses
	int[][] grille;
	
	private static double poidsCaisse = 1;
	private static double poidsJoueur = 1;
	
	public PlateauIA(PlateauIA p){
		
		this.x = p.x;
		this.y = p.y;
		this.sousJoueur = p.sousJoueur;
		this.objectifs = new ArrayList<int[]>();
		this.caisses = new ArrayList<int[]>();
		this.grille = new int[p.grille.length][p.grille[0].length];
		this.poidsCaisse = p.poidsCaisse;
		this.poidsJoueur = p.poidsJoueur;
		for(int[] trou : p.objectifs){
			int[] sub = new int[2];
			sub[0] = trou[0]; sub[1] = trou[1];
			this.objectifs.add(sub);
		}
		for(int[] caisse : p.caisses){
			int[] sub = new int[2];
			sub[0] = caisse[0]; sub[1] = caisse[1];
			this.caisses.add(sub);
		}
		for(int i=0;i<grille.length;i++){
			for(int j=0;j<grille[i].length;j++){
				this.grille[i][j] = p.grille[i][j];
			}
		}
		
	}

	//On construit le plateau à partir d'un fichier
	public PlateauIA(String fichier){
		
		this.objectifs = new ArrayList<int[]>();
		this.caisses = new ArrayList<int[]>();

		int hauteur = 0;
		int largeur = 0;
		int[][] grille_before = new int[50][50];
		//this.grille = new int[hauteur][largeur];

		
		try{
			InputStream flux=new FileInputStream(fichier); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			int x = 0; int y = 0;
			while ((ligne=buff.readLine())!=null && ligne.length()!=0 && hauteur == 0){
				String[] chars = ligne.split("");
				for(String lettre : chars){
					switch(lettre){
						case " ": //Vide
							grille_before[y][x] = 0;
							break;
						case "#": //Mur
							//System.out.println("okkkk");
							grille_before[y][x] = 1;
							break;
						case "$": //Caisse
							grille_before[y][x] = 2;
							int[] coord = {y,x};
							this.caisses.add(coord);
							break;
						case ".": //Trou
							grille_before[y][x] = 3;
							int[] obj = {y,x};
							this.objectifs.add(obj);
							break;
						case "@": //Joueur
							grille_before[y][x] = 4;
							this.x = x;
							this.y = y;
							break;
						case "*": //Trou + caisse
							grille_before[y][x] = 2;
							this.x = x;
							this.y = y;
							int[] caisse_obj = {y,x};
							this.caisses.add(caisse_obj);
							this.objectifs.add(caisse_obj);
							break;
						default :
							hauteur = y;
							break;
					}
					x++;
				}
				y++;
				//System.out.println(x);
				if(x > largeur && hauteur == 0)
					largeur = x;
				x=0;
			}
			System.out.println(hauteur + " "+largeur);
			this.grille = new int[hauteur][largeur];
			
			for(int i=0;i<hauteur;i++){
				for(int j=0;j<largeur;j++)
					this.grille[i][j] = grille_before[i][j];
			}
			
			
			buff.close(); 
		}		
		catch (Exception e){
			System.out.println(e);
		}	
	}
	
	@Override
	public boolean equals(Object p) {
		if (p instanceof PlateauIA) {
			PlateauIA that = (PlateauIA) p;
			if (this.x != that.x)
				return false;
			if (this.y != that.y)
				return false;
			for(int a = 0; a < caisses.size();a++){
				if(caisses.get(a)[0] != that.caisses.get(a)[0] ||
						caisses.get(a)[1] != that.caisses.get(a)[1])
					return false;
			}
			for (int i = 0; i < grille.length; i++) {
				for (int j = 0; j < grille[i].length; j++) {
					if (this.grille[i][j] != that.grille[i][j])
						return false;
				}
			}
			return true;
		} else
			return false;
	}
	
	public int getHash(){
		String h = "";
		for(int i=0;i<grille.length;i++){
			for(int j=0;j<grille[i].length;j++){
				h+=this.grille[i][j];
			}
		}
		return h.hashCode();
	}
	
	
	public ArrayList<PlateauIA> voisins(){
		
		ArrayList<PlateauIA> voisins = new ArrayList<PlateauIA>();
		PlateauIA c1 = new PlateauIA(this);
		PlateauIA c2 = new PlateauIA(this);
		PlateauIA c3 = new PlateauIA(this);
		PlateauIA c4 = new PlateauIA(this);
		if(c1.deplacerJoueur(0, 1)) //Retourne faux si le déplacement est pas possible
			voisins.add(c1);
		if(c2.deplacerJoueur(0, -1))
			voisins.add(c2);
		if(c3.deplacerJoueur(1, 0))
			voisins.add(c3);
		if(c4.deplacerJoueur(-1, 0))
			voisins.add(c4);
		
		return voisins;
	}
	

	 
	
	public int getNbDegre(int[] coord){
		int b = coord[0];
		int a = coord[1];
		int degre = 4;
		
		if(this.grille[b-1][a]==1 || this.grille[b+1][a]==1){ //Si il b a une caisse en haut ou en bas
			if(this.grille[b][a-1]==1 || this.grille[b][a+1]==1)
				return 0;
			else 
				degre -= 2;
		}

		if(this.grille[b][a-1]==1 || this.grille[b][a+1]==1 ){ //Si il b a une caisse à droite ou à gauche
			if(this.grille[b-1][a]==1 || this.grille[b+1][a]==1)
				return 0;
			else 
				degre -= 2;
		}
		return degre;
	}
	
	/**
	 * Fonction qui calcul une heuristique du plateau basée sur la distance de Manhattan.
	 * @return int >= 0
	 */
	public int evaluation() {
		int eval = 0;
		ArrayList<int[]> trouUtilises = new ArrayList<int[]>();
		for (int[] caisse  : this.caisses) {
			if (this.getNbDegre(caisse) == 0 && !caissePlacee(caisse))
				return 10000;
			else {
				int minJoueur = 10000;
				int minCaisse = 10000;
				int[] plusProcheTrou = new int[2];
				if(!caissePlacee(caisse)){
					for (int[] trou : this.objectifs){
						if(!trouUtilises.contains(trou)){
							if(Manhattan(caisse, trou)<minCaisse){
								minCaisse = Manhattan(caisse, trou);
								plusProcheTrou = trou;
							}
							int[] coord = {y,x};
							minJoueur = Math.min(minJoueur, Manhattan(caisse, coord));
						}
					}
					eval += (poidsCaisse*minCaisse+poidsJoueur*minJoueur);
					trouUtilises.add(plusProcheTrou);
				}
			}
		}
		return eval;
	}

	/**
	 * Calcul la distance de Manhattan (à vol d'oiseau) entre la caisse et l'objectif.
	 * @param caisse int[]
	 * @param trou int []
	 * @return int >= 0
	 */
	public int Manhattan(int[] caisse, int[] trou) {
		return (Math.abs(caisse[0] - trou[0]) + Math.abs(caisse[1] - trou[1]));
	}
	
	public boolean caissePlacee(int[] caisse){
		for(int[] obj : this.objectifs)
			if(obj[0]==caisse[0] && obj[1]==caisse[1])
				return true;
		return false;
	}
	
	public boolean partieFinie(){
		
		for(int[] caisse : caisses){
			if(!caissePlacee(caisse)){ //Si il n'y a pas de caisse sur une croix
				return false;
			}
		}
		return true;
	}
	
	//TO DO complexité dégueux
	public void afficher(){
		System.out.println();
		for(int i = 0;i < this.grille.length ; i++){
			System.out.println("");
			for(int j = 0; j < this.grille[i].length ; j++){
				boolean onBox = false;
				boolean onHole = false;
				for(int[] k : caisses)
					if(k[0] == i && k[1] == j){
						onBox = true;
						break;
					}
				for(int[] l : objectifs)
					if(l[0] == i && l[1] == j){
						onHole = true;
						break;
					}
				if(onBox && onHole)
					System.out.print('o');
				else if(onHole)
					System.out.print('t');
				else if(onBox)
					System.out.print('c');
				else if(grille[i][j]==0)
					System.out.print(' ');
				else if(grille[i][j]==1)
					System.out.print('x');
				else if(grille[i][j]==4)
					System.out.print('j');
			}
		}
		System.out.println();
	}
	
	
	public boolean deplacerJoueur(int dy, int dx) {
		
		if(grille[y+dy][x+dx]==0 || grille[y+dy][x+dx]==3){ //Si on se déplace simplement
			grille[y][x] = 0;
			grille[y+dy][x+dx] = 4;
			x += dx;
			y += dy;
			return true;
		}
		
		if(grille[y+dy][x+dx]==2 && (grille[y+dy*2][x+dx*2]==0 || grille[y+dy*2][x+dx*2]==3)){ //Si on pousse une caisse
			grille[y+dy][x+dx] = 4;
			grille[y][x] = 0;
			grille[y+dy*2][x+dx*2] = 2;
			for(int[] caisse : caisses){
				if(caisse[0] == (y+dy) && caisse[1]==(x+dx)){ //Si c'est la bonne caisse
					caisse[0] += dy;
					caisse[1] += dx;
				}
			}
			x += dx;
			y += dy;
			return true;
		}
		return false;
	}
	
	public static void setCoeffs(double cc, double d){
		poidsCaisse = cc;
		poidsJoueur = d;
	}
	
	
	public int direction(PlateauIA fils){
		
		if(fils.x-x < 0){
			return KeyEvent.VK_LEFT;
		}
		if(fils.x-x > 0){
			return KeyEvent.VK_RIGHT;
		}
		if(fils.y-y < 0){
			return KeyEvent.VK_UP;
		}

		// Sinon
		return KeyEvent.VK_DOWN;
	}
	
}
