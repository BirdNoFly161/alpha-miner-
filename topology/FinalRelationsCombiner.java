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

public class FinalRelationsCombiner extends BaseBasicBolt {

	
	String pair;
	String relation;
	
	Map<String,List<String>> relationList;
	Map<String,List<String>> finalRelationList;
	Map<String,String> causalityRelation;
	List<String> relations;
	List<String> relationsFinal;
	Set<String> keys;
	Set<String> keysFinal;
	List<String> list;
	List<String> uniqueRelations;
	
	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {
		finalRelationList=new HashMap<String,List<String>>();
	  }
	
	 public void declareOutputFields(OutputFieldsDeclarer declarer) {
		    declarer.declareStream("causalityRelations",new Fields("type","map"));
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
			  //Utilities.printMap(finalRelationList, "mymapTransmittedFinal.txt");
			  causalityRelation=new HashMap<String,String>();
			  keysFinal=finalRelationList.keySet();
			  for(String key : keysFinal) {
				uniqueRelations=finalRelationList.get(key);
				if(uniqueRelations.contains("<") && uniqueRelations.contains(">"))
					causalityRelation.put(key, "||");
				else {
					if(uniqueRelations.contains("<") && !uniqueRelations.contains(">"))
						causalityRelation.put(key, "->");
					else {
						if(!uniqueRelations.contains("<") && uniqueRelations.contains(">"))
							causalityRelation.put(key, "<-");
					}
				}
			  }
			  
			  //print here	
			  //Utilities.printMap2(causalityRelation, "causalityRelations.txt");
			  outputCollector.emit("causalityRelations",new Values("causality",causalityRelation));
			  
		  }
		  
		  else {
			  relationList=(Map<String,List<String>>) tuple.getValueByField("map");
			  keys=relationList.keySet();
			  for(String key : keys) {
				  if(!finalRelationList.containsKey(key)) {
					  finalRelationList.put(key, new ArrayList<String>());
					  finalRelationList.get(key).addAll(relationList.get(key));
				  }
				  else {
					  relations=relationList.get(key);
					  relationsFinal=finalRelationList.get(key);
					  for(String rel : relations) {
						  if(!relationsFinal.contains(rel)) {
							  finalRelationList.get(key).add(rel);
						  }
					  }
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
