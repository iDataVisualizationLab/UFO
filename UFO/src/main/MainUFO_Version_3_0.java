package main;
/*
 * UFO
 *
 * Copyright 2014 by Tuan Dang.
 *
 * The contents of this file are subject to the Mozilla Public License Version 2.0 (the "License")
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 */
import java.util.Date;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import processing.core.*;

import com.modestmaps.*;
import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.Google;
import com.modestmaps.providers.Microsoft;
import com.modestmaps.providers.Yahoo;


public class MainUFO_Version_3_0 extends PApplet {
	private static final long serialVersionUID = 1L;
	public int count = 0;
	
	public double scaleRate = 1.01;
	// Code used to highlight sighting
	public static WordCloud wordCloud;
	
	// Main buttons
	public static int x1 = 1;
	public static int y1 = 40;		
	public static int w1 = 90;		
	public static int h1 = 27;			
	// Version 2
	PopupMap popupMap = new PopupMap(this);
	PopupSighting popupSighting = new PopupSighting(this);
	
	ClickableButton airportButton = new ClickableButton(x1,y1+60, w1, h1,
			"Airports", new Color(255,150,0));
	ClickableButton militaryButton = new ClickableButton(x1, y1+90, w1, h1,
			"Military", new Color(255,50,50));
	public static ClickableButton kMeansButton = new ClickableButton(x1, y1+130, w1, h1,
			"K-means", new Color(255,255,255));
	public static ClickableButton densityButton = new ClickableButton(x1, y1+160, w1, h1, "Density", new Color(255,255,255));
	public static ClickableButton sameUFOButton = new ClickableButton(x1, y1+190, w1, h1,
			"Same UFO?", new Color(255,255,255));
	ClickableButton wordCloudButton = new ClickableButton(x1, y1+220, w1, h1,
			"Word Cloud", new Color(0,255,0));
	public static ClickableButton graphButton = new ClickableButton(x1, y1+ 250, w1, h1, "Graphs", new Color(255,255,255));
	
	public static float wX1 =0, wX2 =0, wY1 = 0, wY2 =0;
	public PImage airportImage =  this.loadImage("pics/flight.png");
	public PImage baseImage =  this.loadImage("pics/base.png");
	public PImage logoImage =  this.loadImage("pics/sm-skytree-logo.png");
	
	// all the buttons in one place, for looping:
	Button[] buttons = {densityButton, kMeansButton, airportButton, militaryButton,  wordCloudButton, sameUFOButton,graphButton };

	public String[] fields = { "Zipcode", "latitude", "longtitude", "city",
			"state", "county", "type", "population", "land area",
			"population density" };
	
	public PFont font2 = loadFont("Arial-BoldMT-18.vlw");

	// Code used for Plotting dots radius
	public static float r = 7f;

	// Code used for viewing Airport and Military
	public float[] latAir;
	public float[] lonAir;
	public float[] latMil;
	public float[] lonMil;
	
	public static int numMonths = 121;
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[] sightM = new ArrayList[numMonths];
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[][] sight = new ArrayList[numMonths][31];
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[][][] sightH = new ArrayList[numMonths][31][24];
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[][] sightY = new ArrayList[numMonths][31];
	public static int numMins = 30;
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[][] sightD = new ArrayList[24][numMins];
	
	public DrawGraph graph = new DrawGraph(this);
	public DrawSliders drawSliders = new DrawSliders(this);
	
	// Code used for Sliders
	public SliderTime slider = new SliderTime(this);
	public SliderLeft slider1 = new SliderLeft(this);
	public Integrator xs1 = new Integrator(slider1.x);
	public SliderRight slider2 = new SliderRight(this);
	public Integrator xs2 = new Integrator(slider2.x);
	public SliderDistance sliderDistance = new SliderDistance(this);
	public SliderYear sliderY = new SliderYear(this);
	public SliderDay sliderD = new SliderDay(this);
	public ButtonPlay bPlay = new ButtonPlay(50, slider.y - 22, 30, 30, this);
		
	public static int ySliders =718;
	public static InteractiveMap imap;
	int x2 = 0;
	int y2 = 550;
	public CheckBox check1 = new CheckBox(this, x2, y2+0, "Time", false);
	public CheckBox check2 = new CheckBox(this, x2, y2+20, "Yearly", false);
	public CheckBox check3 = new CheckBox(this, x2, y2+40, "Daily", false);
	public CheckBox checkHeat = new CheckBox(this, x2, y2-160, "Heat map", false);
	public CheckBox check0 = new CheckBox(this, x2, y2+60, "Distance", true);
	public CheckBox[] checkboxes = {check0, check1, check2, check3, checkHeat};
	
	// Code used for reading sightings file
	public static int numUFO;
	public static boolean[] isUFOselected; // selected by dragging
	public static boolean[] isUFOdrawn;    // satisfy filtering conditions 
	public static String[] time ;
	public static Date[] timeUFO ;
	public static String[] cityUFO;
	public static String[] stateUFO;
	public static String[] shapeUFO;
	public static String[] durUFO;
	public static String[] desUFO;
	public static String[] phrases;
	public static double[] dis6;
	
	public static float[] lat;
	public static float[] lon;
	public static int[] clusterID;
	public static int numCluster = 0;

	public static int numDistance = 200;
	public int[] sightAirDistance = new int[numDistance];
	public  double[] nearestAirBase;
	public  double minAirBase = Double.POSITIVE_INFINITY;
	public  double maxAirBase = Double.NEGATIVE_INFINITY;
	
	// k-Means
	public double[] centricDistant;
	public static int[] clusterCenter;
	public int[] sightClusterDistance = new int[numDistance];
	public double minCentricDistant = Double.POSITIVE_INFINITY;
	public double maxCentricDistant = Double.NEGATIVE_INFINITY;
	
	// Density
	public static double[] density;
	public int[] densityHistogram = new int[numDistance];
	public  double minDensity = Double.POSITIVE_INFINITY;
	public  double maxDensity = Double.NEGATIVE_INFINITY;
	
	// Same UFO?
	public static int[] dis6Histogram = new int[numDistance];
	public static double minDis6 = Double.POSITIVE_INFINITY;
	public static double maxDis6 = Double.NEGATIVE_INFINITY;
	public static int[] nearest6;
	
		
	public static float sY = 100;
	public static float sH = 400;
	public static ParalellCoordinate sl1 =  new ParalellCoordinate(0, 1280, sY, sH, "Year");
	public static ParalellCoordinate sl2 =  new ParalellCoordinate(1, 1280, sY, sH, "Month");
	public static ParalellCoordinate sl3 =  new ParalellCoordinate(2, 1280, sY, sH, "Day");
	public static ParalellCoordinate sl4 =  new ParalellCoordinate(3, 1280, sY, sH, "Longitude");
	public static ParalellCoordinate sl5 =  new ParalellCoordinate(4, 1280, sY, sH, "Latitude");
	public static ParalellCoordinate sl6 =  new ParalellCoordinate(5, 1280, sY, sH, "Nearest UFO");
	public static ParalellCoordinate[] paralellCoordinate =  {sl1, sl2, sl3, sl4, sl5, sl6};
	public static ParalellCoordinateWord pcShape =  new ParalellCoordinateWord(5, 1280, sY, sH, "Shapes");
	public static ParalellCoordinateWord pcWord =  new ParalellCoordinateWord(6, 1280, sY, sH, "Phrases");
	
	private float min0, max0, min1, max1, min2, max2, min3, max3, min4, max4, min5, max5;
	public static boolean isDragging = false;
	public static ClickableButton detailsButton = new ClickableButton(1180, 0, 100, 30, "Details", new Color(255,255,255));
	public static ClickableButton relButton = new ClickableButton(1080, 0, 100, 30, "Relationship", new Color(255,255,255));
	public static TextBox textbox;
	public static String[] commonPhases;
	public static int[] commonPhasesCount;
	
	public static void main(String args[]){
	  PApplet.main(new String[] { MainUFO_Version_3_0.class.getName() });
    }

	public void setup() {
		size(1440, 900);
		background(0);
		frameRate(12);
		if (frame != null) {
		    frame.setResizable(true);
		}
		
		curveTightness(0.7f); 
		smooth();
		drawSliders = new DrawSliders(this);
		
		String[] lines = loadStrings("./data/AirportData_lat_long.txt");
		latAir = new float[lines.length];
		lonAir = new float[lines.length];
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split(",");
			latAir[i] = Float.parseFloat(p[0]);
			lonAir[i] = Float.parseFloat(p[1]);
		}

		lines = loadStrings("./data/MilitaryBases_lat_long.txt");
		latMil = new float[lines.length];
		lonMil = new float[lines.length];
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split(",");
			latMil[i] = Float.parseFloat(p[0]);
			lonMil[i] = Float.parseFloat(p[1]);
		}

		lines = loadStrings("./data/UFO_cluster_kmeans.results");
		numUFO = lines.length;
		lon = new float[numUFO];
		lat = new float[numUFO];
		centricDistant = new double[numUFO];
		clusterID =  new int[numUFO];
		for (int i = 0; i < numUFO; i++) {
			String[] p = lines[i].split(",");
			lat[i] =  Float.parseFloat(p[0]);
			lon[i] =  Float.parseFloat(p[1]);
			
			centricDistant[i] =  Float.parseFloat(p[2]);
			if (centricDistant[i]>maxCentricDistant)
				maxCentricDistant = centricDistant[i];
			if (centricDistant[i]<minCentricDistant)
				minCentricDistant = centricDistant[i];
			
			clusterID[i] = Integer.parseInt(p[3]);
			if (clusterID[i]>numCluster)
				numCluster = clusterID[i];
		}
		numCluster = numCluster+1; // The first cluster id = 0
		clusterCenter =  new int[numCluster];
		double[] minDis = new double[numCluster];
		for (int i = 0; i < numCluster; i++) {
			minDis[i] = Double.POSITIVE_INFINITY;
		}
			
		for (int i = 0; i < numUFO; i++) {
			// compute histogram
			double val = (centricDistant[i]-minCentricDistant)/(maxCentricDistant-minCentricDistant);
			int index = (int) (val*(numDistance-1));
			sightClusterDistance[index]++;
			
			// find the centers
			int cluster = clusterID[i];
			if (centricDistant[i]<minDis[cluster]){
				minDis[cluster] = centricDistant[i];
				clusterCenter[cluster] = i; 
			}	
		}
		
		nearestAirBase = new double[numUFO];
		lines = loadStrings("./data/UFO_nn_airports.results");
		for (int i = 0; i < numUFO; i++) {
			String[] p = lines[i].split(",");
			nearestAirBase[i] = Double.parseDouble(p[2]);
			if (nearestAirBase[i]>maxAirBase)
				maxAirBase = nearestAirBase[i];
			if (nearestAirBase[i]<minAirBase)
				minAirBase = nearestAirBase[i];
		}
		
		for (int i = 0; i < numUFO; i++) {
			double val = (nearestAirBase[i]-minAirBase)/(maxAirBase-minAirBase);
			int index = (int) (val*(numDistance-1));
			sightAirDistance[index]++;
		}
				
		// Read density 
		density = new double[numUFO];
		lines = loadStrings("./data/UFO_density_kde.results");
		for (int i = 0; i < numUFO; i++) {
			String[] p = lines[i].split(",");
			density[i] = Double.parseDouble(p[2]);
			if (density[i]>maxDensity)
				maxDensity = density[i];
			if (density[i]<minDensity)
				minDensity = density[i];
		}
		for (int i = 0; i < numUFO; i++) {
			double val = (density[i]-minDensity)/(maxDensity-minDensity);
			int index = (int) (val*(numDistance-1));
			densityHistogram[index]++;
		}
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setParent(this);
		}
		imap = new InteractiveMap(this, new Microsoft.AerialProvider());
		imap.setCenter(new Location(36, -93));
		imap.sc = 24.;
		
		
		// enable the mouse wheel, for zooming
		addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				mouseWheel(evt.getWheelRotation());
			}
		});

		lines = loadStrings("./data/UFO_mainland_clustered_and_chunked.txt");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm");
		time = new String[numUFO];
		timeUFO = new Date[numUFO];
		cityUFO = new String[numUFO];
		stateUFO = new String[numUFO];
		shapeUFO = new String[numUFO];
		durUFO = new String[numUFO];
		desUFO = new String[numUFO];
		phrases = new String[numUFO];
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split("\t");
			// Obtain new date + time
			time[i] = p[1];
			try {
				timeUFO[i] = df.parse(time[i]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cityUFO[i] = p[2];
			stateUFO[i] = p[3];
			shapeUFO[i] = p[4];
			durUFO[i] = p[5];
			desUFO[i] = p[6];
			if (p.length==10)
				phrases[i] = p[9];
		}
		
		lines = loadStrings("./data/UFO_time_and_clustering_nn_tsv.results");
		dis6 = new double[numUFO];
		nearest6 = new int[numUFO];
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split("\t");
			dis6[i] = Double.parseDouble(p[8]);
			nearest6[i] = Integer.parseInt(p[9]);
			if (dis6[i]>maxDis6)
				maxDis6 = dis6[i];
			if (dis6[i]<minDis6)
				minDis6 = dis6[i];
		}	
		for (int i = 0; i < numUFO; i++) {
			double val = (dis6[i]-minDis6)/(maxDis6-minDis6);
			int index = (int) (val*(numDistance-1));
			dis6Histogram[index]++;
		}
		isUFOselected = new boolean[numUFO];
		isUFOdrawn = new boolean[numUFO];
		
		// Compute Monthly Data
		for (int i = 0; i < numMonths; i++) {
			for (int j = 0; j < 31; j++) {
				sight[i][j] = new ArrayList<Integer>();
				sightY[i][j] = new ArrayList<Integer>();
				for (int h = 0; h < 24; h++) {
					sightH[i][j][h] = new ArrayList<Integer>();
				}
			}
		}
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < numMins; j++) {
				sightD[i][j] = new ArrayList<Integer>();
			}
		}
		for (int i = 0; i < lines.length; i++) {
			int mon = timeUFO[i].getMonth();
			int mon2 = (int) (timeUFO[i].getMonth() + 12 * (timeUFO[i]
					.getYear() - 101));
			int day = timeUFO[i].getDate() - 1;
			int hour = timeUFO[i].getHours();
			int min = timeUFO[i].getMinutes();
			sight[mon2][day].add(i);
			sightH[mon2][day][hour].add(i);
			sightY[mon][day].add(i);
			sightD[hour][min / 2].add(i);
		}
		for (int i = 0; i < numMonths; i++) {
			sightM[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < lines.length; i++) {
			int mon = (int) (timeUFO[i].getMonth() + 12 * (timeUFO[i].getYear() - 101));
			sightM[mon].add(i);
		}
		
		
		
		wordCloudButton.s=true;
		for (int i = 0; i < numUFO; i=i+100) {
			ParalellCoordinate.a.add(i);
		}
		
		for (int i = 0; i < paralellCoordinate.length; i++) {
			paralellCoordinate[i].computeminMax();
		}	
		ParalellCoordinate.checkall();
		pcWord.checkall();
		pcShape.checkall();
		
		detailsButton.setParent(this);
		relButton.setParent(this);
	
		lines = loadStrings("./data/UFO_mainland_clustered_and_chunked_phrases_only_counts.txt");
		commonPhases = new String[lines.length];
		commonPhasesCount = new int[lines.length];
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split(",");
			commonPhases[i] = p[0];
			commonPhasesCount[i] = Integer.parseInt(p[1]);
		}	
		
		textbox = new TextBox(this, 600,3,"Search common phrases:",commonPhases, commonPhasesCount);
		
	}

	
	public void draw() {
		textFont(font2, 12);
		background(0);
		// draw the map
		if (WordCloud.xLeft.value>120){
			imap.draw();
			this.fill(0,100);
			this.rect(0, 0, this.width, this.height);
		}	
		if (keyPressed && !kMeansButton.s) {
			if (key == CODED) {
				if (keyCode == LEFT) {
					imap.tx += 4.0 / imap.sc;
				} else if (keyCode == RIGHT) {
					imap.tx -= 4.0 / imap.sc;
				} else if (keyCode == UP) {
					imap.ty += 4.0 / imap.sc;
				} else if (keyCode == DOWN) {
					imap.ty -= 4.0 / imap.sc;
				}
			} else if ((key == '+' || key == '=')) {
				imap.sc *= scaleRate;
			} else if (key == '_' || key == '-' && imap.sc > 2) {
				imap.sc *= 1.0 / scaleRate;
			}
		}
		
		
		
		// No heat map if no density selected
		if (!densityButton.s)
			checkHeat.s = false;
		
		if (!relButton.s && !detailsButton.s)
			drawsSliders(); // Including draw sighting here
		
		if (kMeansButton.s){
			drawSliders.drawClusterCenters(clusterCenter);
		}	
		
		
		drawControllers();
		// Draw graph
		if (graphButton.s) {
			graph.draw();
		}
				
		// Draw tag cloud	
		if (wordCloudButton.s ){
			drawsTextAnalysis();
			if (!relButton.s && !detailsButton.s)
				textbox.draw();	
		}
		this.image(logoImage, 110, 8, logoImage.width/2, logoImage.height/2);
		checkMutualExlusiveCondition();
	}
	
	public void drawsTextAnalysis() {
		int count=0;
		for (int i = 0; i < numUFO; i++) {
			if (isUFOselected[i])
				count++;
		}
		WordCloud.b =-1;
		if (!sameUFOButton.s){
			if (count==0 && !isDragging )	{
				this.textAlign(PApplet.LEFT);
				this.textSize(16);
				this.fill(0,0,0);
				this.text("OR  Drag on the map to select sightings", 763, 23);
				this.fill(0,200,0);
				this.text("OR  Drag on the map to select sightings", 762, 22);
			}
			else if (wordCloud!=null){
				noStroke();
				fill(0,0,0, 230); //color of background and transparency
				
				if (detailsButton.s){
		    		WordCloud.xLeft.target(100);
		    		rect(WordCloud.xLeft.value-10, 0, this.width-(WordCloud.xLeft.value-10), this.height);
		    		
		    		String[] listOfHighlightedWords = new String[wordCloud.numWords];
		    		for (int i=0; i<wordCloud.numWords;i++){
		    			listOfHighlightedWords[i] = WordCloud.cloud[i].word;
		    		}
		    		
		    		ParalellCoordinate.drawSightingDetails(this, ParalellCoordinate.a, WordCloud.xLeft.value, 50,listOfHighlightedWords);
		    	}	
		    	else if (relButton.s){
		    		rect(WordCloud.xLeft.value-10, 0, this.width-(WordCloud.xLeft.value-10), this.height);
		    		WordCloud.xLeft.target(100);
		    	}
		    	else{
		    		WordCloud.xLeft.target(wordCloud.x1);
		    		if (!isDragging){
		    			rect(WordCloud.xLeft.value-10, 0, this.width-(WordCloud.xLeft.value-10), this.height);
		    			wordCloud.draw(this);
		    		}	
		    	}
	    		WordCloud.xLeft.update();
	    		detailsButton.x = this.width-100;
	    		relButton.x = this.width-200;
	    		detailsButton.draw();
	    		detailsButton.mouseOver();
	    		relButton.draw();
		    	relButton.mouseOver();
		    
			}
		}
		this.stroke(0,255,0);
		this.noFill();
		if (wX1<=wX2 && wY1<=wY2 && this.mousePressed && WordCloud.b<0 
				&& !detailsButton.b 
				&& !detailsButton.s 
				&& !relButton.b
				&& !relButton.s)
			this.rect(wX1,wY1,wX2-wX1,wY2-wY1);
		
		if (wordCloudButton.s && relButton.s){
			drawPC();
		}		
	}
		
	
	public void drawsSliders() {
		// Draw sightings
		for (int i = 0; i < numUFO; i++) {
			isUFOdrawn[i] = true; 
		}
		
		drawSliders.selectedUFO = -1;
		fill(255, 255, 255);
		textSize(15);
		textAlign(LEFT);
		text("Filtering", 2, y2-10);
		
		if (bPlay.isPlayed) {
			if (check1.s) {
				if (slider.uM < numMonths - 1) {
					slider2.u = slider2.u + slider2.gap;
					if (slider2.day > 31) {
						slider2.u = 0;
						slider.u = slider.u + slider.gap;
						slider.l = slider.u;
					}
				}
				
			}
			// Select 1 year
			else if (check2.s) {
				if (sliderY.u < (sliderY.w - sliderY.gap)) {
					sliderY.u = sliderY.u + sliderY.gap;
					sliderY.l = sliderY.u - sliderY.gap;
				}
				
			}
			// Select 1 Day
			else if (check3.s) {
				if (sliderD.u < (sliderD.w - sliderD.gap)) {
					sliderD.u = sliderD.u + sliderD.gap;
					sliderD.l = sliderD.u - sliderD.gap;
				}
			}
		}
		// Draw Heat map
		if (densityButton.s && checkHeat.s){ 
			drawHeatMap();
		} 

		int yyy = this.height-20;
		if (check0.s){
			if (kMeansButton.s){
				drawSliders.plotSliderDistance(sliderDistance, minCentricDistant, maxCentricDistant,centricDistant, sightClusterDistance, yyy);
				drawSliders.drawClusterCenters(clusterCenter);
			} 
			else if (densityButton.s){
				drawSliders.plotSliderDistance(sliderDistance, minDensity, maxDensity,density, densityHistogram,yyy);
				this.noStroke();
				this.fill(255,0,0);
			}
			else if (sameUFOButton.s){
				drawSliders.plotSliderDistance(sliderDistance, minDis6, maxDis6, dis6, dis6Histogram,yyy);
			}
			else	
				drawSliders.plotSliderDistance(sliderDistance, minAirBase, maxAirBase,nearestAirBase, sightAirDistance,yyy);
			yyy-=140;
		}	
		if (check3.s){
			drawSliders.plotDailySlider(sliderD,yyy);
			yyy-=140;
		}	
		if (check2.s){
			drawSliders.plotYearlySlider(sliderY,yyy);
			yyy-=140;
		}	
		if (check1.s){
			drawSliders.plotTimeSlider(slider, slider1, slider2, xs1, xs2,yyy);
			yyy-=140;
		}	
		
		
		
		// Draw sightings
		if (WordCloud.xLeft.value>120){
			for (int i = 0; i < numUFO; i++) {
				if (isUFOdrawn[i] && !checkHeat.s)
					drawSliders.drawSightings(i);
			}
		}
		
		// Draw sighting text search
		if (wordCloudButton.s && !relButton.s && !detailsButton.s){
			for (int i = 0; i < numUFO; i++) {
				if (isUFOselected[i]){
					drawSliders.drawSightingsHavingCommonWords(i, Color.GREEN);
				}
			}
		}
		// Draw sightings having common words
		if (WordCloud.b>=0){
			String text = WordCloud.cloud[WordCloud.b].word;
			for (int i = 0; i < numUFO; i++) {
				if (phrases[i]!=null && phrases[i].contains(text)){
					drawSliders.drawSightingsHavingCommonWords(i, Color.ORANGE);
				}
			}
		}
		if (WordCloud.s>=0){
			String text = WordCloud.cloud[WordCloud.s].word;
			ArrayList<Integer> a =  new ArrayList<Integer>();
			for (int i = 0; i < numUFO; i++) {
				if (phrases[i]!=null && phrases[i].contains(text)){
					drawSliders.drawSightingsHavingCommonWords(i, Color.RED);
					a.add(i);
				}
			}
		}

		// Airport and Military
	   if (airportButton.s) {
        	for (int i = 0; i < lonAir.length; i++) {
        		Location location = new Location(latAir[i], lonAir[i]);
				Point2f p = imap.locationPoint(location);
				if (p.y>640) continue; 
				this.image(airportImage,p.x-r,p.y-r,r*1.5f,r*1.5f);
			}
		}
		if (militaryButton.s) {
			for (int i = 0; i < latMil.length; i++) {
				Location location = new Location(latMil[i],lonMil[i]);
				Point2f p = imap.locationPoint(location);
				if (p.y>640) continue; 
				this.image(baseImage,p.x-r,p.y-r,r*2,r*2);
			}
		}
		
		// Draw selected sightings
		if (drawSliders.selectedUFO >=0){
			if (sameUFOButton.s)
				drawSliders.drawNearestUFO6(sliderDistance);
			else if (!kMeansButton.s && !wordCloudButton.s)
				drawSliders.drawSelectedUFO();
		}
		if (drawSliders.selectedUFO == -1)
			drawSliders.selectedCluster =-1;
				
		if (kMeansButton.s)
			check0.text = "Cluster-centric distance";
		else if (densityButton.s)
			check0.text = "Density";
		else if (sameUFOButton.s)
			check0.text = "Distance to close neighbors";
		else
			check0.text = "Distance to airports/bases";
	}
	
	public void drawHeatMap(){
		float[][] pix = new float[width][height];
		for (int i = 0; i < numUFO; i++) {
			// Check if the sighting is drawn or NOT
			double val = (density[i]-minDensity)/(maxDensity-minDensity);
			val *= (numDistance-1);
			if (sliderDistance.l/sliderDistance.gap < val && val < sliderDistance.u/sliderDistance.gap){
			}
			else{
				continue;
			}
			
			Location location = new Location(lat[i], lon[i]);
			Point2f p = imap.locationPoint(location);
			int xx = (int) p.x;
			int yy = (int) p.y;
			
			float r=15;
			if (p.x<=100 || p.x>=width-r || p.y<=r || p.y>=height-r) continue;
			pix[xx][yy] +=1;
			for (int x=0;x<r;x++){
				for (int y=0;y<r;y++){
					float dis =  (r-PApplet.sqrt(x*x+y*y)); 
					if (dis<0) continue;
					if (x>0 && y>0){
						pix[xx+x][yy+y] +=  dis/r;
						pix[xx-x][yy+y] +=  dis/r; 
						pix[xx+x][yy-y] +=  dis/r; 
						pix[xx-x][yy-y] +=  dis/r; 
					}	
					else if (x==0 && y>0){
						pix[xx][yy+y] +=  dis/r;
						pix[xx][yy-y] +=  dis/r; 
					}
					else if (x>0 && y==0){
						pix[xx+x][yy] +=  dis/r;
						pix[xx-x][yy] +=  dis/r; 
					}
				}
			}	
			
		}
	
		float max = 0;
		for (int x=0;x<width;x++){
			for (int y=0;y<height;y++){
				if (pix[x][y]>max)
					max = pix[x][y];
			}
		}	
		max = 150;
		
		this.noStroke();
		this.fill(0,0,0,100);
		this.rect(100,0,this.width-100,height);
		for (int x=0;x<width;x++){
			for (int y=0;y<height;y++){
				double v = (double) pix[x][y]/max ;
				if (v>1)
					v=1;
				if (v>0){
					Color c = ColorScales.getColor(v, "fire", 0.5f);
					this.fill(c.getRGB());
					this.rect(x, y, 1,1);
				}
			}
		}		
	}
		
	
	
	public void drawPC() {
		// Draw polylines
		min0 = paralellCoordinate[0].min;
		max0 = paralellCoordinate[0].max;
		min1 = paralellCoordinate[1].min;
		max1 = paralellCoordinate[1].max;
		min2 = paralellCoordinate[2].min;
		max2 = paralellCoordinate[2].max;
		min3 = paralellCoordinate[3].min;
		max3 = paralellCoordinate[3].max;
		min4 = paralellCoordinate[4].min;
		max4 = paralellCoordinate[4].max;
		min5 = paralellCoordinate[5].min;
		max5 = paralellCoordinate[5].max;
		this.strokeWeight(1);
		for (int i=0;i<paralellCoordinate.length;i++){
			paralellCoordinate[i].x = WordCloud.xLeft.value+i*130+100; 
		}
		pcShape.x = WordCloud.xLeft.value+(pcShape.id)*140+190; 
		pcWord.x = WordCloud.xLeft.value+(pcWord.id)*140+190; 
		
		
		// Draw Polylines
		for (int i=0;i<ParalellCoordinate.b.size();i++){
			int index = ParalellCoordinate.b.get(i);
			drawPolyline(index, new Color(255,255,255,60),1);
			if (i>=500) {
				this.textSize(14);
				this.textAlign(PApplet.LEFT);
				this.fill(150);
				this.text("Drawing 500/"+ParalellCoordinate.b.size()+" sightings", 400, 640);
				break;
			}
		}	
		
		
		// Draw paralell coordinate
		ParalellCoordinate.bPoint =-1;
		for (int i=0;i<paralellCoordinate.length;i++){
			paralellCoordinate[i].draw(this);
		}
		pcShape.bWord = null;
		pcShape.draw(this);
		pcWord.bWord = null;
		pcWord.draw(this);
		
		if (ParalellCoordinate.bPoint>=0){
			drawPolyline(ParalellCoordinate.bPoint, Color.RED,2);
			ArrayList<Integer> aa = new ArrayList<Integer>();
			aa.add(ParalellCoordinate.bPoint);
			
			String[] listOfHighlightedWords = new String[ParalellCoordinateWord.numWords];
			for (int i=0; i<ParalellCoordinateWord.numWords;i++){
    			listOfHighlightedWords[i] = WordCloud.cloud[i].word;
    		}
			ParalellCoordinate.drawSightingDetails(this,aa,100, 510, listOfHighlightedWords);
			
		}	
		else if(pcShape.bWord!=null){
			ArrayList<Integer> aa = new ArrayList<Integer>();
			for (int i=0;i<ParalellCoordinate.b.size(); i++){
				int index = ParalellCoordinate.b.get(i);
				if (shapeUFO[index].toLowerCase().equals(pcShape.bWord)){
					aa.add(index);
					drawPolyline(index, Color.RED,2);
				} 
			}
			String[] listOfHighlightedWords = new String[ParalellCoordinateWord.numWords];
			for (int i=0; i<ParalellCoordinateWord.numWords;i++){
    			listOfHighlightedWords[i] = WordCloud.cloud[i].word;
    		}
			ParalellCoordinate.drawSightingDetails(this,aa,100, 510, listOfHighlightedWords);
		}
		else if(pcWord.bWord!=null){
			ArrayList<Integer> aa = new ArrayList<Integer>();
			for (int i=0;i<ParalellCoordinate.b.size(); i++){
				int index = ParalellCoordinate.b.get(i);
				if (phrases[index]==null) continue;
				if (phrases[index].toLowerCase().contains(pcWord.bWord)){
					aa.add(index);
					drawPolyline(index, Color.RED,2);
				}
			}
			
			String[] listOfHighlightedWords = new String[ParalellCoordinateWord.numWords];
			for (int i=0; i<ParalellCoordinateWord.numWords;i++){
    			listOfHighlightedWords[i] = WordCloud.cloud[i].word;
    		}
			ParalellCoordinate.drawSightingDetails(this,aa,100, 510, listOfHighlightedWords);
		}
		else{
			this.textSize(14);
			this.textAlign(PApplet.LEFT);
			this.fill(0,200,0);
			this.text("Total selected sightings = "+ParalellCoordinate.a.size(), 400, 600);
			this.fill(150);
			this.text("Satisfied sightings = "+ParalellCoordinate.b.size(), 400, 620);
			
		}
	}
	public void drawPolyline(int index, Color c, int wei) {
		this.stroke(c.getRGB());
		this.strokeWeight(wei);
		Date d = timeUFO[index];
		long t0 = d.getTime()/(24*60*60*1000); // number of days from 1/1/1970
		float v0 = (t0-min0)/(max0-min0);
		float x0 = paralellCoordinate[0].x;
		float y0 = paralellCoordinate[0].y+paralellCoordinate[0].h*v0;
		
		long t1 = d.getMonth()*30+d.getDate(); 
		float v1 = (t1-min1)/(max1-min1);
		float x1 = paralellCoordinate[1].x;
		float y1 = paralellCoordinate[1].y+paralellCoordinate[1].h*v1;
		this.line(x0, y0, x1, y1);
		
		long t2 = d.getHours()*60+d.getMinutes(); // number of minutes in a day
		float v2 = (t2-min2)/(max2-min2);
		float x2 = paralellCoordinate[2].x;
		float y2 = paralellCoordinate[2].y+paralellCoordinate[2].h*v2;
		this.line(x1, y1, x2, y2);
		
		float v3 = (lon[index]-min3)/(max3-min3);
		float x3 = paralellCoordinate[3].x;
		float y3 = paralellCoordinate[3].y+paralellCoordinate[3].h*v3;
		this.line(x2, y2, x3, y3);
		
		float v4 = (lat[index]-min4)/(max4-min4);
		float x4 = paralellCoordinate[4].x;
		float y4 = paralellCoordinate[4].y+paralellCoordinate[4].h*v4;
		this.line(x3, y3, x4, y4);
		
		float v5 = (float) ((dis6[index]-min5)/(max5-min5));
		v5 = PApplet.sqrt(v5);
		float x5 = paralellCoordinate[5].x;
		float y5 = paralellCoordinate[5].y+paralellCoordinate[5].h*v5;
		this.line(x4, y4, x5, y5);
		
		
		float x6 = pcShape.x;
		float y6 = pcShape.yy[pcShape.numWords-1]-pcShape.hh[pcShape.numWords-1]*0.3f;
		for (int w=0;w<pcShape.numWords-1;w++){
			if (shapeUFO[index].toLowerCase().equals(pcShape.wordArray[w])){
				y6 = pcShape.yy[w]-pcShape.hh[w]*0.32f;
				break;
			}	
		}	
		this.line(x5, y5, x6, y6);
		
		float x7 = pcWord.x;
		float y7 = -1000;
		for (int w=0;w<pcWord.numWords;w++){
			if (phrases[index]==null) continue;
			String[] p =  phrases[index].split(",");
			for (int j=0; j<p.length;j++){
				if (p[j].toLowerCase().equals(pcWord.wordArray[w])){
					y7 = pcWord.yy[w]-pcWord.hh[w]*0.32f;
					line(x6, y6, x7, y7);
				}	
			}
		}			
	}
		
	public void checkMutualExlusiveCondition(){
		if (densityButton.b) {
			if (densityButton.s) {
				kMeansButton.s = false;
				sameUFOButton.s = false;
				graphButton.s = false;
				check1.s = false;
				check2.s = false;
				check3.s = false;
				check0.s = true;
			}
		}
		else if (kMeansButton.b) {
			if (kMeansButton.s) {
				densityButton.s = false;
				sameUFOButton.s = false;
				graphButton.s = false;
			}
		}
		else if (sameUFOButton.b) {
			if (sameUFOButton.s) {
				densityButton.s = false;
				kMeansButton.s = false;
				graphButton.s = false;
				wordCloudButton.s = false;
			}
		}
		else if (graphButton.b) {
			if (graphButton.s) {
				densityButton.s = false;
				kMeansButton.s = false;
				sameUFOButton.s = false;
				wordCloudButton.s = false;
			}
		}
		else if (wordCloudButton.b) {
			if (wordCloudButton.s) {
				graphButton.s = false;
				sameUFOButton.s = false;
			}
		}
		
		if (detailsButton.b) {
			if (detailsButton.s) {
				relButton.s = false;
			}
    	}	
    	else if (relButton.b) {
			if (relButton.s) {
				detailsButton.s = false;
			}
    	}
	}
	
	public void drawControllers() {
		// Draw left frame
		fill(0);
		noStroke();
		rect(0, 0, 100, height);
		
		// Text used for instructions
		fill(255, 255, 255);
		textSize(15);
		textAlign(LEFT);
		text("View Options", 0, y1-8);
		textSize(14);
		
		// draw all the buttons and check for mouse-over
		for (int i = 0; i < buttons.length; i++) {
				buttons[i].draw();
		}
		popupMap.draw();
		popupSighting.draw();
		if (wordCloudButton.s){
			if (WordCloud.xLeft.value<120)
				this.cursor(ARROW);
			else
				this.cursor(CROSS);
		}	
		else
			this.cursor(HAND);
		
		if (densityButton.s) {
			checkHeat.draw();
			return;
		}
		
		// Draw color legend
		if (sameUFOButton.s){
			float x4 = 5;
			float y4 = 350;
			float h4 = 25;
			float w4 = 10;
			this.noStroke();
			this.textAlign(PApplet.LEFT);
			for (int year=0;year<=9;year++){
				double value = (double) year/9;
				Color color = ColorScales.getColor(value, "rainbow", 1f);	
				this.fill(color.getRGB());
				this.rect(x4, y4, w4, h4);
				if (year==0){
					this.text("2001",0,y4-4);
				}
				else if (year==9){
					this.text("2010",80,y4-4);
				}
				x4+=w4;					
			}
			
		}
		if (!detailsButton.s && !relButton.s){
			check1.y = this.height-200;
			check2.y = this.height-180;
			check3.y = this.height-160;
			check0.y = this.height-140;
			bPlay.y = this.height-40;
			check0.draw();
			check1.draw();
			check2.draw();
			check3.draw();
			bPlay.draw();
		}
	}

	
	


	

	public void mouseMoved() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].mouseOver();
		}
		for (int i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i]!=null)
				checkboxes[i].checkBrushing();
		}
		bPlay.mouseOver();
	}
		
	public void mousePressed() {
		if (wordCloudButton.s && WordCloud.b<0 
				&& !detailsButton.b 
				&& !detailsButton.s 
				&& !relButton.b
				&& !relButton.s){
			wX1= mouseX;
			wY1= mouseY;
			wX2= mouseX;
			wY2= mouseY;
			isUFOselected = new boolean[numUFO];
		}
		slider.checkSelectedSlider1();
		sliderY.checkSelectedSlider1();
		sliderDistance.checkSelectedSlider1();
		sliderD.checkSelectedSlider1();
		slider1.checkSelectedSlider1();
		slider2.checkSelectedSlider1();
		for (int i=0;i<paralellCoordinate.length;i++){
			paralellCoordinate[i].checkSelectedSlider1();
		}
	}

	public void mouseReleased() {
		slider.checkSelectedSlider2();
		sliderY.checkSelectedSlider2();
		sliderDistance.checkSelectedSlider2();
		sliderD.checkSelectedSlider2();
		slider1.checkSelectedSlider2();
		slider2.checkSelectedSlider2();;
		if (wordCloudButton.s  && WordCloud.b<0 	
				&& !detailsButton.b 
				&& !detailsButton.s 
				&& !relButton.b
				&& !relButton.s){
			WordCount wc = new WordCount(30);
			
	   // Amit, here is how I process the rectangle selection (GREEN BOX)
	   // I grasp all descriptions of UFO sighting with the rectangle area (by mouse dragging) on tha map 		
			wc.countMainUFO(); 
			wordCloud = new WordCloud(wc.wordArray, wc.counts, this, this.width-280,this.width,70,this.height-60, false);
		}
		
		for (int i=0;i<paralellCoordinate.length;i++){
			paralellCoordinate[i].checkSelectedSlider2();
		}
		isDragging = false;
	}

	public void mouseDragged() {
		// Dragging range slider
		int slider_0 = slider.checkSelectedSlider3();
		if (slider_0 >= 0) {
			return;
		}
		int slider_Distance = sliderDistance.checkSelectedSlider3();
		if (slider_Distance >= 0) {
			return;
		}
		int slider_Y = sliderY.checkSelectedSlider3();
		if (slider_Y >= 0) {
			return;
		}
		int slider_D = sliderD.checkSelectedSlider3();
		if (slider_D >= 0) {
			return;
		}
		int slider_1 = slider1.checkSelectedSlider3();
		if (slider_1 >= 0) {
			return;
		}
		int slider_2 = slider2.checkSelectedSlider3();
		if (slider_2 >= 0) {
			return;
		}
		
		
		// Word Cloud
		isDragging = true;
		if (wordCloudButton.s){
			if (!detailsButton.b 
				&& !detailsButton.s 
				&& !relButton.b
				&& !relButton.s){
				wX2= mouseX;
				wY2= mouseY;
				return;
			}
			else{
				for (int i=0;i<paralellCoordinate.length;i++){
					paralellCoordinate[i].checkSelectedSlider3(this);
				}
				pcWord.checkall();
				pcShape.checkall();
			}
		}	
		else
			imap.mouseDragged();
			
	}

	void mouseWheel(int delta) {
		if (delta > 0) {
			imap.sc *= scaleRate;
		} else if (delta < 0) {
			imap.sc *= 1.0 / scaleRate;
		}
	}

	// see if we're over any buttons, and respond accordingly:
	public void mouseClicked() {
		if (densityButton.s){
			check0.checkSelected();
			checkHeat.checkSelected();
		}
		else{
			for (int i = 0; i < checkboxes.length; i++) {
				checkboxes[i].checkSelected();
			}
		}
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].mouseClicked()){
				isUFOselected = new boolean[numUFO]; // remove the effect of same UFO.
				WordCloud.xLeft.set(wordCloud.x1);
				relButton.s = false;
				detailsButton.s = false;
				wX1= mouseX;
				wY1= mouseY;
				wX2= mouseX;
				wY2= mouseY;
			}
		}
		
		bPlay.mouseClicked();
		graph.mouseClicked();
		popupMap.mouseClicked();
		popupSighting.mouseClicked();
	
		if (wordCloudButton.s){
			int s = textbox.mouseClicked();
			if (s>=0 && textbox.mouseOnTextList>=0){
				for (int i=0; i< numUFO; i++){
					if (phrases[i] == null) continue;
					if (phrases[i].contains(commonPhases[s])){
						isUFOselected[i] =  true;
					}	
				}
				WordCount wc = new WordCount(30);
				wc.countMainUFO(); 
				wordCloud = new WordCloud(wc.wordArray, wc.counts, this, this.width-280,this.width,70,this.height-60, false);
			}
			textbox.mouseOnTextList =-1;
		}
		
		if (detailsButton.mouseClicked()){
			ParalellCoordinate.a = new ArrayList<Integer>(); 
			if (WordCloud.s<0){
				for (int i=0; i< numUFO; i++){
					if (isUFOselected[i])
						ParalellCoordinate.a.add(i);
				}
			}
		}	
		else if (relButton.mouseClicked()){
			if (relButton.s){
				ParalellCoordinate.a = new ArrayList<Integer>(); 
				if (WordCloud.s<0){
					for (int i=0; i< numUFO; i++){
						if (isUFOselected[i])
							ParalellCoordinate.a.add(i);
					}
				}
				else{
					String text = WordCloud.cloud[WordCloud.s].word;
					for (int i = 0; i < numUFO; i++) {
						if (phrases[i]!=null && phrases[i].contains(text)){
							ParalellCoordinate.a.add(i);
						}
					}
				}
				ParalellCoordinate.checkall ();
				pcWord.checkall();
				pcShape.checkall();
			}	
			return;
		}	
		else{
			if (wordCloudButton.s && wordCloud!=null){
				wordCloud.mouseClicked();
			}
		}
	}
	
	public void keyPressed() {
		if (wordCloudButton.s){
			textbox.keyPressed();
			//if (s>=0)
				
		}
		
		
	}
	public void keyReleased() {
		if (!kMeansButton.s) {
			if (key == 's' || key == 'S') {
				save("modest-maps-app.png");
			}
		}
	}
		
}