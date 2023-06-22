package org.firstinspires.ftc.teamcode.autorecorder;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * inspired by
 * https://github.com/StPaulAcademy/HOMAR-FTC-Library/blob/master/src/main/java/edu/spa/ftclib/internal/Log.java
 */

//writer to a .csv file
public class Log {
    private Writer fileWriter;//file writer
    private String line;//current line to be written
    private boolean logTime;//flag which tells us if this class should write the time by itself
    private long startTime;//the time of this object's creation
    private boolean disabled = false;//flag which tells us if the current object is disabled
    Log(String directoryPath,String filename, boolean logTime) {
        if (logTime) startTime = System.nanoTime();//if we log the time initialize startTime
        this.logTime = logTime;//initialization of logTime
        File directory = new File(directoryPath);//create directory object
        line = "";//initialize current line
        directory.mkdir();//create the working directory
        try {
            fileWriter = new FileWriter(directoryPath+"/"+filename+".csv");//open the file in which we log data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDisabled() {//checks if object is disabled
        return disabled;
    }

    public void setDisabled(boolean disabled) {//disables the object
        this.disabled = disabled;
    }

    public void close() {//closes the file
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {//writes the current line
        if (disabled) return;//if disabled do nothing
        try {
            if (logTime) {//if we log time add the time field
                long timeDifference = System.nanoTime()-startTime;
                line = timeDifference/1E9+","+line;
            }
            fileWriter.write(line+"\n");//write the current line
            line = "";//empty line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addData(String data) {//adds data to the output
        if (disabled) return;//if disabled do nothing
        if (!line.equals("")) line += ",";//if line is not empty add a separating comma
        line += data;//add the current data
    }
    public void addData(Object data) {//adds a variable of type Object to output
        addData(data.toString());
    }
    public void addData(boolean data) {//adds a variable of type boolean to output
        addData(String.valueOf(data));
    }
    public void addData(byte data) {//adds a variable of type byte to output
        addData(String.valueOf(data));
    }
    public void addData(char data) {//adds a variable of type char to output
        addData(String.valueOf(data));
    }
    public void addData(short data) {//adds a variable of type short to output
        addData(String.valueOf(data));
    }
    public void addData(int data) {//adds a variable of type int to output
        addData(String.valueOf(data));
    }
    public void addData(long data) {//adds a variable of type long to output
        addData(String.valueOf(data));
    }
    public void addData(float data) {//adds a variable of type float to output
        addData(String.valueOf(data));
    }
    public void addData(double data) {//adds a variable of type double to output
        addData(String.valueOf(data));
    }
}