public class Process {
    private String job;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int startTime;
    private int finishTime;
    private int turnaroundTime;
    private int waitingTime;
    private int responseTime;
    private int queueLevel;
    private int waitingTimeInQueue; // For aging

    public Process(String job, int arrivalTime, int burstTime) {
        this.job = job;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.startTime = -1; // Not started yet
        this.queueLevel = 0;
        this.waitingTimeInQueue = 0;
    }

    // Getters and setters for all fields
    public String getJob() { return job; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public int getFinishTime() { return finishTime; }
    public void setFinishTime(int finishTime) { this.finishTime = finishTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public int getResponseTime() { return responseTime; }
    public void setResponseTime(int responseTime) { this.responseTime = responseTime; }
    public int getQueueLevel() { return queueLevel; }
    public void setQueueLevel(int queueLevel) { this.queueLevel = queueLevel; }
    public int getWaitingTimeInQueue() { return waitingTimeInQueue; }
    public void setWaitingTimeInQueue(int waitingTimeInQueue) { this.waitingTimeInQueue = waitingTimeInQueue; }
    public void incrementWaitingTime() { this.waitingTimeInQueue++; }
}