public class Process {
    private String job;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int finishTime;
    private int turnaroundTime;
    private int waitingTime;
    private int queueLevel;

    public Process(String job, int arrivalTime, int burstTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.queueLevel = 0;
    }

    // Copy constructor for RR algorithm
    public Process(Process other) {
        this.job = other.job;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.remainingTime = other.remainingTime;
        this.queueLevel = other.queueLevel;
    }

    // Getters and setters...
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