package net.ixbob.thepit.scoreboard;

import net.ixbob.thepit.text.PitText;
import net.ixbob.thepit.text.formatter.PitTextFormatterPlayerCoin;
import net.ixbob.thepit.text.formatter.PitTextFormatterPlayerLevel;
import net.ixbob.thepit.text.formatter.PitTextFormatterPlayerUpgradeNeedXp;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ScoreboardContentEnum {

    LINE_9(9, ScoreboardContentFlag.HARD_CODE_TEXT, new PitText<>(" ")),
    LINE_8(8, ScoreboardContentFlag.PLAYER_DATA_TEXT, new PitText<>("硬币:  §6%s", new PitTextFormatterPlayerCoin())),
    LINE_7(7, ScoreboardContentFlag.HARD_CODE_TEXT, new PitText<>("  ")),
    LINE_6(6, ScoreboardContentFlag.PLAYER_DATA_TEXT, new PitText<>("等级:  §e%s", new PitTextFormatterPlayerLevel())),
    LINE_5(5, ScoreboardContentFlag.PLAYER_DATA_TEXT, new PitText<>("距升级:  §b%s 经验", new PitTextFormatterPlayerUpgradeNeedXp())),
    LINE_4(4, ScoreboardContentFlag.HARD_CODE_TEXT, new PitText<>("   "));

    public static List<ScoreboardContentEnum> getContentsOf(ScoreboardContentFlag flag) {
        return Arrays.stream(values())
                .filter(contentEnum -> contentEnum.getFlag() == flag)
                .collect(Collectors.toList());
    }

    private final int displayValue;
    private final ScoreboardContentFlag flag;
    private final PitText<Player> content;

    ScoreboardContentEnum(int displayValue, ScoreboardContentFlag contentFlag, PitText<Player> content) {
        this.displayValue = displayValue;
        this.flag = contentFlag;
        this.content = content;
    }

    public int getDisplayValue() {
        return this.displayValue;
    }

    public ScoreboardContentFlag getFlag() {
        return this.flag;
    }

    public PitText<Player> getContent() {
        return this.content;
    }

}
