/**
 * Created by sagarmistry on 2/20/16.
 */
import java.util.*;
import java.io.*;




public class scheduler {
        int processCount;
        int timeUnits;
        String typeOfScheduler;
        int quantum;
    static process activeProcessID;

    static scheduler setup(scheduler job, Scanner scanner){
        String process = scanner.next();
        job.processCount = scanner.nextInt();
        scanner.nextLine();
        scanner.next();

        job.timeUnits = scanner.nextInt();
        scanner.nextLine();
        scanner.next();

        job.typeOfScheduler = scanner.next();
        scanner.nextLine();
        scanner.next();
        job.quantum = scanner.nextInt();
        scanner.nextLine();
        return job;
    }

    static void setPList(scheduler job, Scanner scanner, ArrayList<process> processList){
        for(int x = 0; x<job.processCount; x++){
            process temp = new process();
            scanner.skip("process name");
            temp.setName(scanner.next());
            scanner.next();
            temp.setArrival(scanner.nextInt());
            scanner.next();
            int temp1 = scanner.nextInt();
            temp.setBurstCount(temp1);
            temp.setOriginalBurstCount(temp1);
            processList.add(temp);
            scanner.nextLine();
        }

        if(job.typeOfScheduler.equals("fcfs")){
            runFCFS(processList, job);
        }
        else if(job.typeOfScheduler.equals("sjf")){
            runSJF(processList, job);
        }
        else if(job.typeOfScheduler.equals("rr")){
            runRR(processList, job);
        }



    }

    static void runRR(ArrayList<process> processList, scheduler job) {
        int currentJobID = 0;
        System.out.println(processList.size() + " processes");
        System.out.println("Using Round-Robin");
        System.out.println("Quantum " + job.quantum + " \n");
        activeProcessID = processList.get(currentJobID);
        for (int x = 0; x < job.timeUnits; x++) {
            if(activeProcessID.burstCount == 0 && x != 0){
                activeProcessID.setFinishTime(x);
                System.out.println("Time " + x + ": " + activeProcessID.getName() + " finished");
                currentJobID++;
                if (currentJobID == processList.size()) {
                    currentJobID = 0;
                }
                if(processList.get(currentJobID).getArrivalFlag() != 1){
                    currentJobID--;
                }
                activeProcessID = processList.get(currentJobID);
                if(activeProcessID.burstCount != 0){
                System.out.println("Time " + x + ": " + activeProcessID.getName() + " selected (burst " + activeProcessID.getBurstCount() + ")");
                }
                else{
                    process temp = new process();
                    temp.setName("IDLE");
                    activeProcessID = temp;
                    System.out.println("Time " + x + ": " + activeProcessID.getName());
                }
            }
            for (process arrival : processList) {
                if (x == arrival.getArrival()) {  // Process Arrived
                    System.out.println("Time " + x + ": " + arrival.getName() + " arrived");
                    arrival.setArrivalFlag(1);
                }
                if( arrival.getArrival() == 0 &&  x == 0) {
                    activeProcessID = arrival;
                    System.out.println("Time " + x + ": " + activeProcessID.getName() + " selected (burst " + activeProcessID.getBurstCount() + ")");

                }
            }// Check when Process Arrives

            if (x % job.quantum == 0 && x != 0) {
                currentJobID++;
                if (currentJobID == processList.size()) {
                    currentJobID = 0;
                }
                if(processList.get(currentJobID).getArrivalFlag() != 1){
                    currentJobID--;
                }
                else {
                    if (processList.get(currentJobID).getBurstCount() != 0) {
                        activeProcessID = processList.get(currentJobID);
                        System.out.println("Time " + x + ": " + activeProcessID.getName() + " selected (burst " + activeProcessID.getBurstCount() + ")");

                    }
                }
            }// End of Quantum Switch
            activeProcessID.reduceBurstCount();

        }
        System.out.println("Finished at time "+job.timeUnits+"\n");

        for(process list: processList){
            int turnaround = list.getFinishTime() - list.getArrival();
            int waiting = turnaround - list.getOriginalBurstCount();
            System.out.println(list.getName() + " wait " + waiting + " turnaround "+ turnaround);
        }

    }
    static void runSJF(ArrayList<process> processList, scheduler job){
        System.out.println(""+ processList.size() + " processes");
        System.out.println("Using Shortest Job First (Pre)\n");
        int shortestJob = Integer.MAX_VALUE;
        for(int x = 0; x<job.timeUnits; x++) {

            for(process arrival : processList){
                if(x == arrival.getArrival()){  // Process Arrived
                    System.out.println("Time "+x+": "+ arrival.getName()+ " arrived");
                    if(arrival.getBurstCount() < shortestJob){  // Is the new Arrived Process shorter than current
                        shortestJob = arrival.getBurstCount();
                        activeProcessID = arrival;
                        System.out.println("Time "+x+": "+ activeProcessID.getName()+ " selected (burst "+ activeProcessID.getBurstCount()+")");
                    }
                }
            }// Check when Process Arrives

            if(activeProcessID.getBurstCount() == 0){
                if(activeProcessID.getName() == "IDLE"){
                    System.out.println("Time " + x + ": " + activeProcessID.getName() + "");
                }

                    System.out.println("Time " + x + ": " + activeProcessID.getName() +" finished");
                    activeProcessID.setFinishTime(x);

                shortestJob = Integer.MAX_VALUE;
                for(process newProcess: processList){
                    if(newProcess.getBurstCount() < shortestJob && newProcess.getBurstCount() != 0){
                        shortestJob = newProcess.getBurstCount();
                        activeProcessID = newProcess;
                    }

                }

                if(shortestJob == Integer.MAX_VALUE){
                    process temp = new process();

                    temp.setName("IDLE");
                    temp.setBurstCount(Integer.MAX_VALUE);
                    activeProcessID = temp;
                }
                else{
                    System.out.println("Time " + x + ": " + activeProcessID.getName() + " selected (burst " + activeProcessID.getBurstCount() + ")");
                }
            }

            if(activeProcessID.getName() == "IDLE"){
                System.out.println("Time " + x + ": " + activeProcessID.getName() + "");
            }
            activeProcessID.reduceBurstCount();


        }
        System.out.println("Finished at time " + job.timeUnits+"\n");

        for(process list: processList){
            int turnaround = list.getFinishTime() - list.getArrival();
            int waiting = turnaround - list.getOriginalBurstCount();
            System.out.println(list.getName() + " wait " + waiting + " turnaround "+ turnaround);
        }
    }
    static void runFCFS(ArrayList<process> processList, scheduler job){
        System.out.println(""+ processList.size() + " processes");
        System.out.println("Using First Come First Served\n");
        int time = 0;
        int currentProcess = 0;

        for(process temp :processList){
            time += temp.burstCount;
        } // Get Total Program Time

        System.out.println("Time 0: P1 arrived");  // First process
        System.out.println("Time 0: P1 selected (burst "+ processList.get(0).getBurstCount()+")"); // First Process
        activeProcessID = processList.get(currentProcess);
        for(int x = 0; x<=job.timeUnits; x++){ // Time of entire program

            for(process temp :processList) {
                if(temp.getArrival() == x){
                    System.out.println("Time " + x + ": " + temp.getName() +" arrived");
                }
            }

            if(activeProcessID.getBurstCount() == 0){
                System.out.println("Time " + x + ": " + activeProcessID.getName() + " finished");
                activeProcessID.setFinishTime(x);
               if(currentProcess != processList.size()-1){
                   currentProcess++;
               }
                activeProcessID = processList.get(currentProcess);
                if(x != job.timeUnits) {
                    System.out.println("Time " + x + ": " + activeProcessID.getName() + " selected (burst " + activeProcessID.getBurstCount() + ")");
                }
            }
            activeProcessID.reduceBurstCount();

        }
        System.out.println("Finished at time "+job.timeUnits+"\n");
        for(process list: processList){
            int turnaround = list.getFinishTime() - list.getArrival();
            int waiting = turnaround - list.getOriginalBurstCount();
            System.out.println(list.getName() + " wait " + waiting + " turnaround "+ turnaround);
        }

    }


    public static void main(String args[]) throws FileNotFoundException {

        File input = new File("/Users/sagarmistry/Desktop/Spring 2016/COP 4600 (Programming Languages) /Assignment2/sampleInput/set2_process.in");

        Scanner scanner = new Scanner(input); // Scanner for Readin
        scheduler job = new scheduler(); // Job object to select type of scheduler
        ArrayList<process> processList = new ArrayList<process>(); // Arraylist of class process
        job = setup(job,scanner);// set up code

        if(job.typeOfScheduler.equals("fcfs")) {
            setPList(job,scanner,processList);
        }
        else if(job.typeOfScheduler.equals("sjf")) {
            setPList(job,scanner,processList);
        }
        else if(job.typeOfScheduler.equals("rr")) {
            setPList(job,scanner,processList);
        }
        else{
            System.out.print("Error: Invalid Scheduler");
        }



    }

}
