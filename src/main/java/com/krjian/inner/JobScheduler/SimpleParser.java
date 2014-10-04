package com.krjian.inner.JobScheduler;

import com.krjian.inner.DAG.DAGGraph;
import com.krjian.inner.DAG.DAGNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhujian on 10/4/14.
 */
public class SimpleParser implements JobInfoParser {
    private HashMap<String, DAGNode> nodeInfo = new HashMap<String, DAGNode>();
    private DAGGraph dag = new DAGGraph();

    @Override
    public void parse(String str) throws Exception {
        LinkedList<String> lines = parseLines(str);
        nodeInfo.clear();
        dag.clear();

        for(String one: lines){
            String line = one.trim().replaceAll("\\s+", "");
            if(line.length() == 0){ continue; }
            if(line.matches("^(\\w+)\\((\\d+)\\)=>[\\w:]+$")){
                parseLine(line);
            }else if(line.matches("^start=>[\\w:]+$")){
                parseStart(line);
            }else {
                throw new RuntimeException("bad job define:" + line);
            }
        }
        validate();
        dag.setGraph(nodeInfo.get("start"));
    }

    @Override
    public HashMap<String, DAGNode> getNodeInfo() {
        return nodeInfo;
    }

    @Override
    public DAGGraph getDAGGraph() {
        return dag;
    }

    private void parseStart(String line){
        Pattern pattern = Pattern.compile("^start=>([\\w:]+)$");
        Matcher m = pattern.matcher(line);
        if(!m.find()){
            throw new RuntimeException("bad job define:" + line);
        }
        if(nodeInfo.get("start") != null){
            throw new RuntimeException("has multiple start cmd line:" + line);
        }
        DAGNode startNode = new DAGNode("start");
        String nodeStr = m.group(1);
        String[] nodes = nodeStr.split(":", -1);
        HashSet<String> set = new HashSet<String>();
        for(String nodeName: nodes){
            if(nodeName.length() == 0){
                throw new RuntimeException("bad job define:" + line + ", encounter ::");
            }
            if(false == set.add(nodeName)){
                throw new RuntimeException("multiple child node name:" + nodeName);
            }
            DAGNode childNode = mustGetNode(nodeName);
            startNode.addChild(childNode);
        }
        nodeInfo.put(startNode.getName(), startNode);
    }

    private void parseLine(String line){
        Pattern pattern = Pattern.compile("^(\\w+)\\((\\d+)\\)=>([\\w:]+)$");
        Matcher m = pattern.matcher(line);
        if(!m.find()){
            throw new RuntimeException("bad job define:" + line);
        }
        String leftNodeName = m.group(1);
        String nodeData = m.group(2);
        String nodeStr = m.group(3);
        DAGNode leftNode = mustGetNode(leftNodeName);
        leftNode.setData(Integer.valueOf(nodeData));
        if(leftNode.getChildren().size() != 0){
            throw new RuntimeException("multiple line define for:" + leftNodeName);
        }
        String[] nodes = nodeStr.split(":", -1);
        HashSet<String> set = new HashSet<String>();
        for(String nodeName: nodes){
            if(nodeName.length() == 0){
                throw new RuntimeException("bad job define:" + line + ", encounter ::");
            }
            if(false == set.add(nodeName)){
                throw new RuntimeException("multiple child node name:" + nodeName);
            }
            DAGNode childNode = mustGetNode(nodeName);
            leftNode.addChild(childNode);
        }
    }

    private DAGNode mustGetNode(String nodeName){
        DAGNode node = nodeInfo.get(nodeName);
        if(node == null){
            node = new DAGNode(nodeName);
            nodeInfo.put(nodeName, node);
        }
        return node;
    }
    private void validate(){
        if(nodeInfo.get("start") == null){ throw new RuntimeException("No start Node in job define"); }
        if(nodeInfo.get("end") == null){ throw new RuntimeException("No end Node in job define"); }
        visitDag(nodeInfo.get("start"));
        Iterator iter = nodeInfo.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            DAGNode node = (DAGNode)(entry.getValue());
            if(node.isVisited() == false){
                throw new RuntimeException("Node can't be reached from start node:" + node.getName());
            }
        }
        nodeInfo.get("start").unVisitedAll();
    }
    public void visitDag(DAGNode node){
        if(node.isVisited() == true){ return; }
        node.setVisited(true);
        if(node.getChildren().size() == 0 &&
                node.getName().equals("end") == false){
            throw new RuntimeException("Node can't reach end node:" + node.getName());
        }
        for(DAGNode one: node.getChildren()){
            visitDag(one);
        }
    }
    private LinkedList<String> parseLines(String fileName) throws Exception{
        File jobFile = new File(fileName);
        FileReader fr = null;
        BufferedReader br = null;
        LinkedList<String> lineList = new LinkedList<String>();
        try {
            fr = new FileReader(jobFile);
            br = new BufferedReader(fr);
            String line = null;
            while((line = br.readLine()) != null){
                lineList.add(line);
            }
        }finally {
            if(fr != null ) { fr.close(); }
            if(br != null) { br.close(); }
        }
        return lineList;
    }
}
