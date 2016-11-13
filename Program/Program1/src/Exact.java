import a.d.b.b.A;

import java.util.*;

public class Exact {
    private int numJobs;
    private double[][] jobs;
    private Map<Double, Map<Joblist, Schedule>> cache;

	
	public Exact(ProblemInstance instance) {
        numJobs = instance.getNumJobs();
        jobs = instance.getJobs();
        cache = new HashMap<>();
	}



    private boolean cacheContainskey(double startTime, Joblist jobs) {
        return cache.containsKey(startTime) && cache.get(startTime).containsKey(jobs);
    }

    private Schedule cacheGetschedule(double startTime, Joblist jobs) {
        return cache.get(startTime).get(jobs);
    }

    private void putCache(double start_time, Joblist jobs, Schedule schedule) {
        cache.putIfAbsent(start_time,new HashMap<Joblist, Schedule>());
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
		

//		for(int i=0; i< this.jobs.length; i++)
//			System.out.println(jobs[i][0]+" "+jobs[i][1]);
//
        long startTime = System.currentTimeMillis();

        ArrayList<Integer> all_jobs = new ArrayList<>();
        for (int i = 0; i < numJobs; i++) all_jobs.add(i);
        Joblist job_list = new Joblist(0,all_jobs,numJobs,numJobs);
        job_list.showJobs();
        //all_jobs.remove(10);
        //System.out.println(all_jobs.get(10));

        Schedule s= getSchedule(null, job_list, 0);


        // Run time & memory analysis
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);


        Performance performance = new Performance();

        Runtime runtime = Runtime.getRuntime();

        System.out.println("Run time " + runtime);

        // Run the garbage collector
        runtime.gc();

        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: " + performance.bytesToMegabytes(memory));


		return s;
	}

	
	private Schedule getSchedule(Schedule s, Joblist job_list, double start_time){
        if(job_list.numJobs()==0)return s;

        if (job_list.numJobs()==1)
            return new Schedule(s,job_list.getIndex(),jobs[job_list.getIndex()][0],jobs[job_list.getIndex()][1]);

        if (cacheContainskey(start_time,job_list)){
            //System.out.println("Lam Gao Sou");
            return cacheGetschedule(start_time,job_list);
        }

        //job_list.showJobs();

        int k = max_process_time(job_list.getJoblist());
        ArrayList<Integer> sigma_list = getSigma(job_list.getJoblist(), k, start_time);

        Schedule opt=null;

        for (int t=0;t<sigma_list.size();t++){
            int sigma = sigma_list.get(t);
            Joblist front_list = new Joblist(0,job_list.getJoblist(),sigma,k);
            Schedule front = getSchedule(s,front_list,start_time);

            Schedule s_k = new Schedule(front,k,jobs[k][0],jobs[k][1]);

            Joblist back_list =  new Joblist(1,job_list.getJoblist(),sigma,k);
            Schedule back = getSchedule(s_k,back_list,s_k.getTardiness());

            if (opt==null) opt = back;
            if (back.compareTo(opt)<0) opt = back;
        }


        putCache(start_time,job_list,opt);

        return opt;
	}
}