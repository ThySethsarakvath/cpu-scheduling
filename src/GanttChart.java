import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.List;

public class GanttChart {
    private Canvas canvas;
    private List<GanttEntry> ganttData;

    public GanttChart(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setGanttData(List<GanttEntry> ganttData) {
        this.ganttData = ganttData;
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        if (ganttData == null || ganttData.isEmpty()) {
            return;
        }
        
        int maxTime = ganttData.get(ganttData.size() - 1).getEndTime();
        double scaleX = 600.0 / Math.max(maxTime, 1); // Scale to fit canvas width
        
        int boxHeight = 40;
        int boxY = 40;
        int textY = boxY + boxHeight / 2 + 5;
        int timeY = boxY + boxHeight + 20;
        
        Color[] colors = {Color.LIGHTBLUE, Color.LIGHTCORAL, Color.LIGHTGREEN, 
                         Color.LIGHTYELLOW, Color.LIGHTPINK, Color.LIGHTSEAGREEN,
                         Color.LIGHTSALMON, Color.LIGHTSTEELBLUE};
        
        // Draw process boxes
        for (int i = 0; i < ganttData.size(); i++) {
            GanttEntry entry = ganttData.get(i);
            double startX = 50 + entry.getStartTime() * scaleX;
            double width = entry.getDuration() * scaleX;
            
            // Choose color based on process name
            int colorIndex = Math.abs(entry.getProcessName().hashCode()) % colors.length;
            if (entry.getProcessName().equals("IDLE")) {
                gc.setFill(Color.LIGHTGRAY);
            } else {
                gc.setFill(colors[colorIndex]);
            }
            
            // Draw process box
            gc.fillRect(startX, boxY, width, boxHeight);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(startX, boxY, width, boxHeight);
            
            // Draw process name
            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font(12));
            gc.fillText(entry.getProcessName(), startX + width / 2, textY);
            
            // Draw start time
            if (i == 0) {
                gc.fillText(String.valueOf(entry.getStartTime()), startX, timeY);
            }
            
            // Draw end time
            gc.fillText(String.valueOf(entry.getEndTime()), startX + width, timeY);
        }
        
        // Draw timeline
        gc.setStroke(Color.BLACK);
        gc.strokeLine(50, timeY - 5, 50 + maxTime * scaleX, timeY - 5);
        
        // Draw title
        gc.setFont(new Font(14));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Gantt Chart", 50, 25);
    }

    public void clear() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}