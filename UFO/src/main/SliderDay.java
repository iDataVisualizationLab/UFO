package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class SliderDay{
	public int pair =-1;
	public PApplet parent;
	public int x,y;
	public int w; 
	public int l =-1;
	public int u =-1;
	public int lH =-1;
	public int uH =-1;
	public int lM =-1;
	public int uM =-1;
	
	public static Color c1  = new Color(130,130,80);
	public static Color c2  = new Color(160,80,80);
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int gap = 1;
	
	
	public SliderDay(PApplet parent_){
		parent = parent_;
		l = 530;
		u = 630;
		x= 150;
		y= MainUFO_Version_3_0.ySliders;
		w= 24*MainUFO_Version_3_0.numMins*gap+gap;
	}
		
	public void update(){
		lH = l/(MainUFO_Version_3_0.numMins);
		uH = u/(MainUFO_Version_3_0.numMins);
		lM = (l%(MainUFO_Version_3_0.numMins))*2;
		uM = (u%(MainUFO_Version_3_0.numMins))*2;
	}

	public void draw(int yyy){
		parent.textSize(14);
		y = yyy;
		checkBrushingSlider();
		int xx1 = x+l;
		int xx2 = x+u;
		parent.stroke(Color.GRAY.getRGB());
		for (int h=0; h<25; h++ ){
			int xx = x+(h*MainUFO_Version_3_0.numMins*gap);
			if (h%12==0){
				parent.strokeWeight(2.0f);
				parent.line(xx, y-8, xx, y+8);
			}	
			else{ 
				parent.strokeWeight(1.0f);
				parent.line(xx, y-3, xx, y+3);
			}	
					
		}
		parent.strokeWeight(1.0f);
		
		//Lower range
		if (sSlider==0){
			c1= Color.WHITE;
		}	
		else if (bSlider==0){
			c1= Color.MAGENTA;
		}	
		else{
			c1 = new Color(130,130,80);
		}
		parent.fill(c1.getRGB());
		parent.triangle(xx1, y, xx1-8, y-12, xx1+8, y-12);
		parent.textAlign(parent.RIGHT, parent.BOTTOM);
		parent.text(lH+":"+lM,xx1+5,y+20);
		
		//Upper range
		if (sSlider==1){
			c2= Color.WHITE;
		}	
		else if (bSlider==1){
			c2= Color.MAGENTA;
		}	
		else{
			c2 = new Color(160,80,80);
		}
		parent.fill(c2.getRGB());
		parent.triangle(xx2, y, xx2-8, y-12, xx2+8, y-12);
		parent.textAlign(parent.LEFT, parent.BOTTOM);
		parent.text(uH+" "+uM,xx2-5,y+20);
		
		parent.fill(255,255,255);
		parent.textAlign(PApplet.RIGHT);
		parent.textSize(14);
		parent.text("Daily",x-5,y+5);
	}
	
	public String numToDayMonth(int m) {
		int year = m/12;
		int mon = m%12;
		String r =  ""; 
		switch ((mon+1)) {
		case 1:
			r = "Jan";
			break;
		case 2:
			r = "Feb";
			break;
		case 3:
			r = "Mar";
			break;
		case 4:
			r = "Apr";
			break;
		case 5:
			r = "May";
			break;
		case 6:
			r = "Jun";
			break;
		case 7:
			r = "Jul";
			break;
		case 8:
			r = "Aug";
			break;
		case 9:
			r = "Sep";
			break;
		case 10:
			r = "Oct";
			break;
		case 11:
			r = "Nov";
			break;
		case 12:
			r = "Dec";
			break;
		}
		return r;
	}	
	
	public void checkBrushingSlider() {
		int xx1 = x+l;
		int xx2 = x+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx1-50<mX && mX < xx1+6 && y-15<mY && mY<y+40){
			bSlider =0;
			return;
		}	
		else if (xx2-6<mX && mX < xx2+50 && y-15<mY && mY<y+40){
			bSlider =1; 
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
			if (l>=u) l=u-1;
			if (l<0)  l=0;
		}	
		else if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<=l) u=l+1;
			if (u>w-1)  u=w-1;
		}	
		
		return sSlider;
		
	}
}