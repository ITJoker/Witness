package com.risenb.witness.utils.sardar;

public interface CompressListener {
    void onExecSuccess(String message);
    void onExecFail(String reason);
    void onExecProgress(String message);
}
