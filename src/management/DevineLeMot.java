/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package management;

import env3d.Env;
import game.Tux;
import game.room;
import java.util.ArrayList;

/**
 *
 * @author faridn
 */
public class DevineLeMot {

    private Env env;
    private Tux tux;
    private ArrayList<Letter> letters;
    private int nbLettresRestantes;
    private int temps;
    private Chronometre chrono;
    private  boolean finished=false;
    

    public DevineLeMot(String mot, Env env, room room) {
        
        Letter lettre;
        int randomx;
        int randomy;
        this.env=env;
        String pion = room.getPion();
        //System.out.println(pion);
        //this.tux=new Tux(20,2,20,"models/tux/tux_happy.png");
        this.tux=new Tux(20,2,20,pion);
        this.letters=new ArrayList<Letter>();
        this.nbLettresRestantes=mot.length();
        int i;
        for(i=0;i<mot.length();i++){
            randomx=(int)(Math.random()*50+1);
            randomy=(int)(Math.random()*40+1);
            lettre=new Letter(mot.charAt(i),randomx,randomy);
            this.letters.add(lettre);
            this.env.addObject(lettre);
       
        }
        
    }

    public void jouer() {
       
       this.env.addObject(tux);
       this.env.addObject(letters);
       this.chrono=new Chronometre(65);
       this.chrono.start();
      
        // The main game loop
        do {
            
            checkUserKey();
            
            if(collision(tux,letters.get(0))){
                
                this.env.removeObject(letters.get(0));
                this.letters.remove(0);
                System.out.println("Bravo une lettre en moins");
                System.out.println("nombre de lettres restantes:"+letters.size());
            }
            
            //updatedisplay
            this.env.advanceOneFrame();
          
           
        } while ((!finished)&&(this.letters.size()>0)&&(this.chrono.remainsTime()>=0));
 
        this.chrono.stop();
        System.out.println("temps restant:"+chrono.remainsTime());
        System.out.println("temps Heure :"+chrono.getHours()+" Minutes  : "+chrono.getMinutes()+" Secondes :"+chrono.getSeconds());
        
        //Post-Process: game is finished
        //we have to keep the data to save our score (chrono, temps, nbLettresRestantes) 
      
    }

    public void checkUserKey() {

        
        //while (!finished) {
            //1 is for escape key
            if (this.env.getKey() == 1) {
                finished = true;
            }
            //this.env.advanceOneFrame();
            this.tux.move(env.getKey());
            // Update display
            
            
            
      //  }
         //this.env.exit();
    }

    private boolean tuxMeetsLetter() {

     return collision(this.tux,this.letters.get(0));
        
    }

    private double Distance(Tux tux, Letter letter) {
        
       return tux.distance(letter);
        

    }
    

    private boolean collision(Tux tux, Letter letter) {
            
      //return Distance(tux,letter)<=this.letters.
    if((Distance(tux,letter)-this.tux.getScale()/2 )<letter.getScale()/2){
        return true;
    }
    else{
        return false;
    }
        
        
    }

    public int getNbLettresRestantes() {
        nbLettresRestantes = letters.size();
        return this.nbLettresRestantes;
    }
    
    public int getTemps(){
        temps = this.chrono.getSeconds();
        return this.temps;
    }

}
