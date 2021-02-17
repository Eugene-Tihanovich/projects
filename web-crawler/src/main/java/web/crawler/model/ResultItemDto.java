package web.crawler.model;

import java.util.HashMap;
import java.util.Map;

public class ResultItemDto {
    private String searchUrl;
    private Map<String, Integer> termsCountMap =
            new HashMap<>();

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public Map<String, Integer> getTermsCountMap() {
        return termsCountMap;
    }

    public void setTermsCountMap(Map<String, Integer> termsCountMap) {
        this.termsCountMap = termsCountMap;
    }
}