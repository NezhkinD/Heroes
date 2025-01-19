package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Итоговая сложность O(log(n * m) * n * m), где n - ширина поля, m - высота поля
 */
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> unitList) {
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] previous = new Edge[WIDTH][HEIGHT];

        Set<String> usedCells = unitList.stream()
                .filter(unit -> unit.isAlive() && unit != attackUnit && unit != targetUnit)
                .map(unit -> unit.getxCoordinate() + "," + unit.getyCoordinate())
                .collect(Collectors.toSet());

        int[][] distance = new int[WIDTH][HEIGHT];
        for (int[] row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));
        buildStartPoint(attackUnit, distance, queue);

        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();
            int cx = current.getX();
            int cy = current.getY();

            if (visited[cx][cy]) continue;
            visited[cx][cy] = true;

            if (cx == targetUnit.getxCoordinate() && cy == targetUnit.getyCoordinate()) break;

            checkNeighbors(current, usedCells, distance, previous, queue);
        }

        return buildPath(previous, attackUnit, targetUnit);
    }

    private void buildStartPoint(Unit attackUnit, int[][] distance, PriorityQueue<EdgeDistance> queue) {
        distance[attackUnit.getxCoordinate()][attackUnit.getyCoordinate()] = 0;
        queue.add(new EdgeDistance(attackUnit.getxCoordinate(), attackUnit.getyCoordinate(), 0));
    }

    private void checkNeighbors(EdgeDistance current, Set<String> usedCells, int[][] distance, Edge[][] previous, PriorityQueue<EdgeDistance> queue) {
        for (int[] dir : DIRECTIONS) {
            int nx = current.getX() + dir[0];
            int ny = current.getY() + dir[1];

            if (!(nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT && !usedCells.contains(nx + "," + ny))) {
                continue;
            }

            int newDistance = distance[current.getX()][current.getY()] + 1;
            if (newDistance < distance[nx][ny] && !queue.contains(new EdgeDistance(nx, ny, newDistance))) {
                distance[nx][ny] = newDistance;
                previous[nx][ny] = new Edge(current.getX(), current.getY());
                queue.add(new EdgeDistance(nx, ny, newDistance));
            }
        }
    }

    private List<Edge> buildPath(Edge[][] previous, Unit attackUnit, Unit targetUnit) {
        List<Edge> path = new ArrayList<>();
        int tx = targetUnit.getxCoordinate();
        int ty = targetUnit.getyCoordinate();

        while (tx != attackUnit.getxCoordinate() || ty != attackUnit.getyCoordinate()) {
            path.add(new Edge(tx, ty));
            Edge prev = previous[tx][ty];
            if (prev == null) {
                System.out.println("Путь для атакующего юнита [" + attackUnit.getName() + "] не найден");
                return Collections.emptyList();
            }
            tx = prev.getX();
            ty = prev.getY();
        }
        path.add(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        Collections.reverse(path);
        return path;
    }
}