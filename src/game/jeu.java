/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import env3d.Env;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import management.DevineLeMot;
import management.Dico;
import management.LectureClavier;
import management.Partie;
import management.Profile;

/**
 *
 * @author faridn
 */
public final class jeu {
    
    private Env env;
    private jeu jeu;
    private room room;
    private boolean finished = false;
    private int level;
    private Dico dico= new Dico();
    private LectureClavier lectureclavier=new LectureClavier();
    DevineLeMot devmot;
    String mot;
    private boolean init_first;
    private Profile profile;
    private Partie partieactu;
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //String chainedate = this.date.toString();
    private String chainedate = dateFormat.format(date);
    private boolean  Sauvegarde;
    
    public jeu(){
        
        
   }
    
    public jeu(String xml){
        
        init_first=true;
        dico.readDictionnary("./src/XML/dico.xml");
        
        this.profile=new Profile(xml);
        this.level=profile.getLastLevel();
    }
    
    public void initialisation(){
        
        if(init_first){
            
        init_first=false;
        this.env = new Env();
        this.room = new room("./src/XML/plateau.xml");
        this.env = env;
        this.env.setRoom(this.room);
        // Sets up the camera
        this.env.setCameraXYZ(25, 50, 90);
        this.env.setCameraPitch(-30);
        // Turn off the default controls
        this.env.setDefaultControl(false);
        
        }
        else{
            this.env.setRoom(this.room);
            this.env.restart();
        }
    }
    
    public void jouer() {
       
       do{
              
       System.out.println("Entrez le niveau : ");
       level=lectureclavier.lireEntier();
        
        mot=dico.getWordFromListLevel(level);
        
        this.partieactu = new Partie(chainedate,this.mot,this.level);
      
        //initialisation env + lancement du jeu
        initialisation();
        devmot = new DevineLeMot(mot,this.env,this.room);
        devmot.jouer();
        
        //ajout de la partie au profile
        this.profile.ajouterPartie(partieactu);   
        this.partieactu.setTrouve(devmot.getNbLettresRestantes());
        this.partieactu.setTemps(devmot.getTemps());
        System.out.println(this.partieactu.toString());
        
        System.out.println("Voulez-vous sauvegarder votre Partie ? o/n");
        Sauvegarde=lectureclavier.lireOuiNon();
        String nomfic;
        if(Sauvegarde){
            System.out.println("Veuillez spécifier le nom du fichier:");
            nomfic = lectureclavier.lireChaine();
            this.profile.save("./src/XML/" + nomfic + ".xml");
        }
        System.out.println("Voulez vous rejouer ? o/n");
        finished = lectureclavier.lireOuiNon();
        }while(finished);
        this.env.exit();
    } 
     
    
}
    

