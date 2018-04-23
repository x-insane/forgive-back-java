package cn.gotohope.forgive.service;

import cn.gotohope.forgive.pojo.BestTransfer;
import cn.gotohope.forgive.pojo.Game;
import cn.gotohope.forgive.service.transfer.Transfer;

import java.util.Collection;
import java.util.List;

public interface GameService {
    Transfer updateScore(String phone, String gameId, int score);
    Transfer updateChallenge(String phone, String gameId, float velocity);
    List<BestTransfer> getGameDataByUserPhone(String phone);
    Collection<Game> getGames();
    Game getGameById(String gameId);
}
