package main;
import java.awt.Color;

import com.modestmaps.InteractiveMap;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.Google;
import com.modestmaps.providers.Yahoo;
import com.modestmaps.providers.AbstractMapProvider;
import com.modestmaps.providers.Microsoft;

import processing.core.PApplet;
import processing.core.PFont;
import static main.MainUFO_Version_2_5.imap;

public class PopupSighting{
	public int b = -1;
	public PApplet parent;
	public float x = main.MainUFO_Version_2_5.x1;
	public float x2 = main.MainUFO_Version_2_5.w1;
	public int y = main.MainUFO_Version_2_5.y1+30;
	public int w = main.MainUFO_Version_2_5.w1;
	public int h = main.MainUFO_Version_2_5.h1;
	public int itemH = 20;
	public Color[] colors = {Color.WHITE,Color.BLACK,Color.PINK, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN}; 
	public static int s =0;
	PFont metaBold;
	public static Color sightingColor = Color.WHITE;
	
	public PopupSighting(PApplet parent_){
		parent = parent_;
	}
	
	
	public void draw(){
		metaBold = parent.loadFont("Arial-BoldMT-18.vlw");
		checkBrushing();
		
		// Color legend
		parent.noStroke();
		parent.strokeWeight(1);
		parent.textSize(13);
		parent.textAlign(PApplet.LEFT);
		parent.fill(150,150,150);
		if (b>=0)
			parent.fill(255,255,255);
		parent.rect(x,y,w,h);
		
		parent.textFont(metaBold,14);
		parent.fill(0,0,0);
		parent.textAlign(PApplet.CENTER);
		parent.text("Sightings",x+w/2,y+19);
	
		if (b>=0){
			parent.textFont(metaBold, 13);
			parent.fill(new Color(50,50,50,240).getRGB());
			parent.noStroke();
			parent.rect(x2, y-2, w*0.63f, itemH*(colors.length+1));
			for (int i=0;i<colors.length;i++){
				if (i==s){
					parent.noStroke();
					parent.fill(0);
					parent.rect(x2,y+itemH*(i)+3,w*0.63f,itemH);
				}
				else if (i==b){
					parent.noStroke();
					parent.fill(Color.PINK.getRGB());
					parent.rect(x2,y+itemH*(i)+3,w*0.63f,itemH);
				}
				
				parent.fill(colors[i].getRGB());
				parent.stroke(100,100,100);
				parent.rect(x2+20,y+itemH*(i)+5, 15,15);
			}	
		}
		parent.textAlign(PApplet.LEFT);
	}
	
	 public void mouseClicked() {
		if (b>=0 && b!=100){
			s =b;
			if (s<colors.length)
				sightingColor = colors[s];
		}	
   	}
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (b==-1){
			if (x<mX && mX<x+w && y<=mY && mY<=y+h){
				b =100;
				return;
			}	
		}
		else{
			if (x<mX && mX<x+w && y<=mY && mY<=y+h){
				b =100;
				return;
			}	
			for (int i=0; i<colors.length; i++){
				if (x2<=mX && mX<=x2+w*0.63f && y+itemH*i<=mY && mY<=y+itemH*(i+1)+5){
					b =i;
					return;
				}	
			}
		}
		b =-1;
	}
	
}