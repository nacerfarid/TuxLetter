/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package management;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *
 * @author faridn
 */
public class Profile {
    
    private String nom;
    private String dateNaissance;
    private String avatar;
    private ArrayList<Partie>Parties = new ArrayList<Partie>();
    private Partie game;
    
    
    public Profile(String nom, String dateNaissance){
        
        this.nom=nom;
        this.dateNaissance=dateNaissance;
        this.avatar="";
    }
    
    public Profile(String filename){
        
         try {
            // analyse du document
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder p = dbFactory.newDocumentBuilder();
            // récupération de la structure objet du document
            Document doc = p.parse(filename);
             
            this.nom=doc.getElementsByTagName("name").item(0).getTextContent();
            this.dateNaissance=doc.getElementsByTagName("birthday").item(0).getTextContent();   
            this.dateNaissance=xmlDateToProfileDate(this.dateNaissance);
            this.avatar=doc.getElementsByTagName("avatar").item(0).getTextContent();
            
           //calcul du nombre de game dans me profile
           Element root = doc.getDocumentElement();
           XPathFactory xpf = XPathFactory.newInstance();
           XPath path = xpf.newXPath();           
           String expression = "count(//game)";
           String str = (String)path.evaluate(expression, root);
           int nbgame = Integer.parseInt(str);
           
           //envoi des game pour créer des parties
           for(int i=0;i<=nbgame;i++){
         
            Element elementgame = (Element)doc.getElementsByTagName("game").item(i);
            this.game = new Partie(elementgame);
            this.Parties.add(game);
         
           }
           
        } catch (Exception e) {
        }
    }
    
    public void ajouterPartie(Partie p){
        
        this.Parties.add(p);
        
    }
    
    public int getLastLevel(){
        
        return Parties.get(Parties.size() - 1).getNiveau();
    }
    
    public String toString(){
         return Parties.size() + " parties pour ce profile";
    }
    
    public void save(String filename) {
        
            try {
            DocumentBuilder dbFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dbFactory.newDocument();
            
            //Profile Générale
            doc.setXmlVersion("1.0");
            Element profile = doc.createElement("profile"); //profile générale
            doc.appendChild(profile);
            profile.setAttribute("xmlns", "http://myGame/profile");
            profile.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            profile.setAttribute("xsi:schemaLocation", "http://myGame/profile ./src/XSD/profileSchema.xsd");

            Element name = doc.createElement("name"); //nom de l'user
            profile.appendChild(name); //Ajout au profile générale
            
            name.setTextContent(this.nom); //Ajout du prénom
            Element avatar = doc.createElement("avatar"); //ajout Avatar
            profile.appendChild(avatar); 
            avatar.setTextContent(this.avatar);
            Element birthday = doc.createElement("birthday");
            profile.appendChild(birthday);
            birthday.setTextContent(profileDateToXmlDate(this.dateNaissance)); //ajout du Birthday avec date convertit

            //parties
            Element games = doc.createElement("games");
            profile.appendChild(games);
            for (Partie p : this.Parties) {
                games.appendChild(p.getDomElement(doc)); //parcours des partie puis ajout
            }

            //création du fichier de save
            File f = new File(filename);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
/// in profile format: dd/mm/yyyy
public static String xmlDateToProfileDate(String xmlDate) {
    
    String date;
    // récupérer le jour
    date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
    date += "/";
    // récupérer le mois
    date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
    date += "/";
    // récupérer l'année
    date += xmlDate.substring(0, xmlDate.indexOf("-"));
 
    return date;
}
 
public static String profileDateToXmlDate(String profileDate) {
    String date;
    // Récupérer l'année
    date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
    date += "-";
    // Récupérer  le mois
    date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
    date += "-";
    // Récupérer le jour
    date += profileDate.substring(0, profileDate.indexOf("/"));
 
    return date;
}
}
