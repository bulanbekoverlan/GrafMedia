package info.androidhive.glide.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import info.androidhive.glide.Folder;
import info.androidhive.glide.R;
import info.androidhive.glide.adapter.RVAdapter;

public class FolderActivity extends AppCompatActivity {
RecyclerView recyclerView;
    Folder folder;
    ArrayList<Folder> list;
    RVAdapter rvAdapter;
    String TAG= "TAG";
    String idd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        idd = getIntent().getStringExtra("id");
        new  ParseTask().execute();
    }

    public class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = "";

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://193.124.117.95/api/v1/subfolder/?format=json&main_folder__id="+idd);


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
            Log.e(TAG, json);

            JSONObject dataJsonObject;
            String secondName;
            int ItemId;
            try {
                dataJsonObject = new JSONObject(json);
                JSONArray menus = dataJsonObject.getJSONArray("objects");

                JSONObject secondObject = menus.getJSONObject(0);
//                secondName = secondObject.getString("name");
//                Folder folder = new Folder();
//                folder.setName(secondName);
//                list.add(folder);

//                Log.d(TAG, "Второе имя: " + secondName);
//
                for (int i = 0; i < menus.length(); i++) {
                    JSONObject menu = menus.getJSONObject(i);

                    secondName = menu.getString("name");
                    ItemId = menu.getInt("id");
                    Folder folder = new Folder();
                    folder.setName(secondName);
                    folder.setID(ItemId);
                    list.add(folder);

                }
                rvAdapter = new RVAdapter(FolderActivity.this, list);
                recyclerView.setAdapter(rvAdapter);

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
