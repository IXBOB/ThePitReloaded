package net.ixbob.thepit.data.mongodb.collection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import net.ixbob.thepit.util.DateTimeUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;

public class RegisteredPlayerCollection extends DBCollection {

    public static final String FIELD_PLAYER_NAME = "playerName";
    public static final String FIELD_REGISTER_TIME = "registerTime";

    public RegisteredPlayerCollection(MongoCollection<Document> legacyDBCollection) {
        super(legacyDBCollection);
    }

    /**
     * 在数据库中记录玩家已注册，若已经在数据库中记录了，则忽略
     *
     * @param player 玩家对象
     */
    public void registerIfFirstJoin(Player player) {
        if (isPlayerRegistered(player)) {
            return;
        }
        insertData(player);
    }

    private boolean isPlayerRegistered(Player player) {
        Bson projectionFields = Projections.fields(
                Projections.include(FIELD_PLAYER_NAME));
        return super.findFirstEqual(FIELD_PLAYER_NAME, player.getName(), projectionFields) != null;
    }

    private void insertData(Player player) {
        super.insertOne(new Document()
                .append(FIELD_ID, new ObjectId())
                .append(FIELD_PLAYER_NAME, player.getName())
                .append(FIELD_REGISTER_TIME, DateTimeUtil.getCurrentTimeStr()));
    }
}
