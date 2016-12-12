package mw.uslugi;


import mw.uslugi.io.ZarzadcaLogowania;
import mw.wspolne.model.GalerieWWW;
import mw.wspolne.model.TypPublikacjiEnum;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mw on 01.02.16.
 */
@Component
public class PublikacjaGaleriiWWWUslugaImpl implements PublikacjaGaleriiWWWUsluga {


    @Autowired
    protected KonfiguratorAplikacji konfiguratorAplikacji;

    private int inkrement = 1;

    //todo
    /*
    * obsluga poprawnosci polaczenia
    * dodawanie katalogu root jesli nie ma
    * obsluga braku katalogu podanego w uri
    * upewnianie sie czy uzytkownik chce usunac/usunac i nadpisac zdalna strukture
    * */
    @Override
    public void publikujGalerie(GalerieWWW aGalerie, String aHost, String aURI, String aLogin, String aHaslo, TypPublikacjiEnum typPublikacji) {
        long czasStart = System.currentTimeMillis();
        FTPClient client = new FTPClient();
        try {
            long aLiczbaZdjec = aGalerie.getListaKategorii().stream().flatMap
                    (kat -> kat.getListaGalerii().stream().flatMap(g -> g.getObrazki().stream())).count();
            inkrement = 1;
            //MonitorPaskaPostepu.podajInstancje().inicjalizujPasekPostepu("Publikacja galerii", Integer.parseInt(String.valueOf(aLiczbaZdjec * 2)));
            client.connect(aHost);
            client.enterLocalPassiveMode();
            client.login(aLogin, aHaslo);

            if(!sprawdzPolaczenie(client)){
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Blad! Problem z połączeniem. (Sprawdź poprawność loginu i hasła!)");
                return;
            }

            if (!isExists(client, aURI)) {
                makeDirectories(client,aURI);
            }

            if (typPublikacji == TypPublikacjiEnum.TYLKO_KASOWANIE) {
                removeDirectory(client, aURI, "");
            } else {
                uploadDirectory(client, aURI, konfiguratorAplikacji.getGaleria().getCel(), "");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        long czasStop = System.currentTimeMillis();

        ZarzadcaLogowania.podajInstancje().logujKomunikat("Czas przetwarzania=>" + (czasStop - czasStart) / 1000 + " sekund");
    }

    /**
     * Upload a whole directory (including its nested sub directories and files)
     * to a FTP server.
     *
     * @param ftpClient       an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param remoteDirPath   Path of the destination directory on the server.
     * @param localParentDir  Path of the local directory being uploaded.
     * @param remoteParentDir Path of the parent directory of the current directory on the
     *                        server (used by recursive calls).
     * @throws IOException if any network or IO error occurred.
     */
    public void uploadDirectory(FTPClient ftpClient,
                                String remoteDirPath, String localParentDir, String remoteParentDir)
            throws IOException {

        ZarzadcaLogowania.podajInstancje().logujKomunikat("LISTING directory: " + localParentDir);

        Path localDir = Paths.get(localParentDir);

        Files.list(localDir).forEach(item -> {
            try {

                String remoteFilePath = remoteDirPath + "/" + remoteParentDir
                        + "/" + item.toFile().getName();
                if (remoteParentDir.equals("")) {
                    remoteFilePath = remoteDirPath + "/" + item.toFile().getName();
                }


                if (Files.isRegularFile(item)) {
                    // upload the file
                    String localFilePath = item.toString();
                    ZarzadcaLogowania.podajInstancje().logujKomunikat("Transfer pliku: " + localFilePath);

                    boolean uploaded = uploadSingleFile(ftpClient,
                            localFilePath, remoteFilePath);
                    if (uploaded) {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Plik transferowany do: "
                                + remoteFilePath);
                    } else {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Blad! Nieudany tranfser pliku: "
                                + localFilePath);
                    }

                    //MonitorPaskaPostepu.podajInstancje().aktualizujStan(inkrement++);
                } else {
                    // create directory on the server
                    boolean created = ftpClient.makeDirectory(remoteFilePath);
                    if (created) {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Utworzono katalog: "
                                + remoteFilePath);
                    } else {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Blad! Nieudane tworzenie katalogu: "
                                + remoteFilePath);
                    }

                    // upload the sub directory
                    String parent = remoteParentDir + "/" + item.toFile().getName();
                    if (remoteParentDir.equals("")) {
                        parent = item.toFile().getName();
                    }
                    uploadDirectory(ftpClient, remoteDirPath, item.toString(),
                            parent);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });

    }


    /**
     * Upload a single file to the FTP server.
     *
     * @param ftpClient      an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param localFilePath  Path of the file on local computer
     * @param remoteFilePath Path of the file on remote the server
     * @return true if the file was uploaded successfully, false otherwise
     * @throws IOException if any network or IO error occurred.
     */
    public boolean uploadSingleFile(FTPClient ftpClient,
                                    String localFilePath, String remoteFilePath) throws IOException {
        File localFile = new File(localFilePath);

        InputStream inputStream = new FileInputStream(localFile);
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.storeFile(remoteFilePath, inputStream);
        } finally {
            inputStream.close();
        }
    }


    /**
     * Removes a non-empty directory by delete all its sub files and
     * sub directories recursively. And finally remove the directory.
     */
    public void removeDirectory(FTPClient ftpClient, String parentDir,
                                String currentDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                if (aFile.isDirectory()) {
                    // remove the sub directory
                    removeDirectory(ftpClient, dirToList, currentFileName);
                } else {
                    // delete the file
                    boolean deleted = ftpClient.deleteFile(filePath);
                    if (deleted) {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Skasowano plik: " + filePath);
                    } else {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Błąd! Nie można skasować pliku: "
                                + filePath);
                    }
                }
            }

            // finally, remove the directory itself
            boolean removed = ftpClient.removeDirectory(dirToList);
            if (removed) {
               ZarzadcaLogowania.podajInstancje().logujKomunikat("Usunięto katalog: " + dirToList);
            } else {
               ZarzadcaLogowania.podajInstancje().logujKomunikat("Błąd!=>Nie można skasować katalogu: " + dirToList);
            }
        }
    }

    public static boolean isExists(FTPClient ftpClient, String pathName) {
        try {
            ftpClient.getStatus(pathName);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return FTPReply.isPositiveCompletion(ftpClient.getReplyCode());
    }

    private boolean sprawdzPolaczenie(FTPClient ftpClient) {
        try {
            // Sends a NOOP command to the FTP server.
            ftpClient.getStatus("");
            return FTPReply.isPositiveCompletion(ftpClient.getReplyCode());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }


    }

    /**
     * Creates a nested directory structure on a FTP server
     * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param dirPath Path of the directory, i.e /projects/java/ftp/demo
     * @return true if the directory was created successfully, false otherwise
     * @throws IOException if any error occurred during client-server communication
     */
    public static boolean makeDirectories(FTPClient ftpClient, String dirPath)
            throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir : pathElements) {
              if(singleDir==null||singleDir.isEmpty()){
                  continue;
              }
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        System.out.println("CREATED directory: " + singleDir);
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        System.out.println("COULD NOT create directory: " + singleDir);
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
