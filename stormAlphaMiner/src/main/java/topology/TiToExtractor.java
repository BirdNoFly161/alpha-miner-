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
import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.TopologyContext;

import java.util.*;

public class TiToExtractor extends BaseBasicBolt{
	List<String> starting;
	List<String> ending;
	String startingActivity;
	String endingActivity;
	BufferedWriter out = null;
	
	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {
	    starting= new ArrayList<String>();
	    ending= new ArrayList<String>();
	    
	  }
	
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declareStream("TiTo",new Fields("type","Ti","To"));
		  }
	  
	  public Map<String, Object> getComponentConfiguration() {
		    Config conf = new Config();
		    conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);
		    return conf;
		  }

	  @Override
	  public void execute(Tuple tuple,
	                      BasicOutputCollector outputCollector) {
		
		if(isTickTuple(tuple)) {
			
			outputCollector.emit("TiTo",new Values("tito",starting,ending));
			
		}   
		
		else {
			String traceStrRaw = tuple.getStringByField("trace");
		    String[] parts = traceStrRaw.split(";");

		    
		    startingActivity=parts[0];
		    endingActivity=parts[parts.length-1];
		    
		    if(!starting.contains(startingActivity))
		    	starting.add(startingActivity);
		    
		    if(!ending.contains(endingActivity))
		    	ending.add(endingActivity);
			
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
