
package com.v380.tv;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.IOException;

public class V380VideoDecoder {

    private MediaCodec decoder;

    public boolean start(Surface surface, int width, int height) {
        try {
            MediaFormat format = MediaFormat.createVideoFormat(
                    MediaFormat.MIMETYPE_VIDEO_AVC,
                    width,
                    height
            );

            decoder = MediaCodec.createDecoderByType(
                    MediaFormat.MIMETYPE_VIDEO_AVC
            );

            decoder.configure(format, surface, null, 0);
            decoder.start();

            return true;

        } catch (IOException | IllegalStateException e) {
            stop();
            return false;
        }
    }

    public void stop() {
        if (decoder != null) {
            try {
                decoder.stop();
            } catch (Exception ignored) {
            }

            try {
                decoder.release();
            } catch (Exception ignored) {
            }

            decoder = null;
        }
    }
}
