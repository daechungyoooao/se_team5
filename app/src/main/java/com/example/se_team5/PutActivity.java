package com.example.se_team5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se_team5.item.AllItems;
import com.example.se_team5.item.Item;
import com.example.se_team5.item.ItemsAdapter;
import com.example.se_team5.ui.refrigerator.RefrigeratorFragment;

import java.io.DataOutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PutActivity extends AppCompatActivity {

    private static ArrayList<Item> ITEM_LIST = new ArrayList<Item>();
    private static ArrayList<Item> SELECTED_ITEMS = new ArrayList<Item>();
    private RecyclerView recyclerView;
    private ItemsAdapter myAdapter;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);

        SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);

        user_id = user.getString("user","");

        Button PutButton = findViewById(R.id.putButton);


        recyclerView = findViewById(R.id.putRecyclerView);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록

        myAdapter = new ItemsAdapter(new AllItems().getAllItem());
        recyclerView.setAdapter(myAdapter);  // Adapter 등록


        PutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //new putItemInRefrigerator(PutActivity.this).execute("/user/refrigerator", "{\"username\":\"hj323\",\"items\":[1,2]}");
            }
        });
    }

    private static class putItemInRefrigerator extends AsyncTask<String, Void, String> {

        private WeakReference<PutActivity> activityReference;
        putItemInRefrigerator(PutActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... params) {

            String response;

            HttpURLConnection httpURLConnection = null;
            try {
                HttpRequest req = new HttpRequest(MyGlobal.getData());
                return req.sendPost(params[0], params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // server와의 connection 해제
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return null;

        }
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            PutActivity activity = activityReference.get();

            if (activity == null) return;
            if(response == null) return;

            if (response.substring(0,3).equals("200")) {
                // 성공 시, 메인 화면 띄우기
                Toast.makeText(activity, "냉장고에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "오류: "+response.substring(0,3), Toast.LENGTH_SHORT).show();
            }
        }
    }
}