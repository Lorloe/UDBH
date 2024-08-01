package com.example.appbanhang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.appbanhang.models.SanPham;
import com.example.appbanhang.models.ThuongHieu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Activity_ThemSP extends AppCompatActivity {
    EditText edttenSP,edtgiaSP,edtmotaSP,edtmathuonghieu,edtsoluongKho,edtgiaGoc;
    ImageView imvhinhSP;
    Button btnthemSP;
    DatabaseReference reference;
    ImageButton imgBack,imgcamera;
    DatabaseReference referenceTH;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    StorageReference storageReference;
    String hinhSP;
    ArrayList<ThuongHieu> listTH = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themsanpham);
        readData();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://udbh-c1d85.appspot.com");
        anhxa();
        btnthemSP.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!validateTenSP() || !validateGiaGoc() || !validateGiaSP() || !validateMaTH() || !validateSoLuongKho() || !validateMotaSP()){
                    return;
                }else {
                    themSP();
                }
            }
        });
        imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });
        imvhinhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void anhxa() {
        edttenSP = findViewById(R.id.edttenSP);
        edtgiaSP = findViewById(R.id.edtgiaSP);
        edtmotaSP = findViewById(R.id.edtmotaSP);
        edtmathuonghieu = findViewById(R.id.edtmathuonghieu);
        edtsoluongKho = findViewById(R.id.edtsoluongkho);
        edtgiaGoc = findViewById(R.id.edtgiaGoc);
        imvhinhSP = findViewById(R.id.imvhinhSP);
        btnthemSP = findViewById(R.id.btnthemSP);
        imgBack = findViewById(R.id.imgBack);
        imgcamera = findViewById(R.id.imgcamera);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void themSP(){
        AtomicBoolean isCheck = new AtomicBoolean();
        reference = FirebaseDatabase.getInstance().getReference().child("sanpham");
        String key = reference.push().getKey();
        String tenSP = edttenSP.getText().toString().trim();
        int giaSP = Integer.parseInt(edtgiaSP.getText().toString().trim());
        String motaSP = edtmotaSP.getText().toString().trim();
        String idTH = edtmathuonghieu.getText().toString().trim();
        int soluongKho = Integer.parseInt(edtsoluongKho.getText().toString().trim());
        int giaGoc = Integer.parseInt(edtgiaGoc.getText().toString().trim());
        listTH.forEach(thuongHieu -> {
            if (thuongHieu.getID().equals(idTH)){
                isCheck.set(true);
            }
        });
        if(!isCheck.get()){
            edtmathuonghieu.setError("Mã thương hiệu không đúng");
        }else {
            SanPham sanPham = new SanPham(key,tenSP,hinhSP,giaSP,motaSP,idTH,soluongKho,giaGoc);
            reference.child(key).setValue(sanPham);
            reference.child(key).child("soluong").removeValue();
            reference.child(key).child("yeuThich").removeValue();
            Toast.makeText(Activity_ThemSP.this,"Thêm sản phẩm thành công",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void readData() {
        referenceTH = FirebaseDatabase.getInstance().getReference().child("thuonghieu");
        referenceTH.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    ThuongHieu th = new ThuongHieu(key,"","");
                    listTH.add(th);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void askCameraPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                imvhinhSP.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(), contentUri);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                imvhinhSP.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }
    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("hihi", "onSuccess: Uploaded Image URl is " + uri.toString());
                        hinhSP = uri.toString();
                    }
                });
                Toast.makeText(Activity_ThemSP.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activity_ThemSP.this, "Upload Failled.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //     File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private Boolean validateTenSP (){
        String val = edttenSP.getText().toString();

        if(val.isEmpty()){
            edttenSP.setError("Vui lòng nhập tên sản phẩm");
            return false;
        }
        else {
            edttenSP.setError(null);
            return true;
        }
    }
    private Boolean validateMaTH (){
        String val = edtmathuonghieu.getText().toString();

        if(val.isEmpty()){
            edtmathuonghieu.setError("Vui lòng nhập mã thương hiệu");
            return false;
        }
        else {
            edttenSP.setError(null);
            return true;
        }
    }
    private Boolean validateGiaGoc (){
        String val = edtgiaGoc.getText().toString();

        if(val.isEmpty()){
            edtgiaGoc.setError("Vui lòng nhập giá gốc của sản phẩm");
            return false;
        }
        else {
            edtgiaGoc.setError(null);
            return true;
        }
    }
    private Boolean validateGiaSP (){
        String val = edtgiaSP.getText().toString();

        if(val.isEmpty()){
            edtgiaSP.setError("Vui lòng nhập giá sản phẩm");
            return false;
        }
        else {
            edtgiaSP.setError(null);
            return true;
        }
    }
    private Boolean validateMotaSP (){
        String val = edtmotaSP.getText().toString();

        if(val.isEmpty()){
            edtmotaSP.setError("Vui lòng nhập mô tả sản phẩm");
            return false;
        }
        else {
            edtmotaSP.setError(null);
            return true;
        }
    }
    private Boolean validateSoLuongKho (){
        String val = edtsoluongKho.getText().toString();

        if(val.isEmpty()){
            edtsoluongKho.setError("Vui lòng nhập số lượng kho");
            return false;
        }
        else {
            edtsoluongKho.setError(null);
            return true;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.aqua));
        }
    }
}
