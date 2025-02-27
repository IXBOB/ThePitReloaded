package net.ixbob.thepit.text.formatter;

import net.ixbob.thepit.data.PitPlayerDataManager;
import net.ixbob.thepit.util.LevelCalculator;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerLevel extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(LevelCalculator.getLevelFromExp(
                PitPlayerDataManager.getInstance().getData(player).getXpAmount()));
    }
}
