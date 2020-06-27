package com.sample.firebasecrud.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sample.firebasecrud.R;
import com.sample.firebasecrud.models.DataMahasiswa;

import java.util.UUID;

public class UpdateData extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 207;
    //Deklarasi Variable
    private EditText nimBaru, namaBaru, jurusanBaru, imageBaru;
    private Button update;
    private DatabaseReference database;
    private String cekNIM, cekNama, cekJurusan, cekImage = "";
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        getSupportActionBar().setTitle("Update Data");
        nimBaru = findViewById(R.id.new_nim);
        namaBaru = findViewById(R.id.new_nama);
        jurusanBaru = findViewById(R.id.new_jurusan);
        imageBaru = findViewById(R.id.new_image);
        update = findViewById(R.id.update);

        storageReference = FirebaseStorage.getInstance().getReference();

        database = FirebaseDatabase.getInstance().getReference();
        getData();

        imageBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mendapatkan Data Mahasiswa yang akan dicek
                cekNIM = nimBaru.getText().toString();
                cekNama = namaBaru.getText().toString();
                cekJurusan = jurusanBaru.getText().toString();

                //Mengecek agar tidak ada data yang kosong, saat proses update
                if (isEmpty(cekNIM) || isEmpty(cekNama) || isEmpty(cekJurusan) || isEmpty(cekImage)) {
                    Toast.makeText(UpdateData.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else {
                    //Mengecek apakah user memilih image baru atau tidak
                    if (imageBaru.getText().toString().equals(cekImage)) {
                        //jika user tidak memilih image baru
                        //melakukan update database
                        DataMahasiswa setMahasiswa = new DataMahasiswa();
                        setMahasiswa.setNim(nimBaru.getText().toString());
                        setMahasiswa.setNama(namaBaru.getText().toString());
                        setMahasiswa.setJurusan(jurusanBaru.getText().toString());
                        setMahasiswa.setImage(cekImage);
                        updateMahasiswa(setMahasiswa);
                    } else {
                        //jika user memilih image baru
                        //Melakukan upload image ke firebase storage
                        StorageReference reference = storageReference.child("image/" + UUID.randomUUID().toString());
                        reference.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //Melakukan update database setelah sukses upload ke firebase storage
                                        DataMahasiswa setMahasiswa = new DataMahasiswa();
                                        setMahasiswa.setNim(nimBaru.getText().toString());
                                        setMahasiswa.setNama(namaBaru.getText().toString());
                                        setMahasiswa.setJurusan(jurusanBaru.getText().toString());
                                        setMahasiswa.setImage(taskSnapshot.getMetadata().getReference().toString());
                                        updateMahasiswa(setMahasiswa);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdateData.this, "Error : " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    // Mengecek apakah ada data yang kosong, sebelum diupdate
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    //Menampilkan data yang akan di update
    private void getData() {
        final String getNIM = getIntent().getExtras().getString("dataNIM");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getJurusan = getIntent().getExtras().getString("dataJurusan");
        final String getImage = getIntent().getExtras().getString("dataImage");
        nimBaru.setText(getNIM);
        namaBaru.setText(getNama);
        jurusanBaru.setText(getJurusan);
        imageBaru.setText(getImage);
        cekImage = getImage;
    }

    //Proses Update data yang sudah ditentukan
    private void updateMahasiswa(DataMahasiswa mahasiswa) {
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Admin")
                .child("Mahasiswa")
                .child(getKey)
                .setValue(mahasiswa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nimBaru.setText("");
                        namaBaru.setText("");
                        jurusanBaru.setText("");
                        imageBaru.setText("");
                        Toast.makeText(UpdateData.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                        finish();
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
            imageBaru.setText(filePath.toString());
        }
    }
}