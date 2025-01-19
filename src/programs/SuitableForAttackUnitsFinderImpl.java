package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сложность O(n * m), где n - кол-ов строк, m - кол-во юнитов в строке
 */
public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        return unitsByRow.stream()
                .filter(Objects::nonNull)
                .flatMap(row -> findUnitsInRow(row, isLeftArmyTarget).stream())
                .collect(Collectors.toList());
    }

    private List<Unit> findUnitsInRow(List<Unit> row, boolean isLeftArmyTarget) {
        if (row == null || row.isEmpty()) {
            return List.of();
        }
        return row.stream()
                .filter(Objects::nonNull)
                .filter(Unit::isAlive)
                .filter(unit -> isSuitableForAttack(row, row.indexOf(unit), isLeftArmyTarget))
                .collect(Collectors.toList());
    }

    private boolean isSuitableForAttack(List<Unit> row, int unitIndex, boolean isLeftArmyTarget) {
        return isLeftArmyTarget ? isRightUnit(row, unitIndex) : isLeftUnit(row, unitIndex);
    }

    private boolean isRightUnit(List<Unit> row, int unitIndex) {
        return unitIndex == row.size() - 1 || row.subList(unitIndex + 1, row.size()).stream().allMatch(Objects::isNull);
    }

    private boolean isLeftUnit(List<Unit> row, int unitIndex) {
        return unitIndex == 0 || row.subList(0, unitIndex).stream().allMatch(Objects::isNull);
    }
}