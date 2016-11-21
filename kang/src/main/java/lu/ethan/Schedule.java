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


	public int compareTo(Schedule o){
		return (int) (getTardiness() - o.getTardiness());
		
		// replace with the following to get a depth-first search
		// return get_depth() - o.get_depth();
	}

	public int getDepth(){
		int depth = 1;
		if(previous != null) depth += previous.getDepth();
		return depth;
	}
	
	public ArrayList<Integer> getJobs(ArrayList<Integer> jobOrder){
		if(previous != null) {previous.getJobs(jobOrder); jobOrder.add(this.jobID); return jobOrder; }
		else { jobOrder.add(jobID);return jobOrder;}

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




	public void ouput_list(){
		if (previous!=null)previous.ouput_list();
		System.out.print(jobID + "-");
	}



	public ArrayList<Integer> getOptlist (ArrayList<Integer> jobs){
		ArrayList<Integer> opt_list = new ArrayList<Integer>();
		if (previous!=null){
			if (jobs.size()<1)return opt_list;
			jobs.remove(new Integer(jobID));
			opt_list=previous.getOptlist(jobs);
			opt_list.add(jobID);
		}
		else {if(jobs.contains(jobID))opt_list.add(jobID);}
		return opt_list;
	}

	public double max_tardiness(){
		if (previous!=null)return Math.max(tardiness-previous.getTardiness(),previous.max_tardiness());
		else return tardiness;

	}
	public ArrayList<Double> all_tardiness(){
		ArrayList<Double> list = new ArrayList<>();
		if (previous!=null){list.addAll(previous.all_tardiness());list.add(tardiness-previous.getTardiness());return list;}
		else {list.add(tardiness);return list;}
	}

}
