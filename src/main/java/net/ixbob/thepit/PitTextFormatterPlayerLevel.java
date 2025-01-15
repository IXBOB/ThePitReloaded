package net.ixbob.thepit;

import net.ixbob.thepit.manager.PlayerEconomyManager;
import net.ixbob.thepit.util.LevelCalculator;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerLevel extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(LevelCalculator.getLevelFromExp(
                PlayerEconomyManager.getInstance().getEconomy(player).getXpAmount()));
    }
}
