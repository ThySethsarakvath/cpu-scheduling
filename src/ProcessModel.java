// public class ProcessModel {
//     private String pid;
//     private int arrivalTime;
//     private int burstTime;
//     private int remainingTime;
//     private Integer startTime;
//     private Integer completionTime;

//     public ProcessModel(String pid, int arrivalTime, int burstTime) {
//         this.pid = pid;
//         this.arrivalTime = arrivalTime;
//         this.burstTime = burstTime;
//         this.remainingTime = burstTime;
//     }

//     public String getPid() { return pid; }
//     public int getArrivalTime() { return arrivalTime; }
//     public int getBurstTime() { return burstTime; }

//     public int getRemainingTime() { return remainingTime; }
//     public void setRemainingTime(int r) { this.remainingTime = r; }

//     public Integer getStartTime() { return startTime; }
//     public void setStartTime(Integer t) { this.startTime = t; }

//     public Integer getCompletionTime() { return completionTime; }
//     public void setCompletionTime(Integer t) { this.completionTime = t; }

//     public int getTurnaroundTime() {
//         if (completionTime == null) return 0;
//         return completionTime - arrivalTime;
//     }

//     public int getWaitingTime() {
//         return getTurnaroundTime() - burstTime;
//     }

//     public int getResponseTime() {
//         if (startTime == null) return 0;
//         return startTime - arrivalTime;
//     }
// }
