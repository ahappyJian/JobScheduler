package com.krjian.inner.JobScheduler;

import com.krjian.inner.DAG.DAGGraph;
import com.krjian.inner.DAG.DAGNode;

/**
 * Created by zhujian on 10/4/14.
 */
public class SimpleMain {
    public static void main(String[] args) throws Exception{
        String fileName = "job.def";
        JobInfoParser parser = new SimpleParser();
        parser.parse(fileName);
        DAGGraph graph = parser.getDAGGraph();
        buildJob(graph);
        //
        Scheduler runner = new Scheduler(parser.getNodeInfo(), graph);
        runner.execute();
    }

    private static void buildJob(DAGGraph graph){
        DAGNode start = graph.getGraph();
        start.unVisitedAll();
        formatJob(start);
    }
    private static void formatJob(DAGNode node){
        if(node.isVisited()){ return; }
        node.setVisited(true);
        //
        Integer data = (Integer)node.getData();
        Job job = new SimpleJob(node.getName(), data);
        node.setData(job);
        for(DAGNode child: node.getChildren()){
            formatJob(child);
        }
    }
}
