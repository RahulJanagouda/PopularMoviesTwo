package com.rahuljanagouda.popularmoviestwo.pojo.videos;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahuljanagouda on 24/07/16.
 */
public class VideoResponse implements Parcelable {

    public static final Parcelable.Creator<VideoResponse> CREATOR = new Parcelable.Creator<VideoResponse>() {
        @NonNull
        @Override
        public VideoResponse createFromParcel(@NonNull Parcel source) {
            return new VideoResponse(source);
        }

        @NonNull
        @Override
        public VideoResponse[] newArray(int size) {
            return new VideoResponse[size];
        }
    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();

    public VideoResponse() {
    }

    protected VideoResponse(@NonNull Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = in.createTypedArrayList(Result.CREATOR);
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeTypedList(this.results);
    }
}