package com.example.anannyauberoi.appfinal;
import com.example.anannyauberoi.appfinal.R;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerUploadActivity extends Activity {

    private static String TAG="MainActivity";
    private Button buttonChoose,buttonUpload;
    private ImageView imageview;
    private EditText editText;

    private Uri filePath;
    //  private String KEY_IMAGE = "image";
    //  private String KEY_NAME = "name";
    private Bitmap bitmap,newpic;
    private byte[]  imgbyte;
    private static final int STORAGE_PERMISSION_CODE=1234;
    private static final int PICK_IMAGE_REQUEST=4321;
    private Socket socket;
    private static final int SERVERPORT = 8000;
    private static final String SERVER_IP="10.0.0.13";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serverupload);
        requestStoragePermission();
        buttonChoose=(Button)findViewById(R.id.buttonChoose);
        buttonUpload=(Button)findViewById(R.id.buttonUpload);
        imageview=(ImageView)findViewById(R.id.imageView);
        editText=(EditText)findViewById(R.id.editTextName);
        buttonChoose.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
         buttonUpload.setOnClickListener(new Button.OnClickListener() {
             @Override
             public void onClick(View v) {
                 uploadImage();
             }
         });
    }
    private void requestStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "You  denied the permission", Toast.LENGTH_LONG).show();
        }
    }
    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && data!=null && data.getData()!=null && resultCode==RESULT_OK)
        {
            filePath=data.getData();
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageview.setImageBitmap(bitmap);
                imgbyte = getBytesFromBitmap(bitmap);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }
    private String getPath(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    private void uploadImage() {
        Log.d(TAG,"I have entered");
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "HELLO");
                    Socket s = new Socket(SERVER_IP, SERVERPORT);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    //String pic = getStringFromBitmap(captured);
                    //dos.writeUTF(Integer.toString(pic.length()));
                    byte[] pic = getByteArrayFromBitmap(bitmap);
                    Log.d(TAG,pic.toString());
                    Log.d(TAG,Integer.toString(pic.length));
                    dos.writeUTF(Integer.toString(pic.length));


                    dos.flush();
                    dos.write(pic, 0, pic.length);
                    dos.flush();
                    //int x = dis.read(res);
                            /*for(int i=0;i<res.length;i++){
                                System.out.println(res[i]);
                            }*/

                    //System.out.println(x);
                    byte[] res = getPicture(dis);
                    newpic = getBitmapFromByteArray(res);
                    Log.d(TAG, "received" + newpic.getByteCount());
                    s.close();
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        //Toast.makeText(view.getContext(), "The message has been sent", Toast.LENGTH_SHORT).show();

    }
    private Handler handler = new Handler(){
        public void handleMessage(Message m){
            super.handleMessage(m);
            Log.d(TAG,"Here now");
            imageview.setImageBitmap(newpic);
        }
    };
    protected byte[] getByteArrayFromBitmap(Bitmap img){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        return b;
    }
    public byte[] getPicture(DataInputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int length = 0;
            while ((length = in.read(data))!=-1) {
                out.write(data,0,length);
            }
            return out.toByteArray();
        } catch(IOException ioe) {
            //handle it
        }
        return null;
    }
    protected Bitmap getBitmapFromByteArray(byte[] res){
        Bitmap bmp = BitmapFactory.decodeByteArray(res, 0, res.length);
        return bmp;
    }


}
