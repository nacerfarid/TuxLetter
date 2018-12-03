/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package management;
import env3d.EnvObject;

/**
 *
 * @author faridn
 */
public class Letter extends EnvObject {
    
    private char letter;
   
    
    public Letter(char l,double x,double z){
        
    this.letter=l;
    
    if (l==' '){
        
         String image="models/letter/cube.png"; 
         this.setTexture(image);
    }
    
    else{
         String image="models/letter/"+this.letter+".png"; 
         this.setTexture(image);
    }
   
    
    this.setX(x);
    this.setY(2);
    this.setZ(z);
    this.setScale(2);
    this.setModel("models/letter/cube.obj");
    
    
     
    }
}