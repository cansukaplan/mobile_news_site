//Veritabani
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.json.JSONObject;
import us.raudi.pushraven.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Veritabani {
    public final static String AUTH_KEY_FCM = "AIzaSyASq-gzEPmveFmz6NwPwnrUElX32E48_Dc";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private static final String DATABASE_URL = "https://mobilhaber-42cb5.firebaseio.com/";
    private static DatabaseReference database;

    public Veritabani() {
        if (database == null) {
            try {
                FileInputStream serviceAccount = new FileInputStream("mobilhaber-42cb5-firebase-adminsdk-w2m5q-f32e196250.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(DATABASE_URL)
                        .build();
                FirebaseApp.initializeApp(options);
                // [END initialize]
            } catch (IOException e) {
                System.out.println("ERROR: invalid service account credentials. See README.");
                System.out.println(e.getMessage());

                System.exit(1);
            }

            // Shared Database reference
            database = FirebaseDatabase.getInstance().getReference();
        }
    }

    public boolean veriEkle(Haber haber) {
        DatabaseReference haberr = database.child("haberler");
        haberr.push().setValueAsync(haber);


            bildirimGonder(haber);


        return true;
    }

    public void bildirimGonder(Haber haber)  {

        try {
            Pushraven.setCredential(new File("mobilhaber-42cb5-firebase-adminsdk-w2m5q-f32e196250.json"));
            Pushraven.setProjectId("mobilhaber-42cb5");


            Notification not = new Notification()
                    .title(haber.getBaslik())
                    .body(haber.getIcerik());

            Message raven = new Message()
                    .name("id")
                    .notification(not)
                    .topic("all")
                    ;

            FcmResponse response = Pushraven.push(raven);
            System.out.println(response.getErrorMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Pushraven.setKey("AAAAE6QcVpc:APA91bF4uOz2P8Z39PMvIQSqChjILgNJj5UuPCwAmfVZK1anhBtQhpinznaVfu9YTnK3uC684HDvixSlqRlcBXyErPcb_ld7cwPeVUs2Sbq25nXuiDEnBtbfqzaxPbpXaE0UwUeL63--");
//
//        Notification raven = new Notification();
//        raven.title("MyTitle")
//                .text("Hello World!")
//                .color("#ff0000")
//                .to("84357699223-bbqp7ddk4p5bm7hh13kanomh335osdlv.apps.googleusercontent.com");
//
//        FcmResponse push = Pushraven.push(raven);
//        System.out.println("------------------------------------------");
//        System.out.println("Varsa error");
//        System.out.println(push.getErrorMessage());
//        System.out.println("Varsa basari");
//        System.out.println(push.getSuccessResponseMessage());
//        System.out.println("------------------------------------------");
//
//        raven.clear();
    }


    public void getHaberler(TableView table) {
        DatabaseReference ref = database.getDatabase().getReference("haberler");
        Query id = ref.orderByChild("id");
        id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ObservableList<Haber> haberler = FXCollections.observableArrayList();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        HashMap map = (HashMap<String, String>) issue.getValue();
                        Haber haber = new Haber(map.get("id").toString(),
                                map.get("baslik").toString(),
                            map.get("icerik").toString(),
                            map.get("haberTuru").toString(),
                            map.get("yayinlanmaTarihi").toString(),
                            ((Long) map.get("begenmeSayisi")).intValue(),
                            ((Long) map.get("begenmemeSayisi")).intValue(),
                            ((Long) map.get("goruntulenmeSayisi")).intValue(),
                            map.get("resimStr").toString());

                        haberler.add(haber);
                    }
                    table.setItems(haberler);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
