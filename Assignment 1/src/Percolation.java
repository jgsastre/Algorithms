public class Percolation {
   public Percolation(int N)              // create N-by-N grid, with all sites blocked
   {
	   this.N = N;
	   this.max1D = N*N + 1;
	   this.quickUnion = new WeightedQuickUnionUF(this.max1D + 1);
	   this.openPoints = new boolean[this.max1D + 1];
	   this.openPoints[0] = true;
	   this.openPoints[this.max1D] = true;
   }
   
   public void open(int i, int j)         // open site (row i, column j) if it is not already
   {
	   if (this.isFull(i, j))
	   {
		   int index = this.xyTo1D(i, j);
		   this.openPoints[index] = true;
		   
		   this.connectWithNeighbor(index, i - 1, j);
		   this.connectWithNeighbor(index, i + 1, j);
		   if ( j > 1)
			   this.connectWithNeighbor(index, i, j - 1);
		   if (j < N)
			   this.connectWithNeighbor(index, i, j + 1);
	   }
   }
   
   public boolean isOpen(int i, int j)    // is site (row i, column j) open?
   {
	   this.checkIndices(i, j);
	   return this.openPoints[this.xyTo1D(i, j)];
   }
   
   public boolean isFull(int i, int j)    // is site (row i, column j) full?
   {
	   boolean isOpen = !this.isOpen(i, j);
	   return isOpen && this.quickUnion.connected(this.xyTo1D(i, j), 0);
   }
   
   public boolean percolates()            // does the system percolate?
   {
	   return this.quickUnion.connected(0, this.max1D);
   }
   
   private void checkIndices(int i, int j)
   {
	   if (i <= 0 || i > this.N) throw new IndexOutOfBoundsException("row index i out of bounds");
	   if (j <= 0 || j > this.N) throw new IndexOutOfBoundsException("column index j out of bounds");
   }
   
   private int xyTo1D(int i, int j)
   {
	   return Math.min(Math.max((i - 1)*this.N + j, 0), max1D);
   }
   
   private void connectWithNeighbor(int index, int i, int j)
   {
	   int newIndex = this.xyTo1D(i, j);
	   if (this.openPoints[newIndex])
	   {
		   this.quickUnion.union(index, newIndex);
	   }
   }
   
   private WeightedQuickUnionUF quickUnion;
   private boolean[] openPoints;
   private int N;
   private int max1D;  
}