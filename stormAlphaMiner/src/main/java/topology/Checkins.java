package topology;

//storm packages
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.commons.io.IOUtils;

import com.rabbitmq.client.AMQP;
//rabbitmq packages
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

//java packages
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
//Underscore
import com.github.underscore.*;

public class Checkins extends BaseRichSpout {
	private List<String> checkins;
	private int nextEmitIndex;
	private SpoutOutputCollector outputCollector;
	private DefaultConsumer consumer;
	
	private final static String QUEUE_NAME = "hello";
    ConnectionFactory factory;
    Connection connection;
    Channel channel;
	
    LinkedList<String> traces;
    
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("activities",new Fields("trace"));
		declarer.declareStream("XY",new Fields("trace"));
		declarer.declareStream("mapper",new Fields("trace"));
	}
	
	public void open(Map map,
			  TopologyContext topologyContext,
              SpoutOutputCollector spoutOutputCollector){
		this.outputCollector = spoutOutputCollector;
		this.nextEmitIndex = 0;
			  traces=new LinkedList<String>();

			
		try {
			
	        factory = new ConnectionFactory();
	        factory.setHost("localhost");
	        connection = factory.newConnection();
	        channel = connection.createChannel();
	        channel.basicQos(10);
	        consumer=new DefaultConsumer(channel) {
	        	public void handleDelivery(String consumerTag, Envelope envelope ,
	        			AMQP.BasicProperties properties, byte[] body) throws IOException {
	        		String traceStrRaw=new String(body, "UTF-8");
	        		String[] traceSplit=traceStrRaw.split(",");
	        		traces.addLast(traceSplit[1]);

	        		//System.out.println("storm spout received message : "+traceStrRaw);
	        	}
	        };
	        channel.basicConsume(QUEUE_NAME, true, consumer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void nextTuple() {
		if(!traces.isEmpty()) {
			String trace=traces.poll();
			System.out.println("STORM SPOUT EMMITED : "+trace);
    		outputCollector.emit("activities",new Values(trace));
    		outputCollector.emit("XY",new Values(trace));
    		outputCollector.emit("mapper",new Values(trace));
		}
	}
}
