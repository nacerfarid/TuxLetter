package management;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <P>Cette classe a pour rôle de faciliter la lecture de données à partir du
 * clavier.</P>
 * <P>Elle definit une méthode de lecture pour les types de base les plus
 * courramment utilisés (int, float, double, boolean, String).</P>
 * <P>La lecture d'une valeur au clavier se fait en tapant celle-ci suivie d'un
 * retour chariot.</P>
 * <P>En cas d'erreur de lecture (par exemple un caractère a été tapé lors de la
 * lecture d'un entier) un message d'erreur est affiché sur la console et
 * l'exécution du programme est interrompue.</P>
 * <P>Toutes les méthodes définies dans cette classe sont présentent sous deux 
 * formes :<BR>
 * <UL>
 *    <LI>une version avec un mesage d'invite en parmètre, message qui sera
 *    affiché sur la console avant la lecture</LI>
 *    <LI>une version sans message d'invite</LI> 
 * </UL> 
 * </P>
 * <B>exemples d'utilisation de cette classe</B><BR>
 * 
 * <PRE>
 * 
 * char c = LectureClavier.lireChar("entrez un caractère :");
 * System.out.println("caractère lu : " + c);
 * 
 * String s = LectureClavier.lireChaine("entrez une chaine :");
 * System.out.println("chaine lue : " + s);
 * 
 * int i = LectureClavier.lireEntier("entrez un entier : ");
 * System.out.println("entier lu : " + i);
 * 
 * float f = LectureClavier.lireFloat("entrez une réel (float) : ");
 * System.out.println("réel (float) lu : " + f);
 * 
 * double d = LectureClavier.lireDouble("entrez une réel (double) : ");
 * System.out.println("réel (double) lu : " + d);
 * 
 * boolean b = LectureClavier.lireOuiNon("entrez une réponse O/N : ");
 * System.out.println("booleen lu : " + b);
 * 
 * </PRE>
 * 
 * @author Philippe Genoud <A
 *         HREF="mailto:Philippe.Genoud@imag.fr">Philippe.Genoud@imag.fr</A>.
 * @version 13/10/98
 */
public class LectureClavier {

	private static BufferedReader stdin = new BufferedReader(
			new InputStreamReader(System.in));

	/**
	 * lecture au clavier d'un entier simple precision (int)
	 * 
	 * @param prompt :
	 *            chaîne d'invite
	 * @return l'entier lu
	 */
	public static int lireEntier(String prompt) {
		System.out.print(prompt);
		System.out.flush();
		return lireEntier();
	}
	
	/**
	 * lecture au clavier d'un entier simple precision (int)
	 * 
	 * @return l'entier lu
	 */
	public static int lireEntier() {
		try {
			return (Integer.parseInt(stdin.readLine()));
		} catch (Exception e) {
			erreurEntree(e, "entier");
			return 0; // même si on passe jamais ici il faut un return !!
		}
	}

	/**
	 * lecture au clavier d'une chaine de caractères
	 * 
	 * @param prompt :
	 *            chaîne d'invite
	 * @return la chaine lue
	 */
	public static String lireChaine(String prompt) {
		System.out.print(prompt);
		System.out.flush();
		return lireChaine();
	}

	/**
	 * lecture au clavier d'une chaine de caractères
	 * 
	 * @return la chaine lue
	 */
	public static String lireChaine() {
		try {
			return (stdin.readLine());
		} catch (Exception e) {
			erreurEntree(e, "chaine de caractères");

			return null;
		}
	}

	/**
	 * lecture au clavier d'un réel simple précision (float)
	 * 
	 * @param prompt :
	 *            chaîne d'invite
	 * @return le float lu
	 */
	public static float lireFloat(String prompt) {
		System.out.print(prompt);
		System.out.flush();
		return lireFloat();
	}

	/**
	 * lecture au clavier d'un réel simple précision (float)
	 * 
	 * @return le float lu
	 */
	public static float lireFloat() {

		try {
			return (Float.valueOf(stdin.readLine()).floatValue());
		} catch (Exception e) {
			erreurEntree(e, "réel (float)");

			return (float) 0.0;
		}
	}

	/**
	 * lecture au clavier d'un reél double précision (double)
	 * 
	 * @param prompt :
	 *            chaîne d'invite
	 * @return le double lu
	 */
	public static double lireDouble(String prompt) {
		System.out.print(prompt);
		System.out.flush();
		return lireDouble();
	}

	/**
	 * lecture au clavier d'un reél double précision (double)
	 * 
	 * @return le double lu
	 */
	public static double lireDouble() {
		try {
			return (Double.valueOf(stdin.readLine()).doubleValue());
		} catch (Exception e) {
			erreurEntree(e, "réel (double)");
			return 0.0;
		}
	}

	/**
	 * lecture au clavier d'une réponse de type oui/non. Si la valeur tapée est
	 * "o" ou "O" cette méthode renvoie <code>true</code>, sinon elle renvoie
	 * <code>false</code>.
	 * 
	 * @param prompt :
	 *            chaîne d'invite
	 * @return <code>true</code> si la chaine lue est "o" ou "O",
	 *         <code>false</code> sinon
	 */
	public static boolean lireOuiNon(String prompt) {
		System.out.print(prompt);
		System.out.flush();
		return lireOuiNon();
	}

	/**
	 * lecture au clavier d'une réponse de type oui/non. Si la valeur tapée est
	 * "o" ou "O" cette méthode renvoie <code>true</code>, sinon elle renvoie
	 * <code>false</code>.
	 * 
	 * @return <code>true</code> si la chaine lue est "o" ou "O",
	 *         <code>false</code> sinon
	 */
	public static boolean lireOuiNon() {
		String ch;
		ch = lireChaine();
		return (ch.equals("o") || ch.equals("O"));
	}

	/**
	 * lecture au clavier d'un caractère
	 * 
	 * @param prompt :
	 *            chaîne d'invite
	 * @return le caractère lu
	 */
	public static char lireChar(String prompt) {
		System.out.print(prompt);
		System.out.flush();
		return lireChar();
	}

	/**
	 * lecture au clavier d'un caractère
	 * 
	 * @return le caractère lu
	 */
	public static char lireChar() {
		String ch;
		ch = lireChaine();
		return ch.charAt(0);
	}
	
	/**
	 * en cas d'erreur lors d'une lecture, affiche sur la sortie standard
	 * (System.out) un message indiquant le type de l'erreur ainsi que la la
	 * pile des appels de méthodes ayant conduit à cette erreur.
	 * 
	 * @param e
	 *            l'exception décrivant l'erreur.
	 * @param message
	 *            le message d'erreur à afficher.
	 */
	private static void erreurEntree(Exception e, String message) {
		System.out.println("Erreur lecture " + message);
		System.out.println(e);
		e.printStackTrace();
		System.exit(1);
	}

} // LectureClavier
