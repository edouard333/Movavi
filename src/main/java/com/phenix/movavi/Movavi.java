package com.phenix.movavi;

import com.phenix.movavi.dto.ClipDTO;
import com.phenix.movavi.dto.TimelineDTO;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Projet Movavi (NLE).
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Movavi {

    /**
     * La timeline dans le projet. TODO : Peut-être plusieurs ?
     */
    public TimelineDTO timeline;

    /**
     * Liste des clips dans la timeline.
     */
    public ArrayList<ClipDTO> liste_clip;

    /**
     * Construit un projet Movavi depuis le JSON "config.json".<br>
     * TODO : Voir pour récupérer directement le fichier de projet (".mepj" qui
     * est un ".zip" en fait).
     *
     * @param fichier_json Le fichier JSON.
     */
    public Movavi(File fichier_json) {

        try {
            FileReader reader = new FileReader(fichier_json, Charset.forName("UTF-8"));
            JSONObject racine = (JSONObject) new JSONParser().parse(reader);

            JSONObject data = (JSONObject) racine.get("data");
            JSONObject content = (JSONObject) data.get("content");
            JSONObject timeline_json = (JSONObject) content.get("timeline");

            JSONObject audio = (JSONObject) timeline_json.get("Audio");
            JSONObject video = (JSONObject) timeline_json.get("Video");

            this.timeline = new TimelineDTO();
            this.timeline.audio.channelLayout = Integer.parseInt(audio.get("channelLayout").toString());
            this.timeline.audio.sampleFormat = Integer.parseInt(audio.get("sampleFormat").toString());
            this.timeline.audio.sampleRate = Integer.parseInt(audio.get("sampleRate").toString());

            this.timeline.video.aspectX = Integer.parseInt(video.get("aspectX").toString());
            this.timeline.video.aspectY = Integer.parseInt(video.get("aspectY").toString());
            this.timeline.video.frameRateD = Integer.parseInt(video.get("frameRateD").toString());
            this.timeline.video.frameRateN = Integer.parseInt(video.get("frameRateN").toString());
            this.timeline.video.height = Integer.parseInt(video.get("height").toString());
            this.timeline.video.width = Integer.parseInt(video.get("width").toString());

            liste_clip = new ArrayList<ClipDTO>();

            JSONArray clips = (JSONArray) timeline_json.get("clips");

            JSONObject clip;

            JSONObject sub_clip;
            JSONObject timing;

            ClipDTO clip_tmp;

            System.out.println("nb clip : " + clips.size());

            for (int i = 0; i < clips.size(); i++) {
                clip = (JSONObject) clips.get(i);
                sub_clip = (JSONObject) clip.get("clip");

                clip_tmp = new ClipDTO();
                clip_tmp.name = sub_clip.get("name").toString();
                clip_tmp.type = Integer.parseInt(sub_clip.get("type").toString());

                timing = (JSONObject) clip.get("timing");

                clip_tmp.in = Integer.parseInt(timing.get("sourcePosition").toString());
                clip_tmp.out = Integer.parseInt(timing.get("sourcePosition").toString()) + Integer.parseInt(timing.get("duration").toString());
                clip_tmp.start = Integer.parseInt(timing.get("timestamp").toString());
                clip_tmp.end = -1;
                clip_tmp.duration = Integer.parseInt(timing.get("duration").toString());
                clip_tmp.end = clip_tmp.start + clip_tmp.duration;

                this.liste_clip.add(clip_tmp);
            }

        } catch (IOException | NumberFormatException | ParseException exception) {

        }
    }
}
