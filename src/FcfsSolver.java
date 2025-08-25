// import java.util.*;

// public class FcfsSolver {

//     public static ScheduleResult solve(List<Integer> arrivalTimes, List<Integer> burstTimes) {
//         List<ProcessModel> processes = new ArrayList<>();
//         for (int i = 0; i < arrivalTimes.size(); i++) {
//             String job = arrivalTimes.size() > 26 ? "P" + (i + 1) : String.valueOf((char) ('A' + i));
//             processes.add(new ProcessModel(job, arrivalTimes.get(i), burstTimes.get(i)));
//         }

//         processes.sort(Comparator.comparingInt(ProcessModel::getArrivalTime));

//         List<GanttSegment> gantt = new ArrayList<>();
//         int[] finishTime = new int[processes.size()];

//         for (int i = 0; i < processes.size(); i++) {
//             ProcessModel p = processes.get(i);

//             if (i == 0 || p.getArrivalTime() > finishTime[i - 1]) {
//                 finishTime[i] = p.getArrivalTime() + p.getBurstTime();
//                 gantt.add(new GanttSegment(p.getPid(), p.getArrivalTime(), finishTime[i]));
//             } else {
//                 finishTime[i] = finishTime[i - 1] + p.getBurstTime();
//                 gantt.add(new GanttSegment(p.getPid(), finishTime[i - 1], finishTime[i]));
//             }

//             p.setStartTime(gantt.get(gantt.size() - 1).start);
//             p.setCompletionTime(finishTime[i]);
//         }

//         return new ScheduleResult(gantt, processes);
//     }
// }
