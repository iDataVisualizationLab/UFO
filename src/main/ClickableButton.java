package main;
import java.awt.Color;

import processing.core.PApplet;


public class ClickableButton extends Button {
 public  String name;
	ClickableButton(float x, float y, float w, float h, String s, Color c){
		super(x, y, w, h, c);
		this.name = s;
	
	}
	public void draw() 
	{
		super.draw();
		parent.textSize(14);
		parent.fill(0);
		parent.noStroke();
		parent.textAlign(PApplet.CENTER);
		if (!name.equals("Relationship")){
			
			parent.text(name, x + w/2, y + 20);
		}
		else{
			if (WordCloud.s>=0){
				parent.fill(255,0,0);
			}	
			else{
				parent.fill(0,200,0);
			}
			parent.text(name, x + w/2, y + 20);	
		}
	}
}
