package com.datatorrent.lib.samplecode.algo;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.api.Context.OperatorContext;
import com.datatorrent.lib.algo.AllAfterMatchMap;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.lib.samplecode.math.RandomKeyValMap;

/**
 * This sample application code for showing sample usage of malhar operator(s). <br>
 * <b>Operator : </b> AverageMap <br>
 * <bClass : </b> com.datatorrent.lib.math.AverageMap
 *
 * @author Dinesh Prasad (dinesh@malhar-inc.com)
 */
public class AllAfterMatchMapSample implements StreamingApplication
{
	@SuppressWarnings("unchecked")
	@Override
	public void populateDAG(DAG dag, Configuration conf)
	{
		// Create application dag.
		dag.setAttribute(DAG.APPLICATION_NAME, "AllAfterMatchMapSample");
		//dag.setAttribute(DAG.DEBUG, true);

		// Add random integer generator operator
		RandomKeyValMap rand = dag.addOperator("rand", RandomKeyValMap.class);

		AllAfterMatchMap<String, Integer> allafter = dag.addOperator("average",
				AllAfterMatchMap.class);
		allafter.setValue(50);
		allafter.setTypeLTE();
		dag.addStream("stream1", rand.outport, allafter.data);
		dag.getMeta(allafter).getAttributes()
				.attr(OperatorContext.APPLICATION_WINDOW_COUNT).set(20);

		// Connect to output console operator
		ConsoleOutputOperator console = dag.addOperator("console",
				new ConsoleOutputOperator());
		dag.addStream("consolestream", allafter.allafter, console.input);

		// done
	}
}
