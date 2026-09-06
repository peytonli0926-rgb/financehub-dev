package com.utfinancing.financehub.etl.passveh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SelectNonConfirmAmountOutputDTO implements Serializable {
    private Date hisDate;
    private String vcWangybh;
    private String vcPich;
    private String vcWangybHy;
    private String vcDaozyh;
    private String vcDaozyhzh;
    private String vcFukzh;
    private String dtDaodsj;
    private BigDecimal decDaozje;
    private BigDecimal decKehxje;
    private BigDecimal decYihxje;
    private String vcBeiz;
    private String vcWangygs;
    private String vcBaozjgs;
    private String vcSpmc;
    private String vcZhaiy;
}
