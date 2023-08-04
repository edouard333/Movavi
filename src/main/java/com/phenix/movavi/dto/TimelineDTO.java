package com.phenix.movavi.dto;

/**
 * Timeline.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class TimelineDTO {

    /**
     * Informations vidéo de la timeline.
     */
    public VideoDTO video;

    /**
     * Informations audio de la timeline.
     */
    public AudioDTO audio;

    /**
     * Construit une timeline.
     */
    public TimelineDTO() {
        this.video = new VideoDTO();
        this.audio = new AudioDTO();
    }
}
