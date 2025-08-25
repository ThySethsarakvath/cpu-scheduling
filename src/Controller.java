import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Label algoBadge;

    @FXML
    private ComboBox<?> algorithmBox;

    @FXML
    private TextField arrivalField;

    @FXML
    private Label avgLabel;

    @FXML
    private TextField burstField;

    @FXML
    private TableColumn<?, ?> colArrival;

    @FXML
    private TableColumn<?, ?> colBurst;

    @FXML
    private TableColumn<?, ?> colFinish;

    @FXML
    private TableColumn<?, ?> colJob;

    @FXML
    private TableColumn<?, ?> colTat;

    @FXML
    private TableColumn<?, ?> colWt;

    @FXML
    private Canvas ganttCanvas;

    @FXML
    private TextField quantumField;

    @FXML
    private TableView<?> resultTable;

    @FXML
    private Button solveBtn;

    @FXML
    void handleSolve(ActionEvent event) {

    }

}
