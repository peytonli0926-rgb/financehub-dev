package com.utfinancing.financehub.engine.hthx.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PageResult<T> implements Serializable {

    private Integer total;

    private Integer pageNum;

    private Integer pageSize;

    private Integer totalPage;

    private List<T> records;

    private Object totalData;

    public PageResult(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageResult(Integer total, Integer pageNum, Integer pageSize, Integer totalPage) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
    }

    public PageResult(Integer total, Integer pageNum, Integer pageSize, Integer totalPage, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.records = records;
    }

    public Integer getTotalPage() {
        if (this.totalPage > 0) {
            return this.totalPage;
        }
        return (this.total % this.pageSize != 0 ? this.total / this.pageSize + 1 : this.total / this.pageSize);
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", records=" + records +
                ", totalData=" + totalData +
                '}';
    }
}
