package com.example.myqrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class RoomAdapter extends ArrayAdapter<Room> {

    public RoomAdapter(Context context, List<Room> rooms) {
        super(context, 0, rooms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_item, parent, false);
        }

        Room room = getItem(position);

        TextView roomIdTextView = convertView.findViewById(R.id.roomId);
        TextView roomNameTextView = convertView.findViewById(R.id.roomTitle_item);
        ImageView qrView = convertView.findViewById(R.id.roomImage_item);
        Button loginButton = convertView.findViewById(R.id.loginButton);


        if (room != null) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(room.getId()),
                        BarcodeFormat.QR_CODE,
                        300,
                        300);

                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                roomIdTextView.setText("Room Id: " + room.getId());
                roomNameTextView.setText(room.getName());
                qrView.setImageBitmap(bitmap);

                loginButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), Scan.class);
                    intent.putExtra("roomId", room.getId());
                    intent.putExtra("roomName", room.getName());

                    byte[] byteArray = convertBitmapToByteArray(bitmap);

                    intent.putExtra("roomQr", byteArray);

                    getContext().startActivity(intent);
                });


            } catch (WriterException e) {
                throw new RuntimeException(e);
            }

        }
        return convertView;
    }
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}

