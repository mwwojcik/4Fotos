package mw.uslugi.io;


import mw.uslugi.bazowe.UslugaBazowa;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.zdarzenia.ProgressEvent;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 05.03.14
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
@Component
public class OperacjeIOUtil extends UslugaBazowa{

    private static String SEP=System.getProperty("file.separator");
/*
    private static Map<String,Plik> przepakujFilesDoPlikow(Collection<File>aKolekcja){
        Map<String,Plik> pKolekcjaDo=new HashMap<String,Plik>();

        for(File pPlik:aKolekcja){
            String[] pTab=pPlik.getName().split("\\.");
            Plik pP=new Plik(pPlik);
            pKolekcjaDo.put(pTab[0],pP);
        }

        return pKolekcjaDo;
    }

    public static MiernikZajetosci podajRozmiarPlikow(Katalog aKatalog, TypyPlikowEnum aTypPlikow){
        MiernikZajetosci pMiernik=new MiernikZajetosci();
        List<Plik> pLista=podajListePlikowDlaDrzewaKatalogow(aKatalog,aTypPlikow);
        for(Plik pPlik:pLista){
            pMiernik.setRozmiar(pMiernik.getRozmiar()+pPlik.getRozmiar());
        }
        pMiernik.setLiczbaPlikow(pLista.size());

        return pMiernik;
    }

    public static List<Plik> podajListePlikowDlaDrzewaKatalogow(Katalog aKatalog, TypyPlikowEnum aTypPlikow){
        List<Plik>pLista=new ArrayList<Plik>();

        for(File pPlik:FileUtils.listFiles(aKatalog.getPlik(), (aTypPlikow!=null)?aTypPlikow.getRozszerzenia():null,true)){
            Plik pP=new Plik(pPlik);
            pP.setRozmiar(pPlik.length());
            pLista.add(pP);
        }
        return pLista;
    }

    public static List<Plik> podajListePlikowDlaKatalogu(Katalog aKatalog, TypyPlikowEnum aTypPlikow){
        List<Plik>pLista=new ArrayList<Plik>();

        for(File pPlik:FileUtils.listFiles(aKatalog.getPlik(), (aTypPlikow!=null)?aTypPlikow.getRozszerzenia():null,false)){
            Plik pP=new Plik(pPlik);
            pP.setRozmiar(pPlik.length());
            pLista.add(pP);
        }
        return pLista;
    }

    public static Map<String,Plik> podajMapePlikowDlaKatalogu(Katalog aKatalog, TypyPlikowEnum aTypPlikow){
        return przepakujFilesDoPlikow(FileUtils.listFiles(aKatalog.getPlik(), aTypPlikow.getRozszerzenia(),false));
    }

    public static Map<String,Plik> podajMapePlikowDlaDrzewaKatalogow(Katalog aKatalog, TypyPlikowEnum aTypPlikow){
        return przepakujFilesDoPlikow(FileUtils.listFiles(aKatalog.getPlik(), aTypPlikow.getRozszerzenia(),true));
    }*/

    public void przeniesPliki(Path aKatalogZ, Path aKatalogDo, TypyPlikowEnum aTypPlikow){

        Collection<File>pPLikiDoSkasowania=FileUtils.listFiles(aKatalogZ.toFile(),(aTypPlikow!=null)?new String[]{aTypPlikow.getRozszerzenie(),aTypPlikow.getRozszerzenie().toUpperCase()}:null,false);

        getPublikujacy().publikujZdarzenie(new ProgressEvent(this,pPLikiDoSkasowania.size(),0,"Import plików typu"+aTypPlikow.getEtykieta().toLowerCase(),""));
        int i=0;
        for(File pPlik:pPLikiDoSkasowania){
            try {

                File pPlikDest= new File(aKatalogDo.toFile().getAbsolutePath()+SEP+(pPlik.getName()).toLowerCase());
                if(pPlikDest.exists()){
                    pPlikDest.delete();
                    ZarzadcaLogowania.podajInstancje().logujKomunikat("Plik=> "+pPlikDest.getAbsolutePath()+" istnieje.Skasowano.");
                    pPlikDest= new File(aKatalogDo.toFile().getAbsolutePath()+SEP+(pPlik.getName()).toLowerCase());
                    continue;
                }
                FileUtils.moveFile(pPlik,pPlikDest);
                i++;
                getPublikujacy().publikujZdarzenie(new ProgressEvent(this,pPLikiDoSkasowania.size(),i,"Import plików typu "+aTypPlikow.getEtykieta(),
                        "Plik przeniesiony do lokalizacji=> "+pPlikDest.getAbsolutePath()));
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Plik przeniesiony do lokalizacji=> "+pPlikDest.getAbsolutePath());

            } catch (IOException e) {
                getPublikujacy().publikujZdarzenie(new ProgressEvent(this,pPLikiDoSkasowania.size(),i,"Import plików typu "+aTypPlikow.getEtykieta(),
                        "Błąd! Nie udało się przenieść pliku=>"+pPlik.getName()));
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Błąd! Nie udało się przenieść pliku=>"+pPlik.getName());
            }
            System.out.println("Conter=>"+i);

        }
    }


   /* public static void przeniesPliki(Set<Plik> aPliki, Katalog aKatalogDo){
        int i=0;
        for(Plik pP:aPliki){
            File pPlik=pP.getPlik();
            try {
                //File pPlik=pP.getPlik();
                //FileUtils.moveFileToDirectory(pPlik,aKatalogDo.getPlik(),false);
                File pPlikDest= new File(aKatalogDo.getPlik().getAbsolutePath()+SEP+(pPlik.getName()).toLowerCase());
                if(pPlikDest.exists()){
                    pPlikDest.delete();
                    ZarzadcaLogowania.podajInstancje().logujKomunikat("Plik=> "+pPlikDest.getAbsolutePath()+" istnieje.Skasowano.");
                    pPlikDest= new File(aKatalogDo.getPlik().getAbsolutePath()+SEP+(pPlik.getName()).toLowerCase());
                    continue;
                }
                FileUtils.moveFile(pPlik,pPlikDest);
                i++;
                *//*  if(pPlikDest.exists()&&pPlik.exists()){
                    pPlik.delete();
                }*//*
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Plik przeniesiony do lokalizacji=> "+pPlikDest.getAbsolutePath());
            } catch (IOException e) {
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Błąd! Nie udało się przenieść pliku=>"+pPlik.getName());
            }
            System.out.println("Conter=>"+i);

        }
    }*/


  /*  public static void wyszukajDuplikatyDlaDrzewaKatalogow( Katalog aRoot ) {
        Katalog pUsuniete=new Katalog(aRoot.getPlik().getAbsolutePath()+SEP+ ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.PODKATALOG_DO_USUNIECIA));

        Map<String,Plik>pMapa=new HashMap<String, Plik>();
        wyszukajDuplikatyDlaKatalogu(pUsuniete,aRoot,pMapa);
    }*/

   /* private static void wyszukajDuplikatyDlaKatalogu(Katalog aKatalogDoUsuniecia, Katalog aKatalog,Map<String,Plik> aMapa) {


        File[] list = aKatalog.getPlik().listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                if(!f.getName().equalsIgnoreCase(ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.PODKATALOG_DO_USUNIECIA))){


                wyszukajDuplikatyDlaKatalogu(aKatalogDoUsuniecia,new Katalog(f),aMapa);
                }
            }
            else {
                try {
                    FileInputStream fis = new FileInputStream(f);
                    String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);

                    if(aMapa.containsKey(md5)){
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Plik=>"+f.getAbsolutePath()+" duplikuje plik=>"+aMapa.get(md5).getPlik().getAbsolutePath());
                       ZarzadcaLogowania.podajInstancje().logujKomunikat("Duplikat przeniesiono do=>"+aKatalogDoUsuniecia.getPlik().getAbsolutePath());

                        File pDocelowy=new File(aKatalogDoUsuniecia.getPlik().getAbsolutePath()+SEP+f.getName());

                        if(pDocelowy.exists()){
                            pDocelowy.delete();
                        }

                        FileUtils.moveFileToDirectory(f,aKatalogDoUsuniecia.getPlik(),true);
                    } else{
                        aMapa.put(md5,new Plik(f));
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);

            } catch (IOException e) {
                    throw new RuntimeException(e);
            }

            }
        }
    }*/

   /* public static void zmienNazwePlikowDlaDrzewaKatalogow( String path,String aPrefix ) {
        zmienNazwePlikowDlaKatalogu(path,aPrefix);
    }

    private static void zmienNazwePlikowDlaKatalogu( String path,String aPrefix ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                zmienNazwePlikowDlaKatalogu( f.getAbsolutePath(),aPrefix );
                System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                String pNowaNazwa=f.getParent()+SEP+aPrefix+f.getName().toLowerCase();
                File pNowyPlik=new File(pNowaNazwa);
                f.renameTo(pNowyPlik);
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Zmieniono nazwe z=>"+f.getAbsolutePath()+" na=>"+pNowyPlik.getAbsolutePath());

            }
        }
    }*/
}
