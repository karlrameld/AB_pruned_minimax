import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class Game {
    private Scanner scanner = new Scanner(System.in);
    private String errorString = "*Directed Profanity*! You cant play there!";
    protected final String EMPTYSTRING = "-";
    protected final String PLAYERSTRING = "X";
    protected final String ENEMYSTRING = "O";
    protected final int GRID = 15; // IMPORTANT! THIS NEEDS TO BE AN ODD NUMBER!
    protected final int EMPTY_VALUE = 0;
    protected final int PLAYER_VALUE = -1;
    protected final int OPPONENT_VALUE = 1;
    protected final int PLAYER_WIN = -100000;
    protected final int OPPONENT_WIN = 100000;
    protected final int WIN_VALUE = 5;
    private String[][] gameArray = new String[GRID][GRID];
    private boolean gameOver = false;
    private final int DEPTH_VALUE = 4;

    public void run() {
        initGameWindow();
        while (!gameOver) {
            gameLoop();
        }
        printGameWindow();
        scanner.close();
    }

    private void initGameWindow() {
        for (int i = 0; i < GRID; i++) {
            String add = "-";
            for (int j = 0; j < GRID; j++) {
                gameArray[i][j] = add;
            }
        }
    }

    private void gameLoop() {
        WinChecker winChecker = new WinChecker();
        if (!winChecker.evaluateWin(gameArray))
            opponentsTurn();
        else {
            gameOver = true;
            return;
        }
        if (!winChecker.evaluateWin(gameArray))
            playerTurn();
        else
            gameOver = true;
    }

    private void playerTurn() { 
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        boolean occupied = true;
        System.out.println("Your turn!");
        while (occupied) {
            printGameWindow();
            System.out.println("Where would you like to play? ");
            System.out.print("Vertical: ");
            if (scanner.hasNextInt())
                x = scanner.nextInt() - 1;
            else {
                System.out.println(errorString);
                scanner.nextLine();
                scanner.next();
                continue;
            }
            if (x >= GRID || x < 0) {
                System.out.println(errorString);
                continue;
            }
            System.out.print("Horizontal: ");
            if (scanner.hasNextInt())
                y = scanner.nextInt() - 1;
            else {
                System.out.println(errorString);
                scanner.nextLine();
                scanner.next();
                continue;
            }
            if (y >= GRID || y < 0) {
                System.out.println(errorString);
                continue;
            }
            if (!gameArray[x][y].equals(EMPTYSTRING)) {
                System.out.println(errorString);
                continue;
            }
            if (!hasAdjacent(x, y)) {
                System.out.println(errorString);
                continue;
            }
            occupied = false;
            gameArray[x][y] = PLAYERSTRING;

        }
    }

    private void opponentsTurn() {
        System.out.println("Opponents Turn!");
        Instant start = Instant.now();
        Move m = findBestMove();
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));
        gameArray[m.getX()][m.getY()] = ENEMYSTRING;
    }

    private Move findBestMove() {
        Move move = null;
        int max = Integer.MIN_VALUE;
        for (int x = 0; x < GRID; x++) {
            for (int y = 0; y < GRID; y++) {
                if (gameArray[x][y].equals(EMPTYSTRING) && hasAdjacent(x, y)) {
                    gameArray[x][y] = ENEMYSTRING;
                    int temp = miniMax(false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    gameArray[x][y] = EMPTYSTRING;
                    if (max < temp) {
                        max = temp;
                        move = new Move(x, y);
                    }
                }
            }
        }
        if (move == null)
            move = new Move((GRID - 1) / 2, (GRID - 1) / 2);
        return move;
    }

    /**
     * Plays recursivly against itself until a terminal state is reached, returns the board value for that boardstate.
     * Uses Alpha beta pruning to provide a terminal state early if Alpha value is lower than Beta, ie
     * if the AIs move has a lower board value than the player which means that we are unlikely to find a 
     * favorable boardstate for the current player if we keep iterating on the current state. 
     * 
     * @param isAI  : if true: returns the maximum board value, else returns the
     *              minimum;
     * @param depth : DEPTH_VALUE - depth number of iterations will be 
     *                carried out before a terminal state is reached
     * 
     * @param alpha : should always be set as low as possilbe, pref
     *              Integer.MIN_VALUE;
     * @param beta  : should always be set as high as possible, pref
     *              Integer.MAX_VALUE;
     *
     * @return maximum or minimum value of the board depending on if isAI = true or
     *         false.
     */
    private int miniMax(boolean isAI, int depth, int alpha, int beta) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int value = evalBoard(depth);
        if (isFull(gameArray) || depth == DEPTH_VALUE || Math.abs(value) > 1000) {
            return value;
        } else {
            for (int x = 0; x < GRID && beta > alpha; x++) {
                for (int y = 0; y < GRID && beta > alpha; y++) {
                    if (gameArray[x][y].equals(EMPTYSTRING) && hasAdjacent(x, y)) {
                        if (isAI) {
                            gameArray[x][y] = ENEMYSTRING;
                            max = Math.max(max, miniMax(false, depth + 1, alpha, beta));
                            gameArray[x][y] = EMPTYSTRING;
                            alpha = Math.max(alpha, max);
                        } else {
                            gameArray[x][y] = PLAYERSTRING;
                            min = Math.min(min, miniMax(true, depth + 1, alpha, beta));
                            gameArray[x][y] = EMPTYSTRING;
                            beta = Math.min(beta, min);
                        }
                    }
                }
            }
        }
        if (isAI)
            return max;
        else
            return min;
    }

    private int evalBoard(int pathLength) {
        WinChecker w = new WinChecker();
        if (w.winConditionMet(ENEMYSTRING, gameArray))
            return OPPONENT_WIN - pathLength;
        if (w.winConditionMet(PLAYERSTRING, gameArray))
            return PLAYER_WIN + pathLength;
        GameState state = new GameState(gameArray);
        return state.getBoardValue();
    }

    private void printGameWindow() {
        for (int i = 0; i < GRID; i++) {
            String row = "";
            for (int j = 0; j < GRID; j++) {
                row = row + " | " + gameArray[i][j];
            }
            System.out.println(row + " | ");
        }
    }

    protected boolean isFull(String[][] temp) {
        for (int i = 0; i < GRID; i++) {
            for (int j = 0; j < GRID; j++) {
                if (temp[i][j].equals(EMPTYSTRING))
                    return false;
            }
        }
        return true;
    }

    private boolean hasAdjacent(int x, int y) {
        int startX = x - 1;
        int startY = y - 1;
        int stopX = x + 1;
        int stopY = y + 1;
        if (x == 0)
            startX++;
        if (x == GRID - 1)
            stopX--;
        if (y == 0)
            startY++;
        if (y == GRID - 1)
            stopY--;
        for (int a = startX; a <= stopX; a++)
            for (int b = startY; b <= stopY; b++) {
                if (!gameArray[a][b].equals(EMPTYSTRING))
                    return true;
            }

        return false;
    }
}
