public class GameState extends Game {
    private final int VALUE_MULTIPLIER = 2;
    private String[][] gameBoardState;
    private int boardValue;

    public GameState(String[][] g) {
        this.gameBoardState = g;
        this.boardValue = calBoardValue();
    }

    public int getBoardValue() {
        return boardValue;
    }

    private int calBoardValue() {
        int sum = 0;
        for (int i = 0; i < GRID; i++)
            for (int j = 0; j < GRID; j++)
                sum += positionValueCalculatior(i, j);
        return sum;
    }

    private int positionValueCalculatior(int i, int j) {
        return horizontalMoveValue(i, j) + verticalMoveValue(i, j) + diagonalOne(i, j) + diagonalTwo(i, j)
                + diagonalThree(i, j) + diagonalFour(i, j);
    }

    private int horizontalMoveValue(int i, int j) {
        String prev = "";
        int sum = 0;
        for (int x = j, travel = 0; x < GRID && travel < WIN_VALUE; x++, travel++) {
            sum += nodeValueAssigner(i, x, travel, prev);
            prev = gameBoardState[i][x];
        }
        prev = "";
        for (int x = j, travel = 0; x > 0 && travel < WIN_VALUE; x--, travel++) {
            sum += nodeValueAssigner(i, x, travel, prev);
            prev = gameBoardState[i][x];
        }
        return sum;
    }

    private int verticalMoveValue(int i, int j) {
        String prev = "";
        int sum = 0;
        for (int x = i, travel = 0; x < GRID && travel < WIN_VALUE; x++, travel++) {
            sum += nodeValueAssigner(x, j, travel, prev);
            prev = gameBoardState[x][j];
        }

        prev = "";
        for (int x = i, travel = 0; x > 0 && travel < WIN_VALUE; x--, travel++) {
            sum += nodeValueAssigner(x, j, travel, prev);
            prev = gameBoardState[x][j];
        }
        return sum;
    }

    private int diagonalOne(int i, int j) {
        String prev = "";
        int sum = 0;
        for (int k = 0, travel = 0; i - k >= 0 && j - k >= 0 && travel < WIN_VALUE; k++, travel++) {
            sum += nodeValueAssigner(i - k, j - k, travel, prev);
            prev = gameBoardState[i - k][j - k];
        }
        return sum;
    }

    private int diagonalTwo(int i, int j) {
        String prev = "";
        int sum = 0;
        for (int k = 0, travel = 0; k + i < GRID && k + j < GRID && travel < WIN_VALUE; k++, travel++) {
            sum += nodeValueAssigner(i + k, j + k, travel, prev);
            prev = gameBoardState[i + k][j + k];
        }
        return sum;
    }

    private int diagonalThree(int i, int j) {
        String prev = "";
        int sum = 0;
        for (int k = 0, travel = 0; i + k < GRID && j - k >= 0 && travel < WIN_VALUE; k++, travel++) {
            sum += nodeValueAssigner(i + k, j - k, travel, prev);
            prev = gameBoardState[i + k][j - k];
        }
        return sum;
    }

    private int diagonalFour(int i, int j) {
        String prev = "";
        int sum = 0;
        for (int k = 0, travel = 0; i - k >= 0 && j + k < GRID && travel < WIN_VALUE; k++, travel++) {
            sum += nodeValueAssigner(i - k, j + k, travel, prev);
            prev = gameBoardState[i - k][j + k];
        }
        return sum;
    }

    private int nodeValueAssigner(int i, int j, int x, String s) {
        int sum = 0;
        if (gameBoardState[i][j].equals(EMPTYSTRING)) {
            sum += EMPTY_VALUE;
            if (s.equals(EMPTYSTRING))
                sum += EMPTY_VALUE;
        }
        if (gameBoardState[i][j].equals(PLAYERSTRING)) {
            sum += PLAYER_VALUE;
            if (s.equals(EMPTYSTRING) && x == 1)
                sum += PLAYER_VALUE;
            if (s.equals(PLAYERSTRING))
                sum += PLAYER_VALUE * VALUE_MULTIPLIER;
        }
        if (gameBoardState[i][j].equals(ENEMYSTRING)) {
            sum += OPPONENT_VALUE;
            if (s.equals(EMPTYSTRING) && x == 1)
                sum += OPPONENT_VALUE;
            if (s.equals(ENEMYSTRING))
                sum += OPPONENT_VALUE * VALUE_MULTIPLIER;
        }

        return sum;
    }

}