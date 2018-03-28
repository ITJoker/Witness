package com.risenb.witness.ui.tasklist;

import android.os.AsyncTask;

class CompressPhotoTask extends AsyncTask<String, String, String> {
    private CompressPhotoTaskCompletedListener compressPhotoTaskCompletedListener;

    @Override
    protected void onPreExecute() {
        if (compressPhotoTaskCompletedListener != null) {
            compressPhotoTaskCompletedListener.onPreExecuteListenerMethod();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if (compressPhotoTaskCompletedListener != null) {
            compressPhotoTaskCompletedListener.doInBackgroundListenerMethod();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (compressPhotoTaskCompletedListener != null) {
            compressPhotoTaskCompletedListener.onPostExecuteListenerMethod();
        }
    }

    public void setCompressPhotoTaskCompletedListener(CompressPhotoTaskCompletedListener compressPhotoTaskCompletedListener) {
        this.compressPhotoTaskCompletedListener = compressPhotoTaskCompletedListener;
    }
}
