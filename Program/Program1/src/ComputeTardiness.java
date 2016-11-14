import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.FileVisitResult.*;
import java.util.stream.Stream;


public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			double[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new double[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = (double)sc.nextInt();
					jobs[nextJobID][1] = (double)sc.nextInt();
					nextJobID++;
				}
			}
			sc.close();
			
			instance = new ProblemInstance(numJobs, jobs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return instance;
	}

	
	
	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) throws IOException {

		if (args.length == 2) {
			int epsilon = Integer.parseInt(args[0]);
			String path = args[1];
			ProblemInstance instance = readInstance(path);
			Exact exact = new Exact(instance);
			Schedule exactSchedule = exact.getSchedule();
			String output = String.format("%d %d", exactSchedule.getTardiness(), exactSchedule.getTardiness() * 2);
			System.out.println(output);
		}

		else {
			String directory = "/Users/ludai/Desktop/Github/Advanced_Algorithms/Program/Program1/src/instances/";

			File folder = new File(directory);
			File[] instances = folder.listFiles();
			System.out.println(instances.length);

			for (int i = 0; i < instances.length; i++) {
			    if (instances[i].isFile()) {
					String filename = directory + instances[i].getName();
					//System.out.println( instances[i].getName());

					ProblemInstance instance = readInstance(filename);

					System.out.println(filename);

//					Greedy greedy = new Greedy(instance);
//					Schedule greedySchedule = greedy.getSchedule();
//					System.out.println("Greedy_Tardiness: " + greedySchedule.getTardiness());


					Exact exact = new Exact(instance);
					Schedule exactSchedule = exact.getSchedule();
					System.out.println("Exact_Tardiness : " + exactSchedule.getTardiness());

//					Approximation approximation = new Approximation(instance);
//					Schedule approximationSchedule = approximation.getSchedule();
//					System.out.println("Approximation_Tardiness : " + approximationSchedule.getTardiness());





			  }
			  else if (instances[i].isDirectory()) {System.out.println( instances[i].getName());}
			}



			//

			//
			//		BestFirst bestFirst = new BestFirst(instance);
			//		Schedule bestFirstSchedule = bestFirst.getSchedule();
			//		System.out.println(bestFirstSchedule.getTardiness());


		}
		
	}
}
