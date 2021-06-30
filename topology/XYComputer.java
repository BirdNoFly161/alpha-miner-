package topology;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.task.TopologyContext;

import java.util.*;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


public class XYComputer extends BaseBasicBolt {
	
	String type;
	List<String> Ti;
	List<String> To;
	List<String> activities;
	Map<String,String> causalityRelations;
	
	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {
		causalityRelations=new HashMap<String,String>();
		Ti=new ArrayList<String>();
		To=new ArrayList<String>();
		activities=new ArrayList<String>();
	  }
	
	 public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declareStream("causalityRelations",new Fields("type","map"));
		  }
	
	  public Map<String, Object> getComponentConfiguration() {
		    Config conf = new Config();
		    conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
		    return conf;
		  }
	  
	  @Override
	  public void execute(Tuple tuple,
	                      BasicOutputCollector outputCollector) {
		
		  if(isTickTuple(tuple)) {
			  
			  
			  if(!Ti.isEmpty() && !To.isEmpty() && !activities.isEmpty() && !causalityRelations.isEmpty()) {
				  //Utilities.printMap2(causalityRelations, "XYCOMPUTERCAUSALITY.txt");
			  	  //Utilities.printMap4(Ti, "XYCOMPUTERstarting.txt");
			  	  //Utilities.printMap4(To, "XYCOMPUTERending.txt");
			  	  //Utilities.printMap4(activities, "XYCOMPUTERactivities.txt");
				  
			  }
		  }
		  
		  else {
			
			type=tuple.getStringByField("type");
			if(type.equals("activities")) {
				activities=(List<String>) tuple.getValueByField("activities");
			}
			
			if(type.equals("tito")) {
				Ti=(List<String>) tuple.getValueByField("Ti");
				To=(List<String>) tuple.getValueByField("To");
			}
			
			if(type.equals("causality")) {
				causalityRelations=(Map<String,String>)tuple.getValueByField("map");
			}
			  
		  }
		
		  		    
	    //output the list of unique activities here, simply emit the type and the list as is 
	    //outputCollector.emit(new Values("UNIQUE ACTIVITIES",activities.get(0)));
	  }
	  
	  
	  
	
	  private boolean isTickTuple(Tuple tuple) {
		    String sourceComponent = tuple.getSourceComponent();
		    String sourceStreamId = tuple.getSourceStreamId();
		    return sourceComponent.equals(Constants.SYSTEM_COMPONENT_ID)
		        && sourceStreamId.equals(Constants.SYSTEM_TICK_STREAM_ID);
		  }
	  
}
