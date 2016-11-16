/**
 * Created by ludai on 16/11/11.
 */
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;

public class Joblist {
    private ArrayList<Integer> job_index;


    public Joblist(int position, ArrayList<Integer> jobs, int sigma, int k ){
        job_index=new ArrayList<Integer>();
        if(position==0) {
            for (int i = 0;  i< jobs.size() && jobs.get(i)<= sigma; i++) {
                if (jobs.get(i) != k) job_index.add(jobs.get(i));
            }
        }
        else {
            for (int i = jobs.indexOf(sigma)+1; i < jobs.size(); i++)job_index.add(jobs.get(i));
        }
    }

    public void showJobs(){
        System.out.println(job_index);
    }

    public int numJobs(){
        return job_index.size();
    }

    public int getIndex(){
        return job_index.get(0);
    }

    public ArrayList<Integer> getJoblist(){
        return job_index;
    }

    public int getJobsize(){
        return job_index.size();
    }



@Override
    public int hashCode() {
        return new HashCodeBuilder(74717, 5351)
                .append(job_index.get(0))
                .append(job_index.get(job_index.size()-1))
                .append(job_index.size())
                .append(job_index.toString())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Joblist list = (Joblist) obj;
        return list.hashCode() == this.hashCode();
    }

}
