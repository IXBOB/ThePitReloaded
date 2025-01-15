package net.ixbob.thepit.text.formatter;

import net.ixbob.thepit.economy.PlayerEconomyManager;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerCoin extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(PlayerEconomyManager.getInstance().getEconomy(player).getCoinAmount());
    }
}
