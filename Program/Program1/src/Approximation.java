import java.util.ArrayList;


public class Approximation {
	private int numJobs;
	private double[][] jobs;
	private double [][] scaled_jobs;
	private double Tmax=-1;
	public double epsilon=100;
	public Approximation(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
		scaled_jobs=new double[numJobs][];
		for (int i=0;i<numJobs;++i)
		{
			scaled_jobs[i]=new double[2];
		}
		for (int i=0;i<numJobs;++i)
		{
			scaled_jobs[i][0]=(double)jobs[i][0];
			scaled_jobs[i][1]=(double)jobs[i][1];
		}
	}
	
	// returns the earliest deadline first schedule
	// sorting is a little quicker, but then it is more tricky
	// to use this as a subroutine for a search method
	public Schedule getSchedule() {
		int jobID = -1;
		int jobLength = -1;
		double jobDueTime = -1;
		
		for(int i = 0; i < numJobs; ++i){
			if(jobDueTime == -1 || jobDueTime > scaled_jobs[i][1]){
				jobID = i;
				jobLength = (int)scaled_jobs[i][0];
				jobDueTime = scaled_jobs[i][1];
			}
		}
		Schedule s =getSchedule(new Schedule(null, jobID, jobLength, jobDueTime));
		if (Tmax==0){
			return s;
		}
		else{
			double k=Tmax*2*epsilon/(numJobs*(numJobs+1));
			updateJobs(k);
			Exact exact = new Exact(numJobs,scaled_jobs);
			Schedule s_exact= exact.getSchedule();
			ArrayList jobOrder= new ArrayList();
			s_exact.getJobs(jobOrder);
			Schedule new_Schedule=new Schedule();
			for (int i=0;i<jobOrder.size();i++){
				if (i==0){
					int ID= (int)jobOrder.get(i);
					new_Schedule=new Schedule(null,ID,jobs[ID][0],jobs[ID][1]);
					
				}
				else{
					int ID= (int)jobOrder.get(i);
					new_Schedule=new Schedule(new_Schedule,ID,jobs[ID][0],jobs[ID][1]);
				}
			}
			/* Implement exact algorithm*/
			return new_Schedule;
			//return s5;
		}
			
		
		
	}
	
	private void updateJobs(double k){
		for(int i=0;i<numJobs;++i){
			scaled_jobs[i][0]=Math.floor(scaled_jobs[i][0]/k);
			scaled_jobs[i][1]=scaled_jobs[i][1]/k;
		}
	}
	// adds the next earliest deadline first job to the schedule
	private Schedule getSchedule(Schedule s){
		if(s.getDepth() >= numJobs) return s;
		
		int jobID = -1;
		int jobLength = -1;
		double jobDueTime = -1;
		
		for(int i = 0; i < numJobs; ++i){
			if(s.containsJob(i) == false && (jobDueTime == -1 || jobDueTime > scaled_jobs[i][1])){
				jobID = i;
				jobLength = (int)scaled_jobs[i][0];
				jobDueTime = scaled_jobs[i][1];
			}
		}
		Schedule s_new=new Schedule(s, jobID, jobLength, jobDueTime);
		Tmax=Double.max(s_new.getMaxT(s),Tmax);
		return getSchedule(s_new);
	}
}
