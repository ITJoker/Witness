package com.risenb.witness.utils.newUtils;

public class ThreadUtil {
    private static boolean isRun = false;
    private static boolean isWait = false;
    private Runnable run;

    public ThreadUtil(Runnable run) {
        this.run = run;
    }

    public void start() {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    if (ThreadUtil.isWait) {
                        ThreadUtil.isWait = false;
                        Thread.sleep(1000L);
                    }

                    if (ThreadUtil.isRun) {
                        ThreadUtil.isWait = true;
                    }

                    while (ThreadUtil.isRun) {
                        if (!ThreadUtil.isWait) {
                            return;
                        }

                        Thread.sleep(500L);
                    }

                    ThreadUtil.isRun = true;
                    if (ThreadUtil.this.run != null) {
                        MUtils.getMUtils().getHandler().post(ThreadUtil.this.run);
                    }

                    ThreadUtil.isRun = false;
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

            }
        })).start();
    }
}
