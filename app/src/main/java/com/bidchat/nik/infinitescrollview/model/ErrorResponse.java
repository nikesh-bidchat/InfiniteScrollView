package com.bidchat.nik.infinitescrollview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikesh on 3/9/2017.
 */

public class ErrorResponse {

    @SerializedName("errors")
    @Expose
    private String errors;

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}