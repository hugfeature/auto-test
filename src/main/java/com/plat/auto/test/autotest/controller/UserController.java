package com.plat.auto.test.autotest.controller;

import com.plat.auto.test.autotest.controller.annotation.PermessionLimit;
import com.plat.auto.test.autotest.entity.Biz;
import com.plat.auto.test.autotest.entity.ReturnT;
import com.plat.auto.test.autotest.entity.User;
import com.plat.auto.test.autotest.mapper.BizMapper;
import com.plat.auto.test.autotest.mapper.UserMapper;
import com.plat.auto.test.autotest.service.LoginService;
import com.plat.auto.test.autotest.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wangzhaoxian
 * @date: 2021/11/18
 * @description
 */
@Controller
@RequestMapping("/user")
@Api(tags = "1.0.0-SNAPSHOT", value = "用户相关")
@Slf4j
public class UserController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BizMapper bizMapper;

    @RequestMapping
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "初始化", notes = "初始化")
    public String index(Model model) {

        List<Biz> bizList = bizMapper.loadAll();
        model.addAttribute("bizList", bizList);

        return "user/user.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "页面列表", notes = "页面列表")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String userName, int type) {
        // page list
        List<User> list = userMapper.pageList(start, length, userName, type);
        int listCount = userMapper.pageListCount(start, length, userName, type);

        // package result
        Map<String, Object> maps = new HashMap<>();
        // 总记录数
        maps.put("recordsTotal", listCount);
        // 过滤后的总记录数
        maps.put("recordsFiltered", listCount);
        // 分页列表
        maps.put("data", list);
        return maps;
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "增加用户", notes = "增加用户")
    public ReturnT<String> add(User user) {
        // valid
        if (StringUtil.isBlank(user.getUserName())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "用户名为空");
        }
        if (StringUtil.isBlank(user.getPassWord())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "密码为空");
        }

        // valid
        User existUser = userMapper.findByUserName(user.getUserName());
        if (existUser != null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "账号已存在");
        }

        // passowrd md5
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassWord().getBytes());
        user.setPassWord(md5Password);

        int ret = userMapper.add(user);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "用户更新", notes = "用户更新")
    public ReturnT<String> update(HttpServletRequest request, User user) {

        User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (loginUser.getUserName().equals(user.getUserName())) {
            return new ReturnT<>(ReturnT.FAIL.getCode(), "禁止操作当前账号");
        }

        // exist
        User existUser = userMapper.findByUserName(user.getUserName());
        if (existUser == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "更新失败，账号不存在");
        }

        // update param
        if (StringUtil.isNotBlank(user.getPassWord())) {
            if (!(user.getPassWord().length()>=4 && user.getPassWord().length()<=50)) {
                return new ReturnT<>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
            }
            // passowrd md5
            String md5Password = DigestUtils.md5DigestAsHex(user.getPassWord().getBytes());
            existUser.setPassWord(md5Password);
        }
        existUser.setType(user.getType());

        int ret = userMapper.update(existUser);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public ReturnT<String> delete(HttpServletRequest request, int id) {

        // valid user
        User delUser = userMapper.findById(id);
        if (delUser == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "用户不存在");
        }

        User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (loginUser.getUserName().equals(delUser.getUserName())) {
            return new ReturnT<>(ReturnT.FAIL.getCode(), "禁止删除当前账号");
        }

        int ret = userMapper.delete(id);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    @ApiOperation(value = "更换密码", notes = "更换密码")
    public ReturnT<String> updatePwd(HttpServletRequest request, String password){

        // new password(md5)
        if (StringUtil.isBlank(password)){
            return new ReturnT<>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        if (!(password.length()>=4 && password.length()<=100)) {
            return new ReturnT<>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // update pwd
        User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY);

        User existUser = userMapper.findByUserName(loginUser.getUserName());
        existUser.setPassWord(md5Password);
        userMapper.update(existUser);

        return ReturnT.SUCCESS;
    }

    @RequestMapping("/updatePermissionBiz")
    @ResponseBody
    @PermessionLimit(superUser = true)
    @ApiOperation(value = "更新业务权限", notes = "更新业务权限")
    public ReturnT<String> updatePermissionBiz(int id,
                                               @RequestParam(required = false) String[] permissionBiz){

        String permissionProjectsStr = StringUtil.join(permissionBiz, ",");
        User existUser = userMapper.findById(id);
        if (existUser == null) {
            return new ReturnT<>(ReturnT.FAIL.getCode(), "用户不存在");
        }
        existUser.setPermissionBiz(permissionProjectsStr);
        userMapper.update(existUser);

        return ReturnT.SUCCESS;
    }
}
