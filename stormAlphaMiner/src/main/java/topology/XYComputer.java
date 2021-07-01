package topology;

//guru imports


import static guru.nidi.graphviz.engine.Format.SVG;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import guru.nidi.graphviz.*;
import guru.nidi.graphviz.parse.*;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.service.*;

import static guru.nidi.graphviz.engine.Format.SVG;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;



import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
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

import com.google.common.collect.Sets;

public class XYComputer extends BaseBasicBolt {
	
	String type;
	String first;
	String second;
	String pair;
	String relation;
	String[] pairParts;
	
	int i=0;
	Set<String> keys;
	
	List<String> list;
	List<String> Ti;
	List<String> To;	
	List<String> activities;
	
	Map<String,String> causalityRelations;
	Map<String,Map<String,String>> causalityMatrix;
	Map<String,String> mapSmall;
	Set<String> activitiesSet;
	Set<Set<String>> powerset;
	List<List<String>> powersetAsList;
	List<List<String>> nonInterConnectedPowerset;
	List<String> tempList;
	
	//making X vars
	List<List<List<String>>> powersetPairs;
	List<List<String>> subset;
	List<List<String>> subset2;
	List<List<List<String>>> X;
	List<String> firstSet;
	List<String> secondSet;
	List<String> firstSet2;
	List<String> secondSet2;
	
	//making Y vars
	List<List<List<String>>> Y;
 	@Override
	  public void prepare(Map config,
	                      TopologyContext context) {
		causalityRelations=new HashMap<String,String>();
		Ti=new ArrayList<String>();
		To=new ArrayList<String>();
		activities=new ArrayList<String>();
		causalityMatrix=new HashMap<String,Map<String,String>>();
		activitiesSet=new HashSet<String>();

		//preparing for X vars

		
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
					for(String activity : activities) {
						
						mapSmall=new HashMap<String,String>();
						for(String activity2 : activities) {
							mapSmall.put(activity2, "#");
						}
						causalityMatrix.put(activity, mapSmall);
					}
					
					for (String rawPair : causalityRelations.keySet()) {
						
						pairParts=rawPair.split(",");
						causalityMatrix.get(pairParts[0]).put(pairParts[1], causalityRelations.get(rawPair));
						
					}
					
					Utilities.printMap3(causalityMatrix, "causalityMatrix.txt");
					Utilities.printSetSet(powerset, "powerSetTest.txt");
					
					//clean power set of interconnected subsets
					nonInterConnectedPowerset=new ArrayList<>();
					boolean validSubset;
					String activity;
					List<String> subsetTemp;
					for(int i=0;i<powersetAsList.size();i+=1) {
						subsetTemp=powersetAsList.get(i);
						validSubset=true;
						for(int j=0;j<subsetTemp.size();j+=1) {
							if(validSubset==false)
								break;
							activity=subsetTemp.get(j);
							for(int k=0;k<subsetTemp.size();k+=1) {
								if(validSubset==false)
									break;
								if(!causalityMatrix.get(activity).get(subsetTemp.get(k)).equals("#"))
									validSubset=false;
							}
						}
						if(validSubset==true) {
							nonInterConnectedPowerset.add(subsetTemp);
						}
					}
					Utilities.printList(nonInterConnectedPowerset, "nonIntrConnected.txt");
					//calculating X
						//get subset pairs
					powersetPairs=new ArrayList<>();
					
					for(int i=0;i<nonInterConnectedPowerset.size();i+=1) {
						for(int j=0;j<nonInterConnectedPowerset.size();j+=1) {
								if(i!=j) {
									subset=new ArrayList<>();
									subset.add(nonInterConnectedPowerset.get(i));
									subset.add(nonInterConnectedPowerset.get(j));
									powersetPairs.add(subset);	
								}
						}
					}
					
					Utilities.printListListList(powersetPairs, "powersetPairs"+i+".txt");
					
					//start populating X
					X=new ArrayList<>();
					boolean validPair;
					
					for(int i=0;i<powersetPairs.size();i+=1) {
						subset=powersetPairs.get(i);
						firstSet=subset.get(0);
						secondSet=subset.get(1);
						validPair=true;
						for(String activityFirst : firstSet) {
							if(validPair==false)
								break;
							for(String activitySecond : secondSet) {
								if(validPair==false)
									break;
								String relationFS=causalityMatrix.get(activityFirst).get(activitySecond);
								if(!(relationFS.equals("->") || relationFS.equals("||"))) {
									validPair=false;
								}
							}
							
						}
						if(validPair==true) {
							X.add(subset);
						}
					}
					//done computing X
					//print it
					Utilities.printListListList(X, "XL"+i+".txt");
					
					//compute Y
					Y=new ArrayList<>();
					for(int i=0;i<X.size();i+=1) {
						subset=X.get(i);
						firstSet=subset.get(0);
						secondSet=subset.get(1);
						validPair=true;
						for(int j=0;j<X.size();j+=1) {
							if(i!=j) {
								subset2=X.get(j);
								firstSet2=subset2.get(0);
								secondSet2=subset2.get(1);
								if(Utilities.subset(firstSet, firstSet2)==true) {
									if(Utilities.subset(secondSet, secondSet2)==true) {
										validPair=false;
									}
								}
							}
						}
						if(validPair==true) {
							Y.add(subset);
						}
					}
					
					//donecomputing Y
					//print Y here
					Utilities.printListListList(Y, "Y.txt");
					
					//drawing the graph
					List<String> places=new ArrayList<String>();
					places.add("start");
					for(int i=0;i<Y.size();i+=1) {
						places.add("c"+String.valueOf(i));
					}
					places.add("end");
					
					//set of edges
					List<String> edges=new ArrayList<String>();
					for(int i=0;i<Y.size();i+=1) {
						subset=Y.get(i);
						firstSet=subset.get(0);
						secondSet=subset.get(1);
						for(String act : firstSet) {
							String edge=act.replace(' ', '_');
							edge=edge+","+"c"+String.valueOf(i);
							edges.add(edge);
						}
						for(String act : secondSet) {
							String edge="c"+String.valueOf(i);
							edge=edge+","+act.replace(' ', '_');
							edges.add(edge);
						}
						
					}
					for(String ti : Ti) {
						edges.add("start"+","+ti.replace(' ', '_'));
					}
					
					for(String to : To) {
						edges.add(to.replace(' ', '_')+","+"end");
					}
					
					//remove spaces from activity names
					
					List<String> activities_no_spaces=new ArrayList<String>();
					for(String act : activities) {
						activities_no_spaces.add(act.replace(' ','_'));
					}
					
					//remove spaces from the places pairs
					
					
					
					
					Utilities.printMap4(edges, "edges.txt");
					i++;
					String fname="";
					fname+="mygraph";
					fname+=String.valueOf(i);
					fname+=".dot";
					Utilities.printGraph(edges, activities_no_spaces, places, fname);
					
					//make the graph.png file
					
					try (InputStream dot = new FileInputStream(fname)) {
						String fname2="";
						fname2+="mygraph";
						fname2+=String.valueOf(i);
						fname2+=".png";
					    MutableGraph g = new Parser().read(dot);
					    Graphviz.fromGraph(g).width(2000).render(Format.PNG).toFile(new File("example/"+fname2));


					}
					catch(IOException e) {	
						
					}
					
					
					
					
					//ends here
					
					
					
			  }
		  }
		  
		  else {
			
			type=tuple.getStringByField("type");
			if(type.equals("activities")) {
				activities=(List<String>) tuple.getValueByField("activities");
				for(String activity : activities) {
					activitiesSet.add(activity);
				}
				powerset=Sets.powerSet(activitiesSet);
				
				//convert powerset from set to list
				powersetAsList=new ArrayList<>();
				for(Set<String> set : powerset) {
					tempList=new ArrayList<String>();
					for(String str : set) {
						tempList.add(str);
					}
					powersetAsList.add(tempList);
				}
				
				//delete emptyset (good lord help me)
				for(int i=0;i<powersetAsList.size();i+=1) {
					tempList=powersetAsList.get(i);
					if (tempList.isEmpty())
						powersetAsList.remove(i);
				}
				
				Utilities.printList(powersetAsList, "powersetAsList.txt");
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
