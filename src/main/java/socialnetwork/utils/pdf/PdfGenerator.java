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
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.domain.dtos.MessageDTO;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class PdfGenerator {

    private final static URL imageMessages = PdfGenerator.class.getResource("/images/iconMessageSmall.png");
    private final static URL imageFriendships = PdfGenerator.class.getResource("/images/iconFriendSmall.png");

    public static <E> void generateReport(List<E> list, String destination, String title,String subTitle,URL imageURL) {
        try (PdfWriter writer = new PdfWriter(destination)){

            PdfDocument pdf = new PdfDocument(writer);
            pdf.addNewPage();
            Document document = new Document(pdf);

            Text textTitle = new Text(title)
                    .setFontColor(new DeviceRgb(63, 114, 175));

            Paragraph paragraphTitle = new Paragraph(textTitle);
            paragraphTitle.setTextAlignment(TextAlignment.CENTER);
            paragraphTitle.setFontSize(32);
            Paragraph paragraphSubTitle = new Paragraph(subTitle)
                    .setFontSize(16);
            document.add(paragraphTitle);
            document.add(paragraphSubTitle);
            insertList(document, list);
            Image image = new Image(ImageDataFactory.create(imageURL));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.setTextAlignment(TextAlignment.CENTER);
            document.add(image);
            document.close();
            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateMessagesReport(List<MessageDTO> list,String destination,User user,LocalDate dateFrom,LocalDate dateTo){
        String title = "Messages report\n";
        String subTitle = "Below is a list containing your messages with "+user.getFirstName()+" "+user.getLastName()+" between " + dateFrom + " and " + dateTo + "\n";
        generateReport(list,destination,title,subTitle,PdfGenerator.imageMessages);
    }

    public static void generateActivityReport(List<MessageDTO> listMessages, List<FriendshipDTO> listFriendships,String destination,User user,LocalDate dateFrom,LocalDate dateTo){
        String titleMessages = "Messages report\n";
        String subTitleMessages = "Below is a list containing "+user.getFirstName()+" "+user.getLastName()+ "'s messages between "+dateFrom+" and "+dateTo;
        String titleFriendships = "Friendships report\n";
        String subTitleFriendships="Below is a list of "+user.getFirstName()+" "+user.getLastName()+"'s new friendships, made between "+dateFrom+" and "+dateTo;

        try(PdfWriter pdfWriter = new PdfWriter(destination)) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            //messages
            Text titleTextMessages = new Text(titleMessages).setFontColor(new DeviceRgb(63, 114, 175));
            Paragraph paragraphTitleMessages = new Paragraph(titleTextMessages)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(32);
            Paragraph paragraphSubTitle = new Paragraph(subTitleMessages);
            paragraphSubTitle.setFontSize(16);
            document.add(paragraphTitleMessages);
            document.add(paragraphSubTitle);
            insertList(document, listMessages);
            Image image = new Image(ImageDataFactory.create(imageMessages))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(image);

            //friendships
            Text titleTextFriendships = new Text(titleFriendships).setFontColor(new DeviceRgb(63, 114, 175));
            Text subTitleTextFriendships = new Text(subTitleFriendships);
            Paragraph paragraphTitleFriendships = new Paragraph(titleTextFriendships);
            paragraphTitleFriendships.setTextAlignment(TextAlignment.CENTER);
            paragraphTitleFriendships.setFontSize(32);
            Paragraph paragraphSubTitleFriendships = new Paragraph(subTitleTextFriendships)
                    .setFontSize(16);
            document.add(paragraphTitleFriendships).add(paragraphSubTitleFriendships);
            insertList(document,listFriendships);
            image = new Image(ImageDataFactory.create(imageFriendships)).setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(image);

            document.close();
            pdfDocument.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static <E> void insertList(Document document, List<E> list) {
        if(list.isEmpty()){
            document.add(new Paragraph("Oops.. there is no data!"));
            return;
        }
        com.itextpdf.layout.element.List pdfList = new com.itextpdf.layout.element.List();
        pdfList.setFontSize(14);
        list.forEach(element -> pdfList.add(element.toString()));
        document.add(pdfList);
    }

}
