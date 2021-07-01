package topology;

import java.io.*;
import java.lang.System;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class Utilities {

	
	  static void printList(List<List<String>>list, String fileName){
		    String[] output = new String[list.size()];
		    int n = 0;
		    for(List<String> list2: list){
		      output[n] = "{ ";
		      for(int i = 0; i < list2.size(); i++){
		        if(i < list2.size() -1){
		          output[n] += list2.get(i) + ", ";
		        } else {
		          output[n] += list2.get(i);
		        }
		      }
		      output[n] += " }";
		      n++;
		    }
		    fileWrite(output, fileName);
		  }

	  static void printSet(Set<String> set, String fileName){
	    String[] output = new String[1];
	    output[0] = "{ ";
	    int i = 0;
	    for(String s: set){
	      if(i < set.size()-1){
	        output[0] += s + ", ";
	      } else {
	        output[0] += s + " }";
	      }
	      i++;
	    }
	    fileWrite(output, fileName);
	  }

	  static void printSetSet(Set<Set<String>> setSet, String fileName){
	    String[] output = new String[setSet.size()];
	    int n = 0;
	    for(Set<String> set: setSet){
	      output[n] = "{ ";
	      int i = 0;
	      for(String s: set){
	        if(i < set.size()-1){
	          output[n] += s + ", ";
	        } else {
	          output[n] += s + " }";
	        }
	      i++;
	      }
	      n++;
	    }

	    fileWrite(output, fileName);
	  }
	
	
    static void printMap3(Map<String,Map<String, String>> hash, String fileName){
    //String that gets printed to the output.txt
    String[] str = new String[hash.size()];
    
    //Keep track of how many keys have passed
    int n = 0;
    
    //Iterate through keys
    for(String key: hash.keySet()){
      //append key to the front of the string
      str[n] = (key + ": ");
    
      //Iterate through more keys
      for(String key2: hash.get(key).keySet()){
        //Copy the key and String onto the output string
        str[n] += ("(" + key2 + ": " + hash.get(key).get(key2) + " ) ");
      }

      //Move onto the next key
      n++;
    }
    
    //Write the entire output string array into the designated file
    fileWrite(str, fileName);
  }
   
	
	static void printMap(Map<String,List<String>> hash, String fileName){
	    //String that gets printed to the output.txt
	    String[] str = new String[hash.size()];
	    
	    //Keep track of how many keys have passed
	    int n = 0;
	    
	    //Iterate through keys
	    for(String key: hash.keySet()){
	      //append key to the front of the string
	      str[n] = (key + ": ");
	        
	      //Iterate through the list
	      for(int i = 0; i < hash.get(key).size(); i++){
	        //Check if end of list has been reached so we can stop appending commas onto the string
	        if(i < hash.get(key).size() - 1){
	          str[n] += hash.get(key).get(i) + ", ";
	        } else {
	          str[n] += hash.get(key).get(i);
	        }
	      }
	        
	      //Move onto the next key
	      n++;
	    }
	    
	    //Write the entire output string array into the designated file
	    fileWrite(str, fileName);
	  }

	//The write file function. Identical to the previous writeFile in the readme.
	static void fileWrite(String[] str, String fileName){
	    try{
	      FileWriter writer = new FileWriter(fileName, true);
	      for(String s: str){
	        writer.write(s + System.getProperty("line.separator"));
	      }
	      writer.close();
	    }
	    catch (IOException exc) {
	      System.out.println("Could not open file");
	      exc.printStackTrace();
	    }
	  }

	  static void printMap2(Map<String,String> hash, String fileName){
		    //String that gets printed to the output.txt
		    String[] str = new String[hash.size()];
		    
		    //Keep track of how many keys have passed
		    int n = 0;
		    
		    //Iterate through keys
		    for(String key: hash.keySet()){
		      //Copy the key and String onto the output string
		      str[n] = (key + ": " + hash.get(key));
		        
		      //Move onto the next key
		      n++;
		    }
		    
		    //Write the entire output string array into the designated file
		    fileWrite(str, fileName);
		  }
	
	  static void printMap4(List<String> list, String fileName){
		    //String that gets printed to the output.txt
		    String[] str = new String[list.size()];
		    for(int i = 0; i < list.size(); i++){
		      //append item to the string
		      str[i] = list.get(i);
		    }
		    
		    //Write the entire output string array into the designated file
		    fileWrite(str, fileName);
		  }

	  static void printListListList(List<List<List<String>>> listListList, String fileName){
		    String[] output = new String[listListList.size()];
		    int n = 0;
		    for(List<List<String>> listList : listListList){
		      int x = 0;
		      output[n] = "";
		      for(List<String> list: listList){
		        output[n] += "{ ";
		        int i = 0;
		        for(String s: list){
		          if(i < list.size()-1){
		            output[n] += s + ", ";
		          } else {
		            output[n] += s + " }";
		          }
		        i++;
		        }
		        if(x < listList.size() -1){
		          output[n]+= " ;;;;;;;;;;;;;; ";
		        }
		        x++;
		      }
		      n++;
		    }

		    fileWrite(output, fileName);
		  }

	  
	  static void placesWithStart(List<List<List<String>>> YL,String activity) {
		  List<Integer> indices=new ArrayList<Integer>();
		  List<List<String>> pair;
		  List<String> first;
		  List<String> second;
		  for(int i=0;i<YL.size();i+=1) {
			  pair=YL.get(i);
			  first=pair.get(0);
			  for(String act : first) {
				  if(act.equals(activity)) {
					  indices.add(i);
				  }
			  }
			  
		  }
	  }
	  
	  
	  static void makeGraph(List<List<List<String>>> places, List<String> Ti, List<String> To) {
		  
	  }
	  
	  static boolean subset(List<String> list1, List<String> list2){
		    if(list1.size() > list2.size()){
		      return false;
		    }
		    for(String s: list1){
		      if(!list2.contains(s)){
		        return false;
		      }
		    }
		    return true;
		  }



	  static void printGraph(List<String> edges, List<String> activities, List<String> places, String fileName){
		    fileWrite("digraph {", fileName);
		    String[] output = new String[activities.size() + places.size()];
		    printNodeEdges(edges, fileName);
		    for(int i = 0; i < activities.size(); i++){
		      output[i] = "";
		      output[i] += "\t" + activities.get(i) + "[shape=box];";
		    }
		    for(int i = activities.size(); i < places.size() + activities.size(); i++){
		      output[i] = "";
		      output[i] += "\t" + places.get(i - activities.size()) + "[shape=circle];";
		    }
		    output[activities.size() + places.size() - 1] += System.getProperty("line.separator") + "}";
		    fileWrite(output, fileName);
		  }

		  static void printNodeEdges(List<String> list, String fileName){
		    String[] output = new String[list.size()];
		    int n = 0;
		    for(String str: list){
		      output[n] = "\t";
		      String[] arr = str.split(",");
		      int i = 0;
		      for(String str2: arr){
		        if(i < arr.length-1){
		          output[n] += str2 + " -> ";
		        } else {
		          output[n] += str2 + ";";
		        }
		        i++;
		      }
		      n++;
		    }
		    
		    fileWrite(output, fileName);
		  }


		  static void fileWrite(String str, String fileName){
			    try{
			      FileWriter writer = new FileWriter(fileName, true);
			      writer.write(str + System.getProperty("line.separator"));
			      writer.close();
			    }
			    catch (IOException exc) {
			      System.out.println("Could not open file");
			      exc.printStackTrace();
			    }
			  }

}

