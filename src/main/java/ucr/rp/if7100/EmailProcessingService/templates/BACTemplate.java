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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BACTemplate {

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
            String datePattern = "Date:(.*) m.";
            Pattern date = Pattern.compile(datePattern);
            Matcher matcherDate = date.matcher(dateElement.text());
            if (matcherDate.find()) {
                String dateCompleto = matcherDate.group(1).trim();
                datos.add(dateCompleto);
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
        // Removes CRC in ammount. I.e CRC 5.000
        datos.set(6, datos.get(6).replace(" CRC", "").replace(",", ""));

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
16 de marzo de 2023 1:45 p.         0
sinpe@notificacionesbaccr.com       1
VILLALUIS24@gmail.com               2
LUIS GABRIEL ARGUEDAS VILLALOB      3
2023031610224011141398013           4
CR5201XXXXXXXXXXXX2896              5
14,000.00                           6


 */

        Transaction transaction = new Transaction();
        Bank bank = new Bank();
        AccountId accountId = new AccountId();
        bank.setName("BAC");
        accountId.setIban(data.get(5));
        accountId.setActNumber(data.get(5));
        String formattedDate = DateConverterfirst(data.get(0));
        //formatear la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formattedDate;
        java.util.Date utilDate = dateFormat.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());


        transaction.setEmail(data.get(2));
        transaction.setDate(sqlDate);
        transaction.setAmount(Float.parseFloat(data.get(6)));
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
     * Will parse a mail string from a BAC Transaction (normally from shops) into an object that can be recorded in db.
     *
     * @param mail String to parse and record in db
     * @author: Allán
     **/
    public List<String> mailParsererSinpeTablexBac(String mail) {

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
        Elements emailElements = doc.select("div p.MsoNormal");
        for (Element emailElement : emailElements) {
            // Obtener la fecha en que se envia el correo
            String emailPattern = "To: (.*)";
            Pattern email = Pattern.compile(emailPattern);
            Matcher matcherDate = email.matcher(emailElement.text());
            if (matcherDate.find()) {
                String emailCompleto = matcherDate.group(1).trim().replace("<", "").replace(">", "");
                String[] palabras = emailCompleto.split(" ");

                if (palabras.length >= 1) {
                    String ultimaPalabra = palabras[palabras.length - 1];
                    data.add(ultimaPalabra);
                }
            }
        }

        // Removes * in last 4 digits of card#
        data.set(3, data.get(3).replace("*", ""));
        // Removes CRC in ammount. I.e CRC 5.000
        data.set(7, data.get(7).replace("CRC ", "").replace(",", ""));

        float ammount = Float.parseFloat(data.get(7));

        return data;
    }

    public Transaction saveBACTransactionInformation3(List<String> data) throws ParseException {
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
TOYS CARTAGO                0
CARTAGO, Costa Rica         1
May 12, 2023, 09:37         2
2710                        3
000346                      4
313209371015                5
COMPRA                      6
1395.00                     7
allcincoceroseis@gmail.com  8
villaluis24@gmail.com       9

 */
        Transaction transaction = new Transaction();
        Bank bank = new Bank();
        AccountId accountId = new AccountId();

        String formattedDate = DateConvertersecond(data.get(2));
        //formatear la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formattedDate;
        java.util.Date utilDate = dateFormat.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        bank.setName("BAC");
        accountId.setLast4(data.get(3));

        transaction.setBank(bank);
        transaction.setAccountId(accountId);
        transaction.setEmail(data.get(9));
        transaction.setDate(sqlDate);
        transaction.setAmount(Float.parseFloat(data.get(7)));
        transaction.setReference(data.get(5));
        transaction.setDescription(data.get(0));
        transaction.setCategory(data.get(6));
        transaction.setExpense(true);


        return transaction;

    }

//---------------------------------

    /**
     * This method will convert a raw date string to a date string for
     * conversion in the saveBACTransactionInformation2 method
     *
     * @param rawDate Raw date obtained from the email extraction
     * @author: Fabiola
     */
    public String DateConverterfirst(String rawDate) {
 /*
        jue,            0
        16              1
        de              2
        marzo           3
        de              4
        2023            5
        1:45            6
        p.              7
         */
        String finalDate = "";
        String[] arrayDate = rawDate.split(" ");

        String day = arrayDate[1];
        String month =arrayDate[3];
        String year =arrayDate[5];

        //ASIGNACION DEL MES
        switch (month.toLowerCase()) {
            case "enero":
                month = "01";
                break;
            case "febrero":
                month = "02";
                break;
            case "marzo":
                month = "03";
                break;
            case "abril":
                month = "04";
                break;
            case "mayo":
                month = "05";
                break;
            case "junio":
                month = "06";
                break;
            case "julio":
                month = "07";
                break;
            case "agosto":
                month = "08";
                break;
            case "septiembre":
                month = "09";
                break;
            case "octubre":
                month = "10";
                break;
            case "noviembre":
                month = "11";
                break;
            case "diciembre":
                month = "12";
                break;
            default:
                month = "-1"; // Valor por defecto para un nombre de mes no válido
                break;
        }

        finalDate = year + "-" + month + "-" + day;
        return finalDate;
    }

    //-----------------------------------------------------------------------------
    public String DateConvertersecond(String dateString) {
        try {
            Locale locale = new Locale("es", "ES"); // Especifica el idioma y país (español de España)
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy, HH:mm", locale);
            java.util.Date utilDate = inputFormat.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}


