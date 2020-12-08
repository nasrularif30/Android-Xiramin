package com.example.coba;

public class Result {
    public String Kipas;
    public Float Kelembaban_Tanah;
    public Float Kelembapan_Udara;
    public Float Suhu_Udara;
    public Float Ph_Tanah;
    public Float Suhu_Air;
    public Float Volume;
    public String Pompa;

    public Result(String kipas, Float kelembapan_udara, Float suhu_udara, Float ph_tanah, Float kelembapan_tanah, Float suhu_air, Float volume, String pompa) {
        Kipas = kipas;
        Kelembapan_Udara = kelembapan_udara;
        Suhu_Udara = suhu_udara;
        Ph_Tanah = ph_tanah;
        Kelembaban_Tanah = kelembapan_tanah;
        Suhu_Air = suhu_air;
        Volume = volume;
        Pompa = pompa;
    }

    public Result() {
    }
}
