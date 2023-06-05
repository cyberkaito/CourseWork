package com.example.polyphonia;

import java.time.LocalDateTime;
import java.util.Date;

public class Root {
    public class Categories{
        public int idCategory;
        public String name;

        public int getIdCategory() {
            return idCategory;
        }

        public String getName() {
            return name;
        }
    }
    public class Channels{
        public int idChannel;
        public String name;
        public String description;
        public String avatar;
        public double rate;

        public int getIdChannel() {
            return idChannel;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getAvatar() {
            return avatar;
        }

        public double getRate() {
            return rate;
        }

        public int getRateCount() {
            return rateCount;
        }

        public int rateCount;
    }
    public static class Clients{
        public int idClient;
        public String name;
        public String email;
        public String key;
        public String avatar;
        public String bio;

        public int getIdClient() {
            return idClient;
        }

        public Clients(String name, String email, String key, String avatar, String bio) {
            this.name = name;
            this.email = email;
            this.key = key;
            this.avatar = avatar;
            this.bio = bio;
        }

        public void setIdClient(int idClient) {
            this.idClient = idClient;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }
    }
    public static class ClientTypes{
        public int idClientType;
        public int idClient;
        public int idRole;

        public ClientTypes(int idClient, int idChannel, int idRole) {
            this.idClient = idClient;
            this.idChannel = idChannel;
            this.idRole = idRole;
        }

        public int getIdClientType() {
            return idClientType;
        }

        public void setIdClientType(int idClientType) {
            this.idClientType = idClientType;
        }

        public int getIdClient() {
            return idClient;
        }

        public void setIdClient(int idClient) {
            this.idClient = idClient;
        }

        public int getIdChannel() {
            return idChannel;
        }

        public void setIdChannel(int idChannel) {
            this.idChannel = idChannel;
        }

        public int idChannel;
    }
    public static class Comments{
        public int idComment;
        public int idClient;
        public int idNews;
        public String text;

        public Comments(int idClient, int idNews, String text) {
            this.idClient = idClient;
            this.idNews = idNews;
            this.text = text;
        }

        public int getIdComment() {
            return idComment;
        }

        public void setIdComment(int idComment) {
            this.idComment = idComment;
        }

        public int getIdClient() {
            return idClient;
        }

        public void setIdClient(int idClient) {
            this.idClient = idClient;
        }

        public int getIdNews() {
            return idNews;
        }

        public void setIdNews(int idNews) {
            this.idNews = idNews;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


    }
    public class Media{
        public int idMedia;
        public String link;
        public int idNews;

        public int getIdMedia() {
            return idMedia;
        }

        public String getLink() {
            return link;
        }

        public int getIdNews() {
            return idNews;
        }

        public int idNewsNavigation;
    }

    public class News{
        public int idNews;
        public String header;
        public String description;
        public double rate;
        public int rateCount;
        public String date;

        public void setRate(double rate) {
            this.rate = rate;
        }

        public void setIdNews(int idNews) {
            this.idNews = idNews;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDate(String date) {
            this.date = date;
        }


        public void setIdCategory(int idCategory) {
            this.idCategory = idCategory;
        }

        public void setIdChannel(int idChannel) {
            this.idChannel = idChannel;
        }

        public void setRateCount(int rateCount) {
            this.rateCount = rateCount;
        }
        public int idCategory;
        public int idChannel;

        public int getIdNews() {
            return idNews;
        }

        public String getHeader() {
            return header;
        }

        public String getDescription() {
            return description;
        }

        public double getRate() {
            return rate;
        }

        public int getRateCount() {
            return rateCount;
        }

        public String getDate() {
            return date;
        }

        public int getIdCategory() {
            return idCategory;
        }

        public int getIdChannel() {
            return idChannel;
        }
    }
    public class Roles{
        public int getIdRole() {
            return idRole;
        }

        public String getName() {
            return name;
        }

        public int idRole;
        public String name;
    }
}
