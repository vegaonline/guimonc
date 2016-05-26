/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import javax.xml.transform.TransformerException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.*;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;

/**
 *
 * @author vega
 */
public class CreatePDFA {

    private PDDocument doc = new PDDocument ();
    private String fName = null;
    private String fontFile = null;

    private CreatePDFA() {

    }

    public void setFileName(String fn) {
        this.fName = fn;
    }

    public void setFontName(String fontName) {
        this.fontFile = fontName;
    }

    public void doPDF() throws IOException, TransformerException {
        try {
            PDPage page = new PDPage (PDRectangle.A4);
            doc.addPage (page);

            PDFont font = PDType1Font.HELVETICA;  // PDType0Font.load (doc, new File (fontFile));
            PDPageContentStream contents = new PDPageContentStream (doc, page);
            contents.beginText ();
            contents.setFont (font, 15);
            contents.newLineAtOffset (100, 700);
            contents.showText ("REPORT");
            contents.showText (" Abhijit Bhattacharyya");
            contents.endText ();
            contents.saveGraphicsState ();
            contents.close ();

            // add XMP metadata
            XMPMetadata xmp = XMPMetadata.createXMPMetadata ();
            try {
                DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema ();
                dc.setTitle (fName);
                PDFAIdentificationSchema id = xmp.
                        createAndAddPFAIdentificationSchema ();
                id.setPart (1);
                id.setConformance ("B");

                XmpSerializer serializer = new XmpSerializer ();
                ByteArrayOutputStream baos = new ByteArrayOutputStream ();
                serializer.serialize (xmp, baos, true);
                PDMetadata metadata = new PDMetadata (doc);
                metadata.importXMPMetadata (baos.toByteArray ());
                doc.getDocumentCatalog ().setMetadata (metadata);
            } catch (BadFieldValueException e) {
                throw new IllegalArgumentException (e);
            }

            //sRGB output intent
            InputStream colorProfile = CreatePDFA.class.getResourceAsStream (
                    "/org/apache/pdfbox/resources/pdfa/sRGB Color Space Profile.icm");
            PDOutputIntent intent = new PDOutputIntent (doc, colorProfile);
            intent.setInfo ("sRGB IEC61966-2.1");
            intent.setOutputCondition ("sRGB IEC61966-2.1");
            intent.setOutputConditionIdentifier ("sRGB IEC61966-2.1");
            intent.setRegistryName ("http://www.color.org");
            doc.getDocumentCatalog ().addOutputIntent (intent);
            doc.save (fName);
        } finally {
            doc.close ();
        }
    }

}
