package com.mobilhaber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Haber {
    private String firebaseId;//firebase'in verdigi id
    private String id;
    private String resimStr;
    private Bitmap resim;
    private String baslik;
    private String icerik;
    private HaberTuru haberTuru;
    private Calendar yayinlanmaTarihi;
    private int begenmeSayisi;
    private int begenmemeSayisi;
    private int goruntulenmeSayisi;

    private String dataPattern = "yyyy-mm-dd hh:mm:ss";
    private DateFormat dateFormat = new SimpleDateFormat(dataPattern);


    /**
     * veri tabanından gelen
     */
    public Haber(String id, String baslik, String icerik, String haberTuru, String yayinlanmaTarihi, int begenmeSayisi, int begenmemeSayisi, int goruntulenmeSayisi, String resimStr) {
        this.id = id;
        this.baslik = baslik;
        this.icerik = icerik;
        this.haberTuru = HaberTuru.valueOf(haberTuru);

        Date date = null;
        try {
            date = dateFormat.parse(yayinlanmaTarihi);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            this.yayinlanmaTarihi = calender;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        byte[] decodedString = Base64.decode(resimStr, Base64.DEFAULT);
        resim = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        this.begenmeSayisi = begenmeSayisi;
        this.begenmemeSayisi = begenmemeSayisi;
        this.goruntulenmeSayisi = goruntulenmeSayisi;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getBaslik() {
        return baslik;
    }


    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public HaberTuru getHaberTuru() {
        return haberTuru;
    }

    public String getHaberTuruStr() {
        return haberTuru.name();
    }

    public void setHaberTuru(HaberTuru haberTuru) {
        this.haberTuru = haberTuru;
    }


    public String getYayinlanmaTarihi() {
        String strDate = dateFormat.format(yayinlanmaTarihi.getTime());
        return strDate;
    }

    public void setYayinlanmaTarihi(Calendar yayinlanmaTarihi) {
        this.yayinlanmaTarihi = yayinlanmaTarihi;
    }

    public int getBegenmeSayisi() {
        return begenmeSayisi;
    }

    public void setBegenmeSayisi(int begenmeSayisi) {
        this.begenmeSayisi = begenmeSayisi;
    }

    public int getBegenmemeSayisi() {
        return begenmemeSayisi;
    }

    public void setBegenmemeSayisi(int begenmemeSayisi) {
        this.begenmemeSayisi = begenmemeSayisi;
    }

    public int getGoruntulenmeSayisi() {
        return goruntulenmeSayisi;
    }

    public void setGoruntulenmeSayisi(int goruntulenmeSayisi) {
        this.goruntulenmeSayisi = goruntulenmeSayisi;
    }

    public String getResimStr() {
        return resimStr;
    }

    public void setResimStr(String resimStr) {
        this.resimStr = resimStr;
    }

    public Bitmap getResim() {
        return resim;
    }

    public void setResim(Bitmap resim) {
        this.resim = resim;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    @Override
    public String toString() {
        return baslik;
    }

    public void goruntulendi(){
        goruntulenmeSayisi++;
    }

    public void begen() {

        begenmeSayisi++;
    }

    public void begenme() {

        begenmemeSayisi++;
    }
}
