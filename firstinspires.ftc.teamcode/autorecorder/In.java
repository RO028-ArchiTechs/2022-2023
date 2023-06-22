package org.firstinspires.ftc.teamcode.autorecorder;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.io.Reader;

/**
 * inspired by
 * https://github.com/StPaulAcademy/HOMAR-FTC-Library/blob/master/src/main/java/edu/spa/ftclib/internal/Log.java
 */

//parser for an input .csv file
public class In {
    private BufferedReader fileReader;//reader from input file
    private String line;//current line from input file
    private boolean disabled = false;//state of object
    In(String directoryPath,String filename) {
        File directory = new File(directoryPath);//getting the working directory
        directory.mkdir();// creating the directory
        try {
            fileReader = new BufferedReader(new FileReader(directoryPath+"/"+filename+".csv"));//try to create a file in the directory
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDisabled() { //check if disabled
         return disabled;
    }

    public void setDisabled(boolean disabled) {//sets the current object as disabled
        this.disabled = disabled;
    }

    public void close() {//closes the file
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {//reads next line from file
        if (disabled) return;//checks if the object is disabled
        try {
            line = fileReader.readLine();//reads the next line from input
            if(line == null){//if null then EOF was reached, so we disable the object
                this.setDisabled(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSData() {///gets the string coresponding to the first field in the csv formated line
        if (disabled) return "-1";//if disabled we return a neutral value
        String ans = "";//the answer string
        int id = 0;//index of the first unprocessed char
        while(id < line.length() && (line.charAt(id) != ',') && (line.charAt(id) != '\n')){//does this untill the current field ends
            ans = ans + line.charAt(id);//adds the current character
            id++;//goes to the next character
        }
        if(id + 1 >= line.length()){//if this was the last field, set the current line to null
            line = null;
        }
        else {//else delete the current field from the line
            line = line.substring(id + 1);
        }
        return ans;
    }

    public byte getByte() {//converts the current field string to byte
        return Byte.valueOf(getSData());
    }

    public Boolean getBoolean() {//converts the current field string to boolean
        return Boolean.valueOf(getSData());
    }

    public short getShort() {//converts the current field string to short
        return Short.valueOf(getSData());
    }

    public int getInt() {//converts the current field string to int
        return Integer.valueOf(getSData());
    }

    public long getLong() {//converts the current field string to long
        return Long.valueOf(getSData());
    }

    public float getFloat() {//converts the current field string to float
        return Float.valueOf(getSData());
    }
    public double getDouble() {//converts the current field string to double
        return Double.valueOf(getSData());
    }
}