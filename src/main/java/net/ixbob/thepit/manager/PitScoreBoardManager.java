package net.ixbob.thepit.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResetScore;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import net.ixbob.thepit.enums.ScoreBoardContentEnum;
import net.ixbob.thepit.enums.ScoreBoardContentFlag;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.observer.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class PitScoreBoardManager implements PlayerJoinObserver, PlayerQuitObserver, PlayerEconomyUpdateObserver {

    private static PitScoreBoardManager instance = new PitScoreBoardManager();
    private final HashMap<Player, HashMap<ScoreBoardContentEnum, String>> lastSendConcreteScoreMap = new HashMap<>();

    private static final String MAIN_OBJECTIVE_NAME = "main";

    private PitScoreBoardManager() {
        PlayerJoinListener.getInstance().attachObserver(this);
        PlayerQuitListener.getInstance().attachObserver(this);
    }

    public static PitScoreBoardManager getInstance() {
        if (instance == null) {
            instance = new PitScoreBoardManager();
        }
        return instance;
    }

    @Override
    public void onNotified(PlayerJoinObservingData data) {
        sendInitScoreBoardPacket(data.player());
    }

    @Override
    public void onNotified(PlayerQuitObservingData data) {
        this.lastSendConcreteScoreMap.remove(data.player());
    }

    @Override
    public void onNotified(PlayerEconomyUpdateObservingData data) {
        sendUpdateEconomyScorePacket(data.playerEconomy().getPlayer());
    }

    private void sendInitScoreBoardPacket(Player player) {
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
            sendCreateOrUpdateCurrentScorePacket(
                    player, contentEnum,
                    contentEnum.getContent().getFormattedText(player));
        }
        WrapperPlayServerDisplayScoreboard displayPacket = new WrapperPlayServerDisplayScoreboard(1, "main");
        user.sendPacket(displayPacket);
    }

    private void sendUpdateEconomyScorePacket(Player player) {
        HashMap<ScoreBoardContentEnum, String> sentScoreMap =
                this.lastSendConcreteScoreMap.getOrDefault(player, new HashMap<>());
        new HashMap<>(sentScoreMap).forEach((contentEnum, lastSentConcreteStr) -> {
            if (contentEnum.getFlag() == ScoreBoardContentFlag.ECONOMY_TEXT) {
                sendResetCurrentScorePacket(player, contentEnum);
            }
                });
        for (ScoreBoardContentEnum contentEnum : ScoreBoardContentEnum.getContentsOf(ScoreBoardContentFlag.ECONOMY_TEXT)) {
            sendCreateOrUpdateCurrentScorePacket(
                    player, contentEnum,
                    contentEnum.getContent().getFormattedText(player));
        }
    }

    private void sendCreateOrUpdateCurrentScorePacket(Player player, ScoreBoardContentEnum contentEnum, String contentStr) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        this.lastSendConcreteScoreMap.putIfAbsent(player, new HashMap<>());
        this.lastSendConcreteScoreMap.get(player).put(contentEnum, contentStr);
        WrapperPlayServerUpdateScore scorePacket = new WrapperPlayServerUpdateScore(
                contentStr,
                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                MAIN_OBJECTIVE_NAME,
                Optional.of(contentEnum.getDisplayValue()));
        user.sendPacket(scorePacket);
    }

    private void sendResetCurrentScorePacket(Player player, ScoreBoardContentEnum contentEnum) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        HashMap<ScoreBoardContentEnum, String> sentScoreMap =
                this.lastSendConcreteScoreMap.getOrDefault(player, new HashMap<>());
        if (!sentScoreMap.containsKey(contentEnum)) {
            throw new RuntimeException("The player haven't received any concrete score content for enum: " + contentEnum);
        }
        WrapperPlayServerResetScore scorePacket = new WrapperPlayServerResetScore(
                sentScoreMap.get(contentEnum), MAIN_OBJECTIVE_NAME);
        sentScoreMap.remove(contentEnum);
        user.sendPacket(scorePacket);
    }
}
