package Controlleur;

import java.util.ArrayList;

import Modèle.PlateauIA;

public class NoeudRecherche implements Comparable<NoeudRecherche> {
	public int nbCoups;
	public int eval;
	public PlateauIA plateau;
	public NoeudRecherche parent;
	public static ArrayList<Integer> hashes = new ArrayList<>();
	public static double coefEval = 1;
	public static double coefCoups = 0.5;
	

	public NoeudRecherche(NoeudRecherche n, PlateauIA p) {
		this.parent = n;
		if (parent != null)
			this.nbCoups = parent.nbCoups + 1;
		else
			this.nbCoups = 0;
		this.plateau = p;
		this.eval = plateau.evaluation();
		NoeudRecherche.hashes.add(this.plateau.getHash());
	}
	
	@Override
	public boolean equals(Object p) {
		if (p instanceof NoeudRecherche) {
			if(((NoeudRecherche) p).getPlateau().getHash()==this.getPlateau().getHash())
				return true;
			else
				return false;
		}else
			return false;
	}

	@Override
	public int compareTo(NoeudRecherche n) {
		if ((coefEval*this.eval+coefCoups*this.nbCoups) < (coefEval*n.eval+coefCoups*n.nbCoups))
			return 0;
		else
			return 1;
	}
	
	public static void setCoefs(double ce, double cc){
		coefEval = ce;
		coefCoups = cc;
	}
	
	public PlateauIA getPlateau(){
		return plateau;
	}
}