package com.abyx.noticer;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class contains methods that enables you to save and load a bunch of Content-objects.
 * This class is typically used in the onResume() and onPause()-methods in an activity to
 * ensure proper saving and loading of previously created data.
 *
 * @author Pieter Verschaffelt
 */
public class IO {
    private Context context;
    private String filename;

    public IO(Context context){
        this.context = context;
        //Default savefile
        this.filename = "save.no";
    }

    public void save(List<Content> data){
        //Write all the app's data
        String start = "";
        for (Content content: data){
            start += content.getSaveRepresentation();
            start += "\n";
        }

        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            writer.write(start);
            writer.close();
        } catch (IOException e){
            throw new RuntimeException("Something went wrong while saving the data", e);
        }
    }

    public void saveOne(Content data){
        List<Content> temp = load();
        temp.add(data);
        save(temp);
    }

    public ArrayList<Content> load(){
        ArrayList<Content> output = new ArrayList<>();
        File file = context.getFileStreamPath(filename);

        if (file != null && file.exists()) {
            //Load all the app's data
            try {
                BufferedReader buffered = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                String line = buffered.readLine();
                while (line != null) {
                    String[] rawData = line.split("\t");
                    String[] dates = rawData[3].split("-");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
                    Content temp = new Content(rawData[0], rawData[1], new Currency(Double.parseDouble(rawData[2])), cal, Integer.parseInt(rawData[4]));
                    output.add(temp);
                    line = buffered.readLine();
                }
                buffered.close();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong while reading the file", e);
            }
        }
        return output;
    }

}
