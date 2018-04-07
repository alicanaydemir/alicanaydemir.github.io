package com.app.aydemir.mustwatch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ACER on 21.3.2018.
 */

public class MovieSearchResponse {
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<MovieSearch> results;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MovieSearch> getResults() {
        return results;
    }

    public void setResults(List<MovieSearch> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}
