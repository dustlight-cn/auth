package cn.dustlight.uim;

import cn.dustlight.uim.utils.ExceptionSupplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;


@SpringBootApplication
public class UimApplication {

    public static void main(String[] args) {
        SpringApplication.run(UimApplication.class, args);
    }

    @RestController
    @RestControllerAdvice
    public static class HelloController {

        @GetMapping("/hello")
        public RestfulResult<String> hello() {
            return RestfulResult.success("Hello World!");
        }

        @ExceptionHandler(Exception.class)
        public RestfulResult onException(Exception e) throws UnsupportedEncodingException {
            Logger.getLogger(UimApplication.class.getName()).warning(new ExceptionSupplier(e));
            return RestfulResult.error().setData(e.toString());
        }
    }
}