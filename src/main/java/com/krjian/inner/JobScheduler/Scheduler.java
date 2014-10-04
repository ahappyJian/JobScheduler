package com.krjian.inner.JobScheduler;

import com.krjian.inner.DAG.DAGGraph;
import com.krjian.inner.DAG.DAGNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by zhujian on 10/4/14.
 */
public class Scheduler {
    private HashMap<String, DAGNode> nodeInfo = null;
    private DAGGraph graph = null;
    private LinkedList<DAGNode> [] depthArray;
    private Integer maxDepth = 0;
    public Scheduler(HashMap<String, DAGNode> nodeInfo, DAGGraph graph){
        this.nodeInfo = nodeInfo;
        this.graph = graph;
        calcNodeDepth(nodeInfo.get("start"), 0);
        depthArray = new LinkedList[maxDepth+1];
        for(int i = 0; i <= maxDepth; i++){
            depthArray[i] = new LinkedList<DAGNode>();
        }
        nodeInfo.get("start").unVisitedAll();
        arrangeNodeByDepth(nodeInfo.get("start"));
    }

    public void execute() throws Exception{
        for(int i = 0; i <= maxDepth; i++){
            for(DAGNode node: depthArray[i]){
                if(node.getName().equals("start")){
                    System.out.println("Job Starts...");
                }else if(node.getName().equals("end")){
                    System.out.println("Job Ends");
                }else {
                    Job job = (Job) (node.getData());
                    job.run();
                }
            }
        }
    }

    private void calcNodeDepth(DAGNode node, Integer depth){
        if(node.getDepth() < depth){
            node.setDepth(depth);
        }
        maxDepth = maxDepth < depth ? depth : maxDepth;
        for(DAGNode child: node.getChildren()){
            calcNodeDepth(child, depth+1);
        }
    }
    private void arrangeNodeByDepth(DAGNode node){
        if(node.isVisited()){ return; }
        node.setVisited(true);
        depthArray[node.getDepth()].add(node);
        for(DAGNode child: node.getChildren()){
            arrangeNodeByDepth(child);
        }
    }
}
