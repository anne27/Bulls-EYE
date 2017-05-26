package com.example.anannyauberoi.testingcamtwo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static String TAG="MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba;
    static{
        System.loadLibrary("MyLibs");
        if (OpenCVLoader.initDebug())
            Log.i(TAG, "loaded successfully");
        else
            Log.i(TAG, "not loaded");
    }

    BaseLoaderCallback mLoaderCallBack=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    mRgba=new Mat();
                    //break;
                } break;
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
            //super.onManagerConnected(status);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView= (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (javaCameraView!=null)
            javaCameraView.disableView();
    }

    protected void onResume()
    {
        super.onResume();
        if (OpenCVLoader.initDebug()){
            Log.i(TAG, "LOADED SUCCESSFULLY");
            mLoaderCallBack.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else{
            Log.i(TAG, "NOT LOADED");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height)
    {
        //Initialise the Mat object here.
        mRgba=new Mat(height, width, CvType.CV_8UC4);
        //imgGray=new Mat(height, width, CvType.CV_8UC1);
        //imgCanny=new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped()
    {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        mRgba=inputFrame.rgba();
        //Imgproc.Canny(mRgba,imgCanny,50,150);
        //Imgproc.cvtColor(mRgba,imgGray,Imgproc.COLOR_RGB2GRAY);
        //return imgCanny;
        cvClass.detect(mRgba.getNativeObjAddr());
        return mRgba;
    }
}
