// import java.util.List;

// public class ScheduleResult {
//     public final List<GanttSegment> timeline;
//     public final List<ProcessModel> processes;
//     public final double avgTurnaround;
//     public final double avgWaiting;
//     public final double avgResponse;
//     public final int totalTime;

//     public ScheduleResult(List<GanttSegment> timeline, List<ProcessModel> processes) {
//         this.timeline = timeline != null ? timeline : List.of();
//         this.processes = processes != null ? processes : List.of();

//         int totalTat = 0;
//         int totalWt = 0;
//         int totalRt = 0;

//         for (ProcessModel p : this.processes) {
//             totalTat += p.getTurnaroundTime();
//             totalWt += p.getWaitingTime();
//             totalRt += p.getResponseTime();
//         }

//         int n = this.processes.size();
//         this.avgTurnaround = n > 0 ? (double) totalTat / n : 0.0;
//         this.avgWaiting = n > 0 ? (double) totalWt / n : 0.0;
//         this.avgResponse = n > 0 ? (double) totalRt / n : 0.0;

//         this.totalTime = this.timeline.isEmpty() ? 0 : this.timeline.get(this.timeline.size() - 1).end;
//     }
// }
