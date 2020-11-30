package edu.wit.comp1050;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class startStageController
{
    @FXML
    private Button startButton;

    public void initialize()
    {
        startButton.setOnMouseClicked(mouseEvent -> {
            try {
                Scene s = startButton.getScene();
                Stage stage = (Stage)s.getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("optionStage.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException exception)
            {
                exception.printStackTrace();
            }
        });
    }
}
