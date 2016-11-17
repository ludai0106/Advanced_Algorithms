package lu.ethan;

import java.util.ArrayList;

public class Schedule implements Comparable<Schedule> {
	// A linked-list is a reletively efficient representation of a schedule
	// Feel free to modify it if you feel there exists a better one
	// The main advantage is that in a search-tree there is a lot of overlap
	// between schedules, this implementation stores this overlap only once
	private Schedule previous;
	private int jobID;
	private double jobLength;
	
	// tardiness can be calculated instead of memorized
	// however, we need to calculate it a lot, so we momorize it
	// if memory is an issue, however, try calculating it
	private double tardiness;
	
	public Schedule(){
		this.previous = null;
		this.jobID = -1;
		this.jobLength = 0;
		this.tardiness = 0;
	}
	
	// add an additional job to the schedule
	public Schedule(Schedule s, int jobID, double jobLength, double jobDueTime){
		this.previous = s;
		this.jobID = jobID;
		this.jobLength = jobLength;
		this.tardiness = Math.max(0, getTotalTime() - jobDueTime);
		
		if(previous != null) {
			this.tardiness += previous.getTardiness();
		}
	}
	
	// used by the best-first search
	// currently, schedules are traversed in smallest total tardiness order

	public int compareTo(Schedule o){
		return (int) (getTardiness() - o.getTardiness());
		
		// replace with the following to get a depth-first search
		// return get_depth() - o.get_depth();
	}
	public double getMaxT(Schedule o){
		return getTardiness() - o.getTardiness();

	}
	public int getDepth(){
		int depth = 1;
		if(previous != null) depth += previous.getDepth();
		return depth;
	}
	
	public ArrayList getJobs(ArrayList jobOrder){
		if(previous != null) {previous.getJobs(jobOrder); jobOrder.add(this.jobID); return jobOrder; }
		else { jobOrder.add(jobID);return jobOrder;}

	}
	public int getJob(){
		return jobID;
	}

	public double getTotalTime(){
		double time = jobLength;
		if(previous != null) time += previous.getTotalTime();
		return time;
	}
	
	public double getTardiness(){
		return tardiness;
	}
	
	public boolean containsJob(int job){
		return (jobID == job) || (previous != null && previous.containsJob(job));
	}

	public void getOpt(){
		if (previous!=null) { previous.getOpt();}
		System.out.print(jobID + " ");
	}

//	public Schedule update_front(Schedule s,ArrayList jobs){
//		ArrayList<Integer> job_list = jobs;
//		System.out.print(job_list);
//		System.out.println(" jobID " + jobID);
//		job_list.remove((Integer)jobID);
//		//System.out.println(job_list);
//		if (job_list.size()==1 && jobID==job_list.get(0)) return new Schedule(s,jobID,jobLength,)
//		else previous.update_front(s,job_list);
//	}
}
