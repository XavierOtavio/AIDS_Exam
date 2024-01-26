package com.example.myqrcode;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ActivityRoomList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        ListView roomsListView = findViewById(R.id.roomsListView);

        OutsystemsAPI.getAllRooms(this, new OutsystemsAPI.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onSuccess(List<Room> result) {
                RoomAdapter roomAdapter = new RoomAdapter(ActivityRoomList.this, result);
                roomsListView.setAdapter(roomAdapter);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ActivityRoomList.this, "API error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
