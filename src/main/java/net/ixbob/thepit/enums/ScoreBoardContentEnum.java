package net.ixbob.thepit.enums;

import net.ixbob.thepit.PitText;
import net.ixbob.thepit.PitTextFormatterPlayerCoin;
import net.ixbob.thepit.PitTextFormatterPlayerLevel;
import net.ixbob.thepit.PitTextFormatterPlayerUpgradeNeedXp;
import org.bukkit.entity.Player;

public enum ScoreBoardContentEnum {

    LINE_9(9, new PitText<>(" ")),
    LINE_8(8, new PitText<>("硬币:  §6%s", new PitTextFormatterPlayerCoin())),
    LINE_7(7, new PitText<>("  ")),
    LINE_6(6, new PitText<>("等级:  §e%s", new PitTextFormatterPlayerLevel())),
    LINE_5(5, new PitText<>("距升级:  §b%s 经验", new PitTextFormatterPlayerUpgradeNeedXp())),
    LINE_4(4, new PitText<>("   "));

    private final int displayValue;
    private final PitText<Player> content;

    ScoreBoardContentEnum(int displayValue, PitText<Player> content) {
        this.displayValue = displayValue;
        this.content = content;
    }

    public int getDisplayValue() {
        return this.displayValue;
    }

    public PitText<Player> getContent() {
        return this.content;
    }

}
