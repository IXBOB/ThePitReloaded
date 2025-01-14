package net.ixbob.thepit;

import net.ixbob.thepit.holder.PlayerEconomyHolder;
import net.ixbob.thepit.util.LevelCalculator;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerLevel extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(LevelCalculator.getLevelFromExp(
                PlayerEconomyHolder.getInstance().getEconomy(player).getXpAmount()));
    }
}
