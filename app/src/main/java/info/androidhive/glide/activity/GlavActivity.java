package info.androidhive.glide.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.androidhive.glide.R;

public class GlavActivity extends AppCompatActivity {
Button portfolioBtn,oNasBtn,enterBtn;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glav);
        portfolioBtn = (Button) findViewById(R.id.portfolioBtn);
        oNasBtn = (Button) findViewById(R.id.oNasBtn);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        password = (EditText) findViewById(R.id.password);

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(GlavActivity.this,FolderActivity.class);
//                startActivity(i);
                String pass = password.getText().toString();
                new ParseTask(pass).execute();
            }
        });

    }

    public class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = "";

        String pass;
        public ParseTask(String pass){
            this.pass = pass;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://193.124.117.95/get_password?password="+pass);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                jsonResult = builder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("JSON", jsonResult);
            return jsonResult;
        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            Log.e("TAG", json);

            JSONObject dataJsonObject;
            String secondName;
            int ItemId;
            try {
                dataJsonObject = new JSONObject(json);
               // JSONArray menus = dataJsonObject.getJSONArray("objects");

               // JSONObject secondObject = menus.getJSONObject(0);
                secondName = dataJsonObject.getString("result");
                Log.e("RESULT", secondName);
                if (secondName.equals("Something went wrong")){
                    Toast.makeText(GlavActivity.this, "Неправб", Toast.LENGTH_SHORT).show();
                    password.setError("неправильно");
                }else{
                    Intent i = new Intent(GlavActivity.this,FolderActivity.class);
                    i.putExtra("id",secondName);
                startActivity(i);
                }
//                Folder folder = new Folder();
//                folder.setName(secondName);
//                list.add(folder);

//                Log.d(TAG, "Второе имя: " + secondName);
//
//                for (int i = 0; i < menus.length(); i++) {
//                    JSONObject menu = menus.getJSONObject(i);
//
//                    secondName = menu.getString("name");
//                    ItemId = menu.getInt("id");
//                    Folder folder = new Folder();
//                    folder.setName(secondName);
//                    folder.setID(ItemId);
//                    list.add(folder);
//
//                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
