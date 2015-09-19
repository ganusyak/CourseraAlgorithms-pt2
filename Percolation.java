import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;                 // stores a state of element : open / closed
    private boolean[] connectedToTop;         // connected to the top (or not)
    private boolean[] connectedToBottom;      // connected to the bottom (or not)
    private WeightedQuickUnionUF qu;          
    private boolean percolates;               // status of the system
    private int sizeOfArray;                  // size of grid

    public Percolation(int size) {
        if (size > 0) {
            // Initialize working array
            grid = new boolean[size][size];
            connectedToTop = new boolean[size*size];
            connectedToBottom = new boolean[size*size];
            percolates = false;
            sizeOfArray = size;
            qu = new WeightedQuickUnionUF(size * size);
        } else {
            // throw exception if sizeOfArray < 1
            throw new IllegalArgumentException();
        }
    }

    public void open(int row, int column) {
        int i = column - 1;
        int j = row - 1;
        if (!isOpen(row, column)) {
            int actualIndex = 0, leftR, rightR,  bottomR;
            boolean connectedToTheTop = false;
            boolean connectedToTheBottom = false;
            boolean leftConnectedT = false, rightConnectedT = false, bottomConnectedT = false, topConnectedT = false;
            boolean leftConnectedB = false, rightConnectedB = false, bottomConnectedB = false, topConnectedB = false;

            grid[i][j] = true;
            int numberInQU = i + j * sizeOfArray;

            if (j == 0) {
                connectedToTheTop = true;  // definitely connected to the top              
            } 

            if (j == sizeOfArray - 1) {
                connectedToTheBottom = true; // // definitely connected to the bottom
            }
            
            // check if upper element exists
            if (isInBoundsOfGrid(i, j - 1)) {
                // and already open
                if (isOpen(row - 1, column)) {
                    int topNeighbourIndex = numberInQU - sizeOfArray;
                    int topNeighbourRoot = qu.find(topNeighbourIndex);
                    topConnectedT = topConnectedT || connectedToTop[topNeighbourRoot];
                    topConnectedB = topConnectedB || connectedToBottom[topNeighbourRoot];
                    // union with upper element if possible
                    qu.union(numberInQU, topNeighbourIndex);

                }
            }
                  
            // check if lower element exists
            if (isInBoundsOfGrid(i, j + 1)) {
                // and already open
                if (isOpen(row + 1, column)) {
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
                if (isOpen(row, column - 1)) {
                    int index2 = numberInQU - 1;
                    leftR = qu.find(index2);
                    leftConnectedT = leftConnectedT || connectedToTop[leftR];
                    leftConnectedB = leftConnectedB || connectedToBottom[leftR];
                    qu.union(numberInQU, index2);

                }
            }
            // right (exactly like upper and lower)
            if (isInBoundsOfGrid(i + 1, j)) {
                if (isOpen(row, column + 1)) {
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
 
    public boolean isOpen(int row, int column) {
        int i = column - 1;
        int j = row - 1;
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
    
    public boolean isFull(int row, int column) {
        int columnIndex = column - 1;
        int rowIndex = row - 1;
        if (!isInBoundsOfGrid(columnIndex, rowIndex)) {
            throw new IndexOutOfBoundsException();
        }
        return connectedToTop[qu.find(rowIndex * sizeOfArray + columnIndex)];
    }
        
    public boolean percolates() {
        return percolates;
    }
    
    public static void main(String[] args) {
    }
}