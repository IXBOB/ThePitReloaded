package net.ixbob.thepit.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import net.ixbob.thepit.enums.ScoreBoardContentEnum;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.observer.PlayerJoinObserverObject;
import net.ixbob.thepit.observer.PlayerJoinObservingData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PitScoreBoardManager implements PlayerJoinObserverObject {

    private static PitScoreBoardManager instance = new PitScoreBoardManager();

    private static final String MAIN_OBJECTIVE_NAME = "main";

    private PitScoreBoardManager() {
        PlayerJoinListener.getInstance().attachObserver(this);
    }

    public static PitScoreBoardManager getInstance() {
        if (instance == null) {
            instance = new PitScoreBoardManager();
        }
        return instance;
    }

    @Override
    public void onNotified(PlayerJoinObservingData data) {
        initScoreBoardToNewJoiner(data.player());
    }

    private void initScoreBoardToNewJoiner(Player player) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        WrapperPlayServerScoreboardObjective objPacket = new WrapperPlayServerScoreboardObjective(
                MAIN_OBJECTIVE_NAME,
                WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE,
                Component.text("天坑乱斗")
                        .color(NamedTextColor.YELLOW)
                        .decorate(TextDecoration.BOLD),
                WrapperPlayServerScoreboardObjective.RenderType.INTEGER);
        user.sendPacket(objPacket);
        for (ScoreBoardContentEnum contentEnum : ScoreBoardContentEnum.values()) {
            sendNormalUpdateScorePacket(user,
                    contentEnum.getContent().getFormattedText(player),
                    contentEnum.getDisplayValue());
        }
        WrapperPlayServerDisplayScoreboard displayPacket = new WrapperPlayServerDisplayScoreboard(1, "main");
        user.sendPacket(displayPacket);
    }

    private void sendNormalUpdateScorePacket(User user, String content, int value) {
        WrapperPlayServerUpdateScore scorePacket = new WrapperPlayServerUpdateScore(
                content,
                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                MAIN_OBJECTIVE_NAME,
                Optional.of(value));
        user.sendPacket(scorePacket);
    }
}
