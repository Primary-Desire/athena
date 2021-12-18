package cn.ruiheyun.athena.common.request;

import lombok.Data;

@Data
public class PageRequestDTO {

    private Integer current = 1;

    private Integer pageSize = 10;

    private String keyword = "";

}
