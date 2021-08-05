package com.ritek.freshwalls.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Box {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("slides")
    @Expose
    private List<Slide> slides = null;
    @SerializedName("packs")
    @Expose
    private List<Pack> packs = null;
    @SerializedName("wallpapers")
    @Expose
    private List<Wallpaper> wallpapers = null;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }

    public List<Pack> getPacks() {
        return packs;
    }

    public void setPacks(List<Pack> packs) {
        this.packs = packs;
    }

    public List<Wallpaper> getWallpapers() {
        return wallpapers;
    }

    public void setWallpapers(List<Wallpaper> wallpapers) {
        this.wallpapers = wallpapers;
    }

}