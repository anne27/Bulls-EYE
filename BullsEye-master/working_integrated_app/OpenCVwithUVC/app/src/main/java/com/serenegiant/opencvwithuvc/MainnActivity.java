package com.serenegiant.opencvwithuvc;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.core.CvType.CV_8UC4;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class MainnActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    SQLiteDatabase db;
    public static final int MEDIA_TYPE_IMAGE = 1;
    JavaCameraView javaCameraView;
    Mat mRgba, mGray, mstore;
    private Bitmap bitmap, newpic;
    private String ans;
    private final String tag = "Server";
    Button capture;
    Button excam;
    public boolean firstTime = true;
    public boolean stored = false;
    public boolean firstTime1 = true;
    public boolean flashison = false;
    private static final int SERVERPORT = 8000;
    private static final String SERVER_IP = "10.0.0.13";
    String newString;

    static {
        System.loadLibrary("LibsCV");
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainn);
        Bundle extras;
        if (savedInstanceState == null) {
            //fetching extra data passed with intents in a Bundle type variable
            extras = getIntent().getExtras();
            if (extras == null)
                newString = null;
            else
                newString = extras.get("mr_no").toString();
            Toast.makeText(getBaseContext(), newString, Toast.LENGTH_SHORT).show();
        }
        capture = (Button) findViewById(R.id.button3);
        capture.setEnabled(true);
        excam=(Button)findViewById(R.id.button4);
        excam.setEnabled(true);

        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.setMaxFrameSize(480, 320);
        //Size size=new Size();
        //size.width=480;
        //size.height=320;
    }

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_home) {
            javaCameraView.turnOffTheFlash();
            Intent it = new Intent(MainnActivity.this, FirstActivity.class);
            startActivity(it);
            finish();
            return true;
        }
        if (id == R.id.action_flash) {
            if (!firstTime1) {
                if (item.getTitle().equals("Turn On Flash")) {
                    javaCameraView.turnOnTheFlash();
                    flashison = true;
                    //if(save.getVisibility()==View.INVISIBLE) {
                    //  save.setVisibility(View.VISIBLE);
                    //}
                    item.setTitle("Turn Off Flash");
                } else {
                    javaCameraView.turnOffTheFlash();
                    flashison = false;
                    item.setTitle("Turn On Flash");
                }
                return true;
            } else {
                Toast.makeText(this, "Start Camera First", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(MainnActivity.this, FirstActivity.class);
        startActivity(it);
        finish();
    }

    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Successfully Loaded");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d(TAG, "FAILED");
            //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CV_8UC4);
        mGray = new Mat(height, width, CV_8UC1);
    }


    public static String TAG = "MainnActivity";

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        String ans = cvClass.detect(mGray.getNativeObjAddr());
        if (Double.parseDouble(ans) > 0.3)
            DetectIris.DetectCenter(mRgba.getNativeObjAddr(), mGray.getNativeObjAddr());
        Log.d(TAG, ans);
        capture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTime1 = false;
                if (firstTime) {
                    capture.setText("Capture");
                    firstTime = false;
                    stored = false;
                    onResume();
                } else {
                    capture.setEnabled(true);
                    firstTime = true;
                    capture.setText("Start");
                    storetheimage(mRgba);
                    onPause();
                    firstTime = true;
                }
            }
        });

        excam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1=new Intent(MainnActivity.this, MainActivity.class);
                startActivity(it1);
            }
        });

        return mRgba;
    }

    private void storetheimage(Mat mRgba) {
        final long currentTimeMillis = System.currentTimeMillis();
        //final String appName = getString(R.string.app_name);
        final String galleryPath =
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString();
        final String albumPath = galleryPath;
        final String photoPath = albumPath + "/" +
                currentTimeMillis + ".png";


// Ensure that the album directory exists.
        File album = new File(albumPath);
        //Mat mIntermediateMat=new Mat();
        if (!album.isDirectory() && !album.mkdirs()) {
            Log.e(TAG, "Failed to create album directory at " +
                    albumPath);
        }
        Mat mIntermediateMat = new Mat();
        Imgproc.cvtColor(mRgba, mIntermediateMat, Imgproc.COLOR_RGBA2BGR);
        //mRgba.convertTo(mRgba, CV_8UC1);
// Try to create the photo.
        if (!imwrite(photoPath, mIntermediateMat))
            Log.e(TAG, "Failed to save photo to " + photoPath);
        else
            Log.d(TAG, "Photo saved successfully to " + photoPath);
    }
}