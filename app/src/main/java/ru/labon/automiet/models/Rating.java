package ru.labon.automiet.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 12.12.2017.
 */

public class Rating {

    @SerializedName("fio")
    public String fio = "";
    @SerializedName("points")
    public float points = 0;
    @SerializedName("position")
    public int position = 0;
    @SerializedName("you")
    public int you = 0;

}
