package main;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import processing.core.PApplet;
import static main.MainUFO_Version_3_0.cityUFO;
import static main.MainUFO_Version_3_0.desUFO;
import static main.MainUFO_Version_3_0.durUFO;
import static main.MainUFO_Version_3_0.lon;
import static main.MainUFO_Version_3_0.lat;
import static main.MainUFO_Version_3_0.paralellCoordinate;
import static main.MainUFO_Version_3_0.numUFO;
import static main.MainUFO_Version_3_0.shapeUFO;
import static main.MainUFO_Version_3_0.stateUFO;
import static main.MainUFO_Version_3_0.timeUFO;
import static main.MainUFO_Version_3_0.dis6;


public class ParalellCoordinate{
	public int pair =-1;
	public float x,y;
	public float h; 
	public float l =-1;
	public float u =-1;
	public float lV =-1;
	public float uV =-1;
	
	public static Color c1  = new Color(130,130,80);
	public static Color c2  = new Color(160,80,80);
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int gap = 1;
	public String text;
	
	int id;
	public static ArrayList<Integer> a =  new ArrayList<Integer>();
	public static ArrayList<Integer> b =  new ArrayList<Integer>();
	float min = Float.POSITIVE_INFINITY;
	float max = Float.NEGATIVE_INFINITY;
	
	public static int numSec =  100;
	public ArrayList<Integer>[] count = new ArrayList[numSec+1];
	
	// This is for the word count
	public int numWords = 10; 
	public static int bPoint = -1;
	public static PApplet parent  = null;
	public ParalellCoordinate( int id_, float x_, float y_, float size_, String text_){
		id = id_;
		l = 0;
		if (id_==5)
			u=100;
		else 
			u = 400;
		x= x_;
		y= y_;
		h= size_;
		text = text_;	
		update();
		updateCount();
	}
		
	public void update(){
		lV = l/h;
		uV = u/h;
	}
	public void updateCount() {
		for(int i=0;i<numSec+1;i++){
			count[i] = new ArrayList<Integer>();
		}
		// Longitude
		if (id==3){
			for(int i=0;i<b.size();i++){
				int index = b.get(i);
				int loc = (int)  (numSec*(lon[index]-min)/(max-min));
				count[loc].add(index);
			}
		}
		// Latitude
		else if (id==4){
			for(int i=0;i<b.size();i++){
				int index = b.get(i);
				int loc = (int)  (numSec*(lat[index]-min)/(max-min));
				count[loc].add(index);
			}
		}
		// Nearest UFO
		else if (id==5){
			for(int i=0;i<b.size();i++){
				int index = b.get(i);
				float v = (float) ((dis6[index]-min)/(max-min));
				v = PApplet.sqrt(v); 
				int loc = (int)  (numSec*v);
				count[loc].add(index);
			}
		}
		// Year (time)
		else if (id==0){
			for(int i=0;i<b.size();i++){
				int index = b.get(i);
				Date d = timeUFO[index];
				long t = d.getTime()/(24*60*60*1000); // number of days from 1/1/1970
				int loc = (int)  (numSec*(t-min)/(max-min));
				count[loc].add(index);
			}
		}
		// Month
		else if (id==1){
			for(int i=0;i<b.size();i++){
				int index = b.get(i);
				Date d = timeUFO[index];
				long t = d.getMonth()*30+d.getDate(); // rough estimate number of days in a year
				int loc = (int)  (numSec*(t-min)/(max-min));
				count[loc].add(index);
			}
		}
		// Hour
		else if (id==2){
			for(int i=0;i<b.size();i++){
				int index = b.get(i);
				Date d = timeUFO[index];
				long t = d.getHours()*60+d.getMinutes(); // number of minutes in a day
				int loc = (int)  (numSec*(t-min)/(max-min));
				count[loc].add(index);
			}
		}
	}

	
	// Draw the details of sightings
	public static void drawSightingDetails(PApplet parent, ArrayList<Integer> a, float xLeft, float yy,String[] listOfHighlightedWords){
		float x =xLeft+30;
		float y = yy;
		
		parent.textAlign(PApplet.LEFT);
		parent.textSize(14);
		parent.fill(180); 
		
		y +=25;
		float x1 =x+12, x2 =x+150, x3 =x+260, x4 =x+370, x5 =x+500;
		parent.text("Time",x1, y);
		parent.text("City",x2, y);
		parent.text("Shape",x3, y);
		parent.text("Duration",x4, y);
		parent.text("Description",x5, y);
		
		for (int i=0; i< a.size();i++){
			y+=20;
			int index = a.get(i);
			drawProfiles(parent, index, x1,x2,x3,x4,x5,y,listOfHighlightedWords);
		}
	}
	public static void drawProfiles(PApplet parent, int i, float x1, float x2, float x3, float x4, float x5, float y, String[] listOfHighlightedWords) {
		if (y>parent.height)  // outside screen;
			return; 
		parent.fill(180);
		String year = ""+(timeUFO[i].getYear()+1900);
		String mon;
		int mmm = timeUFO[i].getMonth()+1;
		if (mmm<10)
			mon = "0"+mmm;
		else 
			mon = ""+mmm;
		String date;
		if (timeUFO[i].getDate()<10)
			date = "0"+timeUFO[i].getDate();
		else 
			date = ""+timeUFO[i].getDate();
		String hour;
		if (timeUFO[i].getHours()<10)
			hour = "0"+timeUFO[i].getHours();
		else 
			hour= ""+timeUFO[i].getHours();
		String min;
		if (timeUFO[i].getMinutes()<10)
			min = "0"+timeUFO[i].getMinutes();
		else 
			min= ""+timeUFO[i].getMinutes();
		
		parent.text(year+"-"+mon+"-"+date+"  "+hour+":"+min,x1, y);
		parent.text(cityUFO[i]+","+stateUFO[i],x2, y);
		parent.text(shapeUFO[i],x3, y);
		parent.text(durUFO[i],x4, y);
		parent.text(desUFO[i].toLowerCase(),x5, y);
		// Hightlight texts
		for (int j=0; j<listOfHighlightedWords.length;j++){
			String w  = listOfHighlightedWords[j];
			if (desUFO[i].toLowerCase().contains(w)){
				int index = desUFO[i].toLowerCase().indexOf(w);
				String subString = desUFO[i].toLowerCase().substring(0, index);
				float width =  parent.textWidth(subString);
				parent.fill(0,255,0);
				
				//check again to see if match
				boolean isOk = true;
				int beginIndex = index-1;
				if (beginIndex>=0){
					String char1 = desUFO[i].toLowerCase().substring(beginIndex, beginIndex+1);
					if (!char1.equals("") && !char1.equals(" ") && !char1.equals(".") && !char1.equals(","))
						isOk = false;
				}	
				int endIndex = index+w.length()+1;
				if (endIndex< desUFO[i].length()){
					String char1 = desUFO[i].toLowerCase().substring(endIndex-1, endIndex);
					if (!char1.equals("") && !char1.equals(" ") && !char1.equals(".") && !char1.equals(","))
						isOk = false;
				}
				if (isOk)	
					parent.text(w,x5+width, y);
			}
		}
	}
	
	public void draw(PApplet parent){
		checkBrushingSlider(parent);
		float yy1 = y+l;
		float yy2 = y+u;
		parent.textSize(12);
		parent.stroke(255);
		parent.strokeWeight(0.1f);
		parent.line(x, y, x, y+h);
		parent.fill(125);
		parent.textAlign(PApplet.RIGHT);
		if (id==0){
			parent.text("2001",x-9,y);
			parent.text("2011",x-9,y+h);
		}
		if (id==1){
			parent.text("1st",x-9,y);
			parent.text("30th",x-9,y+h);
		}
		if (id==2){
			parent.text("12:01am",x-9,y);
			parent.text("midnight",x-9,y+h);
		}
		if (id==3){
			parent.text("East",x-9,y);
			parent.text("West",x-9,y+h);
		}
		else if (id==4){
			parent.text("South",x-9,y);
			parent.text("North",x-9,y+h);
		}
		
		
		//Lower range
		if (sSlider==0){
			c1= Color.WHITE;
		}	
		else if (bSlider==0){
			c1= Color.PINK;
		}	
		else{
			c1 = new Color(125,125,125);
		}
		parent.fill(c1.getRGB());
		parent.triangle(x, yy1, x-6, yy1-10, x+6, yy1-10);
		
		//Upper range
		if (sSlider==1){
			c2= Color.WHITE;
		}	
		else if (bSlider==1){
			c2= Color.PINK;
		}	
		else{
			c2 = new Color(125,125,125);
		}
		parent.fill(c2.getRGB());
		parent.triangle(x, yy2, x-6, yy2+10, x+6, yy2+10);
		
		parent.fill(255,255,255);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(14);
		parent.text(text,x,y-20);
		
		
		// Draw distribution
		Color color = Color.GREEN;
			
		
		int kSatisfied = b.size();
		int numPoints = 200;
		if (kSatisfied> numPoints){
			// Draw chart on top of bar
			for (int i=-1;i<2;i=i+2){
				parent.beginShape();
				float xG = x;
				float yG = y;
				parent.curveVertex(xG, yG);
				parent.curveVertex(xG, yG);
				for (int l =0; l<numSec;l++){
					xG = x + i*count[l].size();
					yG = y + l*h/numSec;
					parent.curveVertex(xG, yG);
				}
				parent.curveVertex(x, y+h);
				parent.curveVertex(x, y+h);
				parent.stroke(new Color(60, 60, 60, 250).getRGB());
				parent.strokeWeight(1);
				parent.fill(color.getRGB());
				parent.endShape();
			}
		}
		else{
			parent.stroke(new Color(0, 0, 0).getRGB());
			parent.strokeWeight(.5f);
			float step = h/numSec;
			for (int l =0; l<numSec;l++){
				for (int i=0; i<count[l].size();i++){
					float xG = x-(count[l].size()*step/2f) + (i+0.5f)*step;
					float yG = y + l*step+step;
					if (PApplet.dist(xG, yG, parent.mouseX, parent.mouseY)<=step){
						if (bPoint<0)
							bPoint =count[l].get(i);
					}
					parent.fill(color.getRGB());
					if (count[l].get(i)==bPoint){
						parent.fill(Color.RED.getRGB());
						parent.ellipse(xG, yG-step/2, step+1, step+1);
					}
					else{
						parent.ellipse(xG, yG-step/2, step, step);
					}
				}
			}
		}
		
	}
	
	
	public void checkBrushingSlider(PApplet parent) {
		float yy1 = y+l;
		float yy2 = y+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (x-10<mX && mX < x+10 && yy1-20<mY && mY<yy1+10){
			bSlider =0;
			return;
		}	
		else if (x-10<mX && mX < x+10 && yy2-10<mY && mY<yy2+20){
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
	public int checkSelectedSlider3(PApplet parent) {
		if (sSlider==0){
			l += (parent.mouseY - parent.pmouseY);
			if (l>=u) l=u-1;
			if (l<0)  l=0;
		}	
		else if (sSlider==1){
			u += (parent.mouseY - parent.pmouseY);
			if (u<=l) u=l+1;
			if (u>h)  u=h;
		}	
		
		// update sliders
		checkall();
		return sSlider;
	}
	
	public void computeminMax() {
		// longitude
		if (id==3){
			for(int i=0;i<numUFO;i++){
				if (lon[i]<min)
					min = lon[i];
				else if (lon[i]>max)
					max = lon[i];
			}
		}
		// latitude
		else if (id==4){
			for(int i=0;i<numUFO;i++){
				if (lat[i]<min)
					min = lat[i];
				else if (lat[i]>max)
					max = lat[i];
			}
		}
		// nearest UFO
		else if (id==5){
			for(int i=0;i<numUFO;i++){
				if (dis6[i]<min)
					min = (float) dis6[i];
				else if (dis6[i]>max)
					max = (float) dis6[i];
			}
		}
		// Year (time)
		else if (id==0){
			for(int i=0;i<numUFO;i++){
				Date d = timeUFO[i];
				long t = d.getTime()/(24*60*60*1000); // number of days from 1/1/1970
				if (t<min)
					min = t;
				else if (t>max)
					max = t;
			}
		}
		// Month
		else if (id==1){
			for(int i=0;i<numUFO;i++){
				Date d = timeUFO[i];
				long t = d.getMonth()*30+d.getDate(); // rough estimate number of days in a year
				if (t<min)
					min = t;
				else if (t>max)
					max = t;
			}
		}
		// Day
		else if (id==2){
			for(int i=0;i<numUFO;i++){
				Date d = timeUFO[i];
				long t = d.getHours()*60+d.getMinutes(); // rough estimate number of days in a year
				if (t<min)
					min = t;
				else if (t>max)
					max = t;
			}
		}
	}
		
	public static void checkall() {
		b =  new ArrayList<Integer>();
				
		for (int i=0;i<a.size();i++){
			int index = a.get(i);
			boolean isNOTsatified = false;
			for (int s=0;s<paralellCoordinate.length;s++){
				float min = paralellCoordinate[s].min;
				float max = paralellCoordinate[s].max;
				float low = (min+paralellCoordinate[s].lV*(max-min));
				float high = (min+paralellCoordinate[s].uV*(max-min));
				if (s==3){
					if (lon[index]<low || lon[index]>high)
						isNOTsatified = true;
				}
				else if (s==4){
					if (lat[index]<low || lat[index]>high)
						isNOTsatified = true;
				}
				else if (s==5){
					low = (min+paralellCoordinate[s].lV*paralellCoordinate[s].lV*(max-min));
					high = (min+paralellCoordinate[s].uV*paralellCoordinate[s].uV*(max-min));
					if (dis6[index]<low || dis6[index]>high)
						isNOTsatified = true;
				}
				else if (s==0){
					Date d = timeUFO[index];
					long t = d.getTime()/(24*60*60*1000); // number of days from 1/1/1970
					if (t<low || t>high)
						isNOTsatified = true;
				}
				else if (s==1){
					Date d = timeUFO[index];
					long t = d.getMonth()*30+d.getDate(); // rough estimate number of days in a year
					if (t<low || t>high)
						isNOTsatified = true;
				}
				else if (s==2){
					Date d = timeUFO[index];
					long t = d.getHours()*60+d.getMinutes(); // number of minutes in a day
					if (t<low || t>high)
						isNOTsatified = true;
				}
			}
			if (!isNOTsatified){
				b.add(index);
			}
		}
		
		for (int s=0;s<paralellCoordinate.length;s++){
			paralellCoordinate[s].update();
			paralellCoordinate[s].updateCount();
		}		
	}
	
	public static void updateTDA() {
		b =  new ArrayList<Integer>();
		
		// 2017: Update the text cloud and topic modeling
		for (int i=0;i<main.MainUFO_Version_3_0.isUFOselected.length; i++){
			main.MainUFO_Version_3_0.isUFOselected[i] = false;
		}
				
		for (int i=0;i<a.size();i++){
			int index = a.get(i);
			boolean isNOTsatified = false;
			for (int s=0;s<paralellCoordinate.length;s++){
				float min = paralellCoordinate[s].min;
				float max = paralellCoordinate[s].max;
				float low = (min+paralellCoordinate[s].lV*(max-min));
				float high = (min+paralellCoordinate[s].uV*(max-min));
				if (s==3){
					if (lon[index]<low || lon[index]>high)
						isNOTsatified = true;
				}
				else if (s==4){
					if (lat[index]<low || lat[index]>high)
						isNOTsatified = true;
				}
				else if (s==5){
					low = (min+paralellCoordinate[s].lV*paralellCoordinate[s].lV*(max-min));
					high = (min+paralellCoordinate[s].uV*paralellCoordinate[s].uV*(max-min));
					if (dis6[index]<low || dis6[index]>high)
						isNOTsatified = true;
				}
				else if (s==0){
					Date d = timeUFO[index];
					long t = d.getTime()/(24*60*60*1000); // number of days from 1/1/1970
					if (t<low || t>high)
						isNOTsatified = true;
				}
				else if (s==1){
					Date d = timeUFO[index];
					long t = d.getMonth()*30+d.getDate(); // rough estimate number of days in a year
					if (t<low || t>high)
						isNOTsatified = true;
				}
				else if (s==2){
					Date d = timeUFO[index];
					long t = d.getHours()*60+d.getMinutes(); // number of minutes in a day
					if (t<low || t>high)
						isNOTsatified = true;
				}
			}
			if (!isNOTsatified){
				b.add(index);
				// 2017: Update the text cloud and topic modeling
				main.MainUFO_Version_3_0.isUFOselected[index] = true;
			}
		}
		
		
		WordCount wc = new WordCount(30);
		wc.countMainUFO(); 
		
		// Topic modeling **************** BEGIN ****************	
		if (wc.listTopics!=null){
			String[] topicArray = new String[WordCount.numTopics];
			int[] topicCount = new int[WordCount.numTopics];
			int i=0;
			for(TopicIdvl m :   wc.listTopics){   	  
		    	  topicArray[i] = m.getTopicString();
		    	  topicCount[i] = (int) (m.getTopicDist()*100);
		    	  System.out.println("i="+i+" count="+topicCount[i]+" topicArray="+topicArray[i]);   	    
		    	  i++;
		    	//  System.out.println(m.getTopicId()+ "\t"+m.getTopicDist()+"\t"+m.getTopicString());
		    }
			MainUFO_Version_3_0.topicCloud = new WTopicCloud(topicArray, topicCount, parent, parent.width/2-180,parent.width-170,parent.height/2+120,parent.height-20, false);
		}
		// Topic modeling **************** END	****************
		
		MainUFO_Version_3_0.wordCloud = new WordCloud(wc.wordArray, wc.counts, parent, parent.width-300,parent.width,50,parent.height/2, false);
		
		
		for (int s=0;s<paralellCoordinate.length;s++){
			paralellCoordinate[s].update();
			paralellCoordinate[s].updateCount();
		}		
	}
	
		
}