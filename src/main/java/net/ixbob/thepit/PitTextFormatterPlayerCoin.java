package net.ixbob.thepit;

import net.ixbob.thepit.manager.PlayerEconomyManager;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerCoin extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(PlayerEconomyManager.getInstance().getEconomy(player).getCoinAmount());
    }
}
