package net.ixbob.thepit;

import net.ixbob.thepit.holder.PlayerEconomyHolder;
import net.ixbob.thepit.util.LevelCalculator;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerUpgradeNeedXp extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        double currentXpAmount = PlayerEconomyHolder.getInstance().getEconomy(player).getXpAmount();
        return String.valueOf(LevelCalculator.getNeededExpForNextLevel(currentXpAmount));
    }
}
