package com.risenb.witness.utils.newNetwork.okhttp.builder;

import com.risenb.witness.utils.newNetwork.okhttp.request.PostFormRequest;
import com.risenb.witness.utils.newNetwork.okhttp.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {
    private List<FileInput> files = new ArrayList();

    public PostFormBuilder() {
    }

    public RequestCall build() {
        return (new PostFormRequest(this.url, this.tag, this.params, this.headers, this.files, this.id)).build();
    }

    public PostFormBuilder files(String key, Map<String, File> files) {
        Iterator var3 = files.keySet().iterator();

        while(var3.hasNext()) {
            String filename = (String)var3.next();
            this.files.add(new PostFormBuilder.FileInput(key, filename, (File)files.get(filename)));
        }

        return this;
    }

    public PostFormBuilder addFile(String name, String filename, File file) {
        this.files.add(new PostFormBuilder.FileInput(name, filename, file));
        return this;
    }

    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public PostFormBuilder addParams(String key, String val) {
        if(this.params == null) {
            this.params = new LinkedHashMap();
        }

        this.params.put(key, val);
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        public String toString() {
            return "FileInput{key=\'" + this.key + '\'' + ", filename=\'" + this.filename + '\'' + ", file=" + this.file + '}';
        }
    }
}
