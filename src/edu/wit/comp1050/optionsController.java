package edu.wit.comp1050;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class optionsController
{
    @FXML
    public ComboBox numOfGuessesComboBox;
    @FXML
    public ComboBox dupColorsComboBox;
    @FXML
    public ComboBox blankSpaceComboBox;
    @FXML
    public Button startGameButton;

    private String pegRows;
    private String dupC;
    private String blankS;
    public optionsController()
    {
        pegRows = "";
        dupC = "";
        blankS = "";
    }

    public void initialize()
    {
        startGameButton.setOnMouseClicked(mouseEvent -> {
            try {
                handle(mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handle(MouseEvent mouseEvent) throws Exception {
        pegRows = (String) numOfGuessesComboBox.getValue();
        if (pegRows.equals("10  (D E F A U L T)"))
        {
            pegRows = "10";
        }

        dupC = (String) dupColorsComboBox.getValue();
        if (dupC.equals("T R U E  (D E F A U L T)"))
        {
            dupC = "true";
        }
        else
        {
            dupC = "false";
        }

        blankS = (String) blankSpaceComboBox.getValue();
        if (blankS.equals("F A L S E  (D E F A U L T)"))
        {
            blankS = "false";
        }
        else
        {
            blankS = "true";
        }

        CreateSettings g = new CreateSettings(startGameButton.getScene(), "4", dupC, pegRows, blankS);
        g.setSettings();

        Stage s = (Stage) startGameButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("gameStage.fxml"));
        s.setScene(new Scene(root));
        s.show();
        mouseEvent.consume();
    }
}
