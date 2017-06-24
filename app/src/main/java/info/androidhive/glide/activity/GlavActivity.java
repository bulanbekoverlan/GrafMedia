package info.androidhive.glide.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import info.androidhive.glide.R;

public class GlavActivity extends AppCompatActivity {
Button portfolioBtn,oNasBtn;
    ImageView download;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glav);
        portfolioBtn = (Button) findViewById(R.id.portfolioBtn);
        oNasBtn = (Button) findViewById(R.id.oNasBtn);
        download = (ImageView) findViewById(R.id.download);
        dataBase = new DataBase(this);

        portfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlavActivity.this, MainActivity.class);
                intent.putExtra("id", 4);
                intent.putExtra("name", "");
                startActivity(intent);
            }
        });
        oNasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GlavActivity.this,AboutActivity.class);

                startActivity(i);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBase.readData();
                Cursor cursor = dataBase.getData();
                if (cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    String pass = cursor.getString(cursor.getColumnIndex(DataBase.PASSWORD_COLUMN));
                    Log.e("pass",pass);
                    Intent i = new Intent(GlavActivity.this,FolderActivity.class);
                    i.putExtra("id",pass);

                    startActivity(i);
                }else {
                    Intent i = new Intent(GlavActivity.this,EnterActivity.class);

                    startActivity(i);
                }

            }
        });




    }


}
