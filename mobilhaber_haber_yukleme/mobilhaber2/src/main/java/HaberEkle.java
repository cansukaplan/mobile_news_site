//HaberEkle
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HaberEkle {


    @FXML
    TextField tfBaslik;
    @FXML
    TextArea taIcerik;
    @FXML
    ComboBox<String> cbTur;
    @FXML
    AnchorPane paneFileChooser;
    @FXML
    TextField tfTarih;
    @FXML
    Button btnEkle;

    private File secilenDosya;

    public void initialize() {
        turlerListesiniEkle();
        dosyaSeciciyiEkle();

        btnEkle.setOnAction(e -> {
            String baslik = tfBaslik.getText();
            String icerik = taIcerik.getText();
            String tur = cbTur.getSelectionModel().getSelectedItem();

            Haber haber = new Haber(secilenDosya, baslik, icerik, HaberTuru.valueOf(tur),tfTarih.getText());
            Veritabani veritabani = new Veritabani();
            boolean b = veritabani.veriEkle(haber);
            if (b) {
                Program.showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Veri tabanına haber ekleme", "Haber Başarıyla Eklendi!!!");
                Program.anasayfa.verileriCek();
                veritabani.bildirimGonder(haber);
            }
            paneFileChooser.getScene().getWindow().hide();
        });
    }

    private void dosyaSeciciyiEkle() {
        FileChooser fileChooser = new FileChooser();


        Button button = new Button("Dosya Seçiniz");
        button.setOnAction(e -> {
            secilenDosya = fileChooser.showOpenDialog((Stage) paneFileChooser.getScene().getWindow());
        });

        paneFileChooser.getChildren().add(button);
    }

    private void turlerListesiniEkle() {
        ObservableList<String> turler = FXCollections.observableArrayList();

        for (HaberTuru haberTuru : HaberTuru.values()) {
            turler.add(haberTuru.name());
        }

        cbTur.setItems(turler);
    }
}
