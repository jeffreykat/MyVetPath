package com.myvetpath.myvetpath.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myvetpath.myvetpath.Data;

//This is used for the API that will send post requests
public class Feed {
    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("data")
    @Expose
    private Data data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "kind='" + kind + '\'' +
                ", data=" + data +
                '}';
    }
}
