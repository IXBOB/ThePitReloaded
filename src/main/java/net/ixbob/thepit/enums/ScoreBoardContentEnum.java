package net.ixbob.thepit.enums;

import net.ixbob.thepit.PitText;
import net.ixbob.thepit.PitTextFormatterPlayerCoin;
import net.ixbob.thepit.PitTextFormatterPlayerLevel;
import net.ixbob.thepit.PitTextFormatterPlayerUpgradeNeedXp;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ScoreBoardContentEnum {

    LINE_9(9, ScoreBoardContentFlag.HARD_CODE_TEXT, new PitText<>(" ")),
    LINE_8(8, ScoreBoardContentFlag.ECONOMY_TEXT, new PitText<>("硬币:  §6%s", new PitTextFormatterPlayerCoin())),
    LINE_7(7, ScoreBoardContentFlag.HARD_CODE_TEXT, new PitText<>("  ")),
    LINE_6(6, ScoreBoardContentFlag.ECONOMY_TEXT, new PitText<>("等级:  §e%s", new PitTextFormatterPlayerLevel())),
    LINE_5(5, ScoreBoardContentFlag.ECONOMY_TEXT, new PitText<>("距升级:  §b%s 经验", new PitTextFormatterPlayerUpgradeNeedXp())),
    LINE_4(4, ScoreBoardContentFlag.HARD_CODE_TEXT, new PitText<>("   "));

    public static List<ScoreBoardContentEnum> getContentsOf(ScoreBoardContentFlag flag) {
        return Arrays.stream(values())
                .filter(contentEnum -> contentEnum.getFlag() == flag)
                .collect(Collectors.toList());
    }

    private final int displayValue;
    private final ScoreBoardContentFlag flag;
    private final PitText<Player> content;

    ScoreBoardContentEnum(int displayValue, ScoreBoardContentFlag contentFlag, PitText<Player> content) {
        this.displayValue = displayValue;
        this.flag = contentFlag;
        this.content = content;
    }

    public int getDisplayValue() {
        return this.displayValue;
    }

    public ScoreBoardContentFlag getFlag() {
        return this.flag;
    }

    public PitText<Player> getContent() {
        return this.content;
    }

}
