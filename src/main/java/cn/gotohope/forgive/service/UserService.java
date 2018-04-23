package cn.gotohope.forgive.service;

import cn.gotohope.forgive.pojo.User;
import cn.gotohope.forgive.service.transfer.Transfer;
import cn.gotohope.forgive.service.transfer.UserTransfer;

public interface UserService {
    UserTransfer login(String phone, String password);
    Transfer register(String phone, String password);
    Transfer resetPassword(String phone, String password);
    Transfer modifyInfo(User user);
    User getUserByPhone(String phone);
}
