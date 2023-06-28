package ucr.rp.if7100.EmailProcessingService.templates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TemplateClassifier {

    public String templateClassifierTool(String html) {
        Document doc = Jsoup.parse(html);
        String bodyText = doc.body().text();

        if (bodyText.contains("A continuación le detallamos la transacción realizada:")) {
            return "toolMailParsererSinpeTablexBac";
        } else if (bodyText.contains("Transferencia Local")) {
            return "toolMailParsererLocalTxBac";
        } else if (bodyText.contains("Notificación de Transferencia SINPE")) {
            return "toolMailParsererSinpeBAC";
        } else if (bodyText.contains("Transacción SINPE MÓVIL")) {
            return "toolMailParsererSinpeBcr";
        } else {
            return null;
        }
    }
}
