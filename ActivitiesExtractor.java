package topology;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.task.TopologyContext;

import java.util.Map;
import java.util.ArrayList;
import java.util.*;

public class ActivitiesExtractor extends BaseBasicBolt{
		
	List<String> activities;
	List<String> temp;
	List<String> distinctTemp;
	BufferedWriter out = null;


	
	
	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {
	    activities=new ArrayList<String>();
		
	  }
	
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declareStream("activities",new Fields("type","activities"));
		  }
	  		
		  @Override
		  public void execute(Tuple tuple,
		                      BasicOutputCollector outputCollector) {
			
			temp=new ArrayList<String>();
			distinctTemp=new ArrayList<String>();
			    
			  
			String traceStrRaw = tuple.getStringByField("trace");
		    String[] parts = traceStrRaw.split(";");
		    /* just remove duplicated from parts the easiest way you can find and then check if each element is in activities list if they
		     * arent present add them, else dont
		     */
		    
		    temp.addAll(Arrays.asList(parts));
		    distinctTemp.addAll(Arrays.asList(temp.stream().distinct().toArray(String[]::new)));
		    
		    for(String str : distinctTemp) {
		    	if(!activities.contains(str))
		    		activities.add(str);
		    }
		    
			try {
			    FileWriter fstream = new FileWriter("out.txt", true); //true tells to append data.
			    out = new BufferedWriter(fstream);
			    
			    for(String str : activities) {
			    	
			    		out.write(str+";;");
			    }
			    out.write("\n NEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINE\nNEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINENEWLINE");
			}

			catch (IOException e) {
			    System.err.println("Error: " + e.getMessage());
			}

			finally {
			    if(out != null) {
			        try{
			        	out.close();
			        }
			        catch(IOException e ) {
			        	
			        }
			    }
			    
			}
		    
		    
		    outputCollector.emit(new Values("UNIQUE ACTIVITIES",activities.get(0)));
		  }
		  
}
