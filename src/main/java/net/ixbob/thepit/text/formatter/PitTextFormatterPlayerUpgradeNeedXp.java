package net.ixbob.thepit.text.formatter;

import net.ixbob.thepit.data.PitPlayerDataManager;
import net.ixbob.thepit.util.LevelCalculator;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerUpgradeNeedXp extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        double currentXpAmount = PitPlayerDataManager.getInstance().getData(player).getXpAmount();
        return String.valueOf(LevelCalculator.getNeededExpForNextLevel(currentXpAmount));
    }
}
