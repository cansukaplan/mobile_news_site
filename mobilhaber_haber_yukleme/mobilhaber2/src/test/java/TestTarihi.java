import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class TestTarihi {
    @Test
    public void test() {
          String dataPattern2 = "dd-mm-yyyy";
          DateFormat dateFormat = new SimpleDateFormat(dataPattern2);

          String ikiNidan = "02-04-2018";

        Date d = null;
        try {
            d = dateFormat.parse(ikiNidan);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(d);
    }
}
