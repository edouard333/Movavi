package com.phenix.movavi.util;

import com.phenix.movavi.Movavi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Utils {

    /**
     * Empêcher d'instancier la classe.
     */
    private Utils() {
    }

    /**
     * Décompresser un projet Adobe Premiere.
     *
     * @param gzipFile
     * @param newFile
     *
     * @throws IOException
     */
    public static void decompressGzipFile(File gzipFile, File newFile) throws IOException {
        FileInputStream fis = new FileInputStream(gzipFile);
        GZIPInputStream gis = new GZIPInputStream(fis);
        FileOutputStream fos = new FileOutputStream(newFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        //close resources
        fos.close();
        gis.close();
    }

    public static File decompressZipFile(File zip_file) throws FileNotFoundException, IOException {
        String fileZip = zip_file.getAbsolutePath();
        File destDir = new File(zip_file.getParent() + File.separator + "TMP");

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
        }

        zis.closeEntry();
        zis.close();

        return destDir;
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     *
     * @param fichier
     * @return
     */
    public static File getFichierXMLTemporaire(File fichier) {
        String chemin = fichier.getAbsolutePath().replace(Movavi.EXTENSION, Movavi.EXTENSION_TMP);
        chemin = chemin.replace(Movavi.EXTENSION2, Movavi.EXTENSION_TMP);
        return new File(chemin);
    }
}
