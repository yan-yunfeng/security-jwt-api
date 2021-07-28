package com.yan.controller;

import com.yan.model.Result;
import com.yan.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author : 鄢云峰
 */
@RestController
@RequestMapping("/yan/api")
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    @Secured(value = {"ROLE_MYSQL","ROLE_ADMIN"})
    @PostMapping("mysql")
    public Result findFromMysql(@RequestBody String sql) throws Throwable {
        return generalService.findFromMysql(sql);
    }

}
