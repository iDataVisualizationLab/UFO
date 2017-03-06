package inputOutput;
/** Simple Program to write a text file
*/

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import processing.core.PApplet;

public class WriteText extends PApplet{
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
		
		
	public static void main(String[] arg){
		WriteText w = new WriteText();
		String[] lines = w.loadStrings("out.txt");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm");
		for (int i = 0; i < lines.length; i++) {
			String[] p = lines[i].split("\t");
			
			// Obtain new date + time
			try {
				timeText[i] = p[0];
				if (!timeText[i].contains(" "))
					timeText[i] += " 00:00";
					timeUFO[i] = df.parse(timeText[i]);
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
		
		
		// Order by date
		int[] index = new int[lines.length];
		for (int i = 0; i < lines.length; i++) {
			index[i] =-1;
		}
		
		int indexMin = -1;
		for (int d = 0; d < lines.length; d++) {
			String ddd = "01/01/2100 00:00";
			Date minDate = null ;
			try {
				minDate = df.parse(ddd);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			for (int i = 0; i < lines.length; i++) {
				if (timeUFO[i].before(minDate) && notInList(i,index, d)){
					minDate = timeUFO[i];
					indexMin = i;
				}
			}
			System.out.println(d+ "  "+indexMin+" "+minDate+" ");		
			index[d] = indexMin;
		}	
		
		
		
		
		// Write File
		try {
			  BufferedWriter out = new BufferedWriter(new FileWriter("src/outTuan2.txt"));
			  for (int i = 0; i < lines.length; i++) {
				  out.write(timeText[index[i]] + "\t"+cityUFO[index[i]]+"\t"+stateUFO[index[i]] +"\t"+shapeUFO[index[i]]
						  +"\t"+durUFO[index[i]]+"\t"+desUFO[index[i]]+"\t"+latUFO[index[i]]+"\t"+lonUFO[index[i]]);		
				  out.newLine();
				  		
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