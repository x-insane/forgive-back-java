package cn.gotohope.forgive.test.service;

import cn.gotohope.forgive.pojo.BestTransfer;
import cn.gotohope.forgive.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void testUpdateScore() {
        System.out.println(gameService.updateScore("xinsane", "dj8a8d", 15));
        System.out.println(gameService.updateScore("xinsane", "5h8d6g", 14));
        System.out.println(gameService.updateScore("xinsane", "lpmnj5", 16));
        System.out.println(gameService.updateScore("xinsane", "9y9z9o", 18));
        System.out.println(gameService.updateScore("xinsane2", "lpmnj5", 106));
    }

    @Test
    public void testUpdateChallenge() {
        System.out.println(gameService.updateChallenge("xinsane", "o34wbf", 115));
        System.out.println(gameService.updateChallenge("xinsane", "380fnh", 151));
        System.out.println(gameService.updateChallenge("xinsane", "r1xtrm", 145));
    }

    @Test
    public void testGetJsonDataByUserPhone() {
        List<BestTransfer> list = gameService.getGameDataByUserPhone("xinsane");
        for (BestTransfer transfer : list)
            System.out.println(transfer.json());
    }


}
