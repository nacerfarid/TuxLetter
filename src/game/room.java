/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Classe SIMPLIFIEE permettant de lire un dictionnaire de Tuxamots Cette classe
 * est un handler permettant le parse SAX
 *
 * @author Johan
 */
public class room extends DefaultHandler {

    // ce qui permet de lire et stocker le contenu xml
    private StringBuffer buffer;
    private double width, depth, height;
    private String textureTop;
    private String textureBottom;
    private String textureEast, textureWest, textureSouth, textureNorth;
    private String pion;
 /**
     * A room with just marble floor and no walls.
     * @param path
     */
    public room(String path)
    {
	//super permet d'appeler le constructeur de la classe parente ici DefaultHandler
        super();
        
	//put default values in case of the parse doesn't work
	this.textureBottom="textures/floor.png";
        this.textureNorth="textures/marble.png";
        this.textureEast="textures/mud.png";
        this.textureWest="textures/stone.png";
        this.depth=50;
        this.height=40;
        this.width=50;
        this.pion = "models/tux/tux_happy.png";    
	//appel du parseur
        this.readConfigRoom(path);
    }


    private void readConfigRoom(String pathToFile) {

        try {
            // création d'une fabrique de parseurs SAX
            SAXParserFactory fabrique = SAXParserFactory.newInstance();

            // création d'un parseur SAX
            SAXParser parseur = fabrique.newSAXParser();

            // lecture d'un fichier XML avec un DefaultHandler
            File fichier = new File(pathToFile);
            parseur.parse(fichier, this);

        } catch (ParserConfigurationException pce) {
            System.out.println("Erreur de configuration du parseur");
            System.out.println("Lors de l'appel à newSAXParser()");
        } catch (SAXException se) {
            System.out.println("Erreur de parsing");
            System.out.println("Lors de l'appel à parse()");
        } catch (IOException ioe) {
            System.out.println("Erreur d'entrée/sortie");
            System.out.println("Lors de l'appel à parse()");
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

	//C'est ici que l'on initialise le StringBuffer à chaque fois que l'on rencontre une balise qui nous permettra de récupérer du texte.
        if (qName.equals("plateau")) {
        }
        else if (qName.equals("mapping")){
            
        }
        else if (qName.equals("dimensions")){
            
        }
        else if (qName.equals("height")){
	  buffer = new StringBuffer(); 
            
        }else if (qName.equals("width")){
	  buffer = new StringBuffer();
        } else if (qName.equals("depth")){
	  buffer = new StringBuffer();
        }
        else if (qName.equals("textureBottom")){
	  buffer = new StringBuffer();
        }
        else if (qName.equals("textureEast")){
	  buffer = new StringBuffer();
        }
        else if (qName.equals("textureWest")){
	  buffer = new StringBuffer();
        }
        else if (qName.equals("textureNorth")){
	  buffer = new StringBuffer();
        }
        
        else {
            //erreur, on peut lever une exception
            throw new SAXException("Balise " + qName + " inconnue.");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

	//On attend d'être à la fin de l'élément afin de récupérer l'entièreté du texte.
	//Une fois le texte récupérer et stocké, on vide le buffer : buffer=null.
         if (qName.equals("plateau")) {
	    
        } 
        else if (qName.equals("mapping")){
            
        }
        else if (qName.equals("dimensions")){
            
        }
        else if (qName.equals("height")) {
	    //transformer un String en double (Google est mon ami)
	     double value = Integer.parseInt(buffer.toString());
	     //Enregister le double dans le bon attribut	
             this.setHeight(value);
	     //vider le buffer
	     buffer = null;
        } 
         
         else if (qName.equals("width")) {
	    //transformer un String en double (Google est mon ami)
	     double value = Integer.parseInt(buffer.toString());
	     //Enregister le double dans le bon attribut	
             this.setWidth(value);
	     //vider le buffer
	     buffer = null;
        } 
        else if (qName.equals("depth")) {
	    //transformer un String en double (Google est mon ami)
	     double value = Integer.parseInt(buffer.toString());
	     //Enregister le double dans le bon attribut	
             this.setDepth(value);
	     //vider le buffer
	     buffer = null;
            
        }
        else if (qName.equals("textureBottom")) {
	    
            String value = buffer.toString();
            this.setTextureBottom(value);
            buffer = null;
        }
        else if (qName.equals("textureEast")) {
	    
            String value = buffer.toString();
            this.setTextureEast(value);
            buffer = null;
        }
        else if (qName.equals("textureWest")) {
	    
            String value = buffer.toString();
            this.setTextureWest(value);
            buffer = null;
        }
        else if (qName.equals("textureNorth")) {
	    
            String value = buffer.toString();
            this.setTextureNorth(value);
            buffer = null;
        }
        
        
        else {
            //erreur, on peut lever une exception
            //throw new SAXException("Balise " + qName + " inconnue.");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

	//on récupère le résultat du parse de la méthode characters si celui-ci n'est pas null.
        String lecture = new String(ch, start, length);
        if (buffer != null) {
            buffer.append(lecture);
        }
        
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Début du parsing");
	//rien de spécial à effectuer dans notre cas.
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Fin du parsing");
	//rien de spécial à effectuer dans notre cas.
    }
     
    //getters and setters.
    
    public String getPion() {
        return pion;
    }

    public void setPion(String pion) {
        this.pion = pion;
    }

    public double getWidth() {
        return width;
    }

    public double getDepth() {
        return depth;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

}
