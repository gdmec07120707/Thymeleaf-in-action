# Thymeleaf-in-action
Thymeleaf使用例子。实现用户管理系统。包括增删改查等操作
1.Controller用户入口，定义接口方法
```
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
```
2.定义User的接口，以及对其实现
```
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final ConcurrentMap<Long,User> userMap = new ConcurrentHashMap<>();

    private static AtomicLong counter = new AtomicLong();

    @Override
    public User saveOrUpdateUser(User user) {
        Long id = user.getId();
        if(id <= 0){
            id = counter.incrementAndGet();
            user.setId(id);
        }
        this.userMap.put(id,user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        this.userMap.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        User user = this.userMap.get(id);
        return user;
    }

    @Override
    public List<User> listUser() {
        return new ArrayList<User>(this.userMap.values());
    }
```
3.对应的html页面
```
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <title th:text="${userModel.title}">welcome</title>
</head>
<body>
<div th:replace="~{fragments/header :: header}">...</div>
<h2 th:text="${userModel.title}">标题</h2>
<div>
    <a href="/users/form.html">创建用户</a>
</div>

<table border="1">
    <thead>
    <tr>
        <td>ID</td>
        <td>年龄</td>
        <td>姓名</td>
    </tr>
    </thead>
    <tbody>
    <tr th:if="${userModel.userList.size()} eq 0">
        <td colspan="3">没有用户信息！</td>
    </tr>

    <tr th:each="user : ${userModel.userList}">
        <td th:text="${user.id}">1</td>
        <td th:text="${user.age}">1</td>
        <td>
            <a th:href="@{'/users/'+${user.id}}"
               th:text="${user.name}">
            </a>
        </td>
    </tr>
    </tbody>
</table>

<div th:replace="~{fragments/footer :: footer}">...</div>
</body>
</html>
```

更对详情请查看源代码
