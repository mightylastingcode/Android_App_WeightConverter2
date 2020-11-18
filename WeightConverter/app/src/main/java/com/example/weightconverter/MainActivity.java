//===============================================================================================
//    The MIT License (MIT)
//    Copyright ©2020 Michael Chi Li
//
//    Permission is hereby granted, free of charge, to any person obtaining a copy of
//    this software and associated documentation files (the “Software”),to deal in the Software
//    without restriction, including without limitation the rights to use, copy, modify, merge,
//    publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
//    to whom the Software is furnished to do so, subject to the following conditions:
//
//    The above copyright notice and this permission notice shall be included in all copies
//    or substantial portions of the Software.
//
//    THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
//    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
//    PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
//    FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
//    OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
//    DEALINGS IN THE SOFTWARE.
//===============================================================================================
//    Project Name: Weight Converter
//    Author: Michael C Li
//    Created Date: 10-28-2020
//    Version: First Beta Version
//
//    The app converts the weight input from oz to gram.
//
//    SDK: Android 4.2 (Jelly Bean) API 17
//    Tool: Android Studio 4.0.1
//    Language: Java
//
//    Tested device: Motorola G Power (2010)  OS Android 10.0 (Android Q)
//
//    Step 1: Press the CLEAR button to clear the input box.
//    Step 2: Enter a decimal value (in oz) of the weight (i.e. 2.5)
//    Step 3: Press the CONVERT button to get the decimal value (in gram).  The input box will
//    show "2.5 oz = 70.87 gram"
//
//    Note: If you enter a bad input (i.e. abc), nothing will happen because the converter cannot
//    convert an invalid input.
//===============================================================================================

package com.example.weightconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.weightconverter.R;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//https://developer.android.com/guide/topics/ui/controls/spinner
//https://www.tutorialspoint.com/android/android_spinner_control.htm


public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    private static final String TAG = "MainActivity";

    // the name used to refer to widgets in the layout
    private EditText userInput;
    private Button convertButton;
    private Button clearButton;
    private TextView resultList;

    private final String TEXT_CONTENTS = "TextContents";  // Used to store resultList content
    private Converter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: in.");
        Log.d(TAG, "onCreate: new Converter.java file.");
        userInput = (EditText) findViewById(R.id.editText);
        convertButton = (Button) findViewById(R.id.convert);
        clearButton = (Button) findViewById(R.id.clear);
        resultList = (TextView) findViewById(R.id.textView);
        converter = new Converter();

        resultList.setText("");
        resultList.setMovementMethod(new ScrollingMovementMethod()); // enable the vertical scroll bar

        // Spinner element
        Spinner inputUnitSpinner = (Spinner) findViewById(R.id.inputUnit);
        Spinner outputUnitSpinner = (Spinner) findViewById(R.id.outputUnit);

        // Spinner click listener
        inputUnitSpinner.setOnItemSelectedListener(this);
        outputUnitSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("oz");
        categories.add("lb");
        categories.add("g");
        categories.add("kg");
        categories.add("ton");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        inputUnitSpinner.setAdapter(dataAdapter);
        outputUnitSpinner.setAdapter(dataAdapter);

        View.OnClickListener ourOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = userInput.getText().toString();
                String result;
                Log.d(TAG, "onClick convert button: in.");
                Log.d(TAG, "onClick convert button: User Input Text is " + userText);
                try {
                    converter.setInputVal(Double.valueOf(userText));
                    Log.d(TAG, "onClick convert button: User Input weigh is " + converter.getInputVal() + " " + converter.getInputUnit());
                    converter.calculateOutputVal();
                    Log.d(TAG, "onClick convert button: User Input weigh is " + converter.getOutputVal() + " " + converter.getOutputUnit());
                    result = String.valueOf(converter.getInputVal()) + " " + converter.getInputUnit() + " = " + String.valueOf(converter.getOutputVal()) + " " + converter.getOutputUnit();
                    resultList.append(result + "\n");
                    userInput.setText("");
                } catch (NumberFormatException e) {
                    Log.d(TAG, "onClick convert button: User Input num is not a float number.");
//                    System.out.println("Exception thrown  :" + e);
                    userInput.setText("Nonvalid input!");
                }
                Log.d(TAG, "onClick convert button: in.");
            }
        };

        View.OnClickListener clearOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick clear button: in.");
                userInput.setText("");
                Log.d(TAG, "onClick clear button: out.");
            }
        };

        convertButton.setOnClickListener(ourOnClickListener);
        clearButton.setOnClickListener(clearOnClickListener);
        Log.d(TAG, "onCreate: out.");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: in.");
        Log.d(TAG, "onItemSelected: parent = " + parent.toString());

        Log.d(TAG, "onItemSelected: inputUnit =  " + parent.toString().contains("inputUnit"));
        Log.d(TAG, "onItemSelected: outputUnit =  " + parent.toString().contains("outputUnit"));

        String item = parent.getItemAtPosition(position).toString();

        String parentName = "";
        if (parent.toString().contains("inputUnit")) {
            parentName = "inputUnit";
            converter.setInputUnitID((int) id);
            converter.setInputUnit(item);
        } else if (parent.toString().contains("outputUnit")) {
            parentName = "outputUnit";
            converter.setOutputUnitID((int) id);
            converter.setOutputUnit(item);
        } else {
            parentName = "unknown";
        }
        Log.d(TAG, "onItemSelected: id = " + id + "(" + parentName + ")");
        // On selecting a spinner item
        Log.d(TAG, "onItemSelected: selected item = " + item + "(" + parentName + ")");
        // Showing selected spinner item for a short time.
        Toast.makeText(parent.getContext(), "Selected: " + item + "(" + parentName + ")", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onItemSelected: out.");
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onNothingSelected: in.");

        Log.d(TAG, "onNothingSelected: out.");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: in");
        super.onRestoreInstanceState(savedInstanceState);
        // Must retrieve the view text after super.onRestoreInstanceState().
        String savedString = savedInstanceState.getString(TEXT_CONTENTS);
        resultList.setText(savedString);
        Log.d(TAG, "onRestoreInstanceState: out");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: in");
        // Must save the view text before super.onSaveInstanceState().
        outState.putString(TEXT_CONTENTS,resultList.getText().toString());  // save the key and value pair
        // by the putString method.
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: out");
    }



}