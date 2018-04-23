package cn.gotohope.forgive.test.service;

import cn.gotohope.forgive.service.UserService;
import cn.gotohope.forgive.service.transfer.Transfer;
import cn.gotohope.forgive.util.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testLogin() throws InterruptedException {
        Transfer transfer = userService.login("xinsane", "123456");
        System.out.println(transfer);
    }

    @Test
    public void testRegister() throws InterruptedException {
        Transfer transfer = userService.register("xinsane2", "123456");
        System.out.println(transfer);
    }

    @Test
    public void testResetPassword() {
        Transfer transfer = userService.resetPassword("18723754836", Helper.md5("123"));
        System.out.println(transfer);
    }

}
