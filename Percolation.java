import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[] status;
    private boolean[] connectedToTop;         // connected to the top (or not)
    private boolean[] connectedToBottom;      // connected to the bottom (or not)
    private WeightedQuickUnionUF quickUnion;          
    private boolean percolates;               // status of the system
    private int sizeOfArray;                  // size of grid

    public Percolation(int size) {
        if (size > 0) {
            // Initialize working array
            status = new boolean[size*size];
            connectedToTop = new boolean[size*size];
            connectedToBottom = new boolean[size*size];
            percolates = false;
            sizeOfArray = size;
            quickUnion = new WeightedQuickUnionUF(size * size);
        } else {
            // throw exception if sizeOfArray < 1
            throw new IllegalArgumentException();
        }
    }

    public void open(int row, int column) {

        if (!isOpen(row, column)) {
            boolean connectedToTheTop = false;
            boolean connectedToTheBottom = false;
            int actualElementIndex = elementIndex(row, column);
            status[actualElementIndex] = true;

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
                    int topNeighbourRoot = quickUnion.find(topNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[topNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[topNeighbourRoot];
                    // union with upper element if possible
                    quickUnion.union(actualElementIndex, topNeighbourIndex);

                }
            }
                  
            // check if lower element exists
            if (isInBounds(row + 1, column)) {
                // and already open
                if (isOpen(row + 1, column)) {
                    int bottomNeighbourIndex = actualElementIndex + sizeOfArray;
                    int bottomNeighbourRoot = quickUnion.find(bottomNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[bottomNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[bottomNeighbourRoot];
                    // union with upper element if possible
                    quickUnion.union(actualElementIndex, bottomNeighbourIndex);

                }
            }
            // left (exactly like upper and lower)
            if (isInBounds(row, column - 1)) {
                if (isOpen(row, column - 1)) {
                    int leftNeighbourIndex = actualElementIndex - 1;
                    int leftNeighbourRoot = quickUnion.find(leftNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[leftNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[leftNeighbourRoot];
                    quickUnion.union(actualElementIndex, leftNeighbourIndex);

                }
            }
            // right (exactly like upper and lower)
            if (isInBounds(row, column + 1)) {
                if (isOpen(row, column + 1)) {
                    int rightNeighbourIndex = actualElementIndex + 1;
                    int rightNeighbourRoot = quickUnion.find(rightNeighbourIndex);
                    connectedToTheTop = connectedToTheTop || connectedToTop[rightNeighbourRoot];
                    connectedToTheBottom = connectedToTheBottom || connectedToBottom[rightNeighbourRoot];
                    quickUnion.union(actualElementIndex, rightNeighbourIndex);
                }
            } 
            int newRoot = quickUnion.find(actualElementIndex);
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

    public boolean isOpen(int row, int column) {

        boolean result;
        
        if (isInBounds(row, column)) {
            if (status[elementIndex(row, column)]) {
                result = true;
            } else {
                result = false;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
        
        return result;
    }
    
    private int elementIndex(int row, int column) {
        return (row - 1) * sizeOfArray + column - 1;
    }
    
    public boolean isFull(int row, int column) {
        if (!isInBounds(row, column)) {
            throw new IndexOutOfBoundsException();
        }
        return connectedToTop[quickUnion.find(elementIndex(row, column))];
    }
        
    public boolean percolates() {
        return percolates;
    }
    
    public static void main(String[] args) {
    }
}