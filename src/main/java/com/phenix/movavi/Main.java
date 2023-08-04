package com.phenix.movavi;

import com.phenix.movavi.dto.ClipDTO;
import com.phenix.tools.Timecode;
import com.phenix.tools.XMLNLE.Media;
import com.phenix.tools.XMLNLE.MediaAudio;
import com.phenix.tools.XMLNLE.MediaVideo;
import com.phenix.tools.XMLNLE.Timeline;
import com.phenix.tools.XMLNLE.XMLFCP7;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Movavi projet = new Movavi(new File("C:\\Users\\win10dev\\Desktop\\config.json"));

        int frameRateN = projet.timeline.video.frameRateN;
        int frameRateD = projet.timeline.video.frameRateD;

        System.out.println("Projet :");
        System.out.println("Résolution : " + projet.timeline.video.width + "x" + projet.timeline.video.height);
        System.out.println("Framerate : " + frameRateN + "/" + frameRateD);

        ArrayList<ClipDTO> liste_clip = projet.liste_clip;

        System.out.println("Nombre clip : " + liste_clip.size());

        ClipDTO clip;

        for (int i = 0; i < liste_clip.size(); i++) {
            clip = liste_clip.get(i);
            System.out.println("-> " + clip.name);
            System.out.println("Start " + milisecondeToTC(clip.start, frameRateN, frameRateD));
            System.out.println("In : " + milisecondeToTC(clip.in, frameRateN, frameRateD)
                    + " - Out : " + milisecondeToTC(clip.out, frameRateN, frameRateD));
        }

        XMLFCP7 xml = new XMLFCP7(new File("C:\\Users\\win10dev\\Desktop\\TimelineForResolve.xml"), XMLFCP7.ECRITURE, XMLFCP7.RESOLVE);

        double framerate = (double) frameRateN / (double) frameRateD;
        int framerate_int = (int) Math.round(framerate);
        System.out.println("Timeline FR : " + framerate);
        System.out.println("Timeline FR(int) : " + framerate_int);

        Timeline timeline = new Timeline("Project from Movavi", framerate_int, new Timecode("00:00:00:00", framerate_int));

        Media media;

        for (int i = 0; i < liste_clip.size(); i++) {
            clip = liste_clip.get(i);

            //if (clip.type == ClipDTO.VIDEO) {
            media = new MediaVideo(clip.name);
            media.setNomFichier(clip.name);
            media.setNomBobine(clip.name.split("\\.", -1)[0]);
            //((MediaVideo) media).setCanaux(2);
            /*} else {
                media = new MediaAudio(clip.name);
            }*/
            media.setStart(new Timecode("00:00:00:00"));
            media.setIn(new Timecode(milisecondeToTC(clip.in, frameRateN, frameRateD)));
            media.setOut(new Timecode(milisecondeToTC(clip.out, frameRateN, frameRateD)));

            timeline.addMedia(
                    clip.number_track,
                    media,
                    new Timecode(milisecondeToTC(clip.start, frameRateN, frameRateD)),
                    new Timecode(milisecondeToTC(clip.end, frameRateN, frameRateD))
            );
        }
        xml.addTimeline(timeline);
        xml.close();
    }

    private static String digit(int nombre) {
        if (nombre > 10) {
            return nombre + "";
        } else {
            return "0" + nombre;
        }
    }

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
