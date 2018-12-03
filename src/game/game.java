/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author faridn
 */
public class game {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        
        
        jeu jeu = new jeu("./src/XML/profile.xml");
        jeu.jouer();
   
        
    }
    
}
