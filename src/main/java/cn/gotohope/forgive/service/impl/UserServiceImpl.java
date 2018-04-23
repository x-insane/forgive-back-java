package cn.gotohope.forgive.service.impl;

import cn.gotohope.forgive.pojo.User;
import cn.gotohope.forgive.service.UserService;
import cn.gotohope.forgive.service.transfer.BaseTransfer;
import cn.gotohope.forgive.service.transfer.Transfer;
import cn.gotohope.forgive.service.transfer.UserTransfer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final Map<String, User> map = new HashMap<>();

    @Value("${dataPath}")
    private String path;

    @PostConstruct
    private void init() throws IOException, ClassNotFoundException {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(path + "user.dat");
        } catch (FileNotFoundException e) {
            return;
        }
        ObjectInputStream stream = new ObjectInputStream(inputStream);
        int num = 0;
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                User user = (User) stream.readObject();
                map.put(user.getPhone(), user);
                num ++;
            }
        } catch (EOFException e) {
            System.out.println("读取完成，共读取了 " + num + " 条用户信息");
        }
        inputStream.close();
        stream.close();
    }

    private void save() {
        synchronized (map) {
            try {
                File file = new File(path + "user.dat");
                assert file.exists() || file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                ObjectOutputStream stream = new ObjectOutputStream(outputStream);
                for (String key : map.keySet())
                    stream.writeObject(map.get(key));
                outputStream.close();
                stream.close();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("更新完成：" + df.format(new Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UserTransfer login(String phone, String password) {
        if (!map.containsKey(phone))
            return new UserTransfer().setError(404).setMsg("不存在该用户");
        User user = map.get(phone);
        if (!user.getPassword().equals(password))
            return new UserTransfer().setError(404).setMsg("密码错误");
        return new UserTransfer().setUser(user);
    }

    @Override
    public Transfer register(String phone, String password) {
        if (map.containsKey(phone))
            return new BaseTransfer().setError(401).setMsg("该用户已经存在");
        map.put(phone, new User().setPhone(phone).setPassword(password).setNickname(phone));
        save();
        return new BaseTransfer();
    }

    @Override
    public User getUserByPhone(String phone) {
        return map.get(phone);
    }

    @Override
    public Transfer resetPassword(String phone, String password) {
        User _user = map.get(phone);
        if (_user != null)
            _user.setPassword(password);
        else
            return new BaseTransfer().setError(404).setMsg("不存在该用户");
        save();
        return new BaseTransfer();
    }

    @Override
    public Transfer modifyInfo(User user) {
        User _user = map.get(user.getPhone());
        if (_user != null) {
            String nickname = user.getNickname();
            if (nickname != null && !nickname.isEmpty())
                _user.setNickname(user.getNickname());
            String description = user.getDescription();
            if (description != null && !description.isEmpty())
                _user.setDescription(description);
        } else
            return new BaseTransfer().setError(404).setMsg("不存在该用户");
        save();
        return new BaseTransfer();
    }
}
