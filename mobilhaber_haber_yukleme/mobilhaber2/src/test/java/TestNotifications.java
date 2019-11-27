import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class TestNotifications {


    @Test
    public void test() {
        Veritabani veritabani = new Veritabani();

        veritabani.bildirimGonder(new Haber(null, "baslik test", "icereik test", HaberTuru.ekonomi, Calendar.getInstance(), 0, 0, 0 ));

    }
}
