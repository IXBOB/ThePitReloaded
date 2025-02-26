package net.ixbob.thepit.text.formatter;

import net.ixbob.thepit.data.PlayerDataManager;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerCoin extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(PlayerDataManager.getInstance().getData(player).getCoinAmount());
    }
}
