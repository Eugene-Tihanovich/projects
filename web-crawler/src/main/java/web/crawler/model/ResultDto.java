package web.crawler.model;

import java.util.ArrayList;
import java.util.List;

public class ResultDto {

    private List<ResultItemDto> resultItemDtoList =
            new ArrayList<>();

    public List<ResultItemDto> getResultItemDtoList() {
        return resultItemDtoList;
    }

    public void setResultItemDtoList(List<ResultItemDto> resultItemDtoList) {
        this.resultItemDtoList = resultItemDtoList;
    }
}