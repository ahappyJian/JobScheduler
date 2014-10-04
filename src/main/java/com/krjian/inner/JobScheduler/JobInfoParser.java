package com.krjian.inner.JobScheduler;

import com.krjian.inner.DAG.DAGGraph;
import com.krjian.inner.DAG.DAGNode;

import java.util.HashMap;

/**
 * Created by zhujian on 10/4/14.
 */
public interface JobInfoParser {
    public void parse(String str)throws Exception;
    public HashMap<String, DAGNode> getNodeInfo();
    public DAGGraph getDAGGraph();
}
