package fi.dwo.dwojprint;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * With this class, you can print a {@link Printable} to a pdf. You get the pdf as a byte-array, that can be stored in a file or sent as an email attachment.
 * 
 * @author G.J. Schouten
 *
 */
public class PdfPrinter {

    /**
     * Prints the given {@link Printable} to a pdf file and returns the result as a byte-array. The size of the document is A4.
     * 
     * @param printable       The {@link Printable} that has to be printed.
     * @param clone           A clone of the first {@link Printable}. Needed because internally, it must be printed twice and the Printable may keep state and may not be re-usable.
     * @param orientation     {@link PageFormat}.PORTRAIT or {@link PageFormat}.LANDSCAPE.
     * @return                A byte-array with the pdf file.
     */
    public static void printToPdf(Printable printable, Printable clone, int orientation, OutputStream out) {
        
        Rectangle pageSize;
        if(orientation == PageFormat.PORTRAIT) {
            pageSize = PageSize.A4;
        } else {
            pageSize = PageSize.A4.rotate();
        }
        
        //The bytes to be returned
        byte[] bytes = null;
        
        //We will count the number of pages that have to be printed
        int numberOfPages = 0;
        
        //We will print the document twice, once to count the number of pages and once for real
        for(int i=0; i<2; i++) {
            try {
                Printable usedPrintable = i==0 ? printable : clone; //First time, use the printable, second time, use the clone
                
                OutputStream bos = i==0 ? new NullOutputStream() : out;
                
                Document document = new Document(pageSize); //This determines the size of the pdf document
                PdfWriter writer = PdfWriter.getInstance(document, bos);
                document.open();
                PdfContentByte contentByte = writer.getDirectContent();
                
                //These lines do not influence the pdf document, but are there to tell the Printable how to print
                double a4WidthInch = 8.26771654; //Equals 210mm
                double a4HeightInch = 11.6929134; //Equals 297mm
                Paper paper = new Paper();
                paper.setSize(a4WidthInch*72, a4HeightInch*72); //72 DPI
                paper.setImageableArea(18, 18, a4WidthInch*72 - 36, a4HeightInch*72 - 36); // 1/4 inch margins
                PageFormat pageFormat = new PageFormat();
                pageFormat.setPaper(paper);
                pageFormat.setOrientation(orientation);
                
                float width = ((float) pageFormat.getWidth());
                float height = ((float) pageFormat.getHeight());
                
                //First time, don't use numberOfPages, since we don't know it yet
                for(int j=0; j < numberOfPages || i==0; j++) {
                    Graphics2D g2d = contentByte.createGraphics(width, height, new PdfFontMapper());
                    int pageReturn = usedPrintable.print(g2d, pageFormat, j);
                    g2d.dispose();
                    
                    //The page that we just printed, actually existed; we only know this afterwards
                    if(pageReturn == Printable.PAGE_EXISTS) {
                        document.newPage(); //We have to create a newPage for the next page, even if we don't yet know if it exists, hence the second run where we do know 
                        if(i == 0) { //First run, count the pages
                            numberOfPages++;
                        }
                    } else {
                        break;
                    }
                }
                document.close();
                writer.close(); 
            } catch (RuntimeException e) {
            	throw e;
            } catch(Exception e) {
                //We expect no Exceptions, so any Exception that occurs is a technical one and should be a RuntimeException
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * This class maps {@link java.awt.Font}s to {@link com.itextpdf.text.pdf.BaseFont}s. It gets the fonts from files, so that the pdf looks identical on all platforms.
     * 
     * @author G.J. Schouten
     *
     */
    private static class PdfFontMapper extends DefaultFontMapper {

        public BaseFont awtToPdf(Font font) {
            
            try {
            	if (true) return super.awtToPdf(font);
            	
            	if (true)
            		return this.getBaseFontFromFile("/META-INF/fonts/verdana/", "VERDANA.TTF");
            	
            	if(font.getFamily().equalsIgnoreCase("Verdana")) {
                    if(font.isBold()) {
                        if(font.isItalic()) {
                            return this.getBaseFontFromFile("/META-INF/fonts/verdana/", "VERDANAZ.TTF");
                        }
                        return this.getBaseFontFromFile("/META-INF/fonts/verdana/", "VERDANAB.TTF");
                    } else if(font.isItalic()) {
                        return this.getBaseFontFromFile("/META-INF/fonts/verdana/", "VERDANAI.TTF");
                    } else {
                        return this.getBaseFontFromFile("/META-INF/fonts/verdana/", "VERDANA.TTF");
                    }
                } else { //Times new Roman is default
                    if(font.isBold()) {
                        if(font.isItalic()) {
                            return this.getBaseFontFromFile("/META-INF/fonts/timesnewroman/", "TIMESBI.TTF");
                        }
                        return this.getBaseFontFromFile("/META-INF/fonts/timesnewroman/", "TIMESBD.TTF");
                    } else if(font.isItalic()) {
                        return this.getBaseFontFromFile("/META-INF/fonts/timesnewroman/", "TIMESI.TTF");
                    } else {
                        return this.getBaseFontFromFile("/META-INF/fonts/timesnewroman/", "TIMES.TTF");
                    }
                }
                
            } catch (RuntimeException e) {
            	throw e;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Font pdfToAwt(BaseFont baseFont, int size) {
            
            throw new UnsupportedOperationException();
        }
        
        /**
         * To get a {@link BaseFont} from a file on the filesystem or in a jar. See: http://www.mail-archive.com/itext-questions@lists.sourceforge.net/msg02691.html
         * 
         * @param directory
         * @param filename
         * @return
         * @throws Exception
         */
        private BaseFont getBaseFontFromFile(String directory, String filename) throws Exception {
            
           InputStream is = null;
           try {
               is = PdfPrinter.class.getResourceAsStream(directory + filename);
    
               ByteArrayOutputStream bos = new ByteArrayOutputStream();
               byte[] buf = new byte[1024];
               while(true) {
                   int size = is.read(buf);
                   if(size < 0) {
                       break;
                   }
                   bos.write(buf, 0, size);
               }
               buf = bos.toByteArray();
               BaseFont bf = BaseFont.createFont(filename, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, BaseFont.NOT_CACHED, buf, null);
               return bf;
           } finally {
               if(is != null) {
                   is.close();
               }
           }
        }
    }
}
