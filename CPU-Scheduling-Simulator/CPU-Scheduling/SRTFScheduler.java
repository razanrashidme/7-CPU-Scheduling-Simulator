import java.util.*;

class Process implements Comparable<Process>{
    int ID, ArrivalTime, BurstTime, RemainingTime, WaitingTime, TurnaroundTime, StartTime, CompleteTime;

    public Process(int id, int arrTime, int burTime){
        ID = id;
        ArrivalTime = arrTime;
        BurstTime = burTime;
        RemainingTime = burTime;
        WaitingTime = 0;
        TurnaroundTime = 0;
        StartTime = -1;
        CompleteTime = 0;
    }

    public int getArrivalTime(){
        return ArrivalTime;
    }

    public int getBurstTime(){
        return BurstTime;
    }

    public void decreaseRemainingTime(){
        RemainingTime--;
    }

    public boolean isCompleted(){
        return RemainingTime == 0;
    }

    @Override
    public int compareTo(Process other){
        if (this.RemainingTime != other.RemainingTime){
            return Integer.compare(this.RemainingTime, other.RemainingTime);
        }
        return Integer.compare(this.ArrivalTime, other.ArrivalTime);
    }
}

class Event implements Comparable<Event>{
    int time;
    String type;
    Process process;

    public Event(int time, String type, Process process){
        this.time = time;
        this.type = type;
        this.process = process;
    }

    @Override
    public int compareTo(Event other){
        return Integer.compare(this.time, other.time);
    }
}

public class SRTFSchedulerEventTrial{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int num = input.nextInt();

        Process[] processes = new Process[num];
        PriorityQueue<Event> eventQueue = new PriorityQueue<>();

        // Read process details
        for(int i = 0; i < num; i++){
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            int arrivalTime = input.nextInt();
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            int burstTime = input.nextInt();
            processes[i] = new Process(i + 1, arrivalTime, burstTime);
            eventQueue.add(new Event(arrivalTime, "Arrived", processes[i]));
        }
        input.close();

        // Display the entered processes
        System.out.print("\nNumber of processes: " + num + " (");
        for(int i = 0; i < num; i++){
            System.out.print("P" + (i + 1));
            if (i < num - 1) System.out.print(", ");
        }
        System.out.println(")");

        System.out.println("Arrival times and burst times for each process:");
        for(int i = 0; i < num; i++){
            System.out.println("P" + (i + 1) + ": Arrival time = " + processes[i].getArrivalTime() +
                    ", Burst time = " + processes[i].getBurstTime() + " ms");
        }

        scheduleSRTF(eventQueue, num);
    }

    public static void scheduleSRTF(PriorityQueue<Event> eventQueue, int num){
        PriorityQueue<Process> readyQueue = new PriorityQueue<>();
        int time = 0, totalWaitingTime = 0, totalTurnaroundTime = 0, numContextSwitches = 0, idleTime = 0;
        Process currentProcess = null;

        System.out.println("\nScheduling Algorithm: Shortest Remaining Time First (SRTF)");
        System.out.println("Context Switch Time: 1 ms\n");
        System.out.println("Time | Process/CS");

        while(!eventQueue.isEmpty() || !readyQueue.isEmpty() || currentProcess != null){
            // Handle events (process arrival)
            while(!eventQueue.isEmpty() && eventQueue.peek().time <= time){
                Event event = eventQueue.poll();
                if(event.type.equals("Arrived")){
                    readyQueue.add(event.process);
                }
            }

            // If no process is currently running, the CPU is idle
            if(currentProcess == null && readyQueue.isEmpty()){
                System.out.println(time + "-" + ++time + " IDLE");
                idleTime++;
                continue;
            }

            // If no process is running or if a new process has shorter remaining time, perform a context switch
            if(currentProcess == null || (!readyQueue.isEmpty() && readyQueue.peek().RemainingTime < currentProcess.RemainingTime)){
                if(currentProcess != null){
                    readyQueue.add(currentProcess);
                    System.out.println(currentProcess.StartTime + "-" + time + " P" + currentProcess.ID);
                    System.out.println(time + "-" + ++time + " CS");
                    numContextSwitches++;
                }
                currentProcess = readyQueue.poll();
                currentProcess.StartTime = time;
            }

            // Execute the current process
            if(currentProcess != null){
                currentProcess.decreaseRemainingTime();
                time++;

                if(currentProcess.isCompleted()){
                    System.out.println(currentProcess.StartTime + "-" + time + " P" + currentProcess.ID);
                    currentProcess.CompleteTime = time;
                    currentProcess.TurnaroundTime = currentProcess.CompleteTime - currentProcess.ArrivalTime;
                    currentProcess.WaitingTime = currentProcess.TurnaroundTime - currentProcess.BurstTime;
                    totalTurnaroundTime += currentProcess.TurnaroundTime;
                    totalWaitingTime += currentProcess.WaitingTime;
                    currentProcess = null;

                    // Context switch if there are still processes in the ready queue
                    if(!readyQueue.isEmpty()){
                        System.out.println(time + "-" + ++time + " CS");
                        numContextSwitches++;
                    }
                }
            }
        }

        // Calculate performance metrics
        double avgWaitingTime = (double) totalWaitingTime / num;
        double avgTurnaroundTime = (double) totalTurnaroundTime / num;
        double cpuUtilization = ((double) (time - numContextSwitches - idleTime) / time) * 100;

        System.out.println("\nPerformance Metrics:");
        System.out.printf("Average Turnaround Time: %.2f ms\n", avgTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f ms\n", avgWaitingTime);
        System.out.printf("CPU Utilization: %.2f%%\n", cpuUtilization);
    }
}