package lu.ethan;

import java.util.*;

public class Exact {
    private int numJobs;
    private double[][] jobs;
    private Map<Double, Map<Joblist, ArrayList<Integer>>> cache;


    public Exact(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        jobs = instance.getJobs();
        cache = new HashMap<>();
    }
    public Exact (int numJobs,double[][] scaled_jobs){
        this.numJobs = numJobs;
        jobs=scaled_jobs;
        cache = new HashMap<>();
    }



    private boolean cacheContainskey(double startTime, Joblist jobs) {
        return cache.containsKey(startTime) && cache.get(startTime).containsKey(jobs);
    }

    private ArrayList<Integer> cacheGetschedule(double startTime, Joblist jobs) {
        return cache.get(startTime).get(jobs);
    }

    private void putCache(double start_time, Joblist jobs, ArrayList<Integer> schedule) {
        cache.putIfAbsent(start_time,new HashMap<Joblist, ArrayList<Integer>>());
        cache.get(start_time).put(jobs, schedule);
    }

    public int max_process_time(ArrayList<Integer> joblist){
        double max = -1;
        int index = -1;
        for (int i = 0; i<joblist.size(); i++) {
            if (jobs[joblist.get(i)][0] >= max){max=jobs[joblist.get(i)][0];index=joblist.get(i);}
        }
        return index;
    }


    public double sum_process_time(ArrayList<Integer> joblist, double d_k, double start_time){
        double sum = start_time;
        for (int i=0; i<joblist.size();i++){
            if (jobs[joblist.get(i)][1]<=d_k) sum = sum + jobs[joblist.get(i)][0];
        }
        return sum;
    }

    public int front_largest(ArrayList<Integer> joblist, double d_k){
        int last_index=joblist.get(0);
        for(int i=0;i<joblist.size();i++){
            if (jobs[joblist.get(i)][1] > d_k) return last_index;
            last_index = joblist.get(i);
        }
        return last_index;
    }

    public int back_smallest(ArrayList<Integer> joblist, double d_k){
        for(int i=0;i<joblist.size();i++){
            if (jobs[joblist.get(i)][1]>d_k)return joblist.get(i);
        }
        return -1;
    }

    public ArrayList<Integer> getSigma(ArrayList<Integer> joblist, int k, double start_time){

        ArrayList<Integer> sigmaList=new ArrayList();
        double due_k = jobs[k][1];

        while(true){
            double d_k_prime = sum_process_time(joblist,jobs[k][1],start_time);
            if(d_k_prime > jobs[k][1]){jobs[k][1] = d_k_prime;continue;}

            int sigma = front_largest(joblist,jobs[k][1]);
            sigmaList.add(sigma);

            int smallest_back = back_smallest(joblist,jobs[k][1]);
            if (smallest_back == -1)break;
            else {jobs[k][1]=jobs[smallest_back][1];}
        }

        jobs[k][1] = due_k;
        return sigmaList;
    }

    public Schedule getSchedule(){

        //sort jobs in non-decreasing order by due time
        Arrays.sort(jobs, new Comparator<double[]>() {
            public int compare(double[] job1, double[] job2) {
                if(job1[1]==job2[1])return  Double.compare(job1[0],job2[0]);
                else return Double.compare(job1[1], job2[1]);
            }
        });

        ArrayList<Integer> all_jobs = new ArrayList<>();
        for (int i = 0; i < numJobs; i++) all_jobs.add(i);
        Joblist job_list = new Joblist(0,all_jobs,numJobs,numJobs);


        return getSchedule(null, job_list, 0);
    }


    private Schedule getSchedule(Schedule s, Joblist job_list, double start_time){
        if(job_list.numJobs()==0)return s;
        if (job_list.numJobs()==1)
            return new Schedule(s,job_list.getIndex(),jobs[job_list.getIndex()][0],jobs[job_list.getIndex()][1]);
        if (cacheContainskey(start_time,job_list)){
            ArrayList<Integer> opts = cacheGetschedule(start_time,job_list);
            Schedule store = s;
            for (int each : opts){
                store = new Schedule(store,each,jobs[each][0],jobs[each][1]);
            }
            return store;
        }


        int k = max_process_time(job_list.getJoblist());
        ArrayList<Integer> sigma_list = getSigma(job_list.getJoblist(), k, start_time);

        Schedule opt=null;
        //System.out.println(job_list.getJoblist());
        //System.out.println(k);

        for (int t=0;t<sigma_list.size();t++){
            int sigma = sigma_list.get(t);
            //System.out.print(sigma + "|| ");
            Joblist front_list = new Joblist(0,job_list.getJoblist(),sigma,k);
            Schedule front = getSchedule(s,front_list,start_time);
            //System.out.print(front_list.getJoblist());

            //System.out.print(" || ");

            Schedule s_k = new Schedule(front,k,jobs[k][0],jobs[k][1]);

            Joblist back_list =  new Joblist(1,job_list.getJoblist(),sigma,k);
            Schedule back = getSchedule(s_k,back_list,s_k.getTotalTime());
            //System.out.println(back_list.getJoblist());

            if (opt==null) opt = back;
            if (back.compareTo(opt)<0) opt = back;
            if (back.getTardiness()==0)break;
        }

        ArrayList<Integer> optlist = new ArrayList<Integer>(job_list.getJobsize());
        optlist.addAll(job_list.getJoblist());
        ArrayList<Integer> opt_list = opt.getOptlist(optlist);

        putCache(start_time,job_list,opt_list);

        return opt;
    }
}