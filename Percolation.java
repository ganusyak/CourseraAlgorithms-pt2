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
            boolean connectedToTheTop = false;
            boolean connectedToTheBottom = false;
            grid[i][j] = true;
            int actualElementIndex = i + j * sizeOfArray;

            if (row == 1) {
                connectedToTheTop = true;  // definitely connected to the top              
            } 

            if (row == sizeOfArray) {
                connectedToTheBottom = true; // // definitely connected to the bottom
            }
            
            // check if upper element exists
            if (isInBounds(row - 1, column)) {
                // and already open
                if (isOpen(row - 1, column)) {
                    int topNeighbourIndex = actualElementIndex - sizeOfArray;
                    int topNeighbourRoot = qu.find(topNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[topNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[topNeighbourRoot];
                    // union with upper element if possible
                    qu.union(actualElementIndex, topNeighbourIndex);

                }
            }
                  
            // check if lower element exists
            if (isInBounds(row + 1, column)) {
                // and already open
                if (isOpen(row + 1, column)) {
                    int bottomNeighbourIndex = actualElementIndex + sizeOfArray;
                    int bottomNeighbourRoot = qu.find(bottomNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[bottomNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[bottomNeighbourRoot];
                    // union with upper element if possible
                    qu.union(actualElementIndex, bottomNeighbourIndex);

                }
            }
            // left (exactly like upper and lower)
            if (isInBounds(row, column - 1)) {
                if (isOpen(row, column - 1)) {
                    int leftNeighbourIndex = actualElementIndex - 1;
                    int leftNeighbourRoot = qu.find(leftNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[leftNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[leftNeighbourRoot];
                    qu.union(actualElementIndex, leftNeighbourIndex);

                }
            }
            // right (exactly like upper and lower)
            if (isInBounds(row, column + 1)) {
                if (isOpen(row, column + 1)) {
                    int rightNeighbourIndex = actualElementIndex + 1;
                    int rightNeighbourRoot = qu.find(rightNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[rightNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[rightNeighbourRoot];
                    qu.union(actualElementIndex, rightNeighbourIndex);
                }
            } 
            int newRoot = qu.find(actualElementIndex);
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
    
    private boolean isInBounds(int row, int column) {
        boolean result = true;

        if ((row < 1) || (row > sizeOfArray()) || (column < 1) || (column > sizeOfArray())) {
            result = false;        
        } 
        return result;
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