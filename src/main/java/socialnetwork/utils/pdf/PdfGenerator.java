package socialnetwork.utils.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import socialnetwork.domain.User;
import socialnetwork.domain.dtos.MessageDTO;

import java.time.LocalDate;
import java.util.List;

public class PdfGenerator {

    public static void generateMessagesReport(List<MessageDTO> list, String destination, User userMessage, LocalDate dateFrom, LocalDate dateTo) {
        try {
            PdfWriter writer = new PdfWriter(destination);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.addNewPage();
            Document document = new Document(pdf);

            Text textTitle = new Text("Messages Report\n")
                    .setFontColor(new DeviceRgb(63, 114, 175));

            Paragraph title = new Paragraph(textTitle);
            title.setTextAlignment(TextAlignment.CENTER);
            title.setFontSize(32);
            Paragraph subTitle = new Paragraph("Below is a list containing your messages with " + userMessage.getFirstName() + " " + userMessage.getLastName() + " between " + dateFrom + " and " + dateTo + "\n")
                    .setFontSize(16);
            document.add(title);
            document.add(subTitle);
            insertList(document, list);
            Image image = new Image(ImageDataFactory.create(PdfGenerator.class.getResource("/images/iconMessageSmall.png")));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.setTextAlignment(TextAlignment.CENTER);
            document.add(image);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <E> void insertList(Document document, List<E> list) {
        if(list.isEmpty()){
            document.add(new Paragraph("Oopps.. there are no messages!"));
            return;
        }
        com.itextpdf.layout.element.List pdfList = new com.itextpdf.layout.element.List();
        pdfList.setFontSize(14);
        list.forEach(element -> pdfList.add(element.toString()));
        document.add(pdfList);
    }

}
