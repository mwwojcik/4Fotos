package mw.uslugi;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import mw.uslugi.io.ZarzadcaLogowania;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mw on 24.01.16.d
 */
public class ObrazkiHelper {

    private static String SEP = ZarzadcaWlasnosciUzytkownika.podajInstancje().separator();

    public static boolean czyJestWykluczony(Path aSciezka){
        String[] wykluczenia={"txt","jalbum"};
        //musimy wykluczyc jakies pliki z podkatalogu jalbum - tam tez sa jpg,dlatego nazwa musi byc pelna
        for(String pStr:wykluczenia){
            if(aSciezka.toFile().getAbsolutePath().contains(pStr)) {
                return true;
            }
        }
        return false;
    }


    public static boolean czyJestObrazem(Path aSciezka){
        String[] dopuszczone={"png","jpg"};
        for(String pStr:dopuszczone){
            if(aSciezka.toFile().getName().contains(pStr)) {
                return true;
            }
        }
        return false;
    }

    public static Path przeskalujObrazek(Path aPlik, Path aKatalogDocelowy, int aRozmiarDocelowy,String KATALOG_ZRODLOWY_BAZA,String aPostfixNazwy) {


        try {



            BufferedImage obrazek = podajObrazek(aPlik, aRozmiarDocelowy);
            BufferedImage pObrazekWyskalowany = Scalr.resize(obrazek, aRozmiarDocelowy);

            String pNazwaPliku = aPlik.getFileName().toString().toLowerCase();
            if (aPostfixNazwy != null) {
                pNazwaPliku = podajNazwePostfixowana(aPlik, aPostfixNazwy);

            }

            String pSciezkaDocelowa = aKatalogDocelowy + SEP + pNazwaPliku;
            Path pPlikDocelowy = Paths.get(pSciezkaDocelowa);

            Files.deleteIfExists(pPlikDocelowy);
            ImageIO.write(pObrazekWyskalowany, "jpg", pPlikDocelowy.toFile());
            ZarzadcaLogowania.podajInstancje().logujKomunikat(aPlik.getFileName() + "=>h=" + obrazek.getHeight() + " w=" + obrazek.getWidth() + " skalowanie=>h=" + pObrazekWyskalowany.getHeight() + " w=" + pObrazekWyskalowany.getWidth());

            return pPlikDocelowy;

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }


    }
    public static String podajNazwePostfixowana(Path aPlik, String aPostfixNazwy) {
        String pNazwaPliku = aPlik.getFileName().toString().toLowerCase();

        pNazwaPliku = pNazwaPliku.replaceAll(".jpg", "") + aPostfixNazwy + ".jpg";

        return pNazwaPliku;
    }


    public static BufferedImage podajObrazek(Path aObrazekSciezka,int aRozmiar) {
        BufferedImage scaledImg = null;
        try {
            scaledImg = ImageIO.read(aObrazekSciezka.toFile());

            scaledImg=Scalr.resize(scaledImg,aRozmiar);
            ZarzadcaLogowania.podajInstancje().logujKomunikat(aObrazekSciezka.toString()+" w="+scaledImg.getWidth()+" h="+scaledImg.getHeight());
            // ---- Begin orientation handling ----
            Metadata metadata = ImageMetadataReader.readMetadata(aObrazekSciezka.toFile());
            ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if(exifIFD0Directory==null){
                return scaledImg;
            }

            int orientation = 1;
            try {
                if(exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                    orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }

            switch (orientation) {
                case 1:
                    break;
                case 2: // Flip X
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.FLIP_HORZ);
                    break;
                case 3: // PI rotation
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.CW_180);
                    break;
                case 4: // Flip Y
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.FLIP_VERT);
                    break;
                case 5: // - PI/2 and Flip X
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.CW_90);
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.FLIP_HORZ);
                    break;
                case 6: // -PI/2 and -width
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.CW_90);
                    break;
                case 7: // PI/2 and Flip
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.CW_90);
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.FLIP_VERT);
                    break;
                case 8: // PI / 2
                    scaledImg = Scalr.rotate(scaledImg, Scalr.Rotation.CW_270);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (ImageProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        return scaledImg;
    }

}
