package com.fong.initializr.controller;

import com.fong.initializr.domain.User;
import com.fong.initializr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private List<User> getUserList(){
        return userRepository.listUser();
    }

    /**
     * 查询所有用户
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView list(Model model){
        model.addAttribute("userList",userRepository.listUser());
        model.addAttribute("title","用户管理");
        return new ModelAndView("users/list","userModel",model);//
    }

    /**
     * 根据id查询用户
     * @param id
     * @param model
     * @return
     */
    @GetMapping("{id}")
    public ModelAndView view(@PathVariable Long id, Model model){
        model.addAttribute("user",userRepository.getUserById(id));
        model.addAttribute("title","查看用户");
        return new ModelAndView("users/view","userModel",model);
    }


    /**
     * 获取form表单
     * @param model
     * @return
     */
    @GetMapping("/form")
    public ModelAndView createForm(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("title","创建用户");
        return new ModelAndView("users/form", "userModel", model);
    }

    /**
     * 新建用户
     * @param user
     * @return
     */
    @PostMapping
    public ModelAndView create(User user){
        user = userRepository.saveOrUpdateUser(user);
        return new ModelAndView("redirect:/users");
    }


    /**
     * 删除用户
     * @param id
     * @param model
     * @return
     */
    @GetMapping("delete/{id}")
    public ModelAndView delete(@PathVariable Long id,Model model){
        userRepository.deleteUser(id);
        model.addAttribute("userList",getUserList());
        model.addAttribute("title","删除用户");
        return new ModelAndView("users/list","userModel",model);
    }


    /**
     * 修改用户
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable Long id,Model model){
        User user = userRepository.getUserById(id);

        model.addAttribute("user",user);
        model.addAttribute("title","修改用户");
        return new ModelAndView("users/form","userModel",model);
    }

























}
