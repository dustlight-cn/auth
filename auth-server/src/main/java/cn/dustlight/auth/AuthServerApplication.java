package cn.dustlight.auth;

//import cn.dustlight.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

//    @Autowired
//    UserService<?, ?> userService;
//
//    @GetMapping("/")
//    public Object index() {
//        return userService.listUsers(null, null, null);
//    }
}
