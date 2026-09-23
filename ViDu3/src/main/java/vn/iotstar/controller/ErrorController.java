package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.webmvc.error.ErrorController {

    @RequestMapping("/error")
    public String error(Model model) {
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "Đã xảy ra lỗi khi xử lý yêu cầu.");
        }
        return "error";
    }
}
