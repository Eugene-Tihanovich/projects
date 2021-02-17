package web.crawler.servlet;

import web.crawler.model.SearchDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SearchParamsBinder {

    public SearchDto bind(HttpServletRequest req) {
        final String seed = req.getParameter("seed");
        final String terms = req.getParameter("terms");

        SearchDto searchDto = new SearchDto();
        searchDto.setSeed(seed);
        searchDto.setTerms(
                Arrays.stream(terms.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())
        );
        return searchDto;
    }
}