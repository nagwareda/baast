package com.tec77.bsatahalk.api.response;

/**
 * Created by Nagwa on 26/05/2018.
 */

public class ResponseHomeVideo {

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public boolean getSuccess() { return this.success; }

    public void setSuccess(boolean success) { this.success = success; }

    private Program program;

    public Program getProgram() { return this.program; }

    public void setProgram(Program program) { this.program = program; }

    private int code;

    public int getCode() { return this.code; }

    public void setCode(int code) { this.code = code; }

    public class Program
    {
        private int id;

        public int getId() { return this.id; }

        public void setId(int id) { this.id = id; }

        private String video_url;

        public String getVideoUrl() { return this.video_url; }

        public void setVideoUrl(String video_url) { this.video_url = video_url; }

        private String video_text;

        public String getVideoText() { return this.video_text; }

        public void setVideoText(String video_text) { this.video_text = video_text; }
    }


}
