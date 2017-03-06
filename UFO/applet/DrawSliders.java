package main;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;

import processing.core.PApplet;
import processing.core.PFont;
import static main.MainUFO_Version_3_0.*;

class DrawSliders {
	PApplet parent;
	public int yr = 2008;
	public int day = 1;
	public int month = 1;
	public int hr = 1;
	public int min= 12;
	public int dayWeek = 0;
	public int selectedUFO = -1;
	public int selectedCluster = -1;

	public int oldSelectedUFO = -100;
	public WordCloud theTagCloudNearest6;
	
	public DrawSliders(PApplet p) {
		parent = p;
	}

	public void drawSightings(int i) {
		if (kMeansButton.s && selectedCluster>=0 && clusterID[i] !=selectedCluster) return;
			
		Location location = new Location(lat[i], lon[i]);
		Point2f p = imap.locationPoint(location);
		parent.noStroke();
		int red = main.PopupSighting.sightingColor.getRed();
		int green = main.PopupSighting.sightingColor.getGreen();
		int blue = main.PopupSighting.sightingColor.getBlue();
		parent.fill(red,green,blue,180);
		
		// color by clusters 
		if (kMeansButton.s){
			parent.fill(255,0,0,100);
			Color color;
			if (clusterID[i]<=numCluster-3){
				double value = (double) clusterID[i]/(numCluster-3);
				color = ColorScales.getColor(value, "rainbow", 0.7f);	
			}	
			else if (clusterID[i]==numCluster-2)
				color = new Color(255,200,200,180);
			else if (clusterID[i]==numCluster-1)
				color = new Color(255,0,255,180);
			else
				color = new Color(200,200,200,180);
					
			parent.fill(color.getRGB());
		}
		else if (sameUFOButton.s){
			int year = timeUFO[i].getYear()-101;
			int maxYear = 9;
			double value = (double) year/maxYear;
			Color color = ColorScales.getColor(value, "rainbow", 0.7f);	
			parent.fill(color.getRGB());
		}
		
		if (wX1<= p.x && p.x<=wX2 && wY1<= p.y &&p.y<=wY2){
			isUFOselected[i] = true;
			parent.fill(0,255,0);
		}
		
		parent.ellipse(p.x, p.y, r, r);
		if (dist(parent.mouseX, parent.mouseY, p.x, p.y) < r) {
			selectedUFO = i;
			selectedCluster = clusterID[i];
		}
		
	}
	
	
	public void drawSightingsHavingCommonWords(int i, Color c) {
		Location location = new Location(lat[i], lon[i]);
		Point2f p = imap.locationPoint(location);
		parent.noStroke();
		parent.fill(c.getRGB());
		parent.ellipse(p.x, p.y, r, r);
		if (dist(parent.mouseX, parent.mouseY, p.x, p.y) < r) {
			selectedUFO = i;
			selectedCluster = clusterID[i];
		}
		
	}
	
	
	public void drawSelectedUFO(){
		parent.textAlign(LEFT);
		parent.textSize(13);
		parent.strokeWeight((float) 2); 
		parent.stroke(0); 
		parent.fill(255); 
		parent.rect(parent.mouseX, parent.mouseY + 10, 340, 220);
		parent.fill(0); 
		String s = "Time: " + timeUFO[selectedUFO] +"\nCity: "+cityUFO[selectedUFO]+"\nShape: "
		+ shapeUFO[selectedUFO]+"\nState: "+stateUFO[selectedUFO]+"\nDuration: "+durUFO[selectedUFO]+"\nDescription: ";
		if(desUFO[selectedUFO].length()>40){
			String[] p = desUFO[selectedUFO].split(" ");
			String s2="";
			boolean stop = false;
			for(int j = 0; j<p.length && !stop;j++){
				if(s2.length()+p[j].length() > 40){
					s+=s2+"\n";
					s2 = p[j]+" ";
				}
				else{
					s2+=p[j] + " ";
				}
			}
		}else{
			s+=desUFO[selectedUFO];
		}
		parent.text( s,parent.mouseX+10, parent.mouseY + 30);
	}
	
	public void drawNearestUFO6(SliderDistance sliderDistance ){
		float x =150;
		float w =1230;
		float h =300;
		float y = parent.mouseY+10;
		if (y>parent.height-h-50)
			y = parent.mouseY-10-h;
		
		parent.textAlign(LEFT);
		parent.textSize(14);
		parent.strokeWeight((float) 2); 
		parent.stroke(0); 
		parent.fill(255); 
		parent.rect(x, y, w, h);
		parent.fill(0); 
		
		y +=25;
		float x1 =x+12, x2 =x+150, x3 =x+260, x4 =x+370, x5 =x+500;
		parent.text("Time",x1, y);
		parent.text("City",x2, y);
		parent.text("Shape",x3, y);
		parent.text("Duration",x4, y);
		parent.text("Description",x5, y);
		
		int i = nearest6[selectedUFO];
		y+=20;
		drawNearest(selectedUFO, x1,x2,x3,x4,x5,y);
		y+=20;
		drawNearest(i, x1,x2,x3,x4,x5,y);
		
		if (oldSelectedUFO != selectedUFO){
			oldSelectedUFO = selectedUFO;
			main.MainUFO_Version_3_0.isUFOselected =  new boolean[numUFO];
			main.MainUFO_Version_3_0.isUFOselected[selectedUFO] = true;
			main.MainUFO_Version_3_0.isUFOselected[i] = true;
			WordCount wc = new WordCount(30);
			wc.countMainUFO(); 
			theTagCloudNearest6 = new WordCloud(wc.wordArray, wc.counts, parent, (int) x5, 1260, (int) y+30, (int) (y+h-30), true);
		}
		parent.textAlign(PApplet.LEFT);
		theTagCloudNearest6.draw(parent);
	}
	public void drawNearest(int i, float x1, float x2, float x3, float x4, float x5, float y) {
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
		parent.text(desUFO[i],x5, y);
	}
		
	
	public void drawClusterCenters(int[]  clusterCenter) {
		for (int i = 0; i < numCluster; i++) {
			if (selectedCluster>=0 && i != selectedCluster) continue;
			int id = clusterCenter[i];
			Location location = new Location(lat[id], lon[id]);
			Point2f p = imap.locationPoint(location);
			parent.fill(255,255,255);
			parent.strokeWeight(1);
			parent.stroke(0);
			parent.ellipse(p.x, p.y, r*1.4f, r*1.4f);
		}
	}
		
	
	public void plotSliderDistance(SliderDistance sliderDistance, 
			double min, double max, double[] dis, int[] countDis, int yyy) {
		for (int i = 0; i < numUFO; i++) {
			double val = (dis[i]-min)/(max-min);
			val *= (numDistance-1);
			if (sliderDistance.l/sliderDistance.gap < val && val < sliderDistance.u/sliderDistance.gap){
				//drawSightings(i);
			}
			else{
				isUFOdrawn[i] = false;
			}
		}
		//parent.fill(new Color(0,0,0,220).getRGB());
		//parent.rect(100,yyy-80,1280,100);
		sliderDistance.draw(yyy);
		
		// Draw graph above main slider
		parent.beginShape();
		int xx = sliderDistance.x;
		int yy = sliderDistance.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int i = 0; i < numDistance; i++) {
				xx = sliderDistance.x + i* sliderDistance.gap;
				yy = sliderDistance.y -13- countDis[i]/10;
				parent.curveVertex(xx, yy);
		}
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		Color color = PopupSighting.sightingColor;
		parent.fill(color.getRGB());
		parent.endShape();	
		
		// Draw remove LEFT inactive area above main slider
		parent.beginShape();
		xx = sliderDistance.x;
		yy = sliderDistance.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int i = 0; i < numDistance; i++) {
			if (i < sliderDistance.l/sliderDistance.gap) {
				xx = sliderDistance.x + i* sliderDistance.gap;
				yy = sliderDistance.y -13- countDis[i]/10;
				parent.curveVertex(xx, yy);
			}
		}
		yy = sliderDistance.y -13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove RIGHT inactive area above main slider
		parent.beginShape();
		int begin = sliderDistance.u/sliderDistance.gap;
		xx = sliderDistance.x + sliderDistance.u;
		yy = sliderDistance.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int i = begin; i < numDistance; i++) {
			for (int d = 0; d < 31; d++) {
				xx = sliderDistance.x + i* sliderDistance.gap;
				yy = sliderDistance.y - 13 - countDis[i]/10;
				parent.curveVertex(xx, yy);
			
			}
		}
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();
	}

	
	
	public void plotTimeSlider(SliderTime slider, SliderLeft slider1, SliderRight slider2,
			Integrator xs1,  Integrator xs2, int yyy) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm");
		String d1 = "01/01/2001 00:00";
		String d2 = "01/01/2001 00:00";
		Date lDate = null;
		Date uDate = null;
		try {
			lDate = df.parse(d1);
			uDate = df.parse(d2);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		lDate.setMonth(lDate.getMonth() + slider.lM);
		uDate.setMonth(uDate.getMonth() + slider.uM);
		lDate.setDate(lDate.getDate() + slider1.day - 1);
		uDate.setDate(uDate.getDate() + slider2.day - 1);
		lDate.setHours(lDate.getHours() + slider1.hour);
		uDate.setHours(uDate.getHours() + slider2.hour);
		parent.noStroke();
		day = uDate.getDate();
		yr = uDate.getYear() + 1900;
		month = uDate.getMonth();
		hr = uDate.getHours();
		dayWeek = uDate.getDay();
		for (int i = 0; i < numUFO; i++) {
			if ((timeUFO[i].equals(lDate) || timeUFO[i].after(lDate))
						&& timeUFO[i].before(uDate)) {
			//	drawSightings(i);
			}
			else{
				isUFOdrawn[i] = false;
			}
		}
		if (slider.lM == slider.uM) {
			slider1.update();
			slider2.update();

		} else {
			slider1.update();
			slider2.update();

		}
		slider.draw(yyy);
		slider.update();
		
		// Draw graph above main slider
		parent.beginShape();
		int xx = slider.x;
		int yy = slider.y - 12;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int mon = 0; mon < numMonths; mon++) {
			xx = slider.x + mon * slider.gap;
			yy = slider.y - 12 - sightM[mon].size() / 10;
			parent.curveVertex(xx, yy);
		}
		xx = slider.x + numMonths * slider.gap;
		yy = slider.y - 12;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		Color color = PopupSighting.sightingColor;
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove LEFT inactive area above main slider
		parent.beginShape();
		xx = slider.x;
		yy = slider.y - 12;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int mon = 0; mon <= slider.lM; mon++) {
			xx = slider.x + mon * slider.gap;
			yy = slider.y - 12 - sightM[mon].size() / 10;
			parent.curveVertex(xx, yy);
		}
		xx = slider.x + (slider.lM) * slider.gap;
		yy = slider.y - 12;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove RIGHT inactive area above main slider
		parent.beginShape();
		xx = slider.x + (slider.uM + 1) * slider.gap;
		yy = slider.y - 12;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int mon = slider.uM + 1; mon < numMonths; mon++) {
			xx = slider.x + mon * slider.gap;
			yy = slider.y - 12 - sightM[mon].size() / 10;
			parent.curveVertex(xx, yy);
		}
		xx = slider.x + (numMonths) * slider.gap;
		yy = slider.y - 12;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw month sliders
			if (slider.lM != slider.uM) {
				float vv1 =slider.x +slider.l-slider1.gap*16;
				if (vv1<30)
					vv1 =30;
			
				float vv2 =slider.x +slider.u-slider2.gap*15;
				
				float overlap = vv1+slider1.gap*35 - vv2;
				if (overlap>0){
					vv1 = vv1-overlap/2;
					vv2 = vv2+overlap/2;
					float dis1 = 30 - vv1 ;
					if (dis1>0){
						vv1 +=dis1;
						vv2 +=dis1;
					}
				}
				
				xs1.target(vv1);
				xs1.update();
				slider1.x = xs1.value;
				
				xs2.target(vv2);
				xs2.update();
				slider2.x = xs2.value;
				//System.out.println("VV1:"+slider1.x);
				
			}	
			else if (slider.lM == slider.uM) {
				if (slider2.u<10)
					slider2.u =10;
				if (slider1.l>=slider2.u-5)
					slider1.l = slider2.u-6;
				float vv1 =slider.x +slider.l-slider1.gap*16;
				if (vv1<30)
					vv1 =30;
				xs1.target(vv1);
				xs1.update();
				slider1.x = xs1.value;
				
				xs2.target(vv1);
				xs2.update();
				slider2.x = xs2.value;
			}
				
			
			// Draw lensing LEFT
			float xl = slider.x+slider.l-8;
			float xl1 = slider1.x+slider1.l;
			float xl2 = slider1.x+31*slider1.gap;
			if (slider.lM == slider.uM) 
				xl2 = slider2.x+slider2.u;
			float yl = slider.y-13;
			float yl1 = slider1.y;
			float yl2 = slider1.y-15;
			parent.beginShape();
			parent.curveVertex(xl, yl);
			parent.curveVertex(xl, yl);
			parent.curveVertex(xl1, yl1);
			parent.curveVertex(xl1, yl2);
			parent.curveVertex(xl2, yl2);
			parent.curveVertex(xl2, yl1);
			parent.curveVertex(xl+16, yl);
			parent.curveVertex(xl, yl);
			parent.curveVertex(xl, yl);
			parent.strokeWeight(0.2f);
			float r = SliderTime.c1.getRed();
			float g = SliderTime.c1.getGreen();
			float b = SliderTime.c1.getBlue();
			parent.fill(r,g,b,80);
			parent.endShape();
			
			// Draw lensing RIGHT
			float xu = slider.x+slider.u-8;
			float xu1 = slider2.x;
			if (slider.lM == slider.uM) 
				xu1 = slider1.x+slider1.l;
			
			float xu2 = slider2.x+slider2.u;
			float yu = slider.y-13;
			float yu1 = slider2.y;
			float yu2 = slider2.y-15;
			parent.beginShape();
			parent.curveVertex(xu, yu);
			parent.curveVertex(xu, yu);
			parent.curveVertex(xu1, yu1);
			parent.curveVertex(xu1, yu2);
			parent.curveVertex(xu2, yu2);
			parent.curveVertex(xu2, yu1);
			parent.curveVertex(xu+16, yu);
			parent.curveVertex(xu, yu);
			parent.curveVertex(xu, yu);
			parent.strokeWeight(0.2f);
			r = SliderTime.c2.getRed();
			g = SliderTime.c2.getGreen();
			b = SliderTime.c2.getBlue();
			parent.fill(r,g,b,70);
			parent.endShape();
			
			slider1.draw(yyy);
			slider2.draw(yyy);
			
			// Draw LEFT month
			parent.beginShape();
			float xL = slider1.x;
			float yL = slider1.y - 14;
			parent.curveVertex(xL, yL);
			parent.curveVertex(xL, yL);
			for (int d = 0; d < 31; d++) {
					xL = slider1.x + d * slider1.gap;
					yL = slider1.y - 14 - sight[slider.lM][d].size() * 2;
					parent.curveVertex(xL, yL);
			}
			xL = slider1.x + 31 * slider1.gap;
			yL = slider1.y - 14;
			parent.curveVertex(xL, yL);
			parent.curveVertex(xL, yL);
			if (slider.lM == slider.uM) {
				parent.stroke(new Color(200, 200, 200, 250).getRGB());
			}
			else{
				parent.stroke(new Color(0, 0, 0, 250).getRGB());
			}
			parent.strokeWeight(1);
			parent.fill(PopupSighting.sightingColor.getRGB());
			parent.endShape();

			float xR = 0;
			float yR = 0;
			// Draw Right month
			parent.beginShape();
			xR = slider2.x;
			yR = slider2.y - 14;
			parent.curveVertex(xR, yR);
			parent.curveVertex(xR, yR);
			for (int d = 0; d < 31; d++) {
				xR = slider2.x + d * slider2.gap;
				yR = slider2.y - 14 - sight[slider.uM][d].size() * 2;
				parent.curveVertex(xR, yR);
			}
			xR = slider2.x + 31 * slider2.gap;
			yR = slider2.y - 14;
			parent.curveVertex(xR, yR);
			parent.curveVertex(xR, yR);
			parent.stroke(new Color(0, 0, 0, 250).getRGB());
			parent.strokeWeight(1);
			color = PopupSighting.sightingColor;
			parent.fill(color.getRGB());
			parent.endShape();
			

			// Filter inactive area of LEFT month
				parent.beginShape();
			xL = slider1.x;
			yL = slider1.y - 14;
			parent.curveVertex(xL, yL);
			parent.curveVertex(xL, yL);
			for (int d = 0; d < slider1.day; d++) {
				if (d > 30)
					continue;
					xL = slider1.x + d * slider1.gap;
					yL = slider1.y - 14 - sight[slider.lM][d].size() * 2;
					parent.curveVertex(xL, yL);
				
			}
			xL = slider1.x + (slider1.day - 1) * slider1.gap;
			yL = slider1.y - 14;
			parent.curveVertex(xL, yL);
			parent.curveVertex(xL, yL);
			parent.stroke(new Color(0, 0, 0, 170).getRGB());
			parent.strokeWeight(2);
			color = new Color(0, 0, 0, 200);
			parent.fill(color.getRGB());
			parent.endShape();

			// Filter inactive area of Right month
			parent.beginShape();
			if (slider.lM != slider.uM) {
				xR = slider2.x + (slider2.day - 1) * slider2.gap;
			} else {
				xR = slider2.x + (slider2.day - 1) * slider2.gap ;
			}
			yR = slider2.y - 14;
			parent.curveVertex(xR, yR);
			parent.curveVertex(xR, yR);
			for (int d = slider2.day - 1; d < 31; d++) {
					xR = slider2.x + d * slider2.gap;
					yR = slider2.y - 14 - sight[slider.uM][d].size() * 2;
					parent.curveVertex(xR, yR);
				
			}
			xR = slider2.x + 31 * slider2.gap;
			yR = slider2.y - 14;
			parent.curveVertex(xR, yR);
			parent.curveVertex(xR, yR);
			parent.stroke(new Color(0, 0, 0, 170).getRGB());
			parent.strokeWeight(2);
			color = new Color(0, 0, 0, 200);
			parent.fill(color.getRGB());
			parent.endShape();
		
	}

	public void plotYearlySlider(SliderYear sliderY, int yyy) {
		parent.noStroke();
		for (int i = 0; i < numUFO; i++) {
			int m = timeUFO[i].getMonth();
			int d = timeUFO[i].getDate() - 1;
			if ((sliderY.lM < m && m < sliderY.uM)
					|| (sliderY.lM == m && m < sliderY.uM && sliderY.lD <= d)
					|| (sliderY.lM < m && m == sliderY.uM && d < sliderY.uD)
					|| (sliderY.lM == m && m == sliderY.uM
							&& sliderY.lD <= d && d < sliderY.uD)) {
			//	drawSightings(i);
			}
			else{
				isUFOdrawn[i] = false;
			}	
		}
		//parent.fill(new Color(0,0,0,220).getRGB());
		//parent.rect(100,yyy-80,1280,100);
		sliderY.draw(yyy);
		sliderY.update();

		// Draw graph above main slider
		parent.beginShape();
		int xx = sliderY.x;
		int yy = sliderY.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int mon = 0; mon < 12; mon++) {
			for (int d = 0; d < 31; d++) {
				xx = sliderY.x + (mon * 31 + d) * sliderY.gap;
				yy = sliderY.y - 13 - sightY[mon][d].size() / 2;
				if (sightY[mon][d].size() > 0) // Avoid invalid days
					parent.curveVertex(xx, yy);
			}
		}
		xx = sliderY.x + (12 * 31) * sliderY.gap;
		yy = sliderY.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		Color color = PopupSighting.sightingColor;
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove LEFT inactive area above main slider
		parent.beginShape();
		xx = sliderY.x;
		yy = sliderY.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int mon = 0; mon < 12; mon++) {
			for (int d = 0; d < 31; d++) {
				if (mon < sliderY.lM || (mon == sliderY.lM && d <= sliderY.lD)) {
					xx = sliderY.x + (mon * 31 + d) * sliderY.gap;
					yy = sliderY.y - 13 - sightY[mon][d].size() / 2;
					if (sightY[mon][d].size() > 0) // Avoid invalid days
						parent.curveVertex(xx, yy);
				}
			}
		}

		xx = sliderY.x + (sliderY.lM * 31 + sliderY.lD) * sliderY.gap;
		yy = sliderY.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove RIGHT inactive area above main slider
		parent.beginShape();
		xx = sliderY.x + (sliderY.uM * 31 + sliderY.uD + 1) * sliderY.gap;
		yy = sliderY.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int mon = sliderY.uM; mon < 12; mon++) {
			for (int d = 0; d < 31; d++) {
				if (mon > sliderY.uM || (mon == sliderY.uM && d > sliderY.uD)) {
					xx = sliderY.x + (mon * 31 + d) * sliderY.gap;
					yy = sliderY.y - 13 - sightY[mon][d].size() / 2;
					if (sightY[mon][d].size() > 0) // Avoid invalid days
						parent.curveVertex(xx, yy);
				}
			}
		}
		xx = sliderY.x + (12 * 31) * sliderY.gap;
		yy = sliderY.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(100, 100, 100, 200).getRGB());
		parent.strokeWeight(1);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();
	}

	public void plotDailySlider(SliderDay sliderD, int yyy) {
		parent.noStroke();
		for (int i = 0; i < numUFO; i++) {
			int h = timeUFO[i].getHours();
			int min = h * 60 + timeUFO[i].getMinutes();
			int sliderMin1 = sliderD.lH * 60 + sliderD.lM;
			int sliderMin2 = sliderD.uH * 60 + sliderD.uM;

			if ((sliderMin1 <= min && min < sliderMin2)) {
			//	drawSightings(i);
			}
			else{
				isUFOdrawn[i] = false;
			}
		}
		// parent.fill(new Color(0,0,0,220).getRGB());
		// parent.rect(100,yyy-80,1280,100);
		sliderD.draw(yyy);
		sliderD.update();

		// Draw graph above main slider
		parent.beginShape();
		int xx = sliderD.x;
		int yy = sliderD.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int h = 0; h < 24; h++) {
			for (int m = 0; m < numMins; m++) {
				xx = sliderD.x + (h * numMins + m) * sliderD.gap;
				yy = sliderD.y - 13 - sightD[h][m].size() / 12;
				parent.curveVertex(xx, yy);
			}
		}
		xx = sliderD.x + (24 * numMins) * sliderD.gap;
		yy = sliderD.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(200, 200, 200, 200).getRGB());
		parent.strokeWeight(2);
		Color color = PopupSighting.sightingColor;
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove LEFT inactive area above main slider
		parent.beginShape();
		xx = sliderD.x;
		yy = sliderD.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);

		for (int h = 0; h <= sliderD.lH; h++) {
			for (int m = 0; m < numMins; m++) {
				xx = sliderD.x + (h * numMins + m) * sliderD.gap;
				yy = sliderD.y - 13 - sightD[h][m].size() / 12;
				parent.curveVertex(xx, yy);

				if (h == sliderD.lH && m * 2 == sliderD.lM) {

					break;
				}
			}
		}

		// xx = sliderD.x+(sliderD.lH*numMins+sliderD.lM)*sliderD.gap;
		yy = sliderD.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(0, 0, 0, 180).getRGB());
		parent.strokeWeight(2);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();

		// Draw remove RIGHT inactive area above main slider
		parent.beginShape();
		xx = sliderD.x + sliderD.uH * numMins + sliderD.uM;
		yy = sliderD.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		for (int h = sliderD.uH; h < 24; h++) {
			for (int m = 0; m < numMins; m++) {
				if (h == sliderD.uH && m * 2 < sliderD.uM)
					continue;
				xx = sliderD.x + (h * numMins + m) * sliderD.gap;
				yy = sliderD.y - 13 - sightD[h][m].size() / 12;
				parent.curveVertex(xx, yy);

			}
		}
		yy = sliderD.y - 13;
		parent.curveVertex(xx, yy);
		parent.curveVertex(xx, yy);
		parent.stroke(new Color(0, 0, 0, 180).getRGB());
		parent.strokeWeight(2);
		color = new Color(0, 0, 0, 215);
		parent.fill(color.getRGB());
		parent.endShape();
	}
}