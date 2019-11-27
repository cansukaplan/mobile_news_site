//Anasayfa
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class Anasayfa {
    @FXML
    TableView table;

    @FXML
    Button btnHaberEkle;

    public void initialize() {
        tabloyuOlustur();
        verileriCek();

        btnHaberEkle.setOnAction((ActionEvent e) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/haberEkle.fxml"));
            try {
                Parent connectionInfoPanel = fxmlLoader.load();

                Scene dialogScene = new Scene(connectionInfoPanel, 300, 400);
                dialog.setScene(dialogScene);
                dialog.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Program.anasayfa = this;
    }

    public void verileriCek() {
//        Haber haber = new Haber(new ImageView(new Image("bear.jpg")), "Başık", "İçerikkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"
//                , HaberTuru.ekonomi, Calendar.getInstance(), 3, 2, 100);
//        Haber haber2 = new Haber(new ImageView(new Image("bb.jpeg")), "Başık2", "İçerikkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk2"
//                , HaberTuru.ekonomi, Calendar.getInstance(), 5, 12, 30);
//        ObservableList<Haber> haberler = FXCollections.observableArrayList();

//        haberler.add(haber);
//        haberler.add(haber2);

//        table.setItems(haberler);
        if (!Bindings.isEmpty(table.getItems()).get()) {
            table.getItems().clear();
        }
        Veritabani veritabani = new Veritabani();
        veritabani.getHaberler(table);
    }

    private void tabloyuOlustur() {
        TableColumn<Haber, ImageView> colResim = new TableColumn<>("Resmi");
        colResim.setCellValueFactory(new PropertyValueFactory<>("resim"));

        TableColumn<Haber, String> colBaslik = new TableColumn<>("Başlık");
        colBaslik.setCellValueFactory(new PropertyValueFactory<>("baslik"));

        TableColumn<Haber, String> colMetin = new TableColumn<>("İçerik");
        colMetin.setCellValueFactory(new PropertyValueFactory<>("icerik"));

        TableColumn<Haber, String> colTuru = new TableColumn<>("Türü");
        colTuru.setCellValueFactory(new PropertyValueFactory<>("haberTuru"));

        TableColumn<Haber, String> colYayinlanmaTarihi = new TableColumn<>("Yayınlanma Tarihi");
        colYayinlanmaTarihi.setCellValueFactory(new PropertyValueFactory<>("yayinlanmaTarihi"));

        TableColumn<Haber, Integer> colBegenmeSayisi = new TableColumn<>("Beğenme Sayisi");
        colBegenmeSayisi.setCellValueFactory(
            new PropertyValueFactory<>("begenmeSayisi")
        );
        ;

        TableColumn<Haber, Integer> colBegenmemeSayisi = new TableColumn<>("Begenmeme Sayisi");
        colBegenmemeSayisi.setCellValueFactory(
            new PropertyValueFactory<>("begenmemeSayisi")
        );
        ;

        TableColumn<Haber, Integer> colGoruntulenmeSayisi = new TableColumn<>("Görüntülenme Sayisi");
        colGoruntulenmeSayisi.setCellValueFactory(new PropertyValueFactory<>("goruntulenmeSayisi"));

//        table.getColumns().addAll(colResim, colBaslik, colMetin, colTuru, colYayinlanmaTarihi, colBegenmeSayisi, colBegenmemeSayisi, colGoruntulenmeSayisi);
        table.getColumns().add(colResim);
        table.getColumns().add(colBaslik);
        table.getColumns().add(colMetin);
        table.getColumns().add(colTuru);
        table.getColumns().add(colYayinlanmaTarihi);
        table.getColumns().add(colBegenmeSayisi);
        table.getColumns().add(colBegenmemeSayisi);
        table.getColumns().add(colGoruntulenmeSayisi);
    }
}
