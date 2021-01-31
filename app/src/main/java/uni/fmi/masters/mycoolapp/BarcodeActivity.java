package uni.fmi.masters.mycoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA_CODE = 666;
    SurfaceView surfaceView;
    TextView barcodeTV;
    Button okB;
    Button cancelB;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    ToneGenerator toneGenerator;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(R.id.okButton == v.getId()){
                Intent intent = new Intent();

                intent.setData(Uri.parse(barcodeTV.getText().toString()));

                setResult(RESULT_OK, intent);
            }else{
                setResult(RESULT_CANCELED);
            }

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        surfaceView = findViewById(R.id.surfaceView);
        barcodeTV = findViewById(R.id.barcodeTextView);
        okB = findViewById(R.id.okButton);
        cancelB = findViewById(R.id.cancelButton);

        okB.setOnClickListener(onClick);
        cancelB.setOnClickListener(onClick);

        toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
        init();
    }

    private void init(){
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(BarcodeActivity.this,
                                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {    }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() != 0){
                    barcodeTV.post(new Runnable() {
                        @Override
                        public void run() {
                            barcodeTV.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                }

            }
        });
    }
}