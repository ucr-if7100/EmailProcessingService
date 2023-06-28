package ucr.rp.if7100.EmailProcessingService.templates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

        List<String> data = new ArrayList<>();

        Document doc = Jsoup.parse(mail);

        Element nombreElement = doc.selectFirst("p:contains(Estimado (a):)");
        if (nombreElement != null) {
            String nombre = nombreElement.text().split(":")[1].trim();
            data.add(nombre);
        }

        Element refElement = doc.selectFirst("p:contains(Número de referencia:)");
        if (refElement != null) {
            String referencia = refElement.text().split(":")[1].trim();
            data.add(referencia);
        }

        Element telefonoElement = doc.selectFirst("p:contains(Teléfono Destino:)");
        if (telefonoElement != null) {
            String telefono = telefonoElement.text().split(":")[1].trim();
            data.add(telefono);
        }

        Element clienteElement = doc.selectFirst("p:contains(Nombre cliente Destino:)");
        if (clienteElement != null) {
            String cliente = clienteElement.text().split(":")[1].trim();
            data.add(cliente);
        }

        Element entidadElement = doc.selectFirst("p:contains(Entidad Destino:)");
        if (entidadElement != null) {
            String entidad = entidadElement.text().split(":")[1].trim();
            data.add(entidad);
        }

        Element montoElement = doc.selectFirst("p:contains(Monto:)");
        if (montoElement != null) {
            String monto = montoElement.text().split(":")[1].trim();
            data.add(monto);
        }

        Element motivoElement = doc.selectFirst("p:contains(Motivo:)");
        if (motivoElement != null) {
            String motivo = motivoElement.text().split(":")[1].trim();
            data.add(motivo);
        }

        Element fechaElement = doc.selectFirst("p:contains(Esta transacción fue realizada el)");
        if (fechaElement != null) {
            String fecha = fechaElement.text().replace("Esta transacción fue realizada el", "").trim();
            fecha = fecha.split(" ")[0];
            data.add(fecha);
        }

        Element correoElement = doc.selectFirst("strong:contains(To:)");
        if (correoElement != null) {
            String correo = correoElement.nextSibling().toString().trim();
            correo = correo.split(" ")[0];
            data.add(correo);
        }


        data.set(5, data.get(5).replace(" ", "").replace("CRC ", "").replace(",", ""));

        Date sqlDate = DateConverter(data.get(7));

        AccountId accountId = new AccountId.Builder()
                .withActNumber("")
                .withIban("")
                .withLast4("")
                .withPhoneNumber("")
                .build();

        Transaction transaction = new Transaction.Builder()
                .withEmail(data.get(8))//email
                .withDate(sqlDate)//date
                .withAmount(Float.parseFloat(data.get(5)))//amount
                .withReference(data.get(1))//reference
                .withDescription(data.get(6))//description
                .withCategory(null)//category
                .withTransactionType(TransactionType.EXPENSE)//expense
                .withBankName("BCR")//bank
                .withAccountId(accountId)//accountid
                .withRead(false)
                .build();

        return transaction;


    }

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
