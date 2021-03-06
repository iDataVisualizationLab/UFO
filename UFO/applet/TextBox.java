package main;
import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;

public class TextBox {
	public boolean s = false;
	public boolean b = false;
	public PApplet parent;
	public int x = 0;
	public int y = 0;
	public int h = 25;
	public int w = 150;
	public char pKey = ' ';
	public String text = "";
	public String searchText = "";
	
	// Draw srach results
	public int nResults = 25;
	public int sIndex = -1;
	public int w2 = 370; 
	public int hBox = 640;
	public int h3 = 24;  // TextBox result high
	public ArrayList<String> indices1 = new ArrayList<String>();;
	public ArrayList<Integer> indices2 = new ArrayList<Integer>();;
	public String[] list;
	public int[] count;
	public int mouseOnTextList = -1;
	
	public TextBox(PApplet parent_, int x_, int y_, String text_, String[] l, int[] count_) {
		parent = parent_;
		x = x_;
		y = y_;
		text = text_;
		list = l;
		count = count_;
	}

	public void draw() {
		checkBrushing();
		
		parent.strokeWeight(1f);
		parent.textAlign(PApplet.LEFT);
		parent.fill(20, 20, 20);
		parent.stroke(125, 125, 125);
		if (b)
			parent.stroke(Color.PINK.getRGB());
		if (s) {
			parent.stroke(Color.WHITE.getRGB());
		}
		parent.rect(x, y, w, h);

		// Main Text
		//parent.textFont(font, 12);
		parent.textAlign(PApplet.LEFT);
		parent.textSize(16);
		parent.fill(0, 255, 0);
		parent.text(searchText, x +5, y + h - 8);

		// Explaining Text
		parent.textAlign(PApplet.RIGHT);
		parent.fill(0, 0, 0);
		parent.text(text, x-3, y + 18);
		parent.fill(0, 200, 0);
		parent.text(text, x-4, y + 17);

		if (s)
			drawClickableSearchResults();
	}

	public void keyPressed() {
		if (s) {
			char c = (char) parent.key;
			indices1 = new ArrayList<String>();
			indices2 = new ArrayList<Integer>();
			if (c == 8 && searchText.length() > 0) {
				searchText = searchText.substring(0, searchText.length() - 1);
				if (searchText.length() == 1 && searchText.equals(""))
					searchText = "";
			}
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9') || (c == ' ') || (c == '-')
					|| (c == '.')) {
				searchText = searchText + parent.key;
			}

			else if (c == 10)
				searchText = "";
			// Making sure duplicates are not included
			
				for (int i = 0; i < list.length; i++) {
					if (list[i]!=null){
						String tolower = list[i].toLowerCase();
						if (tolower.contains(searchText.toLowerCase())) {
							indices1.add(tolower);
							indices2.add(i);
						} 
					}
				}
			
		}
	}

	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (x < mX && mX < x + w && y < mY && mY < y + h + 5) {
			b = true;
			return;
		}
		b = false;
	}

	public int mouseClicked() {
		if (b) {
			s = true;
		}
		else{
			s = false;
		}
		// Making Default list 
		if (searchText.equals("")){
			for (int i = 0; i < list.length; i++) {
				if (i<nResults){
					String tolower = list[i].toLowerCase();
					indices1.add(tolower);
					indices2.add(i);
				}
			}
		}
		return sIndex;
	}

	// used for search box
	void drawClickableSearchResults() {
		float y1 = y+15;
		parent.strokeWeight(1);
		parent.stroke(200);
		parent.fill(100,100,100,250);
		parent.rect(x+w, y, w2, hBox);
		parent.textSize(14);
		int selected =-1;
		mouseOnTextList = -1;
		for (int i = 0; i < nResults && i < indices1.size(); i++) {
			if (parent.mouseX >= x+w  && parent.mouseX <= x + w+w2
				&& parent.mouseY > y1 && parent.mouseY <= y1+h3)
				mouseOnTextList = i;
			if (mouseOnTextList==i) {
				parent.stroke(Color.PINK.getRGB());
				parent.fill(0, 0, 0);
				parent.rect(x + w+20, y1, w2-50, h3-1);
				
				parent.textAlign(PApplet.LEFT);
				parent.fill(Color.PINK.getRGB());
				parent.text(indices1.get(i), x + w + 30, y1 + 18);
				parent.textAlign(PApplet.CENTER);
				y1 += h3;
				selected = indices2.get(i);
			} else {
				parent.noStroke();
				parent.fill(0, 0, 0);
				parent.rect(x + w + 20, y1, w2-50, h3-1);
				parent.textAlign(PApplet.LEFT);
				String tt1 = indices1.get(i);
				
				float wid =  parent.textWidth(tt1);
				String tt2 = "("+count[indices2.get(i)]+")";	
				if (i==0) 
					tt2 = "("+count[indices2.get(i)]+" sightings)";
				parent.fill(220);
				parent.text(tt1, x + w + 30, y1 + 18);
				parent.fill(110);
				parent.text(tt2, x + w + wid +40, y1 + 18);
				
				parent.textAlign(PApplet.CENTER);
				y1 += h3;
			}
		}
		sIndex = selected;
		
	}
}