	import java.io.*;
	import java.lang.System;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;

public class Utilities {


	static void printMap(HashMap<String,List<String>> hash, String fileName){
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
	
}
