package edu.wit.comp1050;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;

public class CreateSettings
{
    private Scene scene;
    private String codeSize;
    private String dupColors;
    private String numOfGuesses;
    private String blankSpaces;

    public CreateSettings(Scene x, String cS, String dC, String nOfG, String bS)
    {
        scene = x;
        codeSize = cS;
        dupColors = dC;
        numOfGuesses = nOfG;
        blankSpaces = bS;
    }

    public void setSettings() {
        var fName = "mastermind.properties";
        File file = new File(fName);
        try (BufferedReader br = new BufferedReader(new FileReader(fName))) {
            ArrayList<String> settingTitle = new ArrayList<>();
            ArrayList<String> settingAnswer = new ArrayList<>();

            String word;
            while ((word = br.readLine()) != null) {
                String[] y = word.split("=");

                settingTitle.add(y[0]);
                settingAnswer.add(y[1]);
            }

            PrintWriter print = new PrintWriter(file);

            for (int i = 0; i < settingTitle.size(); i++) {
                if (settingTitle.get(i).equals("codeSize")) {
                    settingAnswer.set(i, codeSize);
                } else if (settingTitle.get(i).equals("codePegRows")) {
                    settingAnswer.set(i, numOfGuesses);
                } else if (settingTitle.get(i).equals("dupsAllowedInCode")) {
                    settingAnswer.set(i, dupColors);
                } else if (settingTitle.get(i).equals("blanksAllowedInCode")) {
                    settingAnswer.set(i, blankSpaces);
                }
                print.print(settingTitle.get(i) + "=" + settingAnswer.get(i) + "\n");
            }

            print.close();

        } catch (IOException exception) {
            System.out.println("file not found");
        }
    }
}
