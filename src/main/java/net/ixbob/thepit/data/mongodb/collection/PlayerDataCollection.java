package net.ixbob.thepit.data.mongodb.collection;

import com.mongodb.client.MongoCollection;
import net.ixbob.thepit.data.PlayerData;
import net.ixbob.thepit.observer.PlayerDataUpdateObserver;
import net.ixbob.thepit.observer.PlayerDataUpdateObservingData;
import net.ixbob.thepit.util.DateTimeUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerDataCollection extends DBCollection implements PlayerDataUpdateObserver {

    public static final String FIELD_PLAYER_NAME = "playerName";
    public static final String FIELD_LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String FIELD_PLAYER_COIN_AMOUNT = "coinAmount";
    public static final String FIELD_PLAYER_POINT_AMOUNT = "pointAmount";
    public static final String FIELD_PLAYER_XP_AMOUNT = "xpAmount";
    public static final String FIELD_PLAYER_MASTERY_LEVEL = "masteryLevel";  // 精通等级
    public static final String FIELD_PLAYER_REPUTATION_AMOUNT = "reputationAmount";  // 声望数量

    public PlayerDataCollection(MongoCollection<Document> legacyDBCollection) {
        super(legacyDBCollection);
    }

    /**
     * 更新数据库存储的玩家数据
     *
     * @param playerData 玩家数据对象
     */
    public void updatePlayerData(PlayerData playerData) {
        Document savedDoc = this.getPlayerData(playerData.getPlayer());
        if (savedDoc == null) {
            Document newDoc = this.updateDocument(new Document(), playerData);
            super.insertOne(newDoc);
            return;
        }
        Document updatedDoc = this.updateDocument(savedDoc, playerData);
        super.replaceOne(updatedDoc);
    }

    /**
     * 获取数据库中存储的玩家经济信息
     *
     * @param player 玩家对象
     * @return 数据库信息，若不存在玩家信息则返回null
     */
    public Document getPlayerData(Player player) {
        return super.findFirstEqual(FIELD_PLAYER_NAME, player.getName());
    }

    private Document updateDocument(@NotNull Document document, @NotNull PlayerData playerData) {
        document.put(FIELD_PLAYER_NAME, playerData.getPlayer().getName());
        document.put(FIELD_LAST_UPDATE_TIME, DateTimeUtil.getCurrentTimeStr());
        document.put(FIELD_PLAYER_COIN_AMOUNT, playerData.getCoinAmount());
        document.put(FIELD_PLAYER_POINT_AMOUNT, playerData.getPointAmount());
        document.put(FIELD_PLAYER_XP_AMOUNT, playerData.getXpAmount());
        document.put(FIELD_PLAYER_MASTERY_LEVEL, playerData.getMasteryLevel());
        document.put(FIELD_PLAYER_REPUTATION_AMOUNT, playerData.getReputationAmount());
        return document;
    }

    @Override
    public void onNotified(PlayerDataUpdateObservingData data) {
        this.updatePlayerData(data.playerData());
    }
}
