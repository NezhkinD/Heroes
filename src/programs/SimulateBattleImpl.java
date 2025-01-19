package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Итоговая сложность O(n * m), где n - кол-во юнитов игрока, m - кол-во юнитов компьютера
 */
public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army var1, Army var2) throws InterruptedException {
        Set<Unit> player = new HashSet<>(var1.getUnits());
        Set<Unit> computer = new HashSet<>(var2.getUnits());

        while (!player.isEmpty() && !computer.isEmpty()) {
            attack(player, false);
            attack(computer, true);
        }
        String winner = "Ничья";
        if (player.isEmpty() && !computer.isEmpty()) {
            winner = "Победил компьютер!";
        }

        if (!player.isEmpty() && computer.isEmpty()) {
            winner = "Победил пользователь!";
        }

        System.out.println("Битва закончена. " + winner);
    }

    private void attack(Set<Unit> units, boolean isComputer) throws InterruptedException {
        Iterator<Unit> iterator = units.iterator();

        while (iterator.hasNext()) {
            Unit unit = iterator.next();

            if (!unit.isAlive()) {
                iterator.remove();
                System.out.println((isComputer ? "Вражеский юнит " : "Ваш юнит ") + "[" + unit.getName() + "] убит");
                continue;
            }

            Unit target = unit.getProgram().attack();
            if (target != null) {
                printBattleLog.printBattleLog(unit, target);
                System.out.println((isComputer ? "Вражеский юнит " : "Ваш юнит ") + "[" + unit.getName() + "] атаковал [" + target.getName() + "]");
            }
        }
    }
}