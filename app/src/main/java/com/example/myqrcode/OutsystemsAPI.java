package com.example.myqrcode;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class OutsystemsAPI extends AppCompatActivity {
    public interface VolleyCallback {
        void onSuccess(String result);
        void onError(String error);
    }

    static String apiUrl = "https://personal-8o07igno.outsystemscloud.com/AIDS/rest/RestAPI/";

    public static void verifyReservation(String hash, int roomId, Context context, VolleyCallback callback) {
        String url = apiUrl + "VerifyReservation?hash=" + hash + "&roomId=" + roomId;

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
}


