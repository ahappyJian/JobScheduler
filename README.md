JobScheduler
============

A simple job scheduler

Three types of statements:
1, start node:
start=>node1:node2
2, normal node:
node1(12)=>node2:node3
3, end node:
node1(10)=>end
The number in the bracket is the log count that the node job will print.

An example is in the job.def file. the output to the file is as follows:
Job Starts...
job_a:0
job_a:1
job_c:0
job_c:1
job_b:0
job_b:1
job_d:0
job_d:1
job_e:0
job_e:1
Job Ends

