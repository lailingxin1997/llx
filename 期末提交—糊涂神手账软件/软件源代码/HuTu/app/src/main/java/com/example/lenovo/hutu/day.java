package com.example.lenovo.hutu;

/**
 * Created by lenovo on 2017/5/6.
 */

public class day {

        private String username;
        private String created;
        private String title;
        private String weather;
        private String content;
        private String photopath;
        public day(String created,String title,String weather,String username,String content,String photopath)
        {
            this.created=created;
            this.title=title;
            this.weather=weather;
            this.content=content;
            this.username=username;
            this.photopath=photopath;
        }
        public String getCreated()
        {
            return created;
        }

        public String getTitle() {
            return title;
        }

        public String getWeather() {
            return weather;
        }

        public String getUsername(){return username;}

    public String getContent() {
        return content;
    }
public String getPhotopath(){return photopath;}

}
