
import java.io.*;

/**Classe qui instancie un plateau à partir d'un fichier
* Une seule méthode statique, qui lit le fichier.
*/

public class LevelLoader {	
   
	private static LevelLoader ll;
	
	private LevelLoader() {}
	
	public static LevelLoader getInstance() {
		if (ll == null) {
			ll = new LevelLoader();
			}
		return ll;
	}
	
	public void initLevel(Plateau p, String level) throws FileNotFoundException,IOException {
	Case[][] niveau = new Case[16][19];
	InputStream in = getClass().getResourceAsStream(level);
	BufferedReader flot = new BufferedReader(new InputStreamReader(in));
	int ligneCourante  = 0;
	String ligne = flot.readLine();
	while (ligne.charAt(0) != 'A') {
	    for (int i = 0; i < Math.min(19,ligne.length());i++) {
		switch(ligne.charAt(i)) {
		case '@' :
		    niveau[ligneCourante][i] = new Case(p, Case.PERSO);
		    break;
		case ' ' :
		    niveau[ligneCourante][i] = new Case(p, Case.CASE_VIDE);
		    break;
		case '#' :
		    niveau[ligneCourante][i] = new Case(p, Case.MUR);
		    break;
		case '*' :
		    niveau[ligneCourante][i] = new Case(p, Case.CAISSE_SUR_ZONE);
		    break;
		case '$' :
		    niveau[ligneCourante][i] = new Case(p, Case.CAISSE);
		    break;
		case '.' :
		    niveau[ligneCourante][i] = new Case(p, Case.ZONE_RANGEMENT);
		    break;
		case '+' :
		    niveau[ligneCourante][i] = new Case(p, Case.PERSO_SUR_ZONE);
		    break;
		}//switch
	    }//for
	    for (int i = Math.min(19,ligne.length()); i < 19;i++) {	
		niveau[ligneCourante][i] = new Case(p, Case.CASE_VIDE);
	    }//for
	    ligneCourante++;
	    ligne = flot.readLine();
	}//while
	for (int i = ligneCourante; i < 16; i++) {
	    for(int j = 0; j < 19; j++) {
		niveau[i][j] = new Case(p, Case.CASE_VIDE);
	    }//for
	}//for
	p.initialiserPlateau(niveau);
	flot.close();
    }//initLevel
}//class LevelLoader
