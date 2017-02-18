package main;
import java.awt.Color;

import processing.core.PApplet;


class Button  {
  float x, y, w, h;
  PApplet parent; 
  private Color bg = Color.WHITE;
  public boolean s = false;
  public boolean b = false;
  public Color color = Color.WHITE;
  int count =0;
	
  Button(float x, float y, float w, float h, Color c) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    color = c;
  } 
  public void setParent(PApplet p){
	  parent =p;
  }
  
  
  public boolean mouseOver() {
	  	int mX = parent.mouseX;
	  	int mY = parent.mouseY;
		if (mX > x && mX < x + w && mY > y && mY < y + h){
			b =true;
			return b;
		}	
		b =false;
		return b;
	}
  
  
  public boolean mouseClicked() {
	  if (b){
			count=0;
			s = !s;
			return true;
		}	
		return false;
   }
  
  public void draw() {
	parent.stroke(80);
	parent.strokeWeight(1);
	if (s)
		bg = color;
	else if (b)
		bg = Color.PINK;
	else	
		bg = new Color(150,150,150);
	
    parent.fill(bg.getRGB());
    parent.rect(x,y,w,h); 
    
    count++;
    if (count==10000)
    	count=200;
  }
  
}