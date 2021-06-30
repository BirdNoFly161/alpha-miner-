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
import backtype.storm.Config;
import backtype.storm.Constants;
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
	  
	  public Map<String, Object> getComponentConfiguration() {
		    Config conf = new Config();
		    conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);
		    return conf;
		  }
	  		
		  @Override
		  public void execute(Tuple tuple,
		                      BasicOutputCollector outputCollector) {
			
			  if(isTickTuple(tuple)) {
				  
				  outputCollector.emit("activities",new Values("activities",activities));
			  }
			  
			  else {
				
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
