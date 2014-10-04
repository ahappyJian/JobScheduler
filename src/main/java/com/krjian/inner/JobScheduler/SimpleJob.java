package com.krjian.inner.JobScheduler;

/**
 * Created by zhujian on 10/4/14.
 */
public class SimpleJob implements Job {
    private Integer cnt = null;
    private String name = "";
    SimpleJob(String name, Integer cnt){
        this.name = name;
        this.cnt = cnt;
    }

    @Override
    public void run() throws Exception {
        for(int i = 0; i < cnt; i++){
            System.out.println(name + ":" + i);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
