package com.sample.firebasecrud.models;

public class DataMahasiswa {

    //Deklarasi Variable
    private String nim;
    private String nama;
    private String jurusan;
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

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public DataMahasiswa(){
    }

    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    public DataMahasiswa(String nim, String nama, String jurusan) {
        this.nim = nim;
        this.nama = nama;
        this.jurusan = jurusan;
    }
}
