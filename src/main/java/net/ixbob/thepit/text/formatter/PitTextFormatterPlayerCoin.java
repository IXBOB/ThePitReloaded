package net.ixbob.thepit.text.formatter;

import net.ixbob.thepit.data.PitPlayerDataManager;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerCoin extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(PitPlayerDataManager.getInstance().getData(player).getCoinAmount());
    }
}
