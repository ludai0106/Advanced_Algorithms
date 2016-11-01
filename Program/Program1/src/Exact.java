import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Exact {
	private int numJobs;
	private int[][] jobs;
	
	public Exact(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
	}


	public Schedule getSchedule(){
		Arrays.sort(jobs);
		System.out.println(this.numJobs);	
		for(int i=0; i< this.jobs.length; i++)
			System.out.println(jobs[i][0]+" "+jobs[i][1]);
		
		

		
		return null;
		
	}

	
}