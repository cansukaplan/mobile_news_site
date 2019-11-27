//Haber
import com.google.firebase.database.Exclude;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Haber {
    private String id;
    private ImageView resim;
    private String resimStr;
    private String baslik;
    private String icerik;
    private HaberTuru haberTuru;
    private Calendar yayinlanmaTarihi;
    private int begenmeSayisi;
    private int begenmemeSayisi;
    private int goruntulenmeSayisi;

    private String dataPattern = "yyyy-mm-dd hh:mm:ss";
    private String dataPattern2 = "dd-mm-yyyy";
    private DateFormat dateFormat = new SimpleDateFormat(dataPattern);

    /**
     * kullanıcının olusturdugu
     */
    public Haber(ImageView resim, String baslik, String icerik, HaberTuru haberTuru, Calendar yayinlanmaTarihi, int begenmeSayisi, int begenmemeSayisi, int goruntulenmeSayisi) {
        this.id = UUID.randomUUID().toString();
        this.resim = resim;
        this.baslik = baslik;
        this.icerik = icerik;
        this.haberTuru = haberTuru;
        this.yayinlanmaTarihi = yayinlanmaTarihi;
        this.begenmeSayisi = begenmeSayisi;
        this.begenmemeSayisi = begenmemeSayisi;
        this.goruntulenmeSayisi = goruntulenmeSayisi;
    }

    public Haber(File resim, String baslik, String icerik, HaberTuru haberTuru, String yayinlanmaTarihi) {
        this.id = UUID.randomUUID().toString();

        byte[] fileContent = new byte[0];
        try {
            fileContent = FileUtils.readFileToByteArray(resim);
            resimStr = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.baslik = baslik;
        this.icerik = icerik;
        this.haberTuru = haberTuru;


        DateFormat dateFormat = new SimpleDateFormat(dataPattern2);
        Date d = null;
        try {
            d = dateFormat.parse(yayinlanmaTarihi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.yayinlanmaTarihi = Calendar.getInstance();
        this.yayinlanmaTarihi.setTime(d);

        this.begenmeSayisi = 0;
        this.begenmemeSayisi = 0;
        this.goruntulenmeSayisi = 0;
    }

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
            dateFormat = new SimpleDateFormat(dataPattern2);
            try {
                date =dateFormat.parse(yayinlanmaTarihi);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            this.yayinlanmaTarihi = calender;
        }


        this.begenmeSayisi = begenmeSayisi;
        this.begenmemeSayisi = begenmemeSayisi;
        this.goruntulenmeSayisi = goruntulenmeSayisi;

        byte[] decodedBytes = Base64.getDecoder().decode(resimStr);
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            Image image = SwingFXUtils.toFXImage(img, null);
            resim = new ImageView(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public ImageView getResim() {
        return resim;
    }

    public void setResim(ImageView resim) {
        this.resim = resim;
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

    @Override
    public String toString() {
        return "Haber{" +
            "id='" + id + '\'' +
            ", baslik='" + baslik + '\'' +
            ", icerik='" + icerik + '\'' +
            ", haberTuru=" + haberTuru +
            ", yayinlanmaTarihi=" + getYayinlanmaTarihi() +
            ", begenmeSayisi=" + begenmeSayisi +
            ", begenmemeSayisi=" + begenmemeSayisi +
            ", goruntulenmeSayisi=" + goruntulenmeSayisi +
            '}';
    }
}
