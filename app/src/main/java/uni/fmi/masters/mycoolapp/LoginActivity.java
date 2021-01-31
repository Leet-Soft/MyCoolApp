package uni.fmi.masters.mycoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;
    Button loginB;
    TextView registerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        loginB = findViewById(R.id.loginButton);
        registerTV = findViewById(R.id.registerTextView);

        loginB.setOnClickListener(onClick);
        registerTV.setOnClickListener(onClick);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.loginButton){

                String usernameInput = usernameET.getText().toString();
                String passwordInput = passwordET.getText().toString();
                new LoginAsynTask(usernameInput, passwordInput).execute();


            }else{
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }
    };


    private class LoginAsynTask extends AsyncTask<Void, Void, Void> {

        String username;
        String password;
        int loggedUserID = -1;

        ProgressDialog dialog;

        LoginAsynTask(String username, String password){
            this.username = username;
            this.password = password;
            dialog = new ProgressDialog(LoginActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setTitle("Login in...");
            dialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.format("http://78.130.252.70:8989/Login?username=%s&password=%s",
                    username, password);

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();

                if(line != null && !line.equals("")){
                    JSONObject object = new JSONObject(line);
                    loggedUserID = object.getInt("ID");
                }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (JSONException e) {
                e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.hide();

            if(loggedUserID != -1){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                intent.putExtra("userID", loggedUserID);
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, "Wrong credentials!", Toast.LENGTH_LONG).show();
            }
        }

    }
}