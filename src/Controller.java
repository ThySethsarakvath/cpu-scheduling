
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Controller {

    @FXML
    private Label algoBadge;
    @FXML
    private ComboBox<String> algorithmBox;
    @FXML
    private TextField arrivalField;
    @FXML
    private Label avgLabel;
    @FXML
    private TextField burstField;
    @FXML
    private TableColumn<Process, String> colJob;
    @FXML
    private TableColumn<Process, Integer> colArrival;
    @FXML
    private TableColumn<Process, Integer> colBurst;
    @FXML
    private TableColumn<Process, Integer> colStart;
    @FXML
    private TableColumn<Process, Integer> colFinish;
    @FXML
    private TableColumn<Process, Integer> colTat;
    @FXML
    private TableColumn<Process, Integer> colWt;
    @FXML
    private TableColumn<Process, Integer> colRt;
    @FXML
    private Canvas ganttCanvas;
    @FXML
    private TextField quantumField;
    @FXML
    private TableView<Process> resultTable;
    @FXML
    private Button solveBtn;

    private ObservableList<Process> processData = FXCollections.observableArrayList();
    private List<GanttEntry> ganttData = new ArrayList<>();
    private GanttChart ganttChart;

    @FXML
    public void initialize() {
        // Initialize Gantt chart
        ganttChart = new GanttChart(ganttCanvas);

        // Initialize algorithm dropdown
        algorithmBox.getItems().addAll("FCFS", "SJF", "SRT", "RR", "MLFQ");
        algorithmBox.setValue("MLFQ");

        // Initialize table columns
        colJob.setCellValueFactory(new PropertyValueFactory<>("job"));
        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colBurst.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colFinish.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        colTat.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        colWt.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        colRt.setCellValueFactory(new PropertyValueFactory<>("responseTime"));

        resultTable.setItems(processData);

        algorithmBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            algoBadge.setText(newVal);
            updateQuantumFieldVisibility(newVal);
        });

        // Set initial visibility
        updateQuantumFieldVisibility(algorithmBox.getValue());
    }

    private void updateQuantumFieldVisibility(String algorithm) {
        boolean needsQuantum = "RR".equals(algorithm) || "MLFQ".equals(algorithm);
        quantumField.setVisible(needsQuantum);
        quantumField.setManaged(needsQuantum);

        // Update prompt text based on algorithm
        if ("RR".equals(algorithm)) {
            quantumField.setPromptText("Quantum (e.g., 2)");
        } else if ("MLFQ".equals(algorithm)) {
            quantumField.setPromptText("Quantum1 Quantum2 (e.g., 2 4)");
        }
    }

    @FXML
    void handleSolve(ActionEvent event) {
        String selectedAlgorithm = algorithmBox.getValue();

        processData.clear();
        ganttData.clear();
        ganttChart.clear();

        if (!validateInputs(selectedAlgorithm)) {
            return;
        }

        try {
            int[] arrivalTimes = Arrays.stream(arrivalField.getText().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int[] burstTimes = Arrays.stream(burstField.getText().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < arrivalTimes.length; i++) {
                processes.add(new Process("P" + (i + 1), arrivalTimes[i], burstTimes[i]));
            }

            switch (selectedAlgorithm) {
                case "MLFQ":
                    int[] mlfqQuantums = Arrays.stream(quantumField.getText().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    if (mlfqQuantums.length != 2) {
                        showAlert("Input Error", "Invalid Quantum",
                                "MLFQ requires exactly 2 quantum values (e.g., 2 4)");
                        return;
                    }
                    runMLFQ(processes, mlfqQuantums[0], mlfqQuantums[1]);
                    break;

                case "RR":
                    int rrQuantum = Integer.parseInt(quantumField.getText().trim());
                    if (rrQuantum <= 0) {
                        showAlert("Input Error", "Invalid Quantum",
                                "Quantum must be a positive integer");
                        return;
                    }
                    runRR(processes, rrQuantum);
                    break;

                case "FCFS":
                    runFCFS(processes);
                    break;

                case "SJF":
                    runSJF(processes);
                    break;

                case "SRT":
                    runSRT(processes);
                    break;

                default:
                    showAlert("Info", "Algorithm not implemented",
                            selectedAlgorithm + " is selected but not implemented yet.");
                    return;
            }

            calculateAverages();
            ganttChart.setGanttData(ganttData);
            ganttChart.draw();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid Number",
                    "Please enter valid numbers for all fields");
        } catch (Exception e) {
            showAlert("Error", "Unexpected Error",
                    "An error occurred: " + e.getMessage());
        }
    }

    private boolean validateInputs(String algorithm) {
        String arrivalText = arrivalField.getText().trim();
        String burstText = burstField.getText().trim();
        String quantumText = quantumField.getText().trim();

        // Check empty fields
        if (arrivalText.isEmpty() || burstText.isEmpty()) {
            showAlert("Input Error", "Missing Input",
                    "Please fill in arrival and burst times");
            return false;
        }

        // Check if quantum field is required and filled
        if (("RR".equals(algorithm) || "MLFQ".equals(algorithm)) && quantumText.isEmpty()) {
            showAlert("Input Error", "Missing Quantum",
                    "Please enter quantum value(s) for " + algorithm);
            return false;
        }

        String[] arrivalParts = arrivalText.split("\\s+");
        String[] burstParts = burstText.split("\\s+");

        // Check matching number of processes
        if (arrivalParts.length != burstParts.length) {
            showAlert("Input Error", "Mismatched Input",
                    "Number of arrival times (" + arrivalParts.length
                    + ") must match number of burst times (" + burstParts.length + ")");
            return false;
        }

        // Check for negative values
        try {
            for (String part : arrivalParts) {
                if (Integer.parseInt(part) < 0) {
                    showAlert("Input Error", "Invalid Value",
                            "Arrival times cannot be negative");
                    return false;
                }
            }
            for (String part : burstParts) {
                if (Integer.parseInt(part) <= 0) {
                    showAlert("Input Error", "Invalid Value",
                            "Burst times must be positive");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid Number",
                    "Please enter valid integers only");
            return false;
        }

        return true;
    }

    private void runFCFS(List<Process> processes) {
        // Sort by arrival time
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        int currentTime = 0;

        for (Process p : processes) {
            // If CPU is idle until the process arrives, add idle time in Gantt chart
            if (currentTime < p.getArrivalTime()) {
                ganttData.add(new GanttEntry("IDLE", currentTime, p.getArrivalTime()));
                currentTime = p.getArrivalTime();
            }

            // Set start time (first time process gets CPU)
            if (p.getStartTime() == -1) {
                p.setStartTime(currentTime);
                p.setResponseTime(currentTime - p.getArrivalTime());
            }

            // Process execution without preemption
            ganttData.add(new GanttEntry(p.getJob(), currentTime, currentTime + p.getBurstTime()));

            currentTime += p.getBurstTime();
            p.setFinishTime(currentTime);
            p.setTurnaroundTime(currentTime - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
            processData.add(p);
        }
    }

    private void runSJF(List<Process> processes) {
        int n = processes.size();
        int completed = 0;
        int currentTime = 0;
        boolean[] isCompleted = new boolean[n];

        while (completed < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            // Find process with shortest burst among those that have arrived
            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (!isCompleted[i] && p.getArrivalTime() <= currentTime && p.getBurstTime() < minBurst) {
                    minBurst = p.getBurstTime();
                    idx = i;
                }
            }

            if (idx == -1) {
                // No process available to run: CPU is idle until next arrival
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    Process p = processes.get(i);
                    if (!isCompleted[i] && p.getArrivalTime() < nextArrival) {
                        nextArrival = p.getArrivalTime();
                    }
                }
                ganttData.add(new GanttEntry("IDLE", currentTime, nextArrival));
                currentTime = nextArrival;
                continue;
            }

            Process selected = processes.get(idx);

            // Set start time and response time
            if (selected.getStartTime() == -1) {
                selected.setStartTime(currentTime);
                selected.setResponseTime(currentTime - selected.getArrivalTime());
            }

            int startTime = currentTime;
            int finishTime = startTime + selected.getBurstTime();

            // Add Gantt chart entry for the selected process
            ganttData.add(new GanttEntry(selected.getJob(), startTime, finishTime));

            // Update process metrics
            selected.setFinishTime(finishTime);
            selected.setTurnaroundTime(finishTime - selected.getArrivalTime());
            selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());

            processData.add(selected);
            isCompleted[idx] = true;
            completed++;
            currentTime = finishTime;
        }
    }

    private void runSRT(List<Process> processes) {
        int n = processes.size();
        int completed = 0;
        int currentTime = 0;
        String lastProcess = "";
        int segmentStart = 0;

        // Create copies of processes for execution manually
        List<Process> executionProcesses = new ArrayList<>();
        for (Process p : processes) {
            Process copy = new Process(p.getJob(), p.getArrivalTime(), p.getBurstTime());
            copy.setRemainingTime(p.getBurstTime());
            copy.setStartTime(p.getStartTime());
            copy.setResponseTime(p.getResponseTime());
            // Copy other fields if needed
            executionProcesses.add(copy);
        }

        while (completed < n) {
            // Find process with smallest remaining time among those available
            Process currentProc = null;
            int minRemaining = Integer.MAX_VALUE;
            for (Process p : executionProcesses) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (p.getRemainingTime() < minRemaining) {
                        minRemaining = p.getRemainingTime();
                        currentProc = p;
                    }
                }
            }

            if (currentProc == null) {
                // CPU is idle
                if (!lastProcess.equals("IDLE")) {
                    if (!lastProcess.equals("")) {
                        ganttData.add(new GanttEntry(lastProcess, segmentStart, currentTime));
                    }
                    lastProcess = "IDLE";
                    segmentStart = currentTime;
                }
                // Jump to next arrival time
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : executionProcesses) {
                    if (p.getRemainingTime() > 0 && p.getArrivalTime() > currentTime) {
                        nextArrival = Math.min(nextArrival, p.getArrivalTime());
                    }
                }
                if (nextArrival == Integer.MAX_VALUE) {
                    break;
                }
                currentTime = nextArrival;
                continue;
            }

            // Set start time and response time (if first time getting CPU)
            if (currentProc.getStartTime() == -1) {
                currentProc.setStartTime(currentTime);
                currentProc.setResponseTime(currentTime - currentProc.getArrivalTime());
            }

            // If process changes, record the previous segment
            if (!currentProc.getJob().equals(lastProcess)) {
                if (!lastProcess.equals("")) {
                    ganttData.add(new GanttEntry(lastProcess, segmentStart, currentTime));
                }
                lastProcess = currentProc.getJob();
                segmentStart = currentTime;
            }

            // Execute the process for one time unit
            currentProc.setRemainingTime(currentProc.getRemainingTime() - 1);
            currentTime++;

            // If the process finishes execution, update its metrics
            if (currentProc.getRemainingTime() == 0) {
                currentProc.setFinishTime(currentTime);
                currentProc.setTurnaroundTime(currentTime - currentProc.getArrivalTime());
                currentProc.setWaitingTime(currentProc.getTurnaroundTime() - currentProc.getBurstTime());
                completed++;
            }
        }

        // Record final segment
        if (!lastProcess.equals("")) {
            ganttData.add(new GanttEntry(lastProcess, segmentStart, currentTime));
        }

        // Add processes to the table
        for (Process p : executionProcesses) {
            processData.add(p);
        }
    }

    private void runRR(List<Process> processes, int quantum) {
        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;

        List<Process> executionProcesses = new ArrayList<>();
        for (Process p : processes) {
            Process copy = new Process(p.getJob(), p.getArrivalTime(), p.getBurstTime());
            copy.setRemainingTime(p.getBurstTime());
            copy.setStartTime(p.getStartTime());
            copy.setResponseTime(p.getResponseTime());
            // Copy other fields if needed
            executionProcesses.add(copy);
        }

        // Sort by arrival time
        executionProcesses.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int processIndex = 0;
        Process currentProcess = null;

        while (completedProcesses.size() < executionProcesses.size()) {
            // Add all processes that have arrived by current time
            while (processIndex < executionProcesses.size()
                    && executionProcesses.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(executionProcesses.get(processIndex));
                processIndex++;
            }

            // If no process in ready queue but still processes left, add idle time
            if (readyQueue.isEmpty() && processIndex < executionProcesses.size()) {
                int nextArrival = executionProcesses.get(processIndex).getArrivalTime();
                ganttData.add(new GanttEntry("IDLE", currentTime, nextArrival));
                currentTime = nextArrival;
                continue;
            }

            // Get next process from ready queue
            if (currentProcess == null || currentProcess.getRemainingTime() == 0) {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.poll();

                    // Set start time and response time (if first time getting CPU)
                    if (currentProcess.getStartTime() == -1) {
                        currentProcess.setStartTime(currentTime);
                        currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
                    }
                } else {
                    break; // No more processes to execute
                }
            }

            // Execute the process for quantum or remaining time
            int executionTime = Math.min(currentProcess.getRemainingTime(), quantum);
            ganttData.add(new GanttEntry(currentProcess.getJob(), currentTime, currentTime + executionTime));

            currentTime += executionTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);

            // Add arriving processes during execution
            while (processIndex < executionProcesses.size()
                    && executionProcesses.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(executionProcesses.get(processIndex));
                processIndex++;
            }

            // Check if process completed
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setFinishTime(currentTime);
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                completedProcesses.add(currentProcess);
                processData.add(currentProcess);
                currentProcess = null;
            } else {
                // Put back in ready queue if not completed
                readyQueue.add(currentProcess);
                currentProcess = null;
            }
        }
    }

    private void runMLFQ(List<Process> processes, int quantum1, int quantum2) {
        Queue<Process> queue0 = new LinkedList<>();
        Queue<Process> queue1 = new LinkedList<>();
        Queue<Process> queue2 = new LinkedList<>();

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        final int AGING_THRESHOLD = 5;

        while (completedProcesses.size() < processes.size()) {
            // Add arriving processes to queue0
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !completedProcesses.contains(p)
                        && !queue0.contains(p) && !queue1.contains(p) && !queue2.contains(p)) {
                    queue0.add(p);
                }
            }

            // Aging: Check for processes waiting too long in lower queues
            promoteAgingProcesses(queue1, queue0, AGING_THRESHOLD);
            promoteAgingProcesses(queue2, queue1, AGING_THRESHOLD);

            Process currentProcess = null;
            int timeSlice = 0;

            if (!queue0.isEmpty()) {
                currentProcess = queue0.poll();
                timeSlice = Math.min(currentProcess.getRemainingTime(), quantum1);
            } else if (!queue1.isEmpty()) {
                currentProcess = queue1.poll();
                timeSlice = Math.min(currentProcess.getRemainingTime(), quantum2);
            } else if (!queue2.isEmpty()) {
                currentProcess = queue2.poll();
                timeSlice = currentProcess.getRemainingTime();
            } else {
                // IDLE time
                if (completedProcesses.size() < processes.size()) {
                    ganttData.add(new GanttEntry("IDLE", currentTime, currentTime + 1));
                    currentTime++;
                    // Increment waiting time for all processes in queues
                    incrementWaitingTimeInQueues(queue0, queue1, queue2);
                }
                continue;
            }

            // Set start time and response time (if first time getting CPU)
            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);
                currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
            }

            // Record Gantt chart entry
            ganttData.add(new GanttEntry(currentProcess.getJob(), currentTime, currentTime + timeSlice));

            currentTime += timeSlice;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);

            // Reset waiting time since process just got CPU time
            currentProcess.setWaitingTimeInQueue(0);

            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setFinishTime(currentTime);
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                completedProcesses.add(currentProcess);
                processData.add(currentProcess);
            } else {
                // Demotion logic
                int currentQueue = currentProcess.getQueueLevel();
                if (currentQueue == 0) {
                    currentProcess.setQueueLevel(1);
                    queue1.add(currentProcess);
                } else if (currentQueue == 1) {
                    currentProcess.setQueueLevel(2);
                    queue2.add(currentProcess);
                } else {
                    queue2.add(currentProcess);
                }
            }

            // Increment waiting time for all other processes in queues
            incrementWaitingTimeInQueues(queue0, queue1, queue2);
        }
    }

    private void promoteAgingProcesses(Queue<Process> sourceQueue, Queue<Process> targetQueue, int threshold) {
        List<Process> toPromote = new ArrayList<>();

        for (Process p : sourceQueue) {
            p.incrementWaitingTime();
            if (p.getWaitingTimeInQueue() >= threshold) {
                toPromote.add(p);
                p.setWaitingTimeInQueue(0); // Reset waiting time after promotion
            }
        }

        // Remove promoted processes from source queue and add to target queue
        for (Process p : toPromote) {
            sourceQueue.remove(p);
            targetQueue.add(p);
            p.setQueueLevel(p.getQueueLevel() - 1); // Move to higher priority queue
        }
    }

    private void incrementWaitingTimeInQueues(Queue<Process>... queues) {
        for (Queue<Process> queue : queues) {
            for (Process p : queue) {
                p.incrementWaitingTime();
            }
        }
    }

    private void calculateAverages() {
        if (processData.isEmpty()) {
            return;
        }

        double avgTAT = processData.stream().mapToInt(Process::getTurnaroundTime).average().orElse(0);
        double avgWT = processData.stream().mapToInt(Process::getWaitingTime).average().orElse(0);

        avgLabel.setText(String.format("Average TAT: %.2f | Average WT: %.2f", avgTAT, avgWT));
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
