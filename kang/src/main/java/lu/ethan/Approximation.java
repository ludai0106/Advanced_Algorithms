package lu.ethan;

import java.util.ArrayList;


public class Approximation {
	private int numJobs;
	private double[][] jobs;
	private double [][] scaled_jobs;
	private double Tmax;
	private double epsilon;
	private Schedule greedy_s;
	public Approximation(ProblemInstance instance, double epsilon) {
		Greedy greedy_alg = new Greedy(instance);
		greedy_s = greedy_alg.getSchedule();
		this.epsilon = epsilon;
		//System.out.print(this.epsilon);
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
		scaled_jobs=new double[numJobs][2];

	}

	public void updateJobs(double k){
		for(int i=0;i<numJobs;i++){
			scaled_jobs[i][0]=Math.floor(jobs[i][0]/k);
			scaled_jobs[i][1]=jobs[i][1]/k;
		}
	}

	public Schedule getSchedule(){
		Tmax = greedy_s.max_tardiness();

		if (Tmax ==0) {return greedy_s;}
		else{
			double k=Tmax*2*epsilon/(numJobs*(numJobs+1));
			updateJobs(k);

			Exact exact_s = new Exact(numJobs,scaled_jobs);
			Schedule s_exact = exact_s.getSchedule();

			ArrayList<Integer> jobOrder = new ArrayList<>();
			 s_exact.getJobs(jobOrder);

			Schedule new_Schedule=null;
			for (int each : jobOrder){
				new_Schedule = new Schedule(new_Schedule,each,jobs[each][0],jobs[each][1]);
			}

			return new_Schedule;
		}
	}


}
