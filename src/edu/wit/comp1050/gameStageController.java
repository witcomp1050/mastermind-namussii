package edu.wit.comp1050;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gameStageController implements codeMaker, codeBreaker
{
    @FXML
    public Circle redCircle;
    @FXML
    public Circle orangeCircle;
    @FXML
    public Circle yellowCircle;
    @FXML
    public Circle greenCircle;
    @FXML
    public Circle blueCircle;
    @FXML
    public Circle purpleCircle;
    @FXML
    public VBox guessVBox;
    @FXML
    public VBox pegsVBox;
    @FXML
    public Button makeGuessButton;
    @FXML
    public Button deleteButton;
    @FXML
    public HBox colorPegsHBox;

    private Circle[] guessPegs;
    private Circle[] guessCircles;
    private ArrayList<Integer> code;
    private int[] numGuess;

    private int cIndex;
    private int rIndex;
    private int guessMax;

    private boolean dup;
    private boolean blanks;
    private int codeMax;
    private int codeNum;

    public gameStageController()
    {
        guessCircles = new Circle[4];
        guessPegs = new Circle[4];
        cIndex = 0;
        rIndex = 0;
        dup = false;
    }

    public void initialize(){
        initGuessPegs(rIndex);
        initGuessRow(rIndex);

        var fName = "mastermind.properties";
        try (BufferedReader br = new BufferedReader(new FileReader(fName))) {
            ArrayList<String> settingTitle = new ArrayList<>();
            ArrayList<String> settingAnswer = new ArrayList<>();

            String word;
            while ((word = br.readLine()) != null) {
                String[] y = word.split("=");

                settingTitle.add(y[0]);
                settingAnswer.add(y[1]);
            }

            for (int i = 0; i < settingTitle.size(); i++) {
                if (settingTitle.get(i).equals("codeSize")) {
                    codeNum = Integer.parseInt(settingAnswer.get(i));
                } else if (settingTitle.get(i).equals("codePegRows")) {
                    guessMax = Integer.parseInt(settingAnswer.get(i));
                } else if (settingTitle.get(i).equals("dupsAllowedInCode")) {
                    if (settingAnswer.get(i).equals("true"))
                    {
                        dup = true;
                    }
                    else
                    {
                        dup = false;
                    }
                } else if (settingTitle.get(i).equals("blanksAllowedInCode")) {
                    if (settingAnswer.get(i).equals("true")) {
                        blanks = true;
                        codeMax = 7;
                    } else {
                        blanks = false;
                        codeMax = 6;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("settings file not found");
        }

        if (blanks)
        {
            createBlank();
        }

        code = getCode(dup, blanks);
        numGuess = new int[codeNum];

        for (int e: code)
        {
            System.out.println(e);
        }

        redCircle.setOnMouseClicked(mouseEvent -> {
            guessCircles[cIndex].setFill(redCircle.getFill());
            cIndex++;
        });

        orangeCircle.setOnMouseClicked(mouseEvent ->
        {
            guessCircles[cIndex].setFill(orangeCircle.getFill());
            cIndex++;
        });

        yellowCircle.setOnMouseClicked(mouseEvent ->
        {
            guessCircles[cIndex].setFill(yellowCircle.getFill());
            cIndex++;
        });

        greenCircle.setOnMouseClicked(mouseEvent -> {
            guessCircles[cIndex].setFill(greenCircle.getFill());
            cIndex++;
        });

        blueCircle.setOnMouseClicked(mouseEvent -> {
            guessCircles[cIndex].setFill(blueCircle.getFill());
            cIndex++;
        });

        purpleCircle.setOnMouseClicked(mouseEvent -> {
            guessCircles[cIndex].setFill(purpleCircle.getFill());
            cIndex++;
        });

        makeGuessButton.setOnMouseClicked(mouseEvent -> {
            rIndex++;
            cIndex = 0;
            convertGuesses();
            if(getCorrectGuesses(numGuess,code) == 4 )
            {
                Stage currentS = (Stage) makeGuessButton.getScene().getWindow();
                currentS.close();
                Label win = new Label("Y O U   W O N !");
                win.setAlignment(Pos.CENTER);

                Button b = new Button("P L A Y   A G A I N");

                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                v.setSpacing(10);
                v.getChildren().add(win);
                v.getChildren().add(b);

                Scene s = new Scene(v, 500, 300);

                Stage stage = new Stage();
                stage.setScene(s);

                stage.show();
                b.setOnMouseClicked(mouseEvent1 -> {
                    try {
                        Stage x = (Stage)makeGuessButton.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("optionStage.fxml"));
                        x.setScene(new Scene(root));
                        x.show();
                        stage.close();
                    } catch (IOException exception)
                    {
                        exception.printStackTrace();
                    }
                });
            }
            else if(rIndex == guessMax)
            {
                Stage currentS = (Stage) makeGuessButton.getScene().getWindow();
                currentS.close();

                Label lost = new Label("Y O U   L O S T");
                lost.setAlignment(Pos.CENTER);

                Button b = new Button("P L A Y   A G A I N");

                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                v.setSpacing(10);
                v.getChildren().add(lost);
                v.getChildren().add(b);

                Scene s = new Scene(v, 500, 300);

                Stage stage = new Stage();
                stage.setScene(s);

                stage.show();
                b.setOnMouseClicked(mouseEvent1 -> {
                    try {
                        Stage x = (Stage)makeGuessButton.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("optionStage.fxml"));
                        x.setScene(new Scene(root));
                        x.show();
                        stage.close();
                    } catch (IOException exception)
                    {
                        exception.printStackTrace();
                    }
                });
            }
            else
            {
                int correctG = getCorrectGuesses(numGuess, code);
                int correctC = getCorrectColors(numGuess, code);

                System.out.println("correct guesses: " + correctG);
                System.out.println("correct colors: " + correctC);

                for (int i = 0; i < 4; i++)
                {
                    Circle y = guessPegs[i];
                    if(correctG >0) {
                        y.setFill(Color.BLACK);
                        correctG--;
                    }
                    else if(correctC > 0)
                    {
                        y.setFill(Color.GRAY);
                        correctC--;
                    }
                }

                makeGuessRow();
            }
        });

        deleteButton.setOnMouseClicked(mouseEvent ->
        {
            if (cIndex > 0) {
                cIndex--;
                guessCircles[cIndex].setFill(Color.WHITE);
            }
        });
    }

    private void initGuessPegs(int row)
    {
        FlowPane f = (FlowPane) pegsVBox.getChildren().get(row);

        for (int i = 0; i < guessPegs.length; i++)
        {
            guessPegs[i] = (Circle) f.getChildren().get(i);
        }
    }

    private void initGuessRow(int row)
    {
        HBox h = (HBox) guessVBox.getChildren().get(row);

        for (int i = 0; i < guessCircles.length; i++)
        {
            guessCircles[i] = (Circle) h.getChildren().get(i);
        }
    }

    private void convertGuesses()
    {
        for(int i = 0; i < numGuess.length; i++)
        {
            if(guessCircles[i].getFill().equals(redCircle.getFill()))
            {
                numGuess[i] = 0;
            }
            else if(guessCircles[i].getFill().equals(orangeCircle.getFill()))
            {
                numGuess[i] = 1;
            }
            else if(guessCircles[i].getFill().equals(yellowCircle.getFill()))
            {
                numGuess[i] = 2;
            }
            else if(guessCircles[i].getFill().equals(greenCircle.getFill()))
            {
                numGuess[i] = 3;
            }
            else if(guessCircles[i].getFill().equals(blueCircle.getFill()))
            {
                numGuess[i] = 4;
            }
            else if(guessCircles[i].getFill().equals(purpleCircle.getFill()))
            {
                numGuess[i] = 5;
            }
            else if (guessCircles[i].getFill().equals(Color.GRAY))
            {
                numGuess[i] = 6;
            }
        }
    }

    private void makeGuessRow()
    {
        HBox temp = new HBox();
        temp.setAlignment(Pos.CENTER);
        temp.maxHeight(67);
        temp.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        temp.prefWidth(326);
        temp.setSpacing(20);

        for (int i = 0; i < 4; i++) {
            Circle c = new Circle();
            c.setRadius(20);
            c.setFill(Color.WHITE);
            c.setId("circle" + rIndex + i);
            c.setStroke(Color.BLACK);
            temp.getChildren().add(c);
        }
        guessVBox.getChildren().add(temp);

        initGuessRow(rIndex);

        FlowPane p = new FlowPane();
        p.setAlignment(Pos.CENTER);
        p.setHgap(2.5);
        p.setVgap(2.5);
        p.setMaxHeight(67);

        for (int i = 0; i < 4; i++)
        {
            Circle c = new Circle();
            c.setRadius(10);
            c.setFill(Color.WHITE);
            c.setStroke(Color.BLACK);
            p.getChildren().add(c);
        }
        pegsVBox.getChildren().add(p);

        initGuessPegs(rIndex);
    }

    @Override
    public int getCorrectGuesses(int[] guess, ArrayList<Integer> c) {
        int count = 0;
        for (int i = 0; i < guess.length; i++)
        {
            if (guess[i] == c.get(i))
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getCorrectColors(int[] guess, ArrayList<Integer> c) {
        int count = 0;

        List<Integer> gList = new ArrayList<>();
        List<Integer> cList = new ArrayList<>();
        for (int i = 0; i < guess.length; i++)
        {
            cList.add(c.get(i));
            gList.add(guess[i]);
        }

        for (int i = 0; i < guess.length; i++)
        {
            if (guess[i]==c.get(i))
            {
                gList.set(i, -1);
                cList.set(i, -2);
            }
        }

        for (int i = 0; i < gList.size(); i++)
        {
            if (gList.contains(cList.get(i)))
            {
                count++;
                gList.set(i, -1);
                cList.set(i, -2);
            }
        }

        return count;
    }

    @Override
    public ArrayList<Integer> getCode(boolean dup, boolean blank) {
        ArrayList<Integer> x = new ArrayList<>();

        Random r = new Random();

        if(!dup) {
            x.add(r.nextInt(codeMax));

            for (int i = 1; i < 4; i++)
            {
                int temp;
                do {
                   temp = r.nextInt(codeMax);
                }  while(checkDuplicate(temp, x));

                x.add(temp);
            }
        }
        else
        {
            for(int i = 0; i < 4; i++)
            {
                x.add(r.nextInt(codeMax));
            }
        }
        return x;
    }

    private boolean checkDuplicate(int x, ArrayList<Integer> a)
    {
        for (int i = 0; i < a.size(); i++)
        {
            if (a.get(i) == x)
            {
                return true;
            }
        }
        return false;
    }

    private void createBlank()
    {
        Circle blank = new Circle();
        blank.setRadius(25);
        blank.setFill(Color.GRAY);
        blank.setStroke(Color.BLACK);

        colorPegsHBox.getChildren().add(blank);

        blank.setOnMouseClicked(mouseEvent -> {
            guessCircles[cIndex].setFill(blank.getFill());
            cIndex++;
        });
    }
}