package sudoku.computationlogic;

import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGenerator {
    public static SudokuGame getNewGameGrid() {
//        return unsolveGame(getSolvedGame());
        return new SudokuGame(
                GameState.NEW,
                GameGenerator.getNewGameGrid()
        );
    }

    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());
        boolean solvable = false;
        int[][] solvableArray = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];

        while(solvable == false) {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);
            int index = 0;
            while(index < 40) {
              int xCoordinates = random.nextInt(SudokuGame.GRID_BOUNDARY);
              int yCoordinates = random.nextInt(SudokuGame.GRID_BOUNDARY);

              if(solvableArray[xCoordinates][yCoordinates] != 0) {
                  solvableArray[xCoordinates][yCoordinates] = 0;
                  index++;
              }
            }
            int[][] toBeSolved = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }
        return solvableArray;
    }

    private static Object getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[SudokuGame.GRID_BOUNDARY][SudokuGame.GRID_BOUNDARY];

        for(int value = 1; value <= SudokuGame.GRID_BOUNDARY; value++) {
            int allocations = 0;
            int interrupt = 0;

            List<Coordinates> allocTracker = new ArrayList<>();

            int attempts = 0;

            while(allocations < SudokuGame.GRID_BOUNDARY) {
                if(interrupt > 200) {
                    allocTracker.forEach(coord -> {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });
                    interrupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts++;

                    if(attempts > 500) {
                        clearArray(newGrid);
                        attempts= 0;
                        value = 1;
                    }
                }
              int xCoordinates = random.nextInt(SudokuGame.GRID_BOUNDARY);
                int yCoordinates = random.nextInt(SudokuGame.GRID_BOUNDARY);
                if(newGrid[xCoordinates][yCoordinates] == 0) {
                    newGrid[xCoordinates][yCoordinates] = value;

                    if(GameLogic.sudokuIsInvalid(newGrid)) {
                        newGrid[xCoordinates][yCoordinates] = 0;
                        interrupt++;
                    } else {
                        allocTracker.add(new Coordinates(xCoordinates,yCoordinates));
                        allocations++;
                    }
                }
            }
        }
        return newGrid;
    }

    private static void clearArray(int[][] newGrid) {
        for(int xIndex = 0; xIndex < SudokuGame.GRID_BOUNDARY; xIndex++) {
            for(int yIndex = 0; yIndex < SudokuGame.GRID_BOUNDARY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}
