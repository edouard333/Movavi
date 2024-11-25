package com.phenix.movavi.dto;

/**
 * Clip dans la timeline.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public final class ClipDTO {

    /**
     * Clip type video.
     */
    public static final int VIDEO = 1;

    /**
     * Clip type audio.
     */
    public static final int AUDIO = 0;

    /**
     * Clip type texte.
     */
    public static final int TEXT = 4;

    /**
     * Nom du clip (dans la timeline).
     */
    public String name;

    /**
     * 1 ou 2 = video, l'autre est l'audio.
     */
    public int type;

    /**
     * Quand commence dans la timeline le clip (en miliseconde).
     */
    public int start;

    /**
     * Quand fini le clip dans la timeline (en miliseconde).
     */
    public int end;

    /**
     * Durée du clip dans la timeline (pas la durée totale du clip).
     */
    public int duration;

    /**
     * Durée du fichier.
     */
    public int source_duration;

    /**
     * Point in du clip.
     */
    public int in;

    /**
     * Point out du clip.
     */
    public int out;

    /**
     * Numéro de piste audio.
     */
    public int number_track;

    /**
     * Indique si une stabilisation image a été réalisée.
     */
    public boolean stabilization;

    /**
     *
     */
    public String texte;

    /**
     * Construit un clip.
     */
    public ClipDTO() {
        this.number_track = 1;
    }
}
