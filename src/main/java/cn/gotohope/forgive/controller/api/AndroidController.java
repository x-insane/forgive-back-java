package cn.gotohope.forgive.controller.api;

import cn.gotohope.forgive.controller.common.ApiController;
import cn.gotohope.forgive.pojo.BestTransfer;
import cn.gotohope.forgive.pojo.Game;
import cn.gotohope.forgive.pojo.User;
import cn.gotohope.forgive.service.GameService;
import cn.gotohope.forgive.service.UserService;
import cn.gotohope.forgive.service.transfer.Transfer;
import cn.gotohope.forgive.service.transfer.UserTransfer;
import cn.gotohope.forgive.util.Config;
import cn.gotohope.forgive.util.YunpianUtil;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/android",
        produces = { "application/json;charset=UTF-8" },
        method = RequestMethod.POST)
@ResponseBody
public class AndroidController extends ApiController {

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public AndroidController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @RequestMapping("/login")
    public String login(String phone, String passwd, HttpSession session) {
        UserTransfer transfer = userService.login(phone, passwd);
        if (transfer.getError() != 0)
            return error(transfer.getError(), transfer.getMsg());
        session.setAttribute("user", transfer.getUser());
        JsonObject object = new JsonObject();
        object.addProperty("error", 0);
        object.addProperty("msg", "登陆成功");
        JsonObject user = new JsonObject();
        user.addProperty("phone", transfer.getUser().getPhone());
        user.addProperty("nickname", transfer.getUser().getNickname());
        user.addProperty("description", transfer.getUser().getDescription());
        object.add("user", user);
        List<BestTransfer> data = gameService.getGameDataByUserPhone(transfer.getUser().getPhone());
        JsonArray array = new JsonArray();
        for (BestTransfer best : data) {
            JsonObject o = new JsonObject();
            o.addProperty("game_id", best.getGameId());
            if (best.getType().equals("score"))
                o.addProperty("score", best.getScore());
            if (best.getType().equals("velocity"))
                o.addProperty("velocity", best.getVelocity());
            array.add(o);
        }
        object.add("data", array);
        return object.toString();
    }

    @RequestMapping("/register_request_message")
    public String register_request_message(String token, String phone, HttpSession session) {
        if (phone == null || phone.isEmpty() || token == null || token.isEmpty())
            return error(401, "参数不足");
        if (!token.equals(Config.request_message_token))
            return error(403, "token错误");
        if (userService.getUserByPhone(phone) != null)
            return error(400, "该用户已经注册过，请直接登录");
        Random random = new Random(System.currentTimeMillis());
        String code = String.valueOf(random.nextInt(900000-1) + 100000);
        boolean res = YunpianUtil.send(phone, "【梦的天空之城】您的验证码是" + code + "。如非本人操作，请忽略本短信");
        if (!res)
            return error(1, "发送失败");
        session.setAttribute("phone", phone);
        session.setAttribute("code", code);
        session.removeAttribute("code_wrong");
        return ok();
    }

    @RequestMapping("/register")
    public String register(String phone, String passwd, String code, HttpSession session) {
        if (phone == null || phone.isEmpty() || code == null || code.isEmpty() || passwd == null || passwd.isEmpty())
            return error(401, "参数不足");
        if (!phone.equals(session.getAttribute("phone")))
            return error(402, "验证信息已失效，请重新发送短信");
        if (userService.getUserByPhone(phone) != null)
            return error(400, "该用户已经注册过，请直接登录");
        Integer times = (Integer) session.getAttribute("code_wrong");
        if (times != null && times >= 3) {
            session.removeAttribute("code");
            return error(403, "验证码错误次数过多，请重新发送验证码");
        }
        if (!code.equals(session.getAttribute("code"))) {
            if (times == null)
                times = 1;
            else
                times ++;
            session.setAttribute("code_wrong", times);
            return error(403, "验证码错误");
        }
        Transfer transfer = userService.register(phone, passwd);
        if (transfer.getError() != 0)
            return error(transfer.getError(), transfer.getMsg());
        return ok();
    }

    @RequestMapping("/reset_passwd_request_message")
    public String reset_passwd_request_message(String token, String phone, HttpSession session) {
        if (phone == null || phone.isEmpty() || token == null || token.isEmpty())
            return error(401, "参数不足");
        if (!token.equals(Config.request_message_token))
            return error(403, "token错误");
        if (userService.getUserByPhone(phone) == null)
            return error(400, "该用户没有注册过，是否填错了手机号？");
        Random random = new Random(System.currentTimeMillis());
        String code = String.valueOf(random.nextInt(900000-1) + 100000);
        boolean res = YunpianUtil.send(phone, "【梦的天空之城】您的验证码是" + code + "。如非本人操作，请忽略本短信");
        if (!res)
            return error(1, "发送失败");
        session.setAttribute("phone", phone);
        session.setAttribute("code", code);
        session.removeAttribute("code_wrong");
        return ok();
    }

    @RequestMapping("reset_passwd")
    public String reset_passwd(String phone, String passwd, String code, HttpSession session) {
        if (phone == null || phone.isEmpty() || code == null || code.isEmpty() || passwd == null || passwd.isEmpty())
            return error(401, "参数不足");
        if (!phone.equals(session.getAttribute("phone")))
            return error(402, "验证信息已失效，请重新发送短信");
        if (userService.getUserByPhone(phone) == null)
            return error(400, "该用户没有注册过，是否填错了手机号？");
        Integer times = (Integer) session.getAttribute("code_wrong");
        if (times != null && times >= 3) {
            session.removeAttribute("code");
            return error(403, "验证码错误次数过多，请重新发送验证码");
        }
        if (!code.equals(session.getAttribute("code"))) {
            times = times == null ? 1 : times + 1;
            session.setAttribute("code_wrong", times);
            return error(403, "验证码错误");
        }
        Transfer transfer = userService.resetPassword(phone, passwd);
        if (transfer.getError() != 0)
            return error(transfer.getError(), transfer.getMsg());
        return ok();
    }

    @RequestMapping("/modify_user")
    public String modify_user(String nickname, String description, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return error(403, "请先登录");
        Transfer transfer = userService.modifyInfo(new User().setPhone(user.getPhone())
                .setNickname(nickname)
                .setDescription(description));
        if (transfer.getError() != 0)
            return error(transfer.getError(), transfer.getMsg());
        User info = userService.getUserByPhone(user.getPhone());
        session.setAttribute("user", info);
        JsonObject object = new JsonObject();
        object.addProperty("error", 0);
        object.addProperty("msg", "修改成功");
        JsonObject u = new JsonObject();
        u.addProperty("phone", info.getPhone());
        u.addProperty("nickname", info.getNickname());
        u.addProperty("description", info.getDescription());
        object.add("user", u);
        return object.toString();
    }

    @RequestMapping("/upload_score")
    public String upload_score(String token, String type, String game, Integer score, Float velocity, HttpSession session) {
        if (token == null || !token.equals(Config.upload_score_token))
            return error(403, "token错误");
        User user = (User) session.getAttribute("user");
        if (user == null)
            return error(403, "请先登录");
        Game _game = gameService.getGameById(game);
        if (_game == null)
            return error(404, "参数错误");
        Transfer transfer;
        if ("challenge".equals(_game.getType())) {
            if (velocity == null)
                return error(401, "缺少velocity参数");
            transfer = gameService.updateChallenge(user.getPhone(), game, velocity);
        } else {
            if (score == null)
                return error(401, "缺少score参数");
            transfer = gameService.updateScore(user.getPhone(), game, score);
        }
        if (transfer.getError() != 0)
            return error(transfer.getError(), transfer.getMsg());
        return ok();
    }

}
