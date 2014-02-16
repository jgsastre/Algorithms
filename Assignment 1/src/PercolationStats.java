public class PercolationStats {
    private static final double NORM_PERCENTIL = 1.96;
    private double meanValue;
    private double stdevValue;
    private int T;
    
    public PercolationStats(int N, int T)
    {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException("Invalid params");
        
        this.T = T;
        int points = N*N;
        double[] samples = new double[T];
        for (int sim = 0; sim < T; ++sim)
        {
            int attempt = 0;
            Percolation percolation = new Percolation(N);
            while (!percolation.percolates() && attempt < points)
            {
                int i, j;
                do {
                    i = StdRandom.uniform(1, N + 1);
                    j = StdRandom.uniform(1, N + 1);
                } while (percolation.isOpen(i, j));
                percolation.open(i, j);
                ++attempt;
            }
            
            samples[sim] = ((double) attempt)/points;
        }

        this.meanValue = StdStats.mean(samples);
        this.stdevValue = Double.NaN;
        if (T > 1)
            this.stdevValue = StdStats.stddev(samples);
    }

    public double mean()
    {
        return this.meanValue;
    }

    public double stddev()
    {
        return this.stdevValue;
    }

    public double confidenceLo()
    {
        return this.mean() - NORM_PERCENTIL*this.stddev()/Math.sqrt(this.T);
    }

    public double confidenceHi()
    {
        return this.mean() + NORM_PERCENTIL*this.stddev()/Math.sqrt(this.T);
    }

    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println(String.format("%24s = %f", "mean", stats.mean()));
        StdOut.println(String.format("%24s = %f", "stddev", stats.stddev()));
        StdOut.println(String.format("%24s = %f, %f", "95% confidence interval", 
                stats.confidenceLo(), stats.confidenceHi()));
    }
}