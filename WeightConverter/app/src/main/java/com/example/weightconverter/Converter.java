package com.example.weightconverter;


public class Converter {
    private int inputUnitID;  // 0 to 4 (oz lb g kg ton)
    private int outputUnitID;  // 0 to 4 (oz lb g kg ton)
    private String inputUnit;
    private String outputUnit;
    private double inputVal;
    private double outputVal;
    private double convertRatio;
    private double[][] convertTable = new double[5][5];

    public Converter() {
        this.inputUnit = "oz";
        this.outputUnit = "oz";
        this.outputUnitID = 0;
        this.outputUnitID = 0;
        this.convertRatio = 1.0;
        this.inputVal = 0.0;
        this.outputVal = 0.0;
        initialConvertTable(convertTable);
    }

    public int setInputUnitID(int inputUnitID) {
        if (inputUnitID >= 0 && inputUnitID <= 4) {
            this.inputUnitID = inputUnitID;
            this.convertRatio = convertTable[this.inputUnitID][this.outputUnitID];
            return 0;
        } else {
            return -1;
        }
    }

    public int setOutputUnitID(int outputUnitID) {
        if (outputUnitID >= 0 && outputUnitID <= 4) {
            this.outputUnitID = outputUnitID;
            this.convertRatio = convertTable[this.inputUnitID][this.outputUnitID];
            return 0;
        } else {
            return -1;
        }
    }

    public void setOutputUnit(String outputUnit) {
        this.outputUnit = outputUnit;
    }

    public void setInputUnit(String inputUnit) {
        this.inputUnit = inputUnit;
    }

    public void setInputVal(double inputVal) {
        this.inputVal = inputVal;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public String getOutputUnit() {
        return outputUnit;
    }

    public double getInputVal() {
        return inputVal;
    }

    public double getOutputVal() {
        return outputVal;
    }

    public double getConvertRatio() {
        return convertRatio;
    }

    public void calculateOutputVal() {
        outputVal = inputVal * convertRatio;
        if (outputVal > 0.1d) {
            outputVal = Math.round(outputVal * 100.0d) / 100.0d;  // 2 decimal points round off
        } else {
            outputVal = Math.round(outputVal * 100000.0d) / 100000.0d;  // 5 decimal points round off
        }
    }
    private static void initialConvertTable(double array[][]) {
        // Initialize the first row of the conversion table. (The input unit is oz.)
        array[0][0] = 1.0d;  // oz to oz
        array[0][1] = 1.0d / 16.0d;  // oz to lb
        array[0][2] = 28.34d;  // oz to gram
        array[0][3] = 28.34d / 1000.0d;  // oz to kilogram
        array[0][4] = 1.0d / (16.0d * 2000.0d);  // oz to ton

        // Initialize the other rows
        for (int i=1; i<5; i++) {
            for (int j=0; j<5; j++) {
                array[i][j] = array[0][j];
            }
        }
        // The input unit is lb
        for (int j=0; j<5; j++) {
            array[1][j] = array[1][j] * 16.0d;
        }
        // The input unit is g
        for (int j=0; j<5; j++) {
            array[2][j] = array[2][j] / 28.34d;
        }
        // The input unit is kg
        for (int j=0; j<5; j++) {
            array[3][j] = array[3][j] * 1000.0d / 28.34d;
        }
        // The input unit is ton
        for (int j=0; j<5; j++) {
            array[4][j] = array[4][j] * 16.0d * 2000.0d;
        }
    }

}
