import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import topology.Checkins;
import topology.*;

public class LocalTopologyRunner {
	private static final int TEN_MINUTES = 600000;
		public static void main(String[] args) {
			TopologyBuilder builder = new TopologyBuilder();

			builder.setSpout("traceListener", new Checkins());
			builder.setBolt("activitiesExtractor",new ActivitiesExtractor()).shuffleGrouping("traceListener", "activities");

			Config config = new Config();
			config.setDebug(true);

			StormTopology topology = builder.createTopology();

			try{
			LocalCluster cluster = new LocalCluster();
			
				cluster.submitTopology("github-commit-count-topology",
						config,
						topology);

				Utils.sleep(TEN_MINUTES);
				cluster.killTopology("github-commit-count-topology");
				cluster.shutdown();
			}catch (Exception e) {
				System.out.println("storm couldnt starup for some reason");
			}
	  }
}
