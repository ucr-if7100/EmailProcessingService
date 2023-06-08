package ucr.rp.if7100.EmailProcessingService.templates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.util.ArrayList;
import java.util.List;

public class BCRTemplate {

    /**
     Will parse a mail string from a BCR Sinpe into an object that can be recorded in db.
     @author: Allán
     @param mail String to parse and record in db
     **/
    private static List<String> mailParsererSinpeBcr(String mail) {
        Document doc = Jsoup.parse(mail);
        List<String> data = new ArrayList<>();

        Elements paragraphs = doc.select("p");
        for (Element paragraph : paragraphs) {
            Elements spans = paragraph.select("span");
            for (Element span : spans) {
                String text = span.text();
                String[] trimmed = text.split(":");
                if (trimmed.length > 1) {
                    data.add(text.split(":")[1]);
                }
            }
        }
        //Removes corrupted content. I.e: 10:17 am results 17 am in last entry
        data.remove(data.size() - 1);

        return data;
    }

    /**
     Will parse a mail string from a BAC Transaction (normally from shops) into an object that can be recorded in db.
     @author: Allán
     @param mail String to parse and record in db
     **/
    private static List<String> mailParsererTxBac(String mail) {

        Document doc = Jsoup.parse(mail);
        List<String> data = new ArrayList<>();

        int i = 0;
        Element table = doc.select("table").first();

        for (Element row : table.select("tr")) {
            i++;
            for (Element cell : row.select("td")) {
                if (i % 2 != 0 && i > 5 && i < 23) {
                    String content = cell.text();
                    data.add(content);
                }
                i++;
            }
        }

        // Removes * in last 4 digits of card#
        data.set(3, data.get(3).replace("*", ""));
        // Removes CRC in ammount. I.e CRC 5.000
        data.set(7, data.get(7).replace("CRC ", "").replace(",", ""));
        float ammount = Float.parseFloat(data.get(7));
        return data;
    }

}
