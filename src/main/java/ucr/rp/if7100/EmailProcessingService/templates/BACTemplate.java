package ucr.rp.if7100.EmailProcessingService.templates;


import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.entities.Bank;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BACTemplate {


    /*
    private final Transaction transaction = new Transaction();
    private final Bank bank = new Bank();
    private final AccountId accountId = new AccountId();
    */
    /**
     * A transfer from one BAC acc to another one
     *
     * @param mail Will receive an email as an html string
     * @author: Fabiola
     */
    public List<String> mailParsererLocalTxBac(String mail) {
        List<String> data = new ArrayList<>();
        Document doc = Jsoup.parse(mail);

        org.jsoup.select.Elements bElements = doc.select("td p span b");
        for (org.jsoup.nodes.Element bElement : bElements) {
            String content = bElement.text();
            data.add(content);
        }
        Elements selements = doc.select("a");
        for (Element selement : selements) {
            String content = selement.text();

            data.add(content);
        }
        // Removes * & point at end in last 4 digits of card#
        data.set(2, data.get(2).replace("*", "").replace(".", ""));

        // Removes CRC in ammount. I.e CRC 5.000
        data.set(5, data.get(5).replace(" CRC", "").replace(",", ""));

        data.remove(data.size() - 1);
        data.remove(data.size() - 1);
        return data;
    }

    public Transaction saveBACTransactionInformation1(List<String> data) throws ParseException {
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
WILLIAM ALONSO PIEDRA SOLANO        0
LUIS GABRIEL ARGUEDAS VILLALOBOS    1
****9420.                           2
15-05-2023                          3
16:00:43                            4
5,000.00 CRC                        5
2023051510283002825683599           6
2295-9797                           7
Banca Móvil                         8
notificaciones@baccredomatic.com    9
villaluis24@gmail.com               10
 */
        Transaction transaction = new Transaction();
        Bank bank = new Bank();
        AccountId accountId = new AccountId();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = data.get(3);
        java.util.Date utilDate = dateFormat.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());


        bank.setName("BAC");
        accountId.setPhoneNumber(data.get(7));
        accountId.setLast4(data.get(2));
        accountId.setActNumber(data.get(6));

        transaction.setEmail(data.get(10));
        transaction.setDate(sqlDate);
        transaction.setAmount(Float.parseFloat(data.get(5)));
        transaction.setReference(data.get(8));
        transaction.setDescription("Transferencia local entre cuentas");
        transaction.setExpense(false);
        transaction.setBank(bank);
        transaction.setAccountId(accountId);

        return transaction;
    }


    //--------------------------------------------------------------------------------------------

    /**
     * A SINPE transfer from one bank to BAC
     *
     * @param mail Raw html as string to format
     * @author: Fabiola
     */
    public List<String> mailParsererSinpeBAC(String mail) {
        List<String> datos = new ArrayList<>();
        Document doc = Jsoup.parse(mail);


        Elements dateElements = doc.select("div p.MsoNormal");
        for (Element dateElement : dateElements) {
            // Obtener la fecha en que se envia el correo
            String datePattern = "Date: jue., (.*) m.";
            Pattern date = Pattern.compile(datePattern);
            Matcher matcherDate = date.matcher(dateElement.text());
            if (matcherDate.find()) {
                String dateCompleto = matcherDate.group(1).trim();
                datos.add(dateCompleto);
                System.out.println("ESTA ES LA FECHA "+dateCompleto);
            }
        }

        Elements selements = doc.select("a");
        for (Element selement1 : selements) {
            String content = selement1.text();

            datos.add(content);
        }

        Elements nameforp = doc.select("p");
        for (org.jsoup.nodes.Element spanElement : nameforp) {
            // Obtener el nombre completo
            String nombrePattern = "Hola (.*):";
            Pattern pattern = Pattern.compile(nombrePattern);
            Matcher matcher = pattern.matcher(spanElement.text());
            if (matcher.find()) {
                String nombreCompleto = matcher.group(1).trim();
                datos.add(nombreCompleto);
            }
        }

        Elements spanElements = doc.select("p span");
        for (Element spanElement : spanElements) {
            // Obtener el nombre completo
            String nombrePattern = "Hola (.*):";
            Pattern pattern = Pattern.compile(nombrePattern);
            Matcher matcher = pattern.matcher(spanElement.text());
            if (matcher.find()) {
                String nombreCompleto = matcher.group(1).trim();
                datos.add(nombreCompleto);
            }

            // Obtener el número de referencia
            String referenciaPattern = "número de referencia (.*?),";
            pattern = Pattern.compile(referenciaPattern);
            matcher = pattern.matcher(spanElement.text());
            if (matcher.find()) {
                String numeroReferencia = matcher.group(1).trim();
                datos.add(numeroReferencia);
            }

            // Obtener el número de cuenta IBAN
            String ibanPattern = "cuenta IBAN (.*?),";
            pattern = Pattern.compile(ibanPattern);
            matcher = pattern.matcher(spanElement.text());
            if (matcher.find()) {
                String numeroCuentaIBAN = matcher.group(1).trim();
                String numeroCuentaIBANSplit = numeroCuentaIBAN.split(" ")[0];
                datos.add(numeroCuentaIBANSplit);
            }

            // Obtener el monto
            String montoPattern = "monto de (.*?) Colones,";
            pattern = Pattern.compile(montoPattern);
            matcher = pattern.matcher(spanElement.text());
            if (matcher.find()) {
                String monto = matcher.group(1).trim();
                monto = monto.replace(",", "");
                datos.add(monto);
            }
        }

        return datos;
    }

    public Transaction saveBACTransactionInformation2(List<String> data) throws ParseException {
/*
TRANSACTION

email
date
amount
reference
description (La descripción como tal de la transacción)
category
isExpense (Si es gasto o no)
----
BANK

bankname
----
ACCOUNTID

phoneNumber
last4
actNumber
iban
-------
sinpe@notificacionesbaccr.com   0
VILLALUIS24@gmail.com           1
LUIS GABRIEL ARGUEDAS VILLALOB  2
2023031610224011141398013       3
CR5201XXXXXXXXXXXX2896          4
14,000.00                       5
 */

        Transaction transaction = new Transaction();
        Bank bank = new Bank();
        AccountId accountId = new AccountId();

        bank.setName("BAC");

        accountId.setIban(data.get(4));
        accountId.setActNumber(data.get(3));

        transaction.setEmail(data.get(1));
        //DATE
        transaction.setAmount(Float.parseFloat(data.get(5)));
        //REFERENCE
        transaction.setDescription("Notificación de Transferencia SINPE");
        //CATEGORY
        transaction.setExpense(false);
        transaction.setBank(bank);
        transaction.setAccountId(accountId);

        return transaction;

    }
//---------------------------------

    /**
     * This method will convert values in Colones to floats in order to be recorded successfully in db.
     *
     * @param number Specific amount should be converted into float
     */
    public float colonConverterforBAC(String number) {
        number = number.replace("CRC ", "").replace(" CRC", "");
        number = number.replace(",", "");
        float ammount = Float.parseFloat(number);
        return ammount;
    }


    /**
     * This method will convert a raw date string to a date string for
     * conversion in the saveBACTransactionInformation2 method
     *
     * @param rawDate Raw date obtained from the email extraction
     */
    public String DateConverter(String rawDate){

        //16 de marzo de 2023 1:45 p.
        String[] arrayDate = rawDate.split(" ");
        System.out.println(arrayDate.toString());
        return  " ";
    }
}


