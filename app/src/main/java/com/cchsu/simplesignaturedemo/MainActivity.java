package com.cchsu.simplesignaturedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int WRITE_PERMISSION_REQUEST = 0;
    private boolean mDeniedStorageAccess = false;
    private String GIT_STRING = "Hello, Git";  // For Git Demo only
    private String GIT_STRING_1 = "Hello, Git 1";  // For Git Demo only

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 檢查允許使用儲存媒體寫入權限
        grantStorageermissionsThenStartScanning();
        if (mDeniedStorageAccess == true)
            return;

        final SignatureView signature = (SignatureView)findViewById(R.id.view_signature);
        Button btnClear = (Button)findViewById(R.id.btn_clear);
        Button btnSave = (Button)findViewById(R.id.btn_Save);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.clear();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = Bitmap.createBitmap(signature.getMeasuredWidth(), signature.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                signature.draw(new Canvas(bmp));
                creatBitmapFile(bmp, "aa.png");
            }
        });
    }

    /**
     * 保存簽名文件圖片
     * @param bmp Bitmap圖片對象
     * @param filename 保存的檔案名稱
     */
    public void creatBitmapFile(Bitmap bmp, String filename) {
        try {
            /**
            String pathname = Environment.getExternalStorageDirectory() + "/" + FILE_PATH;
            File file = new File(pathname);
            if(!file.exists()) { //判斷文件夾是否存在，不存在則創建
                file.mkdir();
            }
            file = new File(pathname + "/" + filename);
            if(!file.exists()) {
                file.createNewFile();
            }
             **/
            //FileOutputStream fileOut = new FileOutputStream(file);

            File mSDFile = Environment.getExternalStorageDirectory();  //取得SD卡儲存路徑
            //建立文件檔儲存路徑
            String filepath = mSDFile.getParent() + "/" + mSDFile.getName() + "/HandWritingLog";
            File file = new File(filepath);
            //若沒有檔案儲存路徑時則建立此檔案路徑
            if(!file.exists()) {
                file.mkdirs();
            }
            file = new File(filepath + "/" + filename);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
            //updatePhotos(file); //更新圖庫
            Toast.makeText(this, R.string.msg_save_succ, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("creatBitmapFile", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("creatBitmapFile", e.getMessage());
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * 打開圖庫
     */
    private void scanPicture() {

        Intent it_gallery = new Intent();
        it_gallery.addCategory(Intent.CATEGORY_OPENABLE);
        it_gallery.setType("image/*");
        startActivity(it_gallery);
    }

    /**
     * 發送廣播，圖庫更新照片
     * @param file 新增的圖片
     */
    private void updatePhotos(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    private void grantStorageermissionsThenStartScanning()
    {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, WRITE_PERMISSION_REQUEST);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mDeniedStorageAccess = false;
            else
                mDeniedStorageAccess = true;
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
