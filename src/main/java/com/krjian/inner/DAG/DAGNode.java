package com.krjian.inner.DAG;

import java.util.LinkedList;

/**
 * Created by zhujian on 10/4/14.
 */
public class DAGNode {
    private String name = "";
    private LinkedList<DAGNode> children = new LinkedList<DAGNode>();
    private Object data = null;
    private boolean visited = false;
    private Integer depth = 0;

    public DAGNode(){}
    public DAGNode(String name){
        this.name = name;
    }
    public boolean isVisited(){ return visited; }
    public String getName(){return name; }
    public Object getData(){ return data; }
    public LinkedList<DAGNode> getChildren(){ return children; }
    public void setVisited(boolean visited){ this.visited = visited; }
    public void setName(String name){ this.name = name; }
    public void setData(Object data){ this.data = data; }
    public void setChildren(LinkedList<DAGNode> children){ this.children = children; }

    public void unVisitedAll(){
        visited  = false;
        for(DAGNode node: children){
            node.unVisitedAll();
        }
    }
    public void addChild(DAGNode node){
        children.add(node);
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }
}
