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

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


public class Reducer extends BaseBasicBolt {
	
	String pair;
	String relation;
	
	Map<String,List<String>> relationList;
	List<String> list;
	
	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {
		relationList=new HashMap<String,List<String>>();
	  }
	
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declareStream("precedenceMap",new Fields("map"));
		  }
	
	  @Override
	  public Map<String, Object> getComponentConfiguration() {
	    Config conf = new Config();
	    conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);
	    return conf;
	  }

	  @Override
	  public void execute(Tuple tuple,
	                      BasicOutputCollector outputCollector) {
		
		  if(isTickTuple(tuple)) {
			  //Utilities.printMap(relationList, "mymap.txt");
			  outputCollector.emit("precedenceMap",new Values(relationList));
		  }
		  
		  else {
			  pair=tuple.getStringByField("pair");
				relation=tuple.getStringByField("relation");
				
				if(!relationList.containsKey(pair)) {
					relationList.put(pair,new ArrayList<String>());
					relationList.get(pair).add(relation);
				}
				else {
					list=relationList.get(pair);
					if(!list.contains(relation)) {
						relationList.get(pair).add(relation);
					}
				}    
		  }
	  }
	  
	  private boolean isTickTuple(Tuple tuple) {
		    String sourceComponent = tuple.getSourceComponent();
		    String sourceStreamId = tuple.getSourceStreamId();
		    return sourceComponent.equals(Constants.SYSTEM_COMPONENT_ID)
		        && sourceStreamId.equals(Constants.SYSTEM_TICK_STREAM_ID);
		  }
	  
}

