package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *  Итоговая сложность сортировка + выбор юнитов - O(n log n)
 */
public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNIT_EACH_TYPE = 11;
    private static final int MAX_X = 3;
    private static final int MAX_Y = 21;
    private final Random random = new Random();
    private List<String> usedCells;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        usedCells = new ArrayList<>();
        int totalPoints = 0;

        /* сортировка, сложность O(n log n) */
        List<Unit> sortedUnits = unitList.stream()
                .sorted(Comparator.comparingDouble(unit -> -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())))
                .toList();

        /* выбор юнитов, сложность O(n) */
        for (Unit unit : sortedUnits) {
            int maxAddUnit = Math.min((maxPoints - totalPoints) / unit.getCost(), MAX_UNIT_EACH_TYPE);

            for (int i = 0; i < maxAddUnit; i++) {
                selectedUnits.add(createNewUnit(unit, i));
            }

            totalPoints += maxAddUnit * unit.getCost();
        }

        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(totalPoints);
        return computerArmy;
    }

    private Unit createNewUnit(Unit unit, int i) {
        int x, y;
        do {
            x = random.nextInt(MAX_X);
            y = random.nextInt(MAX_Y);
        } while (usedCells.contains(x + "_" + y));
        Unit newUnit = new Unit(
                unit.getName(),
                unit.getUnitType(),
                unit.getHealth(),
                unit.getBaseAttack(),
                unit.getCost(),
                unit.getAttackType(),
                unit.getAttackBonuses(),
                unit.getDefenceBonuses(),
                x,
                y
        );
        newUnit.setName(unit.getUnitType() + " " + i);
        usedCells.add(x + "_" + y);
        return newUnit;
    }
}