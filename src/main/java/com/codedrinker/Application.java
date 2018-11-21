
package com.codedrinker;

import com.codedrinker.adapter.QiniuAdapter;
import com.codedrinker.dto.QiniuFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@SpringBootApplication
@ComponentScan
@Configuration
@ImportResource({"classpath*:applicationContext.xml"})
public class Application {

    @Autowired
    private QiniuAdapter qiniuAdapter;

    @Value("${qiniu.domain}")
    private String domain;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String index() {
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(Model model,
                         @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            QiniuFile qiniuFile = new QiniuFile();
            qiniuFile.setFileName(fileName);
            qiniuFile.setUploadBytes(bytes);
            String path = qiniuAdapter.upload(qiniuFile);
            model.addAttribute("url", domain + path);
            return "index";
        } catch (IOException e) {
            return "error";
        }
    }
}
