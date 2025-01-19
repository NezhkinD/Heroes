## Запуск
1. Для работы приложения требуется:
- java >= v23.0.1
- GNU Make >= v4.3


2. Для запуска программы выполняем команду:
```
   make run
```

## Описание сложности
- [GeneratePresetImpl.java](src%2Fprograms%2FGeneratePresetImpl.java) - Сложность сортировка + выбор юнитов - O(n log n)
- [SimulateBattleImpl.java](src%2Fprograms%2FSimulateBattleImpl.java) - Сложность O(n * m), где n - кол-во юнитов игрока, m - кол-во юнитов компьютера
- [SuitableForAttackUnitsFinderImpl.java](src%2Fprograms%2FSuitableForAttackUnitsFinderImpl.java) - Сложность O(n * m), где n - кол-ов строк, m - кол-во юнитов в строке
- [UnitTargetPathFinderImpl.java](src%2Fprograms%2FUnitTargetPathFinderImpl.java) - Сложность O(log(n * m) * n * m), где n - ширина поля, m - высота поля