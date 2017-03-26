package Controlleur;

import Tools.MinPQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Modèle.PlateauIA;


public class Joueur {
	
	private boolean IA;
	private int nbCoups;
	private int nbIterations;
	private PlateauIA plateau;
	private ArrayList<PlateauIA> solution; //Solution en cours
	private ArrayList<Integer> direction_sol; //Solution en cours
	private MinPQ<NoeudRecherche> filePriorite;
	

	public Joueur(boolean ia,PlateauIA p){
		this.IA = ia;
		this.plateau = p;
		this.nbIterations = 0;
		this.filePriorite = new MinPQ<NoeudRecherche>();
		this.solution = new ArrayList<PlateauIA>();
		this.direction_sol = new ArrayList<Integer>();
		NoeudRecherche s = new NoeudRecherche(null, plateau);
		this.filePriorite.insert(s);
		//System.out.println(this.filePriorite.size());
	}
	
	public void Astar(int nbIter){ 
		//System.out.println(this.filePriorite.size());
		NoeudRecherche s = this.filePriorite.delMin();
		System.out.println("   EVAL :"+s.getPlateau().evaluation());
		s.getPlateau().afficher();
		
		if (!s.plateau.partieFinie()) {
			for (PlateauIA son : s.plateau.voisins()){
				if(!NoeudRecherche.hashes.contains(son.getHash())){
					NoeudRecherche nr = new NoeudRecherche(s, son);
					if(nr.eval < 10000){
						this.filePriorite.insert(nr);
					}
				}
			}
			if(nbIter > 0)
				this.nbIterations++;
				Astar(nbIter-1);
		} else {
			this.nbCoups = s.nbCoups;
			while (s.parent != null) {
				this.solution.add(s.plateau);
				s.plateau.afficher();
				this.direction_sol.add(0, s.parent.getPlateau().direction(s.plateau));
				s = s.parent;
			}
		}
	}
		
	public void afficherSolution(){
		System.out.println("\nSOLUTION");
		System.out.println("\nNombre de coups : "+nbCoups);
		System.out.println("Nombre d'itération de A* : "+nbIterations);
		for(int i = solution.size()-1; i>=0 ; i--){
			PlateauIA p = solution.get(i);
			p.afficher();
			/*try {
				System.in.read();
				//Thread.sleep(1000);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		System.out.println("\nNombre de coups : "+nbCoups);
		System.out.println("Nombre d'itération de A* : "+nbIterations);
	}
	
	public int getIterations(){
		return this.nbIterations;
	}
	
	public ArrayList<Integer> getDirectionsSolution(){
		return this.direction_sol;
	}

}
