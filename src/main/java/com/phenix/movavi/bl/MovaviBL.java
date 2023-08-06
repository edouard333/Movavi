package com.phenix.movavi.bl;

import com.phenix.movavi.Movavi;
import com.phenix.movavi.dto.ClipDTO;
import com.phenix.movavi.exception.MovaviException;
import com.phenix.movavi.util.Utils;
import com.phenix.tools.Timecode;
import com.phenix.tools.XMLNLE.Media;
import com.phenix.tools.XMLNLE.MediaVideo;
import com.phenix.tools.XMLNLE.Timeline;
import com.phenix.tools.XMLNLE.XMLFCP7;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Les traitements.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class MovaviBL {

    /**
     * Empêcher d'instancier la classe.
     */
    private MovaviBL() {
    }

    /**
     * Converti un projet Movavi en projet DaVinci Resolve.
     *
     * @param projet_movavi
     * @throws MovaviException
     */
    public static void convert(File projet_movavi) throws MovaviException {
        try {
            System.out.println(projet_movavi.getAbsolutePath());
            File dossier_tmp = Utils.decompressZipFile(projet_movavi);

            // Récupère le fichier "config" qu'il soit ".xml" ou ".json".
            File config = dossier_tmp.listFiles((dir, name) -> {
                return name.contains("config");
            })[0];

            System.out.println("file_movavi : " + config.getAbsolutePath());

            Movavi projet = new Movavi(config);

            int frameRateN = projet.timeline.video.frameRateN;
            int frameRateD = projet.timeline.video.frameRateD;

            System.out.println("Projet :");
            System.out.println("Résolution : " + projet.timeline.video.width + "x" + projet.timeline.video.height);
            System.out.println("Framerate : " + frameRateN + "/" + frameRateD);

            ArrayList<ClipDTO> liste_clip = projet.liste_clip;

            liste_clip.sort((c1, c2) -> {
                if (c1.start > c2.start) {
                    return 1;
                } else {
                    return -1;
                }
            });

            System.out.println("Nombre clip : " + liste_clip.size());

            ClipDTO clip;

            for (int i = 0; i < liste_clip.size(); i++) {
                clip = liste_clip.get(i);
                System.out.println("-> " + clip.name);
                System.out.println("Start " + milisecondeToTC(clip.start, frameRateN, frameRateD));
                System.out.println("In : " + milisecondeToTC(clip.in, frameRateN, frameRateD)
                        + " - Out : " + milisecondeToTC(clip.out, frameRateN, frameRateD));
            }

            XMLFCP7 xml = new XMLFCP7(new File(projet_movavi.getParent() + File.separator + "TimelineForResolve.xml"), XMLFCP7.ECRITURE, XMLFCP7.RESOLVE);

            double framerate = (double) frameRateN / (double) frameRateD;
            int framerate_int = (int) Math.round(framerate);
            System.out.println("Timeline FR : " + framerate);
            System.out.println("Timeline FR(int) : " + framerate_int);

            Timeline timeline = new Timeline("Project from Movavi", framerate_int, new Timecode("00:00:00:00", framerate_int));
            timeline.setDimension(1920, 1080);

            Media media;

            for (int i = 0; i < liste_clip.size(); i++) {
                clip = liste_clip.get(i);

                if (clip.type == ClipDTO.VIDEO) {
                    media = new MediaVideo(clip.name);
                    media.setNomFichier(clip.name);
                    media.setLocalisation(clip.name);
                    media.setNomBobine(clip.name.split("\\.", -1)[0]);
                    //((MediaVideo) media).setCanaux(2);
                    ((MediaVideo) media).setDimension(1920, 1080);

                    media.setStart(new Timecode("00:00:00:00"));
                    media.setDureeFichier(new Timecode(milisecondeToTC(clip.source_duration, frameRateN, frameRateD)));
                    media.setIn(new Timecode(milisecondeToTC(clip.in, frameRateN, frameRateD)));
                    media.setOut(new Timecode(milisecondeToTC(clip.out, frameRateN, frameRateD)));

                    System.out.println("Numéro piste : " + 1);

                    timeline.addMedia(
                            /*clip.number_track*/1,
                            media,
                            new Timecode(milisecondeToTC(clip.start, frameRateN, frameRateD)),
                            new Timecode(milisecondeToTC(clip.end, frameRateN, frameRateD))
                    );
                }
                /*else {
                media = new MediaAudio(clip.name);
            }*/
            }
            xml.addTimeline(timeline);
            xml.close();

            deleteFile(dossier_tmp);

        } catch (IOException exception) {
            throw new MovaviException(exception.getMessage());
        }
    }

    /**
     *
     * @param fichier
     */
    private static void deleteFile(File fichier) {

        File[] liste_fichier = fichier.listFiles();

        if (liste_fichier.length > 0) {
            for (int i = liste_fichier.length - 1; i >= 0; i--) {
                if (liste_fichier[i].isDirectory()) {
                    // Parcours le dossier pour supprimer son contenu.
                    deleteFile(liste_fichier[i]);
                } else {
                    // Supprime le fichier.
                    liste_fichier[i].delete();
                }

            }
        }

        // Supprime le dossier.
        fichier.delete();
    }

    /**
     *
     * @param nombre
     * @return
     */
    private static String digit(int nombre) {
        if (nombre >= 10) {
            return nombre + "";
        } else {
            return "0" + nombre;
        }
    }

    /**
     *
     * @param miliseconde
     * @param framerateN
     * @param framerateD
     * @return
     */
    private static String milisecondeToTC(int miliseconde, int framerateN, int framerateD) {
        int heure = miliseconde / (1000 * 60 * 60);
        miliseconde -= heure * (1000 * 60 * 60);

        int minute = (miliseconde) / (1000 * 60);
        miliseconde -= minute * (1000 * 60);

        int seconde = miliseconde / (1000);
        miliseconde -= seconde * (1000);

        float image = ((float) miliseconde / 1000F) * ((float) framerateN / (float) framerateD);

        return digit(heure) + ":" + digit(minute) + ":" + digit(seconde) + ":" + digit((int) image);
    }
}
