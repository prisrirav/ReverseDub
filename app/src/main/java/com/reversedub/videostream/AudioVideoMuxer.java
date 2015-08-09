package com.reversedub.videostream;

import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class AudioVideoMuxer  {

    // Doesn't work, debugging in progress
    private Boolean CombineFilesUsingMediaMuxer(String videoFile, String audioFile, String outputFile)
    {
        MediaMuxer muxer;
        try
        {
            muxer = new MediaMuxer(outputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        MediaExtractor mediaVideoExtractor = new MediaExtractor();
        try
        {
            mediaVideoExtractor.setDataSource(videoFile);
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        MediaFormat videoFormat = mediaVideoExtractor.getTrackFormat(0);
        MediaExtractor mediaAudioExtractor = new MediaExtractor();
        try
        {
            mediaAudioExtractor.setDataSource(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        MediaFormat audioFormat = mediaAudioExtractor.getTrackFormat(0);

        int audioTrackIndex = muxer.addTrack(audioFormat);
        int videoTrackIndex = muxer.addTrack(videoFormat);
        ByteBuffer inputBuffer = ByteBuffer.allocate(1000000);
        boolean finished = false;
        boolean isAudioSample = true;
        BufferInfo bufferInfo = new BufferInfo();

        muxer.start();
        while(!finished) {

            if (isAudioSample)
            {
                bufferInfo.offset = 0;
                bufferInfo.size = mediaAudioExtractor.readSampleData(inputBuffer, 0);
                bufferInfo.flags = mediaAudioExtractor.getSampleFlags();
                bufferInfo.presentationTimeUs = mediaAudioExtractor.getSampleTime();
            }
            else
            {
                bufferInfo.offset = 0;
                bufferInfo.size = mediaVideoExtractor.readSampleData(inputBuffer, 0);
                bufferInfo.flags = mediaVideoExtractor.getSampleFlags();
                bufferInfo.presentationTimeUs = mediaVideoExtractor.getSampleTime();
            }

            if (bufferInfo.size < 0)
            {
                finished = true;
                bufferInfo.size = 0;
            }

            if (!finished) {
                int currentTrackIndex = isAudioSample ? audioTrackIndex : videoTrackIndex;
                muxer.writeSampleData(currentTrackIndex, inputBuffer, bufferInfo);
                isAudioSample = !isAudioSample;
            }
        };
        muxer.stop();
        muxer.release();
        return true;
    }

    public static Boolean CombineFilesUsingMp4Parser(String videoFile, String audioFile, String outputFile)
    {
        Movie video;
        try {
            video = new MovieCreator().build(videoFile);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Movie audio;
        try {
            audio = new MovieCreator().build(audioFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        Track audioTrack = audio.getTracks().get(0);
        video.addTrack(audioTrack);

        Container out = new DefaultMp4Builder().build(video);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        BufferedWritableFileByteChannel byteBufferByteChannel = new BufferedWritableFileByteChannel(fos);
        try {
            out.writeContainer(byteBufferByteChannel);
            byteBufferByteChannel.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static class BufferedWritableFileByteChannel implements WritableByteChannel {
        private static final int BUFFER_CAPACITY = 1000000;

        private boolean isOpen = true;
        private final OutputStream outputStream;
        private final ByteBuffer byteBuffer;
        private final byte[] rawBuffer = new byte[BUFFER_CAPACITY];

        private BufferedWritableFileByteChannel(OutputStream outputStream) {
            this.outputStream = outputStream;
            this.byteBuffer = ByteBuffer.wrap(rawBuffer);
        }

        @Override
        public int write(ByteBuffer inputBuffer) throws IOException {
            int inputBytes = inputBuffer.remaining();

            if (inputBytes > byteBuffer.remaining()) {
                dumpToFile();
                byteBuffer.clear();

                if (inputBytes > byteBuffer.remaining()) {
                    throw new BufferOverflowException();
                }
            }

            byteBuffer.put(inputBuffer);

            return inputBytes;
        }

        @Override
        public boolean isOpen() {
            return isOpen;
        }

        @Override
        public void close() throws IOException {
            dumpToFile();
            isOpen = false;
        }
        private void dumpToFile() {
            try {
                outputStream.write(rawBuffer, 0, byteBuffer.position());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
