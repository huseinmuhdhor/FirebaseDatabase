package com.sample.firebasecrud.models;

public class DataMahasiswa {

    //Deklarasi Variable
    private String nim;
    private String nama;
    private String jurusan;
    private String image;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public DataMahasiswa(){
    }

    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    public DataMahasiswa(String nim, String nama, String jurusan, String image) {
        this.nim = nim;
        this.nama = nama;
        this.jurusan = jurusan;
        this.image = image;
    }
}
