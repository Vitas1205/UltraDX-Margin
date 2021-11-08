package com.fota.fotamargin.common.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Tao Yuanming
 * Created on 2018/9/12
 * Description
 */
@Controller
public class OkController {
    @RequestMapping("/ok")
    @ResponseBody
    public String ok() {
        return "ok";
    }
}
