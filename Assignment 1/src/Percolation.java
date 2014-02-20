public class Percolation {
    private WeightedQuickUnionUF quickUnion;
    private boolean[] openPoints;
    private boolean[] bottomConnectedPoints;
    private int N;
    private int max1D;  
    
    public Percolation(int N)
    {
        this.N = N;
        this.max1D = N*N;
        this.quickUnion = new WeightedQuickUnionUF(this.max1D + 1);
        this.openPoints = new boolean[this.max1D + 1];
        this.bottomConnectedPoints = new boolean[this.max1D + 1];
        this.openPoints[0] = true;
    }

    public void open(int i, int j)
    {
        if (!this.isOpen(i, j))
        {
            int index = this.xyTo1D(i, j);
            this.openPoints[index] = true;

            if (i == N)
                // Suppose find(index(i, j)) = index(i, j)
                this.bottomConnectedPoints[index] = true;
            else
                this.connectWithNeighbor(index, i + 1, j);
            this.connectWithNeighbor(index, i - 1, j);
            if (j > 1)
                this.connectWithNeighbor(index, i, j - 1);
            if (j < N)
                this.connectWithNeighbor(index, i, j + 1);
        }
    }

    public boolean isOpen(int i, int j)
    {
        this.checkIndices(i, j);
        return this.openPoints[this.xyTo1D(i, j)];
    }

    public boolean isFull(int i, int j)
    {
        return this.isOpen(i, j) && this.quickUnion.connected(this.xyTo1D(i, j), 0);
    }

    public boolean percolates()
    {
        return this.bottomConnectedPoints[this.quickUnion.find(0)];
    }

    private void checkIndices(int i, int j)
    {
        if (i <= 0 || i > this.N) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > this.N) 
            throw new IndexOutOfBoundsException("column index j out of bounds");
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
            int rootId = quickUnion.find(newIndex);
            boolean bottomConnected = this.bottomConnectedPoints[index];
            bottomConnected = bottomConnected || this.bottomConnectedPoints[rootId];
            this.quickUnion.union(index, newIndex);
            this.bottomConnectedPoints[rootId] = bottomConnected;
            this.bottomConnectedPoints[index] = bottomConnected;
        }
    }
}