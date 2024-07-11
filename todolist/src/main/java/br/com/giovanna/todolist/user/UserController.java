package br.com.giovanna.todolist.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.giovanna.todolist.user.UserModel;

@RestController
@RequestMapping("/users")
public class UserController {

@PostMapping("/")
public void create(@RequestBody UserModel userModel) {
System.out.println(userModel.name);
}
}
