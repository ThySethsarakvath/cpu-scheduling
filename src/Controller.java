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

    @FXML private Label algoBadge;
    @FXML private ComboBox<String> algorithmBox;
    @FXML private TextField arrivalField;
    @FXML private Label avgLabel;
    @FXML private TextField burstField;
    @FXML private TableColumn<Process, String> colJob;
    @FXML private TableColumn<Process, Integer> colArrival;
    @FXML private TableColumn<Process, Integer> colBurst;
    @FXML private TableColumn<Process, Integer> colFinish;
    @FXML private TableColumn<Process, Integer> colTat;
    @FXML private TableColumn<Process, Integer> colWt;
    @FXML private Canvas ganttCanvas;
    @FXML private TextField quantumField;
    @FXML private TableView<Process> resultTable;
    @FXML private Button solveBtn;

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
        colFinish.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        colTat.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        colWt.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        
        resultTable.setItems(processData);
        
        algorithmBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            algoBadge.setText(newVal);
        });
    }

    @FXML
    void handleSolve(ActionEvent event) {
        String selectedAlgorithm = algorithmBox.getValue();
        
        if ("MLFQ".equals(selectedAlgorithm)) {
            solveMLFQ();
        } else {
            showAlert("Info", "Algorithm not implemented", 
                     selectedAlgorithm + " is selected but only MLFQ is implemented for this demo.");
        }
    }

    private void solveMLFQ() {
        processData.clear();
        ganttData.clear();
        ganttChart.clear();
        
        if (!validateInputs()) {
            return;
        }
        
        try {
            int[] arrivalTimes = Arrays.stream(arrivalField.getText().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            
            int[] burstTimes = Arrays.stream(burstField.getText().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            
            int[] quantums = Arrays.stream(quantumField.getText().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            
            if (quantums.length != 2) {
                showAlert("Input Error", "Invalid Quantum", 
                         "Please enter exactly 2 quantum values (for first two queues)");
                return;
            }
            
            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < arrivalTimes.length; i++) {
                processes.add(new Process("P" + (i + 1), arrivalTimes[i], burstTimes[i]));
            }
            
            runMLFQ(processes, quantums[0], quantums[1]);
            calculateAverages();
            
            // Set Gantt data and draw
            ganttChart.setGanttData(ganttData);
            ganttChart.draw();
            
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid Number", 
                     "Please enter valid numbers for all fields");
        }
    }

    private boolean validateInputs() {
        String arrivalText = arrivalField.getText().trim();
        String burstText = burstField.getText().trim();
        String quantumText = quantumField.getText().trim();
        
        if (arrivalText.isEmpty() || burstText.isEmpty() || quantumText.isEmpty()) {
            showAlert("Input Error", "Missing Input", 
                     "Please fill in all fields");
            return false;
        }
        
        String[] arrivalParts = arrivalText.split("\\s+");
        String[] burstParts = burstText.split("\\s+");
        
        if (arrivalParts.length != burstParts.length) {
            showAlert("Input Error", "Mismatched Input", 
                     "Number of arrival times must match number of burst times");
            return false;
        }
        
        return true;
    }

    private void runMLFQ(List<Process> processes, int quantum1, int quantum2) {
        Queue<Process> queue0 = new LinkedList<>();
        Queue<Process> queue1 = new LinkedList<>();
        Queue<Process> queue2 = new LinkedList<>();
        
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        
        while (completedProcesses.size() < processes.size()) {
            // Add arriving processes to queue0
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !completedProcesses.contains(p) && 
                    !queue0.contains(p) && !queue1.contains(p) && !queue2.contains(p)) {
                    queue0.add(p);
                }
            }
            
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
                }
                continue;
            }
            
            // Record Gantt chart entry
            ganttData.add(new GanttEntry(currentProcess.getJob(), currentTime, currentTime + timeSlice));
            
            currentTime += timeSlice;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);
            
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setFinishTime(currentTime);
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                completedProcesses.add(currentProcess);
                processData.add(currentProcess);
            } else {
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
        }
    }

    private void calculateAverages() {
        if (processData.isEmpty()) return;
        
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