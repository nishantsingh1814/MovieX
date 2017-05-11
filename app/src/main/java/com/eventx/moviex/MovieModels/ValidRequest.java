package com.eventx.moviex.MovieModels;

/**
 * Created by Nishant on 4/23/2017.
 */

public class ValidRequest {
    private boolean success;
    private String status_message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }
}
