package lu.ethan;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public static void main (String args[]) throws IOException, ParseException {

		if (args.length == 2) {
			int epsilon = Integer.parseInt(args[0]);
			String path = args[1];
			ProblemInstance instance = readInstance(path);
			Exact exact = new Exact(instance);
			Schedule exactSchedule = exact.getSchedule();

			Approximation approximation = new Approximation(instance, epsilon);
			Schedule approximationSchedule = approximation.getSchedule();

			String output = String.format("%d %d", (int)exactSchedule.getTardiness(), (int)approximationSchedule.getTardiness());
			System.out.println(output);
		}

		else {
			String directory = "/Users/ludai/Desktop/Github/Advanced_Algorithms/kang/src/test/java/lu/ethan/instances/";

			File folder = new File(directory);
			File[] instances = folder.listFiles();
			//System.out.println(instances.length);
			//double[] epsilon = {0.1};
			double anEpsilon = 100.0;

//			PrintWriter pw = new PrintWriter(new File("performance.csv"));
//			StringBuilder sb = new StringBuilder();
//			sb.append("size,RDD,TF,exact,epsilon,approximation,greedy,e_time,a_time,g_time\n");
//			pw.write(sb.toString());
//			pw.close();


			for (int i = 0; i < instances.length; i++)
				if (instances[i].isFile()) {


					String filename = directory + instances[i].getName();
					//String file = "random_RDD=0.2_TF=0.2_#65.dat";
					//String filename = directory + file;
					//System.out.println( instances[i].getName());

					ProblemInstance instance = readInstance(filename);
					//System.out.println(filename);

					Matcher num_matcher = Pattern.compile("\\d+(\\.\\d+)?").matcher(filename);

					ArrayList<Double> description = new ArrayList<>();

					while(num_matcher.find()){
						double value = Double.parseDouble(num_matcher.group());
						description.add(value);
					}

					long g_starttime = System.nanoTime();
					Greedy greedy = new Greedy(instance);
					Schedule greedySchedule = greedy.getSchedule();
					long g_endtime = System.nanoTime();


					long e_starttime = System.nanoTime();
					Exact exact = new Exact(instance);
					Schedule exactSchedule = exact.getSchedule();
					long e_endtime = System.nanoTime();

					long a_starttime = System.nanoTime();
					Approximation approximation = new Approximation(instance, anEpsilon);
					Schedule approximationSchedule = approximation.getSchedule();
					long a_endtime = System.nanoTime();



//					String output = String.format("%d,%.1f,%.1f,%.1f,%d,%d,%d,%d,%d,%d\n",
//							description.get(2).intValue(),description.get(0),description.get(1),anEpsilon,
//							(int)exactSchedule.getTardiness(), (int)approximationSchedule.getTardiness(),
//							(int)greedySchedule.getTardiness(), e_endtime-e_starttime, a_endtime-a_starttime,
//							g_endtime-g_starttime);
					String output = String.format("%d %d",
							(int)exactSchedule.getTardiness(), (int)approximationSchedule.getTardiness());
					System.out.println(output);

//						StringBuilder result = new StringBuilder();
//						result.append(Integer.toString(description.get(2).intValue())).append(",")
//						        .append(description.get(0).toString()).append(",")
//								.append(description.get(1).toString()).append(",")
//						pw.write(result.toString());

				} else if (instances[i].isDirectory()) {
					System.out.println(instances[i].getName());
				}
			//		BestFirst bestFirst = new BestFirst(instance);
			//		Schedule bestFirstSchedule = bestFirst.getSchedule();
			//		System.out.println(bestFirstSchedule.getTardiness());


//            pw.close();



		}
		
	}
}
