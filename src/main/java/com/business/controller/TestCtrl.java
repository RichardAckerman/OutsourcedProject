package com.business.controller;

import com.business.pojo.dto.user.Msg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author yuton
 * @version 1.0
 * @description com.example.demo.ctrl
 * @since 上午10:57 2017/12/25
 */
@Controller
public class TestCtrl {

    @RequestMapping("/")
    public String index(Model model) {
        Msg msg = new Msg("测试标题", "测试内容", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }

    @SendTo("/topic/notice")
    @MessageMapping("/change-notice")
    public String greeting(String value) {
        return value;
    }
}
