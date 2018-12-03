/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.EnvObject;
import org.lwjgl.input.Keyboard;


/**
 *
 * @author faridn
 */
public class Tux extends EnvObject {
    
    
    public Tux(double x,double y,double z,String pion) {
   
      
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    this.setScale(3);
    
    this.setTexture(pion);
    //this.setTexture("models/tux/tux_dead.png");
    this.setModel("models/tux/tux.obj");
}
   public void move(int currentKey){
        
      if (currentKey == Keyboard.KEY_UP) {
       
      this.setRotateY(180);
      this.setZ(this.getZ() - 3);
    }  
      
      if (currentKey == Keyboard.KEY_DOWN){
          this.setRotateY(360);
          this.setZ(this.getZ()+3);
      }
      if (currentKey == Keyboard.KEY_RIGHT){
          this.setRotateY(90);
          this.setX(this.getX()+3);
      }
      if (currentKey == Keyboard.KEY_LEFT){
          this.setRotateY(270);
          this.setX(this.getX()-3);
      }
 }
}
    
    
    

