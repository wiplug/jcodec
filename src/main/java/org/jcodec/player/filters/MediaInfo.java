package org.jcodec.player.filters;

import java.io.Serializable;

import javax.sound.sampled.AudioFormat;

import org.jcodec.common.model.Rational;
import org.jcodec.common.model.Size;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * @author The JCodec project
 * 
 */
public class MediaInfo implements Serializable {

    private static final long serialVersionUID = 4233929725075964221L;

    private String fourcc;
    private int timescale;
    private long duration;
    private long nFrames;

    public static class VideoInfo extends MediaInfo {
        private static final long serialVersionUID = 423837425262959380L;

        private Rational par;
        private Size dim;

        public VideoInfo(String fourcc, int timescale, long duration, long nFrames, Rational par, Size dim) {
            super(fourcc, timescale, duration, nFrames);
            this.par = par;
            this.dim = dim;
        }

        public Rational getPAR() {
            return par;
        }

        public Size getDim() {
            return dim;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            VideoInfo other = (VideoInfo) obj;
            if (dim == null) {
                if (other.dim != null)
                    return false;
            } else if (!dim.equals(other.dim))
                return false;
            if (par == null) {
                if (other.par != null)
                    return false;
            } else if (!par.equals(other.par))
                return false;
            return true;
        }
    };

    public static class AudioInfo extends MediaInfo {
        private static final long serialVersionUID = 8555059625746056969L;

        private AudioFormat af;
        private int framesPerPacket;

        public AudioInfo(String fourcc, int timescale, long duration, long nFrames, AudioFormat af, int framesPerPacket) {
            super(fourcc, timescale, duration, nFrames);
            this.af = af;
            this.framesPerPacket = framesPerPacket;
        }

        public AudioFormat getFormat() {
            return af;
        }

        public int getFramesPerPacket() {
            return framesPerPacket;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            AudioInfo other = (AudioInfo) obj;
            if (af == null) {
                if (other.af != null)
                    return false;
            } else if (!af.equals(other.af))
                return false;
            if (framesPerPacket != other.framesPerPacket)
                return false;
            return true;
        }
    }

    public MediaInfo(String fourcc, int timescale, long duration, long nFrames) {
        this.fourcc = fourcc;
        this.timescale = timescale;
        this.duration = duration;
        this.nFrames = nFrames;
    }

    public String getFourcc() {
        return fourcc;
    }

    public int getTimescale() {
        return timescale;
    }

    public long getDuration() {
        return duration;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public long getNFrames() {
        return nFrames;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaInfo other = (MediaInfo) obj;
        if (duration != other.duration)
            return false;
        if (fourcc == null) {
            if (other.fourcc != null)
                return false;
        } else if (!fourcc.equals(other.fourcc))
            return false;
        if (nFrames != other.nFrames)
            return false;
        if (timescale != other.timescale)
            return false;
        return true;
    }
}