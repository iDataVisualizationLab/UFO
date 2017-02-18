package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class WordCount {
	public int nWords ;
	public String[] wordArray; 
	public int[] counts; 
	
	public WordCount(int n){
		nWords = n;
		wordArray = new String[nWords];
		counts = new int[nWords]; 
		for (int i=0;i<nWords;i++){
			wordArray[i] = "";
			counts[i] =0;
		}
	}
	public void countMainUFO() {
		Set stopWords = readStopWords();
		Map wordMap = new HashMap();
		int count;
		for (int u = 0; u < main.MainUFO_Version_3_0.numUFO; u++) {
			if (main.MainUFO_Version_3_0.isUFOselected[u]){
				if (main.MainUFO_Version_3_0.phrases[u]==null) continue;
				System.out.println("Please look here, Amit: "+main.MainUFO_Version_3_0.desUFO[u]);
				//System.out.println(main.MainUFO_Version_3_0.phrases[u]);
				String[] p =  main.MainUFO_Version_3_0.phrases[u].split(",");
				for (int i = 0; i < p.length; i++) {
					String word = p[i].toLowerCase();
					/*word = word.replace("?", "");
					word = word.replace(",", "");
					word = word.replace(".", "");
					word = word.replace(";", "");
					word = word.replace("!", "");
					word = word.replace(":", "");
					word = word.replace("@", "");
					word = word.replace("#", "");
					word = word.replace(" ", "");
					if (word.equals("lights") || word.equals("objects") )
						word = word.substring(0,word.length()-1);*/
					if (word.contains("((") || word.contains("( (") 
							|| word.contains("))") || word.contains(") )")
							|| word.contains("pd") || word.contains("nuforc")
							|| word.contains("!")){
						word="ok";
					}	
					
				//	System.out.println(i+" word = "+word);
					if (!stopWords.contains(word)) {
						if (wordMap.containsKey(word)) {
							count = (Integer) wordMap.get(word);
							count++;
							wordMap.put(word, new Integer(count));
						} else {
							wordMap.put(word, new Integer(1));
						}
					}
				}	
			}
		}
		maxWord(wordMap,nWords);
	}
	
	public void countSelectedList(ArrayList<Integer> a) {
		Set stopWords = readStopWords();
		Map wordMap = new HashMap();
		int count;
		for (int it = 0; it < a.size(); it++) {
			int index = a.get(it);	
			if (main.MainUFO_Version_3_0.phrases[index]==null) continue;
			String[] p =  main.MainUFO_Version_3_0.phrases[index].split(",");
			for (int i = 0; i < p.length; i++) {
				String word = p[i].toLowerCase();
				/*word = word.replace("?", "");
				word = word.replace(",", "");
				word = word.replace(".", "");
				word = word.replace(";", "");
				word = word.replace("!", "");
				word = word.replace(":", "");
				word = word.replace("@", "");
				word = word.replace("#", "");
				word = word.replace(" ", "");
				if (word.equals("lights") || word.equals("objects") )
					word = word.substring(0,word.length()-1);*/
				if (word.contains("((") || word.contains("( (") 
						|| word.contains("))") || word.contains(") )")
						|| word.contains("pd") || word.contains("nuforc")
						|| word.contains("!")){
					word="ok";
				}	
				
				if (!stopWords.contains(word)) {
					if (wordMap.containsKey(word)) {
						count = (Integer) wordMap.get(word);
						count++;
						wordMap.put(word, new Integer(count));
					} else {
						wordMap.put(word, new Integer(1));
					}
				}
			}	
		}
		maxWord(wordMap,nWords);
	}
	
	public void countShape(ArrayList<Integer> a) {
		Set stopWords = readStopWords();
		Map wordMap = new HashMap();
		int count;
		for (int it = 0; it < a.size(); it++) {
			int index = a.get(it);	
			if (main.MainUFO_Version_3_0.shapeUFO[index]==null) continue;
			String word = main.MainUFO_Version_3_0.shapeUFO[index].toLowerCase();
			if (word.contains("((") || word.contains("( (") 
					|| word.contains("))") || word.contains(") )")
					|| word.contains("pd") || word.contains("nuforc")
					|| word.contains("!")){
				word="ok";
			}	
			
			if (!stopWords.contains(word)) {
				if (wordMap.containsKey(word)) {
					count = (Integer) wordMap.get(word);
					count++;
					wordMap.put(word, new Integer(count));
				} else {
					wordMap.put(word, new Integer(1));
				}
			}
		}
		maxWord(wordMap,nWords);
	}

	private static Set readStopWords() {
		java.io.BufferedReader fin;
		Set stopWords = new HashSet();
		try {
			fin = new java.io.BufferedReader(new java.io.FileReader(
					"data/stopListEnglish.txt"));
			String record;
			while ((record = fin.readLine()) != null) {
				stopWords.add(record);
			}
		} catch (java.io.IOException ie) {
			System.err.println("I/O exception in stopList");
		}
		return stopWords;
	}

	private String[] maxWord(Map wordMap, int num) {
		String[] results = new String[num];
		int index = 0;

		Set set = wordMap.entrySet();
		Iterator im1 = set.iterator();
		int maxCount = 0;
		while (im1.hasNext()) {
			Map.Entry me = (Map.Entry) im1.next();
			if (((Integer) me.getValue()) > maxCount)
				maxCount = ((Integer) me.getValue());
		}
		while (index < num) {
			Iterator im2 = set.iterator();
			while (im2.hasNext()) {
				Map.Entry me = (Map.Entry) im2.next();
				if (((Integer) me.getValue()) == maxCount) {
					if (index >= num)
						break;
					//System.out.println(index+" Value:" + me.getValue() + " "
					//		+ me.getKey());
					results[index] = (String) me.getKey();
					wordArray[index] = (String) me.getKey();
					counts[index] = (Integer)me.getValue();
					index++;
				}
			}
			maxCount--;
			if (maxCount<0)
				break;
		}
		return results;
	}
}
