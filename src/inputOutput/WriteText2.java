package inputOutput;
/** Simple Program to write a text file
*/

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import processing.core.PApplet;

public class WriteText2 extends PApplet{
	// UFO Data from Kevin
		public static int numUFO = 25800;
		public static String[] timeText = new String[numUFO];
		public static Date[] timeUFO = new Date[numUFO];
		public static String[] cityUFO = new String[numUFO];
		public static String[] stateUFO = new String[numUFO];
		public static String[] shapeUFO = new String[numUFO];
		public static String[] durUFO = new String[numUFO];
		public static String[] desUFO = new String[numUFO];
		public static String[] latUFO = new String[numUFO];
		public static String[] lonUFO = new String[numUFO];
		
		static int numDays = 3652;
		@SuppressWarnings("unchecked")
		public static ArrayList<Integer>[] sight = new ArrayList[numDays];
		
		static int numMonths = 120;
		@SuppressWarnings("unchecked")
		public static ArrayList<Integer>[] sightM = new ArrayList[numMonths];
		
		
	public static void main(String[] arg){
		WriteText2 w = new WriteText2();
		String[] lines = w.loadStrings("outTuan.txt");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm");
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split("\t");
			//System.out.println(i+"  "+lines[i]);
			
			// Obtain new date + time
			try {
				timeUFO[i] = df.parse(p[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			cityUFO[i] = p[1];
			stateUFO[i] = p[2];
			shapeUFO[i] = p[3];
			durUFO[i] = p[4];
			desUFO[i] = p[5];
			latUFO[i] = (p[6]);
			lonUFO[i] = (p[7]);
		}
		
		
		
		// Compute Monthly Data
		String ddd = "01/01/2001 00:00";
		Date minDate = null ;
		try {
			minDate = df.parse(ddd);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		for (int i = 0; i < numDays; i++) {
			sight[i] = new ArrayList<Integer>();	
		}
		for (int i = 0; i < lines.length; i++) {
			int dif = (int) ((timeUFO[i].getTime()-minDate.getTime())/(60*60*24*1000));
			sight[dif].add(i);	
		}
		
		
		for (int i = 0; i < numMonths; i++) {
			sightM[i] = new ArrayList<Integer>();	
		}
		for (int i = 0; i < lines.length; i++) {
			int dif = (int) (timeUFO[i].getMonth()+ 12*(timeUFO[i].getYear()-101));
			sightM[dif].add(i);	
		}
		for (int i = 0; i < numMonths; i++) {
			System.out.println(sightM[i].size());
			//numS[dif]++;	
		}
		
		// Write File
		try {
			  BufferedWriter out = new BufferedWriter(new FileWriter("src/outTuan2.txt"));
			  for (int i = 0; i < lines.length; i++) {
				//  out.write(timeText[index[i]] + "\t"+cityUFO[index[i]]+"\t"+stateUFO[index[i]] +"\t"+shapeUFO[index[i]]
				//		  +"\t"+durUFO[index[i]]+"\t"+desUFO[index[i]]+"\t"+latUFO[index[i]]+"\t"+lonUFO[index[i]]);		
				//  out.newLine();
				  		
			  }	
		 
			  out.close();
			} catch (IOException e){
				e.printStackTrace();
	   }
		
	}
	public static boolean notInList(int index, int[] a, int max){
		for (int i = 0; i < max; i++) {
			if (index == a[i])
				return false;
		}
		return true;
	}
		
}