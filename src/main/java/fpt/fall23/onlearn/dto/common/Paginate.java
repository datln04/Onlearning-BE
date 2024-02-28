package fpt.fall23.onlearn.dto.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class Paginate<T> {
    private List<T> contents;
    private int current;
    private int pageSize;
    private int total;
    private int totalPage;

    public Paginate() {

    }

    public Paginate(List<T> contents, int current, int pageSize, int total, int totalPage) {
        this.contents = contents;
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPage = totalPage;
    }

    public Paginate(Page<T> page) {
        this.contents = page.getContent();
        this.pageSize = page.getPageable().getPageSize();
        this.current = page.getPageable().getPageNumber();
        this.total = (int) page.getTotalElements();
        this.totalPage = page.getTotalPages();
    }

}
