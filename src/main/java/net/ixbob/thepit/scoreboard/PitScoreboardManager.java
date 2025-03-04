package net.ixbob.thepit.scoreboard;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResetScore;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.observer.*;
import net.ixbob.thepit.util.SingletonUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class PitScoreboardManager implements PlayerJoinObserver, PlayerQuitObserver, PlayerDataUpdateObserver {

    private static final Supplier<PitScoreboardManager> instance = SingletonUtil.createSingletonEager(PitScoreboardManager::new);
    private final HashMap<Player, HashMap<ScoreboardContentEnum, String>> lastSendConcreteScoreMap = new HashMap<>();

    private static final String MAIN_OBJECTIVE_NAME = "main";

    private PitScoreboardManager() {
        PlayerJoinListener.getInstance().attachObserver(this);
        PlayerQuitListener.getInstance().attachObserver(this);
    }

    public static PitScoreboardManager getInstance() {
        return instance.get();
    }

    @Override
    public void onNotified(PlayerJoinObservingData data) {
        sendInitScoreboardPacket(data.player());
    }

    @Override
    public void onNotified(PlayerQuitObservingData data) {
        this.lastSendConcreteScoreMap.remove(data.player());
    }

    @Override
    public void onNotified(PlayerDataUpdateObservingData data) {
        sendUpdatePlayerDataScorePacket(data.pitPlayerData().getPlayer());
    }

    private void sendInitScoreboardPacket(Player player) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        // 创建计分板 objective
        WrapperPlayServerScoreboardObjective createObjPacket = new WrapperPlayServerScoreboardObjective(
                MAIN_OBJECTIVE_NAME,
                WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE,
                Component.text("天坑乱斗")
                        .color(NamedTextColor.YELLOW)
                        .decorate(TextDecoration.BOLD),
                WrapperPlayServerScoreboardObjective.RenderType.INTEGER);
        user.sendPacket(createObjPacket);
        // 创建计分板 scores
        Arrays.stream(ScoreboardContentEnum.values()).forEach(boardEnum ->
                sendCreateOrUpdateCurrentScorePacket(player, boardEnum));
        WrapperPlayServerDisplayScoreboard displayObjPacket = new WrapperPlayServerDisplayScoreboard(1, "main");
        user.sendPacket(displayObjPacket);
    }

    private void sendUpdatePlayerDataScorePacket(Player player) {
        HashMap<ScoreboardContentEnum, String> sentScoreMap =
                this.lastSendConcreteScoreMap.getOrDefault(player, new HashMap<>());
        // 重置曾经创建过的计分板 scores
        new HashMap<>(sentScoreMap).forEach((contentEnum, lastSentConcreteStr) -> {
            if (contentEnum.getFlag() == ScoreboardContentFlag.PLAYER_DATA_TEXT) {
                sendResetCurrentScorePacket(player, contentEnum);
            }
        });
        // 创建新的计分板 scores
        ScoreboardContentEnum.getContentsOf(ScoreboardContentFlag.PLAYER_DATA_TEXT)
                .forEach(contentEnum ->
                sendCreateOrUpdateCurrentScorePacket(player, contentEnum)
        );
    }

    private void sendCreateOrUpdateCurrentScorePacket(Player player, ScoreboardContentEnum contentEnum) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        String contentStr = contentEnum.getContent().getFormattedText(player);
        this.lastSendConcreteScoreMap.putIfAbsent(player, new HashMap<>());
        this.lastSendConcreteScoreMap.get(player).put(contentEnum, contentStr);
        WrapperPlayServerUpdateScore scorePacket = new WrapperPlayServerUpdateScore(
                contentStr,
                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                MAIN_OBJECTIVE_NAME,
                Optional.of(contentEnum.getDisplayValue()));
        user.sendPacket(scorePacket);
    }

    private void sendResetCurrentScorePacket(Player player, ScoreboardContentEnum contentEnum) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        HashMap<ScoreboardContentEnum, String> sentScoreMap =
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
