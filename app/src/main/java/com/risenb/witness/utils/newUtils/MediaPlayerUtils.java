package com.risenb.witness.utils.newUtils;

import android.media.MediaPlayer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerUtils<T extends MediaBean> {
    private static MediaPlayerUtils mediaPlayerUtils;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MediaPlayerUtils.OnPosition onPosition;
    private List<T> list = new ArrayList();
    private int idx = 0;
    private int length;

    public static MediaPlayerUtils getMediaPlayerUtils() {
        if (mediaPlayerUtils == null) {
            mediaPlayerUtils = new MediaPlayerUtils();
        }

        return mediaPlayerUtils;
    }

    public MediaPlayerUtils() {
        MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
            public void run() {
                MUtils.getMUtils().getHandler().postDelayed(this, 1000L);
                if (MediaPlayerUtils.this.onPosition != null) {
                    MediaPlayerUtils.this.onPosition.onPosition(MediaPlayerUtils.this.getCurrentPosition());
                }

            }
        }, 1000L);
    }

    public void player() {
        if (this.mediaPlayer != null) {
            (new Thread(new Runnable() {
                public void run() {
                    try {
                        URL e = new URL(MediaPlayerUtils.this.get().getMediaBeanUrl());
                        HttpURLConnection urlcon = (HttpURLConnection) e.openConnection();
                        urlcon.connect();
                        MediaPlayerUtils.this.length = urlcon.getContentLength();
                    } catch (Exception var3) {
                        ;
                    }

                }
            })).start();
            (new ThreadUtil(new Runnable() {
                public void run() {
                    try {
                        MediaPlayerUtils.this.mediaPlayer.reset();
                        MediaPlayerUtils.this.mediaPlayer.setDataSource(MediaPlayerUtils.this.get().getMediaBeanUrl());
                        MediaPlayerUtils.this.mediaPlayer.prepare();
                        MediaPlayerUtils.this.mediaPlayer.start();
                        if (MediaPlayerUtils.this.onPosition != null) {
                            MediaPlayerUtils.this.onPosition.onPlayerIdx(MediaPlayerUtils.this.idx);
                        }

                        MediaPlayerUtils.this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                MediaPlayerUtils.this.next();
                                if (MediaPlayerUtils.this.onPosition != null) {
                                    MediaPlayerUtils.this.onPosition.onPlayerIdx(MediaPlayerUtils.this.idx);
                                }

                            }
                        });
                    } catch (IllegalArgumentException var2) {
                        var2.printStackTrace();
                    } catch (SecurityException var3) {
                        var3.printStackTrace();
                    } catch (IllegalStateException var4) {
                        var4.printStackTrace();
                    } catch (IOException var5) {
                        var5.printStackTrace();
                    }

                }
            })).start();
        }

    }

    public void start() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.start();
        }

    }

    public void pause() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }

    }

    public int getCurrentPosition() {
        return this.mediaPlayer != null ? this.mediaPlayer.getCurrentPosition() : 0;
    }

    public String getCurrentPositionStr() {
        return this.trimTime(this.getCurrentPosition() / 1000);
    }

    public int getDuration() {
        return this.mediaPlayer != null ? this.mediaPlayer.getDuration() : 0;
    }

    public String getDurationStr() {
        return this.trimTime(this.getDuration() / 1000);
    }

    public void seekTo(int msec) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.seekTo(msec);
        }

    }

    public void stop() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }

    }

    public void release() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
        }

    }

    public void setOnPosition(MediaPlayerUtils.OnPosition onPosition) {
        this.onPosition = onPosition;
    }

    public boolean isPlaying() {
        return this.mediaPlayer != null ? this.mediaPlayer.isPlaying() : false;
    }

    public void add(T bean) {
        this.list.add(bean);
    }

    public void addAll(List<T> list) {
        this.list.addAll(list);
    }

    public void up() {
        --this.idx;
        this.idx = this.idx >= 0 ? this.idx : this.list.size() - 1;
        this.player();
    }

    public void next() {
        ++this.idx;
        this.idx = this.idx < this.list.size() ? this.idx : 0;
        this.player();
    }

    public void setIdx(int ide) {
        this.idx = ide;
        this.idx = this.idx >= 0 ? this.idx : this.list.size() - 1;
        this.idx = this.idx < this.list.size() ? this.idx : 0;
        this.player();
    }

    public T get() {
        return this.list.get(this.idx);
    }

    public T get(int i) {
        return this.list.get(i);
    }

    public String trimTime(int time) {
        int fen = time / 60;
        time %= 60;
        return (fen > 9 ? Integer.valueOf(fen) : "0" + fen) + ":" + (time > 9 ? Integer.valueOf(time) : "0" + time);
    }

    public int getFileLength() {
        return this.length;
    }

    public String getFileLengthStr() {
        int leng = this.length;
        leng >>= 10;
        leng /= 102;
        return String.valueOf((double) leng / 10.0D);
    }

    public void clear() {
        this.stop();
        this.list.clear();
    }

    public interface OnPosition {
        void onPosition(int var1);

        void onPlayerIdx(int var1);
    }
}
