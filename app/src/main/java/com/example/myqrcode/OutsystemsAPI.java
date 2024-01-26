package com.example.myqrcode;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutsystemsAPI extends AppCompatActivity {
    public interface VolleyCallback {
        void onSuccess(String result);
        void onSuccess(List<Room> result);
        void onError(String error);
    }

    static String apiUrl = "https://personal-8o07igno.outsystemscloud.com/AIDS/rest/RestAPI/";

    public static void verifyReservation(String hash, int roomId, Context context, VolleyCallback callback) {
        String url = apiUrl + "RegisterExit?hash=" + hash + "&roomId=" + roomId;

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    callback.onSuccess(response);
                },
                error -> {
                    callback.onError(error.getMessage());
                });

        queue.add(stringRequest);
    }

    public static void getAllRooms(Context context, VolleyCallback callback) {
        String url = apiUrl + "GetAllRooms";

        ArrayList<Room> roomsList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("HTTPCode").equals("200")) {

                            JSONArray roomList = new JSONArray(obj.getString("RoomList"));

                            for (int i = 0; i < roomList.length(); i++) {
                                JSONObject roomObj = roomList.getJSONObject(i);
                                Room room = new Room(roomObj.getInt("Id"), roomObj.getString("Name"), roomObj.getString("Description"));
                                roomsList.add(room);
                            }

                            callback.onSuccess(roomsList);
                        } else {
                            Toast.makeText(context, obj.getString("Message"), Toast.LENGTH_SHORT).show();
                            callback.onError(obj.getString("Message"));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        callback.onError(e.getMessage());
                    }
                }, error -> {
            try {
                JSONObject obj = new JSONObject(error.getMessage());
                Toast.makeText(context, obj.getString("Message"), Toast.LENGTH_SHORT).show();
                callback.onError(obj.getString("Message"));
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onError(e.getMessage());
            }
        });

        queue.add(stringRequest);
    }
}


