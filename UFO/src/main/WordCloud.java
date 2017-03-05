package main;

/**
 * Tag Cloud by Wray Bowling

 * For more information: http://processing.org/discourse/yabb2/YaBB.pl?num=1214504959
 */
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class WordCloud{
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public int height;
	public PFont font2;
	float baseline_ratio = (float)0.28;
	int large_font = 40;
	int small_font = 13;
	float spring = (float)0.02;
	int numWords;
	public static Tag[] cloud; 
	PApplet parent;
	int maxCount = 0;
	// Draw nearest neighbor, task 6
	boolean isNearest;
	public static int b = -1;
	public static int s = -1;
	public static Integrator xLeft = new Integrator(1280);
	
	
	//CHANGE HERE PARAMETERS OF CLOUD
	public WordCloud(String[] strings, int[] counts,  PApplet p, 
			int x1_, int x2_, int y1_, int y2_, boolean isNearest_){
		//change these for location of tags
		parent = p;
		x1 = x1_;
		x2 = x2_;
		y1 = y1_;
		y2 = y2_;
		xLeft.target(x1);
		isNearest = isNearest_;
		maxCount = counts[0];
		cloud = new Tag[strings.length];
		numWords = strings.length;
		font2 = parent.loadFont("Arial-BoldMT-18.vlw");
		parent.textFont(font2);
		for (int i = 0; i < numWords; i++) {
			cloud[i] = new Tag(cloud, i, strings[i], counts[i], maxCount , x1, y1);
		}
		b = -1;
		s=-1;
	}

	//CHANGE HERE FOR BACKGROUND BLACK BOX
	public void draw(PApplet p) {
		for (int i = 0; i < numWords; i++) {
			cloud[i].checkBrushing(p);
			cloud[i].collide();
			cloud[i].move();
			cloud[i].display(p, isNearest);  
		}
    }
	public void mouseClicked(){
		if (b>=0){
			if (s==b){
				s=-100;
			}
			else
				s=b;
		}
		else{
			s=-1;
		}
	}
	
	
	class Tag {
		float x, y;
		float word_width;
		float  font_size;
		public String word;
		Color  color;
		float vx = 0;
		float vy = 0;
		int id;
		Tag[] others;
		
		Tag(Tag[] others, int id, String str, int count, int maxCount,  int x, int y) {
			this.x = x + parent.random(0, x2-x1-50);
			this.y = y + parent.random(0, y2-y1-150);
			
			large_font = 40;
			small_font = 14;
			float dif = large_font - small_font;
			if (maxCount*2<dif){
				large_font = small_font+maxCount*2;
			}	
			
			font_size = (PApplet.map(count, 0, maxCount, small_font, large_font));
			
			float v = PApplet.map(count, 0, maxCount, 0, 1);
			color = new Color(0,0.3f+v*0.7f,0);
			
			word = str;
			parent.textFont(font2, font_size);
			word_width = parent.textWidth(word);
			while (word_width>x2-x1 && word.length()>1){
				word = word.substring(0, word.length()-1);
				word_width = parent.textWidth(word);
			}
			this.id = id;
			this.others = others;
		} 

		/* I mucked with this function pretty heavily, but to no avail :( */
		void collide() {
			for (int i = id + 1; i < numWords; i++) {
				float dx = others[i].x + others[i].word_width/2 - x + word_width/2;
				float dy = others[i].y + others[i].font_size/2 - y + font_size/2;
				float minDistX = word_width + others[i].word_width;
				float minDistY = font_size + others[i].font_size;

				if ( (dx < minDistX) && (dy < minDistY) ){ 
					float angle = PApplet.atan2(dy, dx);
					float targetX = x + PApplet.cos(angle) * minDistX;
					float ax = (targetX - others[i].x) * spring;
					vx -= ax;
					others[i].vx += ax;
					float targetY = y + minDistY;
					float ay = (targetY - others[i].y) * spring;
					vy -= ay;
					others[i].vy += ay;
				}
			}   
		}

		void move() {
			vx *= 0.1;
			vy *= 0.75;
			x += vx;
			y += vy;
			if (x + word_width/2 > x2) {
				x = x2 - word_width/2;
				vx *= -0.5; 
			}
			else if (x - word_width/2 < x1) {
				x = x1+word_width/2;
				vx *= -0.5;
			}
			if (y + font_size/2 > y2) {
				y = y2 - font_size/2;
				vy *= -0.5; 
			} 
			else if (y - font_size/2 < y1) {
				y = y1+font_size/2;
				vy *= -0.5;
			}
		}

		int checkBrushing(PApplet p) {
			if (x-word_width/2<parent.mouseX && parent.mouseX<x+word_width/2 &&
				y-font_size<parent.mouseY && parent.mouseY<y){
				b=id;
			}	
			return b;
		}
		
		
		void display(PApplet p, boolean isNearest) {
			parent.textAlign(PApplet.LEFT);
			if (!isNearest)
				parent.fill(color.getRGB());
			else 
				parent.fill(0);
			if (id==b){
				parent.fill(Color.ORANGE.getRGB());
			}
			if (id==s)
				parent.fill(Color.RED.getRGB());
				
			if (!isNearest)
				parent.textFont(font2, font_size);
			else
				parent.textFont(font2, font_size);
					
			parent.text(word, (int)(x - word_width/2), y);
		}
	} 
}