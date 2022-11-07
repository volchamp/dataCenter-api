package com.yxc.imapi.model.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author yxc
 * @title: merchantmsg
 * @projectName api
 * @description: TODO
 * @date 2020/11/02 20:24
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class merchantmsg {
    @NotNull
    private String sendUserId;// String Yes 发送者id
    @NotNull
    private String receiverUserId;//接收者id
    @NotNull
    private String message;// String 聊天内容；内容小于 500 汉字字符
    @NotNull
    private Integer contentType;// Integer Yes 消息类型，每次消息只能传 1 种类型（发送对应消息类型内容不能为空）1 文本 3 图片4 位置
    private picture picture;// Object 图片信息
    private location location;// Object 位置信息
    private String fileName;//文件名称
    private String fileDownloadUrl;//文件下载地址
}
