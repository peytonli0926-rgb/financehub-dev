package com.utfinancing.financehub.engine.hthx.page;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PageParam  extends SortingField implements Serializable {



    private static final Integer PAGE_NUM = 1;
    private static final Integer PAGE_SIZE = 10;
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNum = PAGE_NUM;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小值为 1")
    @Max(value = 500, message = "每页条数最大值为 500")
    private Integer pageSize = PAGE_SIZE;

}
