package net.ixbob.thepit.text.formatter;

import net.ixbob.thepit.economy.PlayerEconomyManager;
import net.ixbob.thepit.util.LevelCalculator;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerLevel extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(LevelCalculator.getLevelFromExp(
                PlayerEconomyManager.getInstance().getEconomy(player).getXpAmount()));
    }
}
