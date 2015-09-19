import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private boolean[] connectedToTop;
    private boolean[] connectedToBottom;
    private WeightedQuickUnionUF qu;
    private boolean percolates; 
    private int sizeOfArray;

    public Percolation(int size) {
        if (size > 0) {
            // Initialize working array
            grid = new boolean[size][size];
            connectedToTop = new boolean[size*size];
            connectedToBottom = new boolean[size*size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    grid[i][j] = false;
                }
            }
            percolates = false;
            sizeOfArray = size;
            qu = new WeightedQuickUnionUF(size * size);
        } else {
            // throw exception if sizeOfArray < 1
            throw new IllegalArgumentException();
        }
    }

    public void open(int i, int j) {
        openPrivate(j - 1, i - 1);
    }    
  
    private void openPrivate(int i, int j) {
        if (!isOpenPrivate(i, j)) {
            int actualIndex = 0, leftR = 0, rightR = 0, upperR = 0, bottomR = 0;
            boolean connectedToTheTop = false;
            boolean connectedToTheBottom = false;
            boolean leftConnectedT = false, rightConnectedT = false, bottomConnectedT = false, topConnectedT = false;
            boolean leftConnectedB = false, rightConnectedB = false, bottomConnectedB = false, topConnectedB = false;

            grid[i][j] = true;
            int numberInQU = i + j * sizeOfArray;

            if (j == 0) {
                connectedToTheTop = true;                
            } 

            if (j == sizeOfArray - 1) {

                connectedToTheBottom = true;
            }
            
            // check if upper element exists
            if (isInBoundsOfGrid(i, j - 1)) {
                // and already open
                if (isOpenPrivate(i, j - 1)) {
                    int index2 = numberInQU - sizeOfArray;
                    
                    upperR = qu.find(index2);
                    topConnectedT = topConnectedT || connectedToTop[upperR];
                    topConnectedB = topConnectedB || connectedToBottom[upperR];
                    // union with upper element if possible
                    qu.union(numberInQU, index2);

                }
            }
                  
            // check if lower element exists
            if (isInBoundsOfGrid(i, j + 1)) {
                // and already open
                if (isOpenPrivate(i, j + 1)) {
                    int index2 = numberInQU + sizeOfArray;
                    bottomR = qu.find(index2);
                    bottomConnectedT = bottomConnectedT || connectedToTop[bottomR];
                    bottomConnectedB = bottomConnectedB || connectedToBottom[bottomR];
                    // union with upper element if possible
                    qu.union(numberInQU, index2);

                }
            }
            // left (exactly like upper and lower)
            if (isInBoundsOfGrid(i - 1, j)) {
                if (isOpenPrivate(i - 1, j)) {
                    int index2 = numberInQU - 1;
                    leftR = qu.find(index2);
                    leftConnectedT = leftConnectedT || connectedToTop[leftR];
                    leftConnectedB = leftConnectedB || connectedToBottom[leftR];
                    qu.union(numberInQU, index2);

                }
            }
            // right (exactly like upper and lower)
            if (isInBoundsOfGrid(i + 1, j)) {
                if (isOpenPrivate(i + 1, j)) {
                    int index2 = numberInQU + 1;
                    rightR = qu.find(index2);
                    rightConnectedT = rightConnectedT || connectedToTop[rightR];
                    rightConnectedB = rightConnectedB || connectedToBottom[rightR];
                    qu.union(numberInQU, index2);

                }
            } 
            connectedToTheTop = connectedToTheTop || topConnectedT || bottomConnectedT || rightConnectedT || leftConnectedT;
            connectedToTheBottom = connectedToTheBottom || topConnectedB || bottomConnectedB || rightConnectedB || leftConnectedB;
            int newRoot = qu.find(numberInQU);

            connectedToTop[newRoot] = connectedToTheTop;
            connectedToBottom[newRoot] = connectedToTheBottom;
            if (connectedToTheTop && connectedToTheBottom) {
                percolates = true;
            }
        }
    }
    
    private int sizeOfArray() {
        return sizeOfArray;
    }    
    
    private boolean isInBoundsOfGrid(int i, int j) {
        boolean result = true;
        int maxIndex = sizeOfArray() - 1;
        if ((i < 0) || (i > maxIndex) || (j < 0) || (j > maxIndex)) {
            result = false;        
        } 
        return result;
    }
 
    public boolean isOpen(int i, int j) {
        return isOpenPrivate(j - 1, i - 1);
    }
    
    private boolean isOpenPrivate(int i, int j) {
        boolean result;
        
        if (isInBoundsOfGrid(i, j)) {
            if (grid[i][j]) {
                result = true;
            } else {
                result = false;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
        return result;
    }
    
    public boolean isFull(int i, int j) {
        if (!isInBoundsOfGrid(i - 1, j - 1)) {
            throw new IndexOutOfBoundsException();
        }
        int row = i - 1;
        int column = j - 1;
        return connectedToTop[qu.find(row * sizeOfArray + column)];
    }
        
    public boolean percolates() {
        return percolates;
    }
    
    public static void main(String[] args) {
    }
}