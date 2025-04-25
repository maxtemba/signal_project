package com.data_management;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileDataListener implements DataListener {
    DataParser dataParser;


    @Override
    public void readData() {
        List<File> files = new ArrayList<>();

        File cholesterol = new File("output/Cholesterol.txt");
        File diastolicPressure = new File("output/DiastolicPressure.txt");
        File ECG = new File("output/ECG.txt");
        File RedBloodPressure = new File("output/RedBloodPressure.txt");
        File Saturation = new File("output/Saturation.txt");
        File SystolicPressure = new File("output/SystolicPressure.txt");
        File WhiteBloodPressure = new File("output/WhiteBloodPressure.txt");

        files.add(cholesterol);
        files.add(diastolicPressure);
        files.add(ECG);
        files.add(RedBloodPressure);
        files.add(Saturation);
        files.add(SystolicPressure);
        files.add(WhiteBloodPressure);

        parseData(files);
    }

    private void parseData(List<File> files) {
        dataParser = new DataParser();
        dataParser.parseData(files);
    }
}
