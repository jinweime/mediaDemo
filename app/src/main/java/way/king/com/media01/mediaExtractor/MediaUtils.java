package way.king.com.media01.mediaExtractor;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/*
 * Author: VICK
 * Create: 2020-02-29
 * Description:
 */
public class MediaUtils {

    private MediaExtractor mediaExtractor;
    private static String TAG = "VICK";

    /**
     * 提取视频
     *
     * @param sourceVideoPath 原始视频文件
     * @throws Exception 出错
     */
    public static void extractVideo(String sourceVideoPath, String outVideoPath) throws Exception {
        MediaExtractor sourceMediaExtractor = new MediaExtractor();
        sourceMediaExtractor.setDataSource(sourceVideoPath);
        int numTracks = sourceMediaExtractor.getTrackCount();
        int sourceVideoTrackIndex = -1; // 原始视频文件视频轨道参数
        for (int i = 0; i < numTracks; ++i) {
            MediaFormat format = sourceMediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            Log.d(TAG, "MediaFormat: " + mime);
            //编码格式有二种
            // 1、 H.264/AVC video/avc  video是视频轨道
            // 2、"audio/mp4a-latm" -  audio是音频轨道   AAC audio (note, this is raw AAC packets, not packaged in LATM!)     //由于获取到的视频轨道的mime为video/avc,所以知道视频编码格式为.h264,获取到的音频轨道的mime为audio/mp4a-latm，对应的音频编码格式为aac。
            if (mime.startsWith("video/")) {
                sourceMediaExtractor.selectTrack(i);
                sourceVideoTrackIndex = i;
                Log.d(TAG, "selectTrack index=" + i + "; format: " + mime);
                break;
            }
        }
        //path 输出路径
        //format 当前只支持MP4格式；
        MediaMuxer outputMediaMuxer = new MediaMuxer(outVideoPath,
                MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        outputMediaMuxer.addTrack(sourceMediaExtractor.getTrackFormat(sourceVideoTrackIndex));
        outputMediaMuxer.start();
        //分配一个buffer 内存， 分配的内存要尽量大一些
        ByteBuffer inputBuffer = ByteBuffer.allocate(1024 * 1024 * 2);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int sampleSize;
        while ((sampleSize = sourceMediaExtractor.readSampleData(inputBuffer, 0)) >= 0) {
            long presentationTimeUs = sourceMediaExtractor.getSampleTime();
            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs = presentationTimeUs;
            outputMediaMuxer.writeSampleData(sourceVideoTrackIndex, inputBuffer, info);
            //读取下一帧数据
            sourceMediaExtractor.advance();
        }
        //停止合成
        outputMediaMuxer.stop();
        outputMediaMuxer.release();    // 停止并释放 MediaMuxer
        sourceMediaExtractor.release();
        sourceMediaExtractor = null;   // 释放 MediaExtractor
    }


    public static void extractAudio(String srcPath, String outPath) throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.setDataSource(srcPath);//媒体文件的位置
        for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {//遍历媒体轨道，包括视频和音频轨道
            MediaFormat format = mediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio")) {//获取音频轨道
                mediaExtractor.selectTrack(i);//选择此音频轨道
                break;
            }
        }
        ByteBuffer inputBuffer = ByteBuffer.allocate(100 * 1024 * 2);
        FileOutputStream fe = new FileOutputStream(outPath, true);
        while (true) {
            int readSampleCount = mediaExtractor.readSampleData(inputBuffer, 0);
            if (readSampleCount < 0) {
                break;
            }
            byte[] buffer = new byte[readSampleCount];
            inputBuffer.get(buffer);
            fe.write(buffer);
            inputBuffer.clear();
            mediaExtractor.advance();
        }
        fe.flush();
        fe.close();
        mediaExtractor.release();
        mediaExtractor = null;

    }

    private static void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2;  //AAC LC，MediaCodecInfo.CodecProfileLevel.AACObjectLC;
        int freqIdx = 5;  //32K, 见后面注释avpriv_mpeg4audio_sample_rates中32000对应的数组下标，来自ffmpeg源码
        int chanCfg = 2;  //见后面注释channel_configuration，Stero双声道立体声

        /*int avpriv_mpeg4audio_sample_rates[] = {
            96000, 88200, 64000, 48000, 44100, 32000,
                    24000, 22050, 16000, 12000, 11025, 8000, 7350
        };
        channel_configuration: 表示声道数chanCfg
        0: Defined in AOT Specifc Config
        1: 1 channel: front-center
        2: 2 channels: front-left, front-right
        3: 3 channels: front-center, front-left, front-right
        4: 4 channels: front-center, front-left, front-right, back-center
        5: 5 channels: front-center, front-left, front-right, back-left, back-right
        6: 6 channels: front-center, front-left, front-right, back-left, back-right, LFE-channel
        7: 8 channels: front-center, front-left, front-right, side-left, side-right, back-left, back-right, LFE-channel
        8-15: Reserved
        */

        // fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }
}
