package com.risenb.witness.utils.newNetwork;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReqParams {
    private Map<String, String> mapParam = new HashMap();
    private Map<String, File> mapFile = new HashMap();
    private Map<String, String> mapHead = new HashMap();

    public ReqParams() {
    }

    public void addParam(String key, String value) {
        if (key != null && value != null) {
            this.mapParam.put(key, value);
        }
    }

    public void addParam(String key, File value) {
        if (key != null && value != null) {
            this.mapFile.put(key, value);
        }
    }

    public void addHead(String key, String value) {
        if (key != null && value != null) {
            this.mapHead.put(key, value);
        }
    }

    public Map<String, String> getParam() {
        return this.mapParam;
    }

    public Map<String, File> getFile() {
        return this.mapFile;
    }

    public Map<String, String> getHead() {
        return this.mapHead;
    }
}
