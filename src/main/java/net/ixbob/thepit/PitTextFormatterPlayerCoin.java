package net.ixbob.thepit;

import net.ixbob.thepit.holder.PlayerEconomyHolder;
import org.bukkit.entity.Player;

public class PitTextFormatterPlayerCoin extends PitTextFormatter<Player> {

    @Override
    public String getFormattedString(Player player) {
        return String.valueOf(PlayerEconomyHolder.getInstance().getEconomy(player).getCoinAmount());
    }
}
