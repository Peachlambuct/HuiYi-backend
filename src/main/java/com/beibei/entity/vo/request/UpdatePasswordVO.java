package com.beibei.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordVO {
    private String oldPassword;
    private String newPassword;
}
