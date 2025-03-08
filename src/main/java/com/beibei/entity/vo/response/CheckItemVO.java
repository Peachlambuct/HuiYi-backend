package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检查项目信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckItemVO {
    /**
     * 检查项目名称
     */
    private String name;

    /**
     * 检查室
     */
    private String room;

    /**
     * 检查项目图片
     */
    private String img;

    /**
     * 检查状态
     */
    private String status;

    /**
     * 检查时间
     */
    private String time;
}
