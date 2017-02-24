package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class SliderLeft{
	public int pair =-1;
	public PApplet parent;
	public float x,y;
	public int w; 
	public int l =-1;
	public int day =-1;
	public int hour =-1;
	public int bSlider = -1;
	public int sSlider = -1;
	public int gap= 6;
	
	
	public SliderLeft(PApplet parent_){
		parent = parent_;
		l = 0;
		x= 125;
		y= MainUFO_Version_3_0.ySliders-67;
		w=gap*31;
	}
	
	
	public void update(){
		day = l/gap +1;
		hour = l%gap;
	}
		
		
	public void draw(int yyy){
		y = yyy-67;
		checkBrushingSlider();
		parent.stroke(SliderTime.c1.getRGB());
		for (int d=0; d<=31;d++ ){
			float xx = x+(d*gap);
			parent.strokeWeight(1f);
			if (d%10==0){
				parent.line(xx, y-3, xx, y+3);
			}
			else{
				parent.line(xx, y-1, xx, y+1);
			}
		}
		
		parent.strokeWeight(1f);
		//Lower range
		parent.fill(SliderTime.c1.getRGB());
		if (sSlider==0)
			parent.fill(Color.WHITE.getRGB());
		else if (bSlider==0)
			parent.fill(Color.MAGENTA.getRGB());
		float xx1 = x+l;
		parent.triangle(xx1, y, xx1-8, y-12, xx1+8, y-12);
		parent.textAlign(PApplet.RIGHT, PApplet.BOTTOM);
		parent.text(day,xx1-2,y+18);	
	}
	
	public void checkBrushingSlider() {
		float xx1 = x+l;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx1-100<mX && mX < xx1+4 && y-15<mY && mY<y+20){
			bSlider =0;
			return;
		}	
		bSlider =-1;
	}
	
	public void checkSelectedSlider1() {
		sSlider = bSlider;
	}
	public void checkSelectedSlider2() {
		sSlider = -1;
	}
	public int checkSelectedSlider3() {
		if (sSlider==0){
			l += (parent.mouseX - parent.pmouseX);
			if (l>=w) l=w-1;
			if (l<0)  l=0;
		}	
		return sSlider;
		
	}
}