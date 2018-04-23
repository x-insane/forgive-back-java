package cn.gotohope.forgive.service.impl;

import cn.gotohope.forgive.pojo.*;
import cn.gotohope.forgive.service.GameService;
import cn.gotohope.forgive.service.UserService;
import cn.gotohope.forgive.service.transfer.BaseTransfer;
import cn.gotohope.forgive.service.transfer.Transfer;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Service
public class GameServiceImpl implements GameService {

    @Value("${dataPath}")
    private String path;

    private final Map<String, Game> map = new HashMap<>(); // key = gameId
    private final Map<String, GameBest> bestMap = new HashMap<>(); // key = gameId . userPhone
    private final Map<String, ChallengeBest> challengeMap = new HashMap<>(); // key = gameId . userPhone
    private final Map<String, List<BestTransfer>> userBest = new HashMap<>(); // key = userPhone

    private final UserService userService;

    private void readResource(Resource resource) throws IOException {
        InputStream stream = resource.getInputStream();
        String json = new String(IOUtils.readFully(stream, -1, true));
        JsonElement data = new JsonParser().parse(json);
        JsonArray array = data.getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement item : array) {
            JsonObject object = item.getAsJsonObject();
            Game game = gson.fromJson(object, Game.class);
            map.put(game.getId(), game);
        }
    }

    private void save() {
        synchronized (bestMap) {
            try {
                File file = new File(path + "bestMap.dat");
                assert file.exists() || file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                ObjectOutputStream stream = new ObjectOutputStream(outputStream);
                for (GameBest best : bestMap.values())
                    stream.writeObject(best);
                outputStream.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        synchronized (challengeMap) {
            try {
                File file = new File(path + "challengeMap.dat");
                assert file.exists() || file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                ObjectOutputStream stream = new ObjectOutputStream(outputStream);
                for (ChallengeBest best : challengeMap.values())
                    stream.writeObject(best);
                outputStream.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readGameBest() throws IOException, ClassNotFoundException {
        FileInputStream inputStream;
        try {
            inputStream= new FileInputStream(path + "bestMap.dat");
        } catch (FileNotFoundException e) {
            return;
        }
        ObjectInputStream stream = new ObjectInputStream(inputStream);
        int num = 0;
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                GameBest best = (GameBest) stream.readObject();
                best.setUser(userService.getUserByPhone(best.getUserPhone()));
                String key = best.getGameId() + best.getUserPhone();
                bestMap.put(key, best);
                synchronized (map) {
                    Game game = map.get(best.getGameId());
                    if (game == null)
                        continue;
                    if (game.getBest() == null || best.getScore() > game.getBest().getScore())
                        game.setBest(best);
                }
                synchronized (userBest) {
                    if (!userBest.containsKey(best.getUserPhone()))
                        userBest.put(best.getUserPhone(), new ArrayList<>());
                    userBest.get(best.getUserPhone()).add(best);
                }
                num ++;
            }
        } catch (EOFException e) {
            System.out.println("读取完成，共读取了 " + num + " 条游戏信息");
        }
        inputStream.close();
        stream.close();
    }

    private void readChallengeBest() throws IOException, ClassNotFoundException {
        FileInputStream inputStream;
        try {
            inputStream= new FileInputStream(path + "challengeMap.dat");
        } catch (FileNotFoundException e) {
            return;
        }
        ObjectInputStream stream = new ObjectInputStream(inputStream);
        int num = 0;
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                ChallengeBest best = (ChallengeBest) stream.readObject();
                best.setUser(userService.getUserByPhone(best.getUserPhone()));
                String key = best.getGameId() + best.getUserPhone();
                challengeMap.put(key, best);
                synchronized (map) {
                    Game game = map.get(best.getGameId());
                    if (game == null)
                        continue;
                    if (game.getBest() == null || best.getVelocity() > game.getBest().getVelocity())
                        game.setBest(best);
                }
                synchronized (userBest) {
                    if (!userBest.containsKey(best.getUserPhone()))
                        userBest.put(best.getUserPhone(), new ArrayList<>());
                    userBest.get(best.getUserPhone()).add(best);
                }
                num ++;
            }
        } catch (EOFException e) {
            System.out.println("读取完成，共读取了 " + num + " 条挑战信息");
        }
        inputStream.close();
        stream.close();
    }

    @PostConstruct
    private void init() throws IOException, ClassNotFoundException {
        readResource(new ClassPathResource("data/game_list.json"));
        readResource(new ClassPathResource("data/challenge.json"));
        readGameBest();
        readChallengeBest();
    }

    @Autowired
    public GameServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Transfer updateScore(String phone, String gameId, int score) {
        synchronized (bestMap) {
            String key = gameId + phone;
            GameBest best = bestMap.get(key);
            if (best == null) {
                best = new GameBest().setGameId(gameId).setScore(score)
                        .setUserPhone(phone).setUser(userService.getUserByPhone(phone));
                bestMap.put(key, best);
                synchronized (userBest) {
                    if (!userBest.containsKey(phone))
                        userBest.put(phone, new ArrayList<>());
                    userBest.get(phone).add(best);
                }
            } else
                best.setScore(score);
            synchronized (map) {
                Game game = map.get(gameId);
                if (game != null) {
                    if (game.getBest() == null || score > game.getBest().getScore())
                        game.setBest(best);
                }
            }
        }
        save();
        return new BaseTransfer();
    }

    @Override
    public Transfer updateChallenge(String phone, String gameId, float velocity) {
        synchronized (challengeMap) {
            String key = gameId + phone;
            ChallengeBest best = challengeMap.get(key);
            if (best == null) {
                best = new ChallengeBest().setGameId(gameId).setVelocity(velocity)
                        .setUserPhone(phone).setUser(userService.getUserByPhone(phone));
                challengeMap.put(key, best);
                synchronized (userBest) {
                    if (!userBest.containsKey(phone))
                        userBest.put(phone, new ArrayList<>());
                    userBest.get(phone).add(best);
                }
            } else
                best.setVelocity(velocity);
            synchronized (map) {
                Game game = map.get(gameId);
                if (game != null) {
                    if (game.getBest() == null || velocity > game.getBest().getVelocity())
                        game.setBest(best);
                }
            }
        }
        save();
        return new BaseTransfer();
    }

    @Override
    public List<BestTransfer> getGameDataByUserPhone(String phone) {
        synchronized (userBest) {
            if (!userBest.containsKey(phone))
                userBest.put(phone, new ArrayList<>());
            return userBest.get(phone);
        }
    }

    @Override
    public Collection<Game> getGames() {
        return map.values();
    }

    @Override
    public Game getGameById(String gameId) {
        return map.get(gameId);
    }
}
