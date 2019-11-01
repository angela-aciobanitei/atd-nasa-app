package com.ang.acb.nasaapp.data.vo.library;

import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("rel")
    private String rel;

    @SerializedName("href")
    private String href;

    @SerializedName("render")
    private String render;

    public void setRel(String rel){
        this.rel = rel;
    }

    public String getRel(){
        return rel;
    }

    public void setHref(String href){
        this.href = href;
    }

    public String getHref(){
        return href;
    }

    public void setRender(String render){
        this.render = render;
    }

    public String getRender(){
        return render;
    }
}
