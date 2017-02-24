package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class SliderTime{
	public int pair =-1;
	public PApplet parent;
	public int x,y;
	public int w; 
	public int l =-1;
	public int u =-1;
	public int lM =-1;
	public int uM =-1;
	
	
	public static Color c1  = new Color(130,130,80);
	public static Color c2  = new Color(160,80,80);
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int gap = 8;
	
	
	public SliderTime(PApplet parent_){
		parent = parent_;
		l = 500;
		u = 730;
		x= 160;
		y= MainUFO_Version_3_0.ySliders;
		w=121*gap;
	}
		
	public void update(){
		lM = l/gap;
		uM = u/gap;
	}
		
	public void draw(int yyy){
		checkBrushingSlider();
		y=yyy;
		int xx1 = x+l;
		int xx2 = x+u;
		parent.stroke(Color.GRAY.getRGB());
		parent.textSize(14);
		for (int year=0; year<11;year++ ){
			int xx = x+(year*12*gap);
			parent.strokeWeight(2.6f);
			parent.line(xx, y-8, xx, y+8);
			if (year>=10) break;
			for (int mon=1; mon<12; mon++ ){
				parent.strokeWeight(1.0f);
				parent.line(xx+mon*gap, y-4, xx+mon*gap, y+4);
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
		String[] mL = numToMonth(lM);
		parent.textAlign(parent.RIGHT, parent.BOTTOM);
		parent.text(mL[1]+" "+mL[0],xx1+5,y+20);
		
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
		String[] mU = numToMonth(uM);
		parent.textAlign(parent.LEFT, parent.BOTTOM);
		parent.text(mU[1]+" "+mU[0],xx2-5,y+20);
		
		parent.fill(255,255,255);
		parent.textAlign(PApplet.RIGHT);
		parent.textSize(14);
		parent.text("Time",x-5,y+5);
	}
	
	public String[] numToMonth(int m) {
		int year = m/12;
		int mon = m%12;
		String[] r = new String[2];
		r[0] = ""+(year+2001);
		r[1] = ""; 
		switch ((mon+1)) {
		case 1:
			r[1] = "Jan";
			break;
		case 2:
			r[1] = "Feb";
			break;
		case 3:
			r[1] = "Mar";
			break;
		case 4:
			r[1] = "Apr";
			break;
		case 5:
			r[1] = "May";
			break;
		case 6:
			r[1] = "Jun";
			break;
		case 7:
			r[1] = "Jul";
			break;
		case 8:
			r[1] = "Aug";
			break;
		case 9:
			r[1] = "Sep";
			break;
		case 10:
			r[1] = "Oct";
			break;
		case 11:
			r[1] = "Nov";
			break;
		case 12:
			r[1] = "Dec";
			break;
		}
		return r;
	}	
	
	public void checkBrushingSlider() {
		int xx1 = x+l;
		int xx2 = x+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx1-80<mX && mX < xx1+6 && y-15<mY && mY<y+40){
			bSlider =0;
			return;
		}	
		else if (xx2-6<mX && mX < xx2+80 && y-15<mY && mY<y+40){
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
			if (l>=u) l=u;
			if (l<0)  l=0;
		}	
		else if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<=l) u=l;
			if (u>w-1)  u=w-1;
		}	
		
		return sSlider;
		
	}
}