package net.ixbob.thepit.mongodb.collection;

import com.mongodb.client.MongoCollection;
import net.ixbob.thepit.PlayerEconomy;
import net.ixbob.thepit.observer.PlayerEconomyUpdateObserver;
import net.ixbob.thepit.observer.PlayerEconomyUpdateObservingData;
import net.ixbob.thepit.util.DateTimeUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerEconomyCollection extends DBCollection implements PlayerEconomyUpdateObserver {

    public static final String FIELD_PLAYER_NAME = "playerName";
    public static final String FIELD_LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String FIELD_PLAYER_COIN_AMOUNT = "coinAmount";
    public static final String FIELD_PLAYER_POINT_AMOUNT = "pointAmount";
    public static final String FIELD_PLAYER_XP_AMOUNT = "xpAmount";

    public PlayerEconomyCollection(MongoCollection<Document> legacyDBCollection) {
        super(legacyDBCollection);
    }

    /**
     * 在数据库中更新玩家经济数据，若已经在数据库中记录了，则忽略
     *
     * @param playerEconomy 玩家经济对象
     */
    public void updatePlayerEcoData(PlayerEconomy playerEconomy) {
        Document savedDoc = this.getPlayerEcoData(playerEconomy.getPlayer());
        if (savedDoc == null) {
            Document newDoc = this.updateDocument(new Document(), playerEconomy);
            super.insertOne(newDoc);
            return;
        }
        Document updatedDoc = this.updateDocument(savedDoc, playerEconomy);
        super.replaceOne(updatedDoc);
    }

    /**
     * 获取数据库中存储的玩家经济信息
     *
     * @param player 玩家对象
     * @return 数据库信息，若不存在玩家信息则返回null
     */
    public Document getPlayerEcoData(Player player) {
        return super.findFirstEqual(FIELD_PLAYER_NAME, player.getName());
    }

    private Document updateDocument(@NotNull Document document, @NotNull PlayerEconomy playerEconomy) {
        document.put(FIELD_PLAYER_NAME, playerEconomy.getPlayer().getName());
        document.put(FIELD_LAST_UPDATE_TIME, DateTimeUtil.getCurrentTimeStr());
        document.put(FIELD_PLAYER_COIN_AMOUNT, playerEconomy.getCoinAmount());
        document.put(FIELD_PLAYER_POINT_AMOUNT, playerEconomy.getPointAmount());
        document.put(FIELD_PLAYER_XP_AMOUNT, playerEconomy.getXpAmount());
        return document;
    }

    @Override
    public void onNotified(PlayerEconomyUpdateObservingData data) {
        this.updatePlayerEcoData(data.playerEconomy());
    }
}
