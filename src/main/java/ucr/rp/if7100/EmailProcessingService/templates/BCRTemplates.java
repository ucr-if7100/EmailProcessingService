package ucr.rp.if7100.EmailProcessingService.templates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.enums.TransactionType;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BCRTemplates {


    public Transaction mailParsererSinpeBcr(String mail) {

        Document doc = Jsoup.parse(mail);
        List<String> data = new ArrayList<>();

        Elements elementsdate = doc.select("p:contains(Esta transacción fue realizada)");
        if (!elementsdate.isEmpty()) {
            Element element = elementsdate.first();
            String fechaTransaccion = element.text().replace("Esta transacción fue realizada el ", "");
            int indiceEspacio = fechaTransaccion.indexOf(" ");
            if (indiceEspacio != -1) {
                String fecha = fechaTransaccion.substring(0, indiceEspacio);
                data.add(fecha);
            }
        }

        Elements elementsmail = doc.select("p.MsoNormal:contains(To:)");
        if (!elementsmail.isEmpty()) {
            Element elemente = elementsmail.first();
            String emailText = elemente.text();
            int startIndex = emailText.indexOf(":") + 1;
            if (startIndex != -1) {
                String email = emailText.substring(startIndex).trim();
                data.add(email);
            }
        }

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
        data.set(0, data.get(0).replace(" ", ""));
        data.set(1, data.get(1).replace(" ", ""));
        data.set(2, data.get(2).replace(" ", ""));
        data.set(3, data.get(3).replace(" ", ""));
        data.set(4, data.get(4).replace(" ", ""));
        data.set(5, data.get(5).replace(" ", ""));
        data.set(7, data.get(7).replace(" ", ""));
        data.set(8, data.get(8).replace(" ", ""));
        data.set(7, data.get(7).replace("CRC ", "").replace(",", "").replace(" ", ""));

  /*
email
date
amount
reference
description (La descripción como tal de la transacción)
category
isExpense (Si es gasto o no)
----
bankname
----
phoneNumber
last4
actNumber
iban
-------
13/12/2022                          0
allcincoceroseis@gmail.com          1
ALLANFABIANTREJOSSALAZAR            2
2022121315183010961550962           3
0                                   4
ChaconMunozSeidyTatiana             5
BancoNacionaldeCostaRica            6
5,000.00                            7
Costuras                            8
 */

        Date sqlDate = DateConverter(data.get(0));

        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber(data.get(4))
                .build();

        Transaction transaction = new Transaction.Builder()
                .withEmail(data.get(1))//email
                .withDate(sqlDate)//date
                .withAmount(Float.parseFloat(data.get(7)))//amount
                .withReference(data.get(3))//reference
                .withDescription(data.get(8))//description
                .withCategory(null)//category
                .withTransactionType(TransactionType.INCOME)//expense
                .withBankName("BCR")//bank
                .withAccountId(accountId)//accountid
                .build();

        return transaction;


    }

//-------------------------------------------------------------------
    public Date DateConverter(String fechaString) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date fechaUtil = formato.parse(fechaString);
            return new Date(fechaUtil.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
