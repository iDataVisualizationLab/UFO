package main;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;



public class WordCount {
	public static int numTopics=10 ;
	public int nWords ;
	public String[] wordArray; 
	public int[] counts; 
	List<TopicIdvl> listTopics;
	// Amit added topic array
	public String[] topicArray; 
	public int[] topicCount; 
	
	public WordCount(int n){
		nWords = n;
		wordArray = new String[nWords];
		counts = new int[nWords]; 
		for (int i=0;i<nWords;i++){
			wordArray[i] = "";
			counts[i] =0;
		}
		listTopics = null;
	}
	public void countMainUFO() {
		Set stopWords = readStopWords();
		Map wordMap = new HashMap();
		int count;
		List<String> phrasesList = new ArrayList<String>(); //Amit added to hold the selected phrase list
		for (int u = 0; u < main.MainUFO_Version_3_0.numUFO; u++) {
			if (main.MainUFO_Version_3_0.isUFOselected[u]){
				if (main.MainUFO_Version_3_0.phrases[u]==null) continue;
			//	System.out.println("Please look here, Amit: "+main.MainUFO_Version_3_0.desUFO[u]);
				//System.out.println(main.MainUFO_Version_3_0.phrases[u]);
				
				phrasesList.add(main.MainUFO_Version_3_0.desUFO[u]);
				
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
		try {
			if(phrasesList.size() > 0){
				listTopics = topicModeling(phrasesList);
				for(TopicIdvl m :  listTopics){   	  
			    	//  System.out.println("m="+m+" topicArray="+topicArray);
			    	//  topicArray[i] = m.getTopicString();
			    	//topicCount[i] = Integer.parseInt(m.getTopicDist()*100);
			    	  System.out.println(m.getTopicId()+ "\t"+m.getTopicDist()+"\t"+m.getTopicString());
			     }
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		 maxWord(wordMap,nWords); //commented by amit
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
//	private String[] topicModeling(List<String> phraseListSelected) throws IOException{	
	private List<TopicIdvl> topicModeling(List<String> phraseListSelected) throws IOException{
		
	StringBuilder inputData =  new StringBuilder();
	  for(String phrase : phraseListSelected){
		  inputData.append(phrase);
		  inputData.append("\n");
		  
	  }
	  String[] results = new String[10];
		  // Begin by importing documents from text to feature sequences
      ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

      // Pipes: lowercase, tokenize, remove stopwords, map to features
       pipeList.add(new CharSequenceLowercase() );
       pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
      // pipeList.add( new TokenSequenceRemoveStopwords(new File("C:\\Users\\nisha\\mallet-2.0.8\\stoplists\\en.txt"), "UTF-8", false, false, false) );
       pipeList.add( new TokenSequenceRemoveStopwords(new File("./data/stopListEnglish.txt"), "UTF-8", false, false, false) );
      
       pipeList.add( new TokenSequence2FeatureSequence() );
      InstanceList instances = new InstanceList (new SerialPipes(pipeList));

     // Reader fileReader = new InputStreamReader(new FileInputStream(new File("D:\\TTU\\Dr.Dang\\git\\UFO\\UFO\\src\\data\\UFO_mainland_clustered_and_chunked.txt")), "UTF-8");
      Reader fileReader = new InputStreamReader(IOUtils.toInputStream(inputData.toString(), "UTF-8"));
      instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                                             3, 2, 1)); // data, label, name fields

      // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
      //  Note that the first parameter is passed as the sum over topics, while
      //  the second is the parameter for a single dimension of the Dirichlet prior.
     // ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
      ParallelTopicModel model = new ParallelTopicModel(numTopics);

      model.addInstances(instances);

      // Use two parallel samplers, which each look at one half the corpus and combine
      //  statistics after every iteration.
      model.setNumThreads(2);

      // Run the model for 50 iterations and stop (this is for testing only, 
      //  for real applications, use 1000 to 2000 iterations)
      model.setNumIterations(50);
      model.estimate();

      // Show the words and topics in the first instance

      // The data alphabet maps word IDs to strings
      Alphabet dataAlphabet = instances.getDataAlphabet();
      
      FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
      LabelSequence topics = model.getData().get(0).topicSequence;
      
      Formatter out = new Formatter(new StringBuilder(), Locale.US);
      for (int position = 0; position < tokens.getLength(); position++) {
          out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
         
      }
      
      System.out.println(out);
      
      // Estimate the topic distribution of the first instance, 
      //  given the current Gibbs state.
      double[] topicDistribution = model.getTopicProbabilities(0);

      // Get an array of sorted sets of word ID/count pairs
      ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
      List<TopicIdvl> list = new ArrayList<TopicIdvl>();
      // Show top 5 words in topics with proportions for the first document
      for (int topic = 0; topic < numTopics; topic++) {
          Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
          
          out = new Formatter(new StringBuilder(), Locale.US);
          out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
          TopicIdvl tpModel = new TopicIdvl();
          tpModel.setTopicId(topic);
          tpModel.setTopicDist(topicDistribution[topic]);
          
          int rank = 0;
          int weightSum = 0 ;
          while (iterator.hasNext() && rank < 6) {
              IDSorter idCountPair = iterator.next();
              //out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
              out.format("%s ", dataAlphabet.lookupObject(idCountPair.getID()));
              weightSum += idCountPair.getWeight();
              
              rank++;
          }
        
         // TopicMapComaprator
          wordArray[topic] = out.toString();
          results[topic] = out.toString();
         // System.out.println("results[topic]="+results[topic]);
          
          counts[topic] = topic+1 ;
          tpModel.setTopicString(out.toString().split("\\t")[2]);
          list.add(tpModel);
          System.out.println(out);
      }
      Collections.sort(list);
      for(TopicIdvl m :  list){   	  
    	//  System.out.println("m="+m+" topicArray="+topicArray);
    	//  topicArray[i] = m.getTopicString();
    	//topicCount[i] = Integer.parseInt(m.getTopicDist()*100);
    	//  System.out.println(m.getTopicId()+ "\t"+m.getTopicDist()+"\t"+m.getTopicString());
      }
      return list;
      
     // return results;
      
	}
		
}
