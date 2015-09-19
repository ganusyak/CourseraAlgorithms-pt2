import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
public class PercolationStats {
   private int numberOfExperiments;
   private int sizeOfGrid;
   private double[] statResults;
   private double mean;
   private double stddev;
   private double confidenceLo;
   private double confidenceHi;
    
    
   public PercolationStats(int N, int T) {
       if ((N < 1) || (T < 1)) {
           throw new IllegalArgumentException();
       } else {
           numberOfExperiments = T;
           sizeOfGrid = N;
           statResults = new double[numberOfExperiments];
           doExperiment();
       }
   }
   
   private void doExperiment() {
       for (int i = 0; i < this.numberOfExperiments; i++) {
           int counter = 0;
           Percolation percolation = new Percolation(getSizeOfGrid());
           while (!percolation.percolates()) {

               int randomX = StdRandom.uniform(1, getSizeOfGrid() + 1);
               int randomY = StdRandom.uniform(1, getSizeOfGrid() + 1);
               if (!percolation.isOpen(randomX, randomY)) {
                   counter++;
                   percolation.open(randomX, randomY);
               }
           }

           statResults[i] = (double) counter / (double) (getSizeOfGrid() * getSizeOfGrid());

       }
             
       setMean(StdStats.mean(statResults));
       
       setStddev(StdStats.stddev(statResults));
       
       confidenceLo = mean - 1.96 * stddev / Math.sqrt(numberOfExperiments);
       confidenceHi = mean + 1.96 * stddev / Math.sqrt(numberOfExperiments);
       
   }
   
   public double stddev() {
       return stddev;
   }
   
   private void setStddev(double deviation) {
       stddev = deviation;
   }
   
   public double mean() {
       return mean;
   }
   
   private void setMean(double meanValue) {
       mean = meanValue;
   }
   
   private int getSizeOfGrid() {
       return sizeOfGrid;
   }
// perform T independent experiments on an N-by-N grid
   //public double mean()                      // sample mean of percolation threshold

   public double confidenceLo() {
       return confidenceLo;
   }

   public double confidenceHi() {
       return confidenceHi;
   }           // high endpoint of 95% confidence interval

   public static void main(String[] args) {
       System.out.println("Input size of grid:");

       int size = StdIn.readInt();
       System.out.println("Input number of experiments:");
       int number = StdIn.readInt();
       PercolationStats ps = new PercolationStats(size, number);
       System.out.println("Mean: " + ps.mean());
       System.out.println("stddev: " + ps.stddev());
       System.out.println("95% confidence interval: " + ps.confidenceLo() + ", " + ps.confidenceHi());
   }    
}
