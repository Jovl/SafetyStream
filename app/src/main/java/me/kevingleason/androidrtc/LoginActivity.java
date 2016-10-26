package me.kevingleason.androidrtc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import me.kevingleason.androidrtc.util.Constants;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

/**
 * Login Activity for the first time the app is opened, or when a user clicks the sign out button.
 * Saves the username in SharedPreferences.
 */
public class LoginActivity extends Activity {

    private EditText FirstName;
    private EditText LastName;
    private EditText Address;
    private EditText City;
    private EditText ZipCode;
    private AutoCompleteTextView State;
    private EditText PhoneNumber;
    private EditText Race;
    private Spinner Gender;
    private EditText Age;
    private EditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initializes a variable to store the username, allows the user to not have to sign in a second time
        mUsername = (EditText) findViewById(R.id.FirstName);

        //initializes each textfield to a variable to manipulate the text field from java code
        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        Address = (EditText) findViewById(R.id.Address);
        City = (EditText) findViewById(R.id.City);
        ZipCode = (EditText) findViewById(R.id.ZipCode);
        Age = (EditText) findViewById(R.id.Age);
        //Sets up the State text field to be auto-filled
        State = (AutoCompleteTextView) findViewById(R.id.State);
        String[] states = getResources().getStringArray(R.array.State_Array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);
        State.setAdapter(adapter);
        PhoneNumber = (EditText) findViewById(R.id.PhoneNumber);
        Race = (EditText) findViewById(R.id.Race);
        //sets up the gender textfield to be a drop down list
        Gender = (Spinner) findViewById(R.id.GenderList);
        Spinner dropdown = (Spinner)findViewById(R.id.GenderList);
        String[] genders = new String[]{"Gender", "Male", "Female"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        dropdown.setAdapter(adapter2);


    }


    public void testButton (View view){
        //when the app is opened for the first time the user enters all their information. After the information has been entered,
        //it is stored and their first name is used as the username and stored so that in the future the user does not need to sign
        //in
        String username = mUsername.getText().toString(); 

            SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(Constants.USER_NAME, username);
            edit.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }
}
