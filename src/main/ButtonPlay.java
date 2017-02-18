package main;
import java.awt.Color;

import processing.core.PApplet;


class ButtonPlay  {
  float x, y, w, h;
  PApplet parent; 
  boolean isPlayed = false;
  ButtonPlay(float x, float y, float w, float h,PApplet p) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    parent =p;
  } 
  
  
  
  public boolean mouseOver() {
    return (parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h);
  }
  
  public boolean mouseClicked() {
	    if (parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h){
	    	isPlayed = !isPlayed;
	    	return true;
	    }
	    return false;
 }
  
  public void draw() {
	parent.stroke(80);
    if (isPlayed){
     	parent.fill(mouseOver() ? new Color(255,100,0,255).getRGB() : new Color(255,100,0,190).getRGB());
    	parent.rect(x,y,w*0.4f,h); 
    	parent.rect(x+w*.6f,y,w*0.4f,h); 
    }	
    else{
     	parent.fill(mouseOver() ? new Color(0,255,0,255).getRGB() : new Color(0,255,0,180).getRGB());
    	parent.triangle(x,y,x+w,y+h/2,x,y+h); 
    }
  }
}