import a.d.b.b.A;

import java.util.*;

public class Exact {
	private int numJobs;
	private int[][] jobs;
    private int store_size;
    ArrayList stored_start_time;
    ArrayList stored_job_list;
    ArrayList stored_schedules;

	
	public Exact(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
        store_size = 9;

	}

	
	public int maxPtime(int start, int end, int[] ptimes){
		int max = -1;
		int index = -1;
		for(int i=start; i<=end; i++){
			if(ptimes[i]>max){max=ptimes[i];index=i;}
		}
		return index;
	}
	
	//order the Processing time, the  biggest P_time is marked with the largest number
	public void sortPtime(int[] pt_order, int[][] alljobs){
		int[] ptimes = new int[alljobs.length];
		for(int i=0;i<alljobs.length;i++){ptimes[i]=alljobs[i][0];}
		for(int i=0;i<ptimes.length;i++){
			int max_index = maxPtime(0,numJobs-1,ptimes);
			pt_order[max_index] = ptimes.length-1-i;
			ptimes[max_index] = -1;
		}
	}
	
    public boolean check(int[] pt_order, int start, int end){
    	for(int i=start;i<=end;i++){
    		if(pt_order[i]!=-1)return false;
    	}
    	return true;
    }

    public int check_size(int i, int j, int[] pt_order){
        int sum=0;
        for (int count=i;count<=j;count++){
            if(pt_order[count]!=-1)sum++;
        }
        return sum;
    }

    public int sumPtime(ArrayList p_time, ArrayList d_time, int d_k, int start_time){
        int sum = start_time;
        for (int i=0; i<p_time.size();i++){
            if((int)d_time.get(i)<=d_k){sum=sum+(int)p_time.get(i);}
        }
        return sum;
    }

    public int largestFront(ArrayList d_time, int d_k){
        for (int i=0;i<d_time.size();i++){
            if ((int)d_time.get(i)>d_k)return i-1;
        }
        return (d_time.size()-1);
    }

    public int size_Backpart(ArrayList d_time, int d_k){
        for (int i=0; i<d_time.size();i++){
            if ((int)d_time.get(i)>d_k)return i;
        }
        return -1;
    }

    public ArrayList getSigma(int k, int i, int j, int[] pt_order, int start_time){
        ArrayList p_time = new ArrayList();
        ArrayList d_time = new ArrayList();
        ArrayList real_index = new ArrayList();
        while(i<=j){
            if(pt_order[i]!=-1){
                real_index.add(i);
                p_time.add(jobs[i][0]);
                d_time.add(jobs[i][1]);
            }
            i++;
        }

        int k_index = real_index.indexOf(k);


		ArrayList sigmaList=new ArrayList();


		while(true){

            int d_k_prime = sumPtime(p_time,d_time, (int)d_time.get(k_index), start_time);

            if(d_k_prime > (int)d_time.get(k_index)){d_time.set(k_index,d_k_prime);continue;}

            int sigma_index = largestFront(d_time,(int)d_time.get(k_index));


            if (sigma_index!=-1) {
                int sigma = (int)real_index.get(sigma_index)-k;
                sigmaList.add(sigma);
            }

            int back_small = size_Backpart(d_time,(int)d_time.get(k_index));


            if (back_small == -1)break;
            else {d_time.set(k_index,(int)d_time.get(back_small));}
        }

		return sigmaList;
	}
	
	public Schedule getSchedule(){	

		//System.out.println(this.numJobs);
		
		//sort jobs in non-decreasing order by due time
		Arrays.sort(jobs, new Comparator<int[]>() {
		    public int compare(int[] job1, int[] job2) {
                if(job1[1]==job2[1])return  Integer.compare(job1[0],job2[0]);
		        else return Integer.compare(job1[1], job2[1]);
		    }
		});
		
		
		//order the processing time	
		int[] pt_order = new int[numJobs];
		sortPtime(pt_order,jobs);
		
//		for(int i=0; i< this.jobs.length; i++)
//			System.out.println(jobs[i][0]+" "+jobs[i][1]+" "+ pt_order[i]);

        stored_schedules= new ArrayList();
        stored_start_time = new ArrayList();
        stored_job_list = new ArrayList();


        long startTime = System.currentTimeMillis();

        Schedule s= getSchedule(null, pt_order, 0, numJobs-1,numJobs,0);

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);

		return s;

	}
	
	private Schedule getSchedule(Schedule s, int[] pt_order, int i, int j, int k, int start_time){

		if(i>j)return null;
		if((i==j)&(i==k)) {return s;}
		if(check(pt_order,i,j)) {return s;}

        ArrayList job_list= new ArrayList();

        if (check_size(i,j,pt_order)>store_size) {

            for (int job = i; job <= j; job++) {
                if (pt_order[job] != -1) job_list.add(job);
            }

            if (stored_job_list.contains(job_list)) {
                if ((int) stored_start_time.get(stored_job_list.indexOf(job_list)) == start_time) {
                    //Schedule tmp = (Schedule) stored_schedules.get(stored_job_list.indexOf(job_list));
                    //System.out.println(job_list);
                    //System.out.println("Yes! Tardiness:" + tmp.getTardiness() + " Start_time:" + start_time);
                    return (Schedule) stored_schedules.get(stored_job_list.indexOf(job_list));
                }
            }
        }

		int k_c = maxPtime(i,j,pt_order);

		int order_c = pt_order[k_c];


				
		Map<Integer, Schedule> schedules = new HashMap();
		
		int min_index = -1;
		
        Schedule s_front = null;
        Schedule s_k = null;
        Schedule s_back = null;

        int last_tardiness = Integer.MAX_VALUE;

        ArrayList sigmaList = getSigma(k_c,i,j,pt_order,start_time);

        pt_order[k_c] = -1;



		for(int t=0; t<sigmaList.size();t++) {
            int sigma = (int) sigmaList.get(t);
            s_front = getSchedule(s, pt_order, i, k_c + sigma, k_c, start_time);
            s_k = new Schedule(s_front, k_c, jobs[k_c][0], jobs[k_c][1]);
            s_back = getSchedule(s_k, pt_order, k_c + sigma + 1, j, k_c, s_k.getTotalTime());
            if (s_back != null) {
                if (last_tardiness > s_back.getTardiness()) {
                    last_tardiness = s_back.getTardiness();min_index = sigma;
                    schedules.put(sigma, s_back);
                }
            }
            else {
                if (last_tardiness > s_k.getTardiness()) {
                    last_tardiness = s_k.getTardiness();min_index = sigma;
                    schedules.put(sigma, s_k);

                }
            }
        }
		//System.out.println(schedules.get(min_index).getDepth());
		pt_order[k_c]=order_c;
        if(check_size(i,j,pt_order)>store_size) {
            stored_job_list.add(job_list);
            stored_start_time.add(start_time);
            stored_schedules.add(schedules.get(min_index));
        }
		return schedules.get(min_index);
	}
}