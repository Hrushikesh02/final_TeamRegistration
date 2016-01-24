package com.example.hrushipc.stregapp;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
//import android.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;
public class FirstActivity extends AppCompatActivity implements View.OnClickListener {
    /*
    *assigning variable names(Fields) required
    */

    static EditText team_name, name1, entry1, name2, entry2, name3, entry3;
    static Button reset, submit,add_btn,remove_btn;
    static LinearLayout third_layout,add_layout;
    int no_of_members = 2;
    /*
    **OnCreate:
    *assigning the field handlers created above to corresponding UI elements
    *setting onClicklisteners to buttons and all those required
    * making teamname as custom font using the xml file created in fonts.xml
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        team_name = (EditText) findViewById(R.id.team_name);
        name1 = (EditText) findViewById(R.id.editText1);
        entry1 = (EditText) findViewById(R.id.editText2);
        name2 = (EditText) findViewById(R.id.editText3);
        entry2 = (EditText) findViewById(R.id.editText4);
        name3 = (EditText) findViewById(R.id.editText5);
        entry3 = (EditText) findViewById(R.id.editText6);
        reset = (Button) findViewById(R.id.reset);
        submit = (Button) findViewById(R.id.submit);
        add_btn=(Button) findViewById(R.id.add_btn);
        remove_btn=(Button) findViewById(R.id.remove_btn);
        third_layout=(LinearLayout)findViewById(R.id.third_layout);
        add_layout=(LinearLayout)findViewById(R.id.add_layout);


        Typeface team_font = Typeface.createFromAsset(getAssets(), "fonts/myfont.ttf");
        team_name.setTypeface(team_font);

        submit.setOnClickListener(FirstActivity.this);
        reset.setOnClickListener(FirstActivity.this);
        add_btn.setOnClickListener(FirstActivity.this);
        remove_btn.setOnClickListener(FirstActivity.this);
    }
    /*
    *adding exit option in options menu and deleting the default setting options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        menu.add(1, 2, 400, "Exit");
        menu.removeItem(R.id.action_settings);
        return true;
    }
    /*
    *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == 2) {
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        /*
        *setting every error to null on click of any button and checks for new errors and old errors
        * before clicking are disgarded this is to handle duplicate error as written in documentation
         */
        team_name.setError(null);
        entry1.setError(null);
        entry2.setError(null);
        entry3.setError(null);
        name1.setError(null);
        name2.setError(null);
        name3.setError(null);
        Button button = (Button) v;
        /*
        *togging visiblity when add or delete member is pressed
        *also resetting when reset is clicked
         */
        if(button.getText().toString().equalsIgnoreCase("ADD MEMBER")){
            add_layout.setVisibility(View.GONE);
            third_layout.setVisibility(View.VISIBLE);
            no_of_members = 3;
        }else if(button.getText().toString().equalsIgnoreCase("DELETE MEMBER")){
            add_layout.setVisibility(View.VISIBLE);
            third_layout.setVisibility(View.GONE);
            no_of_members = 2;
            name3.setText("");
            entry3.setText("");
        }else if (button.getText().toString().equalsIgnoreCase("Reset")) {
            team_name.setText("");
            name1.setText("");
            entry1.setText("");
            name2.setText("");
            entry2.setText("");
            name3.setText("");
            entry3.setText("");
        } else {
            String emptyNames = "";
            String invalidNames = "";
            /*
            *checking for special characters in name using regex package(matcher and patter classes)
             */
            Pattern special_char = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
            Matcher matcher_name1 = special_char.matcher(name1.getText().toString().trim());
            boolean arethere_char1 = matcher_name1.find();

            if (arethere_char1){
                name1.setError("special characters not allowed");
                invalidNames += name1.getText().toString().trim();
            }
            Matcher matcher_name2 = special_char.matcher(name2.getText().toString().trim());
            boolean arethere_char2 = matcher_name2.find();

            if (arethere_char2){
                name2.setError("special characters not allowed");
                if(invalidNames.equalsIgnoreCase("")){
                    invalidNames += name2.getText().toString().trim();
                }else {
                    invalidNames += ", " + name2.getText().toString().trim();
                }
            }
            Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
            Matcher matcher_name3 = special_char.matcher(name3.getText().toString().trim());
            boolean arethere_char3 = matcher_name2.find();

            if (arethere_char3){
                name3.setError("special characters not allowed");
                if(invalidNames.equalsIgnoreCase("")){
                    invalidNames += name3.getText().toString().trim();
                }else {
                    invalidNames += ", " + name3.getText().toString().trim();
                }
            }
            /*
            *checking whether name edit texts field is empty input and setting errors and adding to
            * final message string to be shown in toast accordingly
             */
            if (name1.getText().toString().trim().equalsIgnoreCase("")) {
                name1.setError("Empty!");
                emptyNames += "First Member ";
            }
            if (name2.getText().toString().trim().equalsIgnoreCase("")) {
                name2.setError("Empty!");
                emptyNames += "Second Member ";
            }
            if (name3.getText().toString().trim().equalsIgnoreCase("") && (no_of_members == 3)) {
                name3.setError("Empty!");
                emptyNames += "Third Member ";
            }
            /*
            *taking status messages processed by entryStatus method, name field status checked above
             *and adding them to the Toast to be shown
            *
            */
            String[] status = entryStatus();

            String finalMessage = "";
            if (team_name.getText().toString().trim().equalsIgnoreCase("")) {
                finalMessage += "Team name isn't chosen.";
                team_name.setError("Empty");
            }
            if (!emptyNames.equalsIgnoreCase("")) {
                finalMessage += "The name(s) of " + emptyNames;
            }
            if ((!finalMessage.equalsIgnoreCase("")) && (!status[0].equalsIgnoreCase(""))) {
                finalMessage += ",";
            }
            if (!status[0].equalsIgnoreCase("")) {
                finalMessage += "The entry number(s) of " + status[0];
            }
            if (!finalMessage.equalsIgnoreCase("")) {
                finalMessage += "is/are not chosen.";
            }
            if (!status[2].equalsIgnoreCase("")) {
                finalMessage += "The entry numbers of " + status[2] + "are same.";
            }
            if(!invalidNames.equalsIgnoreCase("")){
                finalMessage += "The Name(s) " + invalidNames + " is/are not allowed!";
            }
            if (!status[1].equalsIgnoreCase("")) {
                finalMessage += "The entry number(s) " + status[1] + " is/are not valid!";
            }
            if (!finalMessage.equalsIgnoreCase("")) {
                Toast.makeText(FirstActivity.this, finalMessage, Toast.LENGTH_LONG).show();
            } else {
                /*
                *making a POST object to be sent and sending and receiving the JSON response and
                * showing the message in Toast (even if there is any errors it will be shown
                 */
                Toast.makeText(FirstActivity.this,"now sending to server",Toast.LENGTH_LONG).show();
                String url = "http://agni.iitd.ernet.in/cop290/assign0/register/";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String response_status = jsonResponse.getString("RESPONSE_MESSAGE");
                                    String response_success = jsonResponse.getString("RESPONSE_SUCCESS");
                                    System.out.println(response_status);
                                    Toast.makeText(FirstActivity.this, response_status + " " + response_success, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(FirstActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(FirstActivity.this, error.getMessage()  , Toast.LENGTH_LONG).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("teamname", team_name.getText().toString().trim());
                        params.put("entry1", entry1.getText().toString().trim());
                        params.put("name1", name1.getText().toString().trim());
                        params.put("entry2", entry2.getText().toString().trim());
                        params.put("name2", name2.getText().toString().trim());
                        params.put("entry3", entry3.getText().toString().trim());
                        params.put("name3", name3.getText().toString().trim());
                        return params;

                    }
                };
                Volley.newRequestQueue(this).add(postRequest);


            }
        }
    }
    /*
    *maintains an array which indicates status of entry numbers
     * processes the given input in entrynumber edit text  checks if it empty or invalid etc and
     * gives an array of strings(produced by checking the array of ints) which can be added to Toast message.
     */
    public String[] entryStatus() {
        int flags[] = {2, 2, 2};
        String[] messages = {"", "", ""};
        if (entry1.getText().toString().trim().equalsIgnoreCase("")) {
            entry1.setError("Empty!");
            flags[0] = 1;
            messages[0] += "First Member ";
        }
        if (entry2.getText().toString().trim().equalsIgnoreCase("")) {
            flags[1] = 1;
            entry2.setError("Empty!");
            messages[0] += "Second Member ";
        }
        if (entry3.getText().toString().trim().equalsIgnoreCase("") &&(no_of_members == 3)) {
            flags[2] = 1;
            entry3.setError("Empty!");
            messages[0] += "Third Member";
        }
        if ((flags[0] != 1) && (flags[1] != 1) && (entry1.getText().toString().trim().equalsIgnoreCase(entry2.getText().toString().trim()))) {
            flags[0] = 3;
            flags[1] = 3;
            entry1.setError("duplicate!");
            entry2.setError("duplicate!");
            messages[2] += "First Member,Second Member";
        }
        if(no_of_members == 3) {
            if ((flags[1] != 1) && (flags[2] != 1) && (entry2.getText().toString().trim().equalsIgnoreCase(entry3.getText().toString().trim()))) {
                flags[1] = 3;
                flags[2] = 3;
                entry2.setError("duplicate!");
                entry3.setError("duplicate!");
                if (messages[2] == "") {
                    messages[2] += "Second Member, Third Member";
                } else {
                    messages[2] += ",Third Member";
                }
            }
            if ((flags[2] != 1) && (flags[0] != 1) && (entry3.getText().toString().trim().equalsIgnoreCase(entry1.getText().toString().trim()))) {
                flags[2] = 3;
                flags[0] = 3;
                entry1.setError("duplicate!");
                entry3.setError("duplicate!");
                if (messages[2] == "") {
                    messages[2] += "First Member, Third Member";
                }
            }
        }
        try {
            InputStream inStream = this.getResources().openRawResource(R.raw.entry_numbers);
            BufferedReader DataBase = new BufferedReader(new InputStreamReader(inStream));
            String entry_number = null;
            while ((entry_number = DataBase.readLine()) != null) {
                if (flags[0] == 2) {
                    if (entry1.getText().toString().trim().equalsIgnoreCase(entry_number)) {
                        flags[0] = 0;
                    }
                }
                if (flags[1] == 2) {
                    if (entry2.getText().toString().trim().equalsIgnoreCase(entry_number)) {
                        flags[1] = 0;
                    }
                }
                if (flags[2] == 2 &&(no_of_members == 3)) {
                    if (entry3.getText().toString().trim().equalsIgnoreCase(entry_number)) {
                        flags[2] = 0;
                    }
                }
            }
            if (flags[0] == 2) {
                entry1.setError("Incorrect");
                messages[1] += entry1.getText().toString().trim() + " ";
            }
            if (flags[1] == 2) {
                entry2.setError("Incorrect");
                messages[1] += entry2.getText().toString().trim() + " ";
            }
            if (flags[2] == 2 && (no_of_members == 3)) {
                entry3.setError("Incorrect");
                messages[1] += entry3.getText().toString().trim() + " ";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }

}
