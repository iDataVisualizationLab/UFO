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

import processing.core.*;


public class MainProcessDurations extends PApplet {
	
	public static void main(String args[])
    {
	  PApplet.main(new String[] { MainProcessDurations.class.getName() });
    }

	public void setup() {
		size(1280, 738);
		background(0);
		stroke(255);
		frameRate(12);
		curveTightness(0.7f); 
		
		String[] lines = loadStrings("outTuan.txt");
		int numUFO = lines.length;
		String[] unformatted = new String[numUFO];
		double[] formatted = new double[numUFO];
		
		String[] timeUnits = {"blank","day","hour","hr","min","sec"};
		int[][] unparsable = new int[timeUnits.length][2]; // Used this for testing
		int[] total = new int[timeUnits.length];  // total of sightings checked for each time units
		
		for (int i = 0; i < numUFO; i++) {
			formatted[i] = Double.NaN;
			String[] p = lines[i].split("\t");
			unformatted[i] = p[4].toLowerCase().trim();
			
			//if (durUFO[i].trim().equals("blank")){
			//	total0++;
			//	formatted[i] = 0;
			//}
			 
			String timeToken = "";
			int tokenId =-1;
			for (int t = 0; t < timeUnits.length; t++) {
				if (unformatted[i].contains(timeUnits[t])){
					timeToken = timeUnits[t];
					tokenId=t;
					total[t]++;
					break;
				}		
			}
			
			if (timeToken.equals("")){	
				System.out.println(unformatted[i]);
				continue; // can not find a time token in the string
			}
			else if (unformatted[i].trim().equals("blank")){
					formatted[i] = 0;
			}	
			else if (tokenId>=1){ // found a time unit	
				processOneTimeUnit(unformatted[i], i, timeToken, tokenId, formatted, unparsable);
			}	
			else{
				
			}
		}
		System.out.println();
		int totalProcessed = 0;
		int totalUnparsable1 = 0;
		int totalUnparsable2 = 0;
		for (int t = 0; t < timeUnits.length; t++) {
			totalProcessed += total[t];
			totalUnparsable1 += unparsable[t][0];
			totalUnparsable2 += unparsable[t][1];
			System.out.println("Process time unit = "+timeUnits[t]+"	"+total[t]+"/"+ lines.length+ "	Unparsable = "+unparsable[t][0]+"	"+unparsable[t][1]);
		}
		System.out.println("  TotalProcessed = "+totalProcessed+"/"+numUFO
				+ "	Unparsable = "+totalUnparsable1+"	"+totalUnparsable2);
		
		double totalUnparse = totalUnparsable2+numUFO-totalProcessed;
		System.out.println("  Total Unparsable = "+totalUnparse+"	"+totalUnparse/numUFO+"%");
		
		String[] lines2 = loadStrings("outTuan.txt");
		String[] outs = new String[lines.length];
		for (int i = 0; i < numUFO; i++) {
			String[] p = lines2[i].split("\t");
			outs[i] = p[0]+"\t"+p[1]+"\t"+p[2]+"\t"+p[3]+"\t"+formatted[i]+"\t"+
					p[5]+"\t"+p[6]+"\t"+p[7];
		}
		this.saveStrings("/Users/nhontuan/Desktop/UFOdata2.txt", outs);
	}

	// Process a time unit. The input string may contain more than one time unit but this is rare and complicated
	// We already order time units array. If this is the case, we take the biggest time unit
	
	public void processOneTimeUnit(String input, int id, String timeToken, int tokenId,  double[] formatted, int[][]  unparsable) {
		double mul = -1;
		if (timeToken.equals("min"))
			mul = 60;
		else if (timeToken.equals("hr") || timeToken.equals("hour"))
			mul = 60*60;
		else if (timeToken.equals("day"))
			mul = 24*60*60;
		
		if (input.trim().equals(timeToken)){
			formatted[id] = mul;  // if no number indicated
			return;
		}
		String str = input.split(timeToken)[0];
		
		// Cover words describing numbers to actual number such as three -> 3
		str = textToNum(str);
		// Remove word contain unclear information such as about, or more than... 
		str = removeNoMeaningWords(str);
		str = str.trim();
		try{
			double num = Double.parseDouble(str);
			formatted[id] = num*mul;
		}
		catch(NumberFormatException e2){
			unparsable[tokenId][0]++;
			// Process string containing interval words such as "to" or "-" 
			String intervalWords = getIntervalWords(str);
			if (intervalWords.equals("")){ // No interval words
				unparsable[tokenId][1]++;  // Unable to process further
			}
			else{
				try{
					String[] pieces = str.split(intervalWords);
					if (pieces.length==1){
						String str1 = pieces[0].trim();
						double num1 = Double.parseDouble(str1);
						formatted[id] = num1*mul; // Missing the second value. This is a BUG input data 
					}
					else if (pieces.length>1){
						String str1 = pieces[0].trim();
						String str2 = pieces[1].trim();
						double num1 = Double.parseDouble(str1);
						double num2 = Double.parseDouble(str2);
						formatted[id] = (num1+num2)*mul/2;
					}
				}
				catch(NumberFormatException e3){
					unparsable[tokenId][1]++;
					// Unable to process further
					
				}	
			}
			
		}
	}
		
		
	// Get interval words such as 1 to 2 minutes or 1-2 minutes
	public String getIntervalWords(String input) {
		String[] intervalList = { "to","-","or"};
		for (int i=0;i<intervalList.length;i++){
			if (input.contains(intervalList[i]))
				return intervalList[i];
		}
		return "";
	}
	
	// Remove no meaning words
	public String removeNoMeaningWords(String input) {
		String[] removeList = { "witnessed for about","about", "approximately","approx.","approx","aprox.","appx", "aprox", "aprx.","apprx.", "appox.","apx.",
				"less than one","less then","less than","almost","no longer than","@","~",">","<","it has been", "not long maybe",
				"on and off","observed","at lest","a little over","roughly","est.", "at least","maybe", "app.", "around","amatly","+/-",
				"over","+","under","felt like","more than","for"};
		for (int i=0;i<removeList.length;i++){
			if (input.contains(removeList[i]))
				return input.replace(removeList[i], "");
		}
		return input;
	}
	
	// Covert text into number if text was used to describe numbers
	public String textToNum(String input) {
		// input String may contain more than one number
		if (input.contains("one"))
			input = input.replace("one", "1");
		if (input.contains("two"))
			input = input.replace("two", "2");
		if (input.contains("three"))
			input = input.replace("three", "3");
		if (input.contains("four"))
			input = input.replace("four", "4");
		if (input.contains("five"))
			input = input.replace("five", "5");
		if (input.contains("six"))
			input = input.replace("six", "6");
		if (input.contains("seven"))
			input = input.replace("seven", "7");
		if (input.contains("eight"))
			input = input.replace("eight", "8");
		if (input.contains("nine"))
			input = input.replace("nine", "9");
		if (input.contains("ten"))
			input = input.replace("ten", "10");
		if (input.contains("fifteen"))
			input = input.replace("fifteen", "15");
		if (input.contains("twenty"))
			input = input.replace("twenty", "20");
		if (input.contains("thirty"))
			input = input.replace("thirty", "30");
		if (input.contains("half"))
			input = input.replace("half", "0.5");
		if (input.contains("couple"))
			input = input.replace("couple", "2");
		if (input.contains("a few"))
			input = input.replace("a few", "2");
		if (input.contains("few"))
			input = input.replace("few", "2");
		if (input.contains("several"))
			input = input.replace("several", "3");
		if (input.contains("many"))
			input = input.replace("many", "4");
		if (input.contains("a "))
			input = input.replace("a ", "1");
		return input;
	}
	
	
	
	public void draw() {
	}
}	