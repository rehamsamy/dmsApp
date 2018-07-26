package com.dmsegypt.dms.rest.model;

/**
 * Created by ProG-amr on 19/01/2017.
 */
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by ProG-amr on 09/12/2016.
 */
public class ProgressRequestBody extends RequestBody {
    private File mFile;
    private UploadCallbacks mListener;
    String id;

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public interface UploadCallbacks {
        void onProgressUpdate(String id, int percentage);

    }

    public ProgressRequestBody(final File file, final  UploadCallbacks listener, String id) {
        mFile = file;
        mListener = listener;
        this.id=id;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {

            if (mListener!=null){
            mListener.onProgressUpdate(id,(int)(100 * mUploaded / mTotal));
            }
        }
    }
}

