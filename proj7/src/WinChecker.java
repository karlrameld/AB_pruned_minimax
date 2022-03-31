  
public class WinChecker extends Game {

    public WinChecker(){}
    
    public boolean evaluateWin(String[][] board) {
        if (winConditionMet(PLAYERSTRING, board) || winConditionMet(ENEMYSTRING, board))
            return true;
        if (isFull(board)) {
            return true;
        }
        return false;
    }

    public boolean winConditionMet(String s, String[][] temp) {
        for (int i = 0; i < GRID; i++) {
            if (straightWinChecker(i, s, temp)) {
                return true;
            }
            for (int j = 0; j < GRID; j++) {
                if (diagonalWinChecker(s, i, j, temp)) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean straightWinChecker(int i, String s, String[][] temp) {
        if (horizontalWinChecker(i, s, temp) || verticalWinChecker(i, s, temp))
            return true;
        return false;
    }

    private boolean horizontalWinChecker(int i, String s, String[][] temp) {
        for (int j = 4; j < GRID; j++) {
            if (temp[i][j - 4].equals(s) && temp[i][j - 3].equals(s) && temp[i][j - 2].equals(s)
                    && temp[i][j - 1].equals(s) && temp[i][j].equals(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean verticalWinChecker(int i, String s, String[][] temp) {
        for (int j = 4; j < GRID; j++) {
            if (temp[j - 4][i].equals(s) && temp[j - 3][i].equals(s) && temp[j - 2][i].equals(s)
                    && temp[j - 1][i].equals(s) && temp[j][i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean diagonalWinChecker(String s, int i, int j, String[][] temp) {
        if (diagonalWinCheckerDown(s, i, j, temp)) {
            return true;
        }
        if (diagonalWinCheckerUp(s, i, j, temp)) {
            return true;
        }
        return false;
    }

    private boolean diagonalWinCheckerDown(String s, int i, int j, String[][] temp) {
        int found = 0;
        for (int k = 0; k + i < GRID && k + j < GRID; k++) {
            if (temp[i + k][j + k].equals(s))
                found++;
            else {
                break;
            }
        }
        for (int k = 0; i - k >= 0 && j - k >= 0; k++) {
            if (temp[i - k][j - k].equals(s))
                found++;
            else {
                break;
            }
        }

        if (found == WIN_VALUE + 1)
            return true;
        return false;
    }

    private boolean diagonalWinCheckerUp(String s, int i, int j, String[][] temp) {
        int found = 0;
        for (int k = 0; k + i < GRID && j - k >= 0; k++) {
            if (temp[i + k][j - k].equals(s))
                found++;
            else {
                break;
            }
        }
        for (int k = 0; i - k >= 0 && k + j < GRID; k++) {
            if (temp[i - k][j + k].equals(s))
                found++;
            else {
                break;
            }
        }
        if (found == WIN_VALUE + 1)
            return true;
        return false;
    }
}
