package ucr.rp.if7100.EmailProcessingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.entities.Bank;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.templates.BACTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class EmailProcessingServiceApplication {

	public static void main(String[] args) throws ParseException {


		SpringApplication.run(EmailProcessingServiceApplication.class, args);

		List<String> lista = new ArrayList<>();
		BACTemplate bac = new BACTemplate();

		/*
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

		lista.add("WILLIAM ALONSO PIEDRA SOLANO");
		lista.add("LUIS GABRIEL ARGUEDAS VILLALOBOS");
		lista.add("9420");
		lista.add("15-05-2023");
		lista.add("16:00:43");
		lista.add("5000.00");
		lista.add("2023051510283002825683599");
		lista.add("2295-9797");
		lista.add("Banca Móvil");
		lista.add("notificaciones@baccredomatic.com");
		lista.add("villaluis24@gmail.com");

		//System.out.println(lista);

		Transaction transaction = bac.saveBACTransactionInformation1(lista);
		System.out.println(transaction.getEmail());
		System.out.println(transaction.getDate());
		System.out.println(transaction.getAmount());
		//Transaction ttt = bac.saveBACTransactionInformation1(lista,transaction,bank,accountId);
		//System.out.println(ttt);


	}

}
