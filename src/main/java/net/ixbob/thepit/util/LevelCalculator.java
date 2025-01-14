package net.ixbob.thepit.util;

public class LevelCalculator {

    public static double getNeededExpForNextLevel(double totalExp) {
        int level = 1;
        double expTemp = totalExp;  // 临时变量，用于扣减升级所需经验
        while (true) {
            double expNeeded = getExpForNextLevel(level);
            if (expTemp >= expNeeded) {
                expTemp -= expNeeded;
                level++;
            } else {
                // 这里 expTemp 不足以升下一级
                // 差值就是还需要多少才能升级
                return expNeeded - expTemp;
            }
        }
    }

    /**
     * 获取“从当前等级 level 升到下一级”所需的经验值。
     * - level = 1 → 返回 10
     * - level = 2 → 返回 15
     * - level >= 3 → 返回略微指数增长的值
     */
    public static double getExpForNextLevel(int level) {
        if (level == 1) {
            return 10;
        }
        if (level == 2) {
            return 15;
        }
        // 从 level=3 开始，让升级需求在 15 基础上乘以 1.048^(level-2)
        double ratio = 1.048;
        double base = 15;
        int exponent = level - 2;
        double result = base * Math.pow(ratio, exponent);
        return (double) Math.round(result); // 四舍五入取整
    }

    /**
     * 根据总经验值 totalExp 计算当前等级。
     * 注意：此处假设从等级 1 开始检查，一直检查是否能够升级到下一级，
     *       如果总经验足够，就扣除相应的经验并 level++，
     *       直到无法继续升级为止。
     */
    public static int getLevelFromExp(double totalExp) {
        int level = 1;
        while (true) {
            double expNeeded = getExpForNextLevel(level);
            if (totalExp >= expNeeded) {
                totalExp -= expNeeded;
                level++;
            } else {
                break;
            }
        }
        return level;
    }

    // 测试
    public static void main(String[] args) {
        // 举例：我们来测试从 0 点经验开始，一直增加经验，看等级如何变化
        double[] testExps = {0, 9, 10, 24, 25, 30, 50, 200, 500, 50000, 80000};
        for (double exp : testExps) {
            int level = getLevelFromExp(exp);
            System.out.printf("总经验值: %s -> 等级: %s%n", exp, level);
        }

        // 也可单独测试：给定当前等级时，下一级所需经验
        for (int lv = 1; lv <= 100; lv++) {
            System.out.printf("当前等级: %s -> 升级所需经验: %s%n", lv, getExpForNextLevel(lv));
        }
    }

}
