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

public class Mapper extends BaseBasicBolt{
	
	String first;
	String second;
	String pair;
	BufferedWriter out = null;
	
	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {

	    
	    
	  }
	
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declareStream("relations",new Fields("pair","relation"));
		  }
	  
	
	  @Override
	  public void execute(Tuple tuple,
	                      BasicOutputCollector outputCollector) {
		
		    
		  
		String traceStrRaw = tuple.getStringByField("trace");
	    String[] parts = traceStrRaw.split(";");
	    
	    for(int i=0;i<parts.length-1;i+=1) {
		      first=parts[i];
			  second=parts[i+1];
			  pair=first+","+second;
			  outputCollector.emit("relations",new Values(pair,"<"));
			  pair=second+","+first;
			  outputCollector.emit("relations",new Values(pair,">"));
	    }
	    
	    
	    
	    
	    //output the list of unique activities here, simply emit the type and the list as is 
	    //outputCollector.emit(new Values("UNIQUE ACTIVITIES",activities.get(0)));
	  }
	  
	  
}
