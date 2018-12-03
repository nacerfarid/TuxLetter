/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package management;
import management.Chronometre;
import org.w3c.dom.*;

/**
 *
 * @author faridn
 */
public class Partie {

    private String date;
    private String word;
    private int level;
    private int trouve;
    private int temps;

    public Partie(String date, String word, int level) {

        this.date = date;
        this.word = word;
        this.level = level;
       
    }

    public Partie(Element domPartie) {

        this.date = domPartie.getAttributes().getNamedItem("date").getTextContent();
        this.word = domPartie.getElementsByTagName("word").item(0).getTextContent();
        this.level = Integer.parseInt(domPartie.getElementsByTagName("word").item(0).getAttributes().getNamedItem("level").getTextContent());

        //test si time existe
        if (domPartie.getElementsByTagName("time").item(0) != null) {

            this.temps = Integer.parseInt(domPartie.getElementsByTagName("time").item(0).getTextContent());
        }
        //test si found existe
        if (domPartie.getAttributes().getNamedItem("found") != null) {

            //suppression du pourcentage de found pour pourvoir integrer dans un int
            String withpercent = domPartie.getAttributes().getNamedItem("found").getTextContent();
            String withoutpercent = withpercent.substring(0, withpercent.length() - 1);
            this.trouve = Integer.parseInt(withoutpercent);

        }

    }

    public Element getDomElement(Document doc) {

        Element resultat = null;

        if (this.date != null && this.word != null && this.level != 0) {

            Element ElementGame = doc.createElement("game");
            ElementGame.setAttribute("date", this.date);

            if (this.trouve !=0 && this.trouve!=100) {

                ElementGame.setAttribute("found", String.valueOf(this.trouve) + "%");

            }
            Node Games = doc.getElementsByTagName("games").item(0).appendChild(ElementGame);

            if (this.temps !=0 && this.temps!=61) {

                Element ElementTime = doc.createElement("time");
                ElementTime.setTextContent(String.valueOf(this.temps));
                Games.appendChild(ElementTime);
            }

            Element ElementWord = doc.createElement("word");
            ElementWord.setAttribute("level", String.valueOf(this.level));
            ElementWord.setTextContent(this.word);
            Games.appendChild(ElementWord);

            resultat = (Element) Games;
        }
        return resultat;
    }

    public int setTrouve(int nbLettresRestantes) {

        this.trouve = ((this.word.length() - nbLettresRestantes) * 100) / this.word.length();
        return this.trouve;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }
    
    public int getNiveau() {
        return this.level;
    }

    public String toString() {

        return "A la date : " + date + " le mot --" + word + "-- dans le niveau " + level + " possede " + this.trouve + " % de lettres trouvees au bout de " + this.temps + "secondes";
    }

}
