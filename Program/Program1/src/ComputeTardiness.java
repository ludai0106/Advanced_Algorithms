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
			int[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new int[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = sc.nextInt();
					jobs[nextJobID][1] = sc.nextInt();
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
    	
    	File folder = new File("/Users/ludai/Desktop/Github/Advanced_Algorithms/Program/Program1/src/instances");
    	File[] instances = folder.listFiles();
        System.out.println(instances.length);
//    	    for (int i = 0; i < instances.length; i++) {
//    	      if (instances[i].isFile()) {
//    	        System.out.println( instances[i].getName());
//    	      } else if (instances[i].isDirectory()) {
//    	        System.out.println( instances[i].getName());
//    	      }
//    	    }

    	String directory = "/Users/ludai/Desktop/Github/Advanced_Algorithms/Program/Program1/src/instances/";
    	String filename = directory +instances[3].getName();
    	System.out.println(filename);
		ProblemInstance instance = readInstance(filename);
		
		Greedy greedy = new Greedy(instance);
		Schedule greedySchedule = greedy.getSchedule();
		System.out.println(greedySchedule.getTardiness());
		
		BestFirst bestFirst = new BestFirst(instance);
		Schedule bestFirstSchedule = bestFirst.getSchedule();
		System.out.println(bestFirstSchedule.getTardiness());

	}
}
