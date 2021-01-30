package uni.fmi.masters.mycoolapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    static final int TAKE_PICTURE = 666;

    ImageView avatarIV;
    EditText usernameET;
    EditText passwordET;
    EditText repeatPasswordET;
    EditText firstNameET;
    EditText lastNameET;
    RadioGroup genderRG;
    EditText otherET;
    Button registerB;
    Button cancelB;
    String gender;

    private RadioGroup.OnCheckedChangeListener onCheckedChanged = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.otherRadioButton){
                otherET.setEnabled(true);
                gender = null;
            }else{
                RadioButton rb = findViewById(checkedId);
                gender = rb.getText().toString();
                otherET.setText("");
                otherET.setEnabled(false);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        avatarIV = findViewById(R.id.avatarImageView);
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        repeatPasswordET = findViewById(R.id.repeatPasswordEditText);
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        genderRG = findViewById(R.id.genderRadioGroup);
        otherET = findViewById(R.id.otherEditText);
        registerB = findViewById(R.id.registerButton);
        cancelB = findViewById(R.id.cancelButton);

        otherET.setEnabled(false);
        avatarIV.setOnLongClickListener(onLongClick);

        registerB.setOnClickListener(onClick);
        cancelB.setOnClickListener(onClick);

        genderRG.setOnCheckedChangeListener(onCheckedChanged);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.registerButton){
                if(usernameET.getText().length() > 0
                        && passwordET.getText().length() > 0
                        && passwordET.getText().toString().equals(repeatPasswordET.getText().toString())){

                    String username = usernameET.getText().toString();
                    String password = passwordET.getText().toString();
                    String firstName = firstNameET.getText().toString();
                    String lastName = lastNameET.getText().toString();

                    if(gender == null && otherET.getText().length() > 0){
                        gender = otherET.getText().toString();
                    }

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setGender(gender);

                    new RegisterAsyncTask(user).execute();

                }else{
                    Toast.makeText(RegisterActivity.this,
                            "Please provide all the necessary information!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    View.OnLongClickListener onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(pictureIntent, TAKE_PICTURE);
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap avatarImage = (Bitmap) extras.get("data");

            avatarIV.setImageBitmap(avatarImage);
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        boolean isSuccess = false;

        ProgressDialog dialog;

        RegisterAsyncTask(User user){
            this.user = user;
            dialog = new ProgressDialog(RegisterActivity.this);
        }

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Registration in progress...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.format("http://78.130.252.70:8989/RegisterUser?" +
                    "username=%s&password=%s&gender=%s&fname=%s&lname=%s", user.getUsername(),
                    user.getPassword(), user.getGender(), user.getFirstName(), user.getLastName());

            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String result = reader.readLine();

                if(result != null){
                    if(result.contains("true")){
                        isSuccess = true;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.hide();

            if(isSuccess){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }

}