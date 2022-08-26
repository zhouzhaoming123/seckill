package com.zzmhome.seckill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Api(value = "demo", tags = "demo")
@Controller
@RequestMapping("/demo")
public class DemoController {

    @ApiOperation(value = "demo", notes = "demo")
    @GetMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","zzmhome");
        return "hello";
    }

}
