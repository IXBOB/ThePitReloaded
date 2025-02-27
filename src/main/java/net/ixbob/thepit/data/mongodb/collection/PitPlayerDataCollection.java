package net.ixbob.thepit.data.mongodb.collection;

import com.mongodb.client.MongoCollection;
import net.ixbob.thepit.data.PitPlayerData;
import net.ixbob.thepit.observer.PlayerDataUpdateObserver;
import net.ixbob.thepit.observer.PlayerDataUpdateObservingData;
import net.ixbob.thepit.util.DateTimeUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PitPlayerDataCollection extends DBCollection implements PlayerDataUpdateObserver {

    public static final String FIELD_PLAYER_NAME = "playerName";
    public static final String FIELD_LAST_UPDATE_TIME = "lastUpdateTime";
    public static final String FIELD_PLAYER_COIN_AMOUNT = "coinAmount";
    public static final String FIELD_PLAYER_POINT_AMOUNT = "pointAmount";
    public static final String FIELD_PLAYER_XP_AMOUNT = "xpAmount";
    public static final String FIELD_PLAYER_MASTERY_LEVEL = "masteryLevel";  // 精通等级
    public static final String FIELD_PLAYER_REPUTATION_AMOUNT = "reputationAmount";  // 声望数量

    public PitPlayerDataCollection(MongoCollection<Document> legacyDBCollection) {
        super(legacyDBCollection);
    }

    /**
     * 更新数据库存储的玩家数据
     *
     * @param pitPlayerData 玩家数据对象
     */
    public void update(PitPlayerData pitPlayerData) {
        Document savedDoc = this.get(pitPlayerData.getPlayer());
        if (savedDoc == null) {
            Document newDoc = this.updateDocument(new Document(), pitPlayerData);
            super.insertOne(newDoc);
            return;
        }
        Document updatedDoc = this.updateDocument(savedDoc, pitPlayerData);
        super.replaceOne(updatedDoc);
    }

    /**
     * 获取数据库中存储的玩家信息
     *
     * @param player 玩家对象
     * @return 数据库信息，若不存在玩家信息则返回null
     */
    public Document get(Player player) {
        return super.findFirstEqual(FIELD_PLAYER_NAME, player.getName());
    }

    private Document updateDocument(@NotNull Document document, @NotNull PitPlayerData pitPlayerData) {
        document.put(FIELD_PLAYER_NAME, pitPlayerData.getPlayer().getName());
        document.put(FIELD_LAST_UPDATE_TIME, DateTimeUtil.getCurrentTimeStr());
        document.put(FIELD_PLAYER_COIN_AMOUNT, pitPlayerData.getCoinAmount());
        document.put(FIELD_PLAYER_POINT_AMOUNT, pitPlayerData.getPointAmount());
        document.put(FIELD_PLAYER_XP_AMOUNT, pitPlayerData.getXpAmount());
        document.put(FIELD_PLAYER_MASTERY_LEVEL, pitPlayerData.getMasteryLevel());
        document.put(FIELD_PLAYER_REPUTATION_AMOUNT, pitPlayerData.getReputationAmount());
        return document;
    }

    @Override
    public void onNotified(PlayerDataUpdateObservingData data) {
        this.update(data.pitPlayerData());
    }
}
