import java.util.Arrays;

class CookTest {

    private static final int A = 1;
    private static final int B = 2;
    private static final int C = 3;
    private static final int D = 4;
    private static final int E = 5;

    private static final int MAZE[][] = {
        { A, B, A, A, A, A, A, A, A, A, A, A },
        { A, C, A, D, D, E, A, C, C, C, D, A },
        { A, C, C, D, A, E, A, D, A, D, A, A },
        { A, A, A, A, A, E, D, D, A, D, E, A },
        { A, C, C, D, D, D, A, A, A, A, E, A },
        { A, C, A, A, A, A, A, D, D, D, E, A },
        { A, D, D, D, E, E, A, C, A, A, A, A },
        { A, A, A, E, A, E, A, C, C, D, D, A },
        { A, D, E, E, A, D, A, A, A, A, A, A },
        { A, A, D, A, A, D, A, C, D, D, A, A },
        { A, D, D, D, A, D, C, C, A, D, E, B },
        { A, A, A, A, A, A, A, A, A, A, A, A }
    };

    private static final int GOAL = B;
    private static final int MAXSTEPS = 2; //val -1, cos I start counting in 0.
    private static final int STARTX = 0;
    private static final int STARTY = 1;

    public static void main(final String args[]) {
        final long start = System.currentTimeMillis();
        final Node<Step> root = setup();
        deepExplore(root);
        find(root);
        final long end = System.currentTimeMillis();
        System.out.println("\n Took: " + (end - start) + "ms");
    }

    static Node<Step> setup() {
        final Step start = new Step();
        start.val = B;
        start.cant = MAXSTEPS;
        start.x = STARTX;
        start.y = STARTY;
        start.end = false;
        start.path = initializePathMatrix();
        start.path[STARTX][STARTY] = GOAL;

        return new Node<>(start);
    }

    //TODO mejorar a una busqueda que retorne una lista con todos los nodos solucion
    private static boolean find(final Node<Step> node) {
        boolean res = false;
        if (node.getData().val == B && node.getData().x != STARTX && node.getData().y != STARTY) {
            System.out.println("\n");
            System.out.println("Solution #" + node.getData().path.hashCode());
            printSolution(node.getData().path);
            return true;
        } else {
            for (Node<Step> child : node.getChildren()) {
                if (find(child)) {
                    res = true;
                }
            }
        }
        return res;
    }

    static void deepExplore(final Node<Step> node) {

        //TODO could be rewritten in a millon different ways
        if (node.getData().cant != MAXSTEPS) {
            if (canGoDown(node, true)) {
                deepExplore(move(node, node.getData().x + 1, node.getData().y));
            }
            if (canGoUp(node, true)) {
                deepExplore(move(node, node.getData().x - 1, node.getData().y));
            }

            if (canGoRight(node, true)) {
                deepExplore(move(node, node.getData().x, node.getData().y + 1));
            }
            if (canGoLeft(node, true)) {
                deepExplore(move(node, node.getData().x, node.getData().y - 1));
            }
        } else {
            if (canGoDown(node, false)) {
                deepExplore(jump(node, node.getData().x + 1, node.getData().y));
            }
            if (canGoUp(node, false)) {
                deepExplore(jump(node, node.getData().x - 1, node.getData().y));
            }

            if (canGoRight(node, false)) {
                deepExplore(jump(node, node.getData().x, node.getData().y + 1));
            }
            if (canGoLeft(node, false)) {
                deepExplore(jump(node, node.getData().x, node.getData().y - 1));
            }
        }
    }

    //TODO refactor with nodeFactory
    static Node<Step> move(final Node<Step> node, final int newx, final int newy) {
        final Step start = new Step();
        //TODO getters setter and constructor
        start.val = node.getData().val;
        start.cant = node.getData().cant + 1;
        start.x = newx;
        start.y = newy;
        start.end = false;
        int sol[][] = Arrays.stream(node.getData().path).map(int[]::clone).toArray(int[][]::new);
        sol[newx][newy] = MAZE[newx][newy];
        start.path = sol;
        final Node newnode = new Node<Step>(start);
        node.addChild(newnode);

        return newnode;
    }

    //TODO refactor with nodeFactory
    static Node<Step> jump(final Node<Step> node, final int newx, final int newy) {
        final Step start = new Step();
        start.val = MAZE[newx][newy];
        start.cant = 0;
        start.x = newx;
        start.y = newy;
        if (MAZE[newx][newy] == GOAL) {
            start.end = true;
        } else {
            start.end = false;
        }
        final int sol[][] = Arrays.stream(node.getData().path).map(int[]::clone).toArray(int[][]::new);
        sol[start.x][start.y] = MAZE[newx][newy];
        start.path = sol;
        final Node newnode = new Node<>(start);
        node.addChild(newnode);

        return newnode;
    }

    //TODO refactor with DIRECTION as enum and single function
    // HYPER-EDITION: direction vector for nDimentionMaze
    static boolean canGoUp(final Node<Step> node, final boolean sameVal) {
        return node.getData().x - 1 >= 0 && node.getData().x - 1 < MAZE.length
            && node.getData().y >= 0 && node.getData().y < MAZE.length
            && node.getData().path[node.getData().x - 1][node.getData().y] == 0
            && (!(MAZE[node.getData().x - 1][node.getData().y] == node.getData().val) ^ sameVal);
    }

    static boolean canGoDown(final Node<Step> node, final boolean sameVal) {
        return node.getData().x + 1 >= 0 && node.getData().x + 1 < MAZE.length
            && node.getData().y >= 0 && node.getData().y < MAZE.length
            && node.getData().path[node.getData().x + 1][node.getData().y] == 0
            && (!(MAZE[node.getData().x + 1][node.getData().y] == node.getData().val) ^ sameVal);
    }

    static boolean canGoRight(final Node<Step> node, final boolean sameVal) {
        return node.getData().x >= 0 && node.getData().x < MAZE.length
            && node.getData().y + 1 >= 0 && node.getData().y + 1 < MAZE.length
            && node.getData().path[node.getData().x][node.getData().y + 1] == 0
            && (!(MAZE[node.getData().x][node.getData().y + 1] == node.getData().val) ^ sameVal);
    }

    static boolean canGoLeft(final Node<Step> node, final boolean sameVal) {
        return node.getData().x >= 0 && node.getData().x < MAZE.length
            && node.getData().y - 1 >= 0 && node.getData().y - 1 < MAZE.length
            && node.getData().path[node.getData().x][node.getData().y - 1] == 0
            && (!(MAZE[node.getData().x][node.getData().y - 1] == node.getData().val) ^ sameVal);
    }

    //TODO Could go in a utils
    static void printSolution(final int sol[][]) {
        for (int i = 0; i < MAZE.length; i++) {
            for (int j = 0; j < MAZE.length; j++) {
                System.out.print(" " + translateNumber(sol[i][j]) + " ");
            }
            System.out.println();
        }
    }

    //TODO puedo mover esto a un utils
    static String translateNumber(final int foor) {
        switch (foor) {
        case 1:
            return "A";
        case 2:
            return "B";
        case 3:
            return "C";
        case 4:
            return "D";
        case 5:
            return "E";
        }
        return "_";
    }

    //TODO puedo mover esto a un utils
    static int[][] initializePathMatrix() {
        int sol[][] = new int[MAZE.length][MAZE.length];

        for (int i = 0; i < sol.length; i++) {
            for (int j = 0; j < sol[i].length; j++) {
                sol[i][j] = 0;
            }
        }
        return sol;
    }
}
