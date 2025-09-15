package com.saju.saju_site.web;

import com.saju.saju_site.service.SajuService;
import com.saju.saju_site.web.dto.SajuRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SajuController {
    private final SajuService service;

    public SajuController(SajuService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("sajuRequest", new SajuRequest());
        return "main"; // main.html
    }

    @PostMapping("/saju")
    public String analyze(@ModelAttribute SajuRequest request, Model model) {
        System.out.println("입력값 확인 => " + request.getName() + ", "
                + request.getBirth() + ", "
                + request.getGender() + ", "
                + request.getQuestion());
        String answer = service.analyze(request);
        model.addAttribute("answer", answer);
        model.addAttribute("sajuRequest", request);
        return "result";
    }
}
