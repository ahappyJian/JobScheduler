package com.krjian.inner.DAG;

/**
 * Created by zhujian on 10/4/14.
 */
public class DAGGraph {
    private DAGNode start = null;

    public DAGGraph(){}

    public DAGGraph(DAGNode start){
        this.start = start;
    }
    public DAGNode getGraph(){
        return start;
    }
    public void setGraph(DAGNode start){
        this.start = start;
    }
    public void clear(){
        start = null;
    }
}
