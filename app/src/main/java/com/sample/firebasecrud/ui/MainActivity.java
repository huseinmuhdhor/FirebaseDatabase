package com.sample.firebasecrud.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sample.firebasecrud.R;
import com.sample.firebasecrud.models.DataMahasiswa;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 207;

    //Deklarasi Variable
    private ProgressBar progressBar;
    private EditText NIM, Nama, Jurusan, Image;
    private Button Simpan, ShowData;
    private StorageReference storageReference;
    private DatabaseReference getReference;
    private Uri filePath;
    private String getNIM, getNama, getJurusan, getImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        //Inisialisasi ID (Button)
        Simpan = findViewById(R.id.save);
        Simpan.setOnClickListener(this);
        ShowData = findViewById(R.id.showdata);
        ShowData.setOnClickListener(this);

        //Inisialisasi ID (EditText)
        NIM = findViewById(R.id.nim);
        Nama = findViewById(R.id.nama);
        Jurusan = findViewById(R.id.jurusan);
        Image = findViewById(R.id.image);

        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    // Mengecek apakah ada data yang kosong
    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                //Mendapatkan Instance dari Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                //Menyimpan Data yang diinputkan User kedalam Variable
                getNIM= NIM.getText().toString();
                getNama = Nama.getText().toString();
                getJurusan = Jurusan.getText().toString();

                getReference = database.getReference(); // Mendapatkan Referensi dari Database

                // Mengecek apakah ada data yang kosong
                if(isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)){
                    //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
                    Toast.makeText(MainActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }else {
                    /*
                    Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
                    Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
                    */
                    StorageReference reference = storageReference.child("image/" + UUID.randomUUID().toString());
                    reference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    getImage = taskSnapshot.getMetadata().getReference().toString();
                                    saveData();
                                }
                            });
                }
                break;

            case R.id.showdata:
                startActivity(new Intent(MainActivity.this, MyListData.class));
                break;
        }
    }

    private void saveData() {
        getReference.child("Admin").child("Mahasiswa").push()
                .setValue(new DataMahasiswa(getNIM, getNama, getJurusan, getImage))
                .addOnSuccessListener(this, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                        NIM.setText("");
                        Nama.setText("");
                        Jurusan.setText("");
                        Image.setText("");
                        Toast.makeText(MainActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            Image.setText(filePath.toString());
        }
    }
}