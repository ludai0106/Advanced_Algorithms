
public class Exact {
	private int numJobs;
	private int[][] jobs;
	
	public Exact(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
	}
}
