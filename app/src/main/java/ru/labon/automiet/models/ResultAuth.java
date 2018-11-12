package ru.labon.automiet.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 20.02.2018.
 */

public class ResultAuth {
    @SerializedName("token")
    public String token = "";
    @SerializedName("checksum")
    public String checksum = "";
}
