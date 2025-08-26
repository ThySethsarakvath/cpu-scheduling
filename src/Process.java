public class Process {
    private String job;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int finishTime;
    private int turnaroundTime;
    private int waitingTime;
    private int queueLevel; // Which queue the process is currently in

    public Process(String job, int arrivalTime, int burstTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.queueLevel = 0; // Start in queue 0
    }

    // Getters and setters for all fields
    public String getJob() { return job; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public int getFinishTime() { return finishTime; }
    public void setFinishTime(int finishTime) { this.finishTime = finishTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public int getQueueLevel() { return queueLevel; }
    public void setQueueLevel(int queueLevel) { this.queueLevel = queueLevel; }
}