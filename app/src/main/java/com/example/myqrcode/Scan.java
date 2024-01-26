package com.example.myqrcode;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myqrcode.databinding.ActivityScanBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Arrays;
import java.util.List;

public class Scan extends AppCompatActivity {

    private ActivityScanBinding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    showCamera();
                }
                else {

                }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result ->{
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            compareHashAndRoomId(result.getContents());
        }
    });

    private void setResult(String contents) {
        binding.textResult.setText(contents);
    }
    private void setQrImage(byte[] byteArray) {
        System.out.println(Arrays.toString(byteArray));
        if (byteArray != null) {
            Bitmap roomQrBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            binding.qrImage.setImageBitmap(roomQrBitmap);
        }
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR code");
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();

    }

    private void initViews() {
        binding.fab.setOnClickListener(view -> {
            int roomId = getIntent().getIntExtra("roomId", -1);

            if (roomId != -1) {
                checkPermissionAndShowActivity(this);
            } else {
                Toast.makeText(Scan.this, "Please, choose a room before trying to read Qr", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnGoToRoomList.setOnClickListener(view -> {
            Intent intent = new Intent(Scan.this, ActivityRoomList.class);
            startActivity(intent);
        });
        setResult(getIntent().getStringExtra("roomName"));
        setQrImage(getIntent().getByteArrayExtra("roomQr"));
    }

    private void checkPermissionAndShowActivity(Context context) {
        if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void compareHashAndRoomId(String scannedContent) {
        int roomId = getIntent().getIntExtra("roomId", -1);
            OutsystemsAPI.verifyReservation(scannedContent, roomId, this, new OutsystemsAPI.VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                    Toast.makeText(Scan.this, "API response: " + result, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<Room> result) {

                }

                @Override
                public void onError(String error) {

                    Toast.makeText(Scan.this, "API error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
    }
}