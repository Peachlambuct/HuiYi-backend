package com.beibei.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseQuery {
    private LocalDateTime from;
    private LocalDateTime to;
    private String title;
}
