package com.yxc.imapi.controller;

import com.yxc.imapi.mapper.PhotoWallDao;
import com.yxc.imapi.model.PhotoWall;
import com.yxc.imapi.model.Users;
import com.yxc.imapi.util.JwtUtil;
import com.yxc.imapi.utils.Result;
import com.yxc.imapi.utils.UuidUtil;
import com.yxc.imapi.utils.enums.ResultEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/photowall")
public class PhotoWallController {
    @Autowired
    PhotoWallDao photoWallDao;

    /**
     * 添加照片墙，用mybatis-plus框架
     *
     * @param v_token
     * @param photoWall
     * @param request
     * @param hresponse
     * @return
     */
    @PostMapping("/addPhotoWall")
    @ApiOperation(value = "添加照片墙")
    public Result addPhotoWall(@RequestHeader(value = "v_token", required = true) String v_token, @RequestBody @Validated @ApiParam(value = "{json对象") PhotoWall photoWall,
                               HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        String url = photoWall.getUrl();
        if (url == null || url.equals("")) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("图片路径不能为空！");
            return result;
        }
        photoWall.setId(UuidUtil.getUuid());
        photoWall.setUser_id(user_id);
        photoWall.setState(1);
        photoWall.setCreate_time(new Date());
        photoWall.setCreate_user(user_id);
        int flag = photoWallDao.insert(photoWall);
        if (flag <= 0) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("操作失败");
        } else {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("操作成功");
        }


        return result;
    }

    /**
     * 批量添加照片墙，用mybatis框架
     *
     * @param v_token
     * @param request
     * @param hresponse
     * @return
     */
    @PostMapping("/addPhotoWallV2")
    @ApiOperation(value = "批量添加照片墙")
    public Result addPhotoWallV2(@RequestHeader(value = "v_token", required = true) String v_token, @RequestBody @Validated @ApiParam(value = "{json对象") PhotoWall photoWall,
                                 HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        String url = photoWall.getUrl();
        if (url == null || url.equals("")) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("图片路径不能为空！");
            return result;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        String[] arr = url.split(",");
        for (int i = 0; i < arr.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",UuidUtil.getUuid());
            map.put("user_id",user_id);
            map.put("url", arr[i]);
            list.add(map);
        }


        int flag = photoWallDao.insertPhotoWall(list);
        if (flag <= 0) {
            result.setCode(ResultEnum.EREOR.getCode());
            result.setMsg("操作失败");
        } else {
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg("操作成功");
        }


        return result;
    }


    @PostMapping("/getPhotoWallList")
    @ApiOperation(value = "获取照片墙")
    public Result getPhotoWallList(@RequestHeader(value = "v_token", required = true) String v_token,
                                   HttpServletRequest request, HttpServletResponse hresponse) {
        Result result = new Result();
        Users users = JwtUtil.getCurrUserFromToken(v_token);
        String user_id = users.getUserId();

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        List<Map<String, Object>> photoWallList = photoWallDao.getPhotoWallList(map);

        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg("获取成功");
        result.setData(photoWallList);
        return result;
    }


}
