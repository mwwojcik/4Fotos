package mw.wspolne;

import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

/**
 * Created by mw on 15.01.16.
 */
public class Czytacz {

    /*void listFiles(Path path,Element pRoot) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                String pNazwa=entry.getFileName().toString();
                if (Files.isDirectory(entry)) {
                    if(pNazwa.contains("thums")||pNazwa.contains("cache")||pNazwa.contains("jalbum")){
                        continue;
                    }
                    listFiles(entry,pRoot.addElement("galeria").addAttribute("katalog",pNazwa).addAttribute("nazwa",""));
                }
            }
        }
    }*/

    public static void main(String[]args){

        Czytacz pCzyt=new Czytacz();

        Path pPlik=Paths.get(ZarzadcaWlasnosciUzytkownika.podajInstancje().
                podajKatalogHome()+ZarzadcaWlasnosciUzytkownika.podajInstancje().separator()+"szablony"+ZarzadcaWlasnosciUzytkownika.podajInstancje().separator()+"galeria.html");
        try {
            String pZawartosc=new String(Files.readAllBytes(pPlik));

            System.out.println(pZawartosc);

        } catch (IOException e) {
            e.printStackTrace();
        }

       /* Path pPlik=Paths.get("H:\\mw-git\\traper\\@pomocnicze\\galerie-zrodlo\\przyroda");
        System.out.println(pPlik.toFile().exists());*/


       /* try {
            Path pSzablon=Paths.get(ClassLoader.getSystemResource("szablony/galeria.html").toURI());
            Files.readAllLines(pSzablon);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

       /* Document document = DocumentHelper.createDocument();
        Element root = document.addElement( "galerie" );

        Czytacz pCzyt=new Czytacz();
        try {
            pCzyt.listFiles(Paths.get("/home/zdjecia/galerie-www/galeria-zrodlo"),root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            pCzyt.write(document);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void write(Document document) throws IOException {

        // lets write to a file
        XMLWriter writer = new XMLWriter(
                new FileWriter( "output.xml" )
        );
        writer.write( document );
        writer.close();


        // Pretty print the document to System.out
        OutputFormat format = OutputFormat.createPrettyPrint();
        writer = new XMLWriter( System.out, format );
        writer.write( document );

        // Compact format to System.out
        format = OutputFormat.createCompactFormat();
        writer = new XMLWriter( System.out, format );
        writer.write( document );
    }
}
