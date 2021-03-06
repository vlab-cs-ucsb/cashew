package challenge2hack.util;

public class ModLinearHash extends AbstractBoundedUniformHash
{
    private static long HASH_STATE_INIT;
    private static long HASH_PARAM_A0;
    private static long HASH_PARAM_A1;
    private static long HASH_PARAM_AF;
    private static long HASH_PARAM_M0;
    private static long HASH_PARAM_M1;
    private static long HASH_PARAM_MF;
    protected long mHashState;
    
    public void reset() {
        this.mHashState = ModLinearHash.HASH_STATE_INIT;
    }
    
    public void update(final long n) {
//    	System.out.println("Updating hash with: " + n);	
        this.mHashState += (n + ModLinearHash.HASH_PARAM_A0) * ModLinearHash.HASH_PARAM_M0;
        this.mHashState = (this.mHashState + ModLinearHash.HASH_PARAM_A1) * ModLinearHash.HASH_PARAM_M1;
    }
    
    public long getHash() {
        long mLowerBound;
        long mUpperBound;
        long n;
        long n2;
        if (this.mLowerBound == this.mUpperBound) {
            mLowerBound = 0L;
            mUpperBound = 0L;
            n = this.mLowerBound;
            n2 = 0L;
        }
        else {
            if (this.mLowerBound > this.mUpperBound) {
                throw new RuntimeException(new StringBuilder().append("invalid bounds: [").append(this.mLowerBound).append(", ").append(this.mUpperBound).append("]").toString());
            }
            if (this.mLowerBound >= 0L) {
                mLowerBound = 0L;
                mUpperBound = this.mUpperBound - this.mLowerBound;
                n = this.mLowerBound;
                n2 = (Long.highestOneBit(mUpperBound) << 1) - 1L;
            }
            else if (this.mUpperBound < 0L) {
                mLowerBound = this.mLowerBound - this.mUpperBound - 1L;
                mUpperBound = -1L;
                n = this.mUpperBound + 1L;
                n2 = (Long.highestOneBit(~mLowerBound) << 1) - 1L;
            }
            else {
                mLowerBound = this.mLowerBound;
                mUpperBound = this.mUpperBound;
                n = 0L;
                final long max = Math.max(Long.highestOneBit(~mLowerBound), Long.highestOneBit(mUpperBound));
                if (max == 0L) {
                    n2 = 0L;
                }
                else {
                    n2 = (max << 1) - 1L;
                }
            }
        }
        long n3;
        do {
            this.mHashState = (this.mHashState + ModLinearHash.HASH_PARAM_AF) * ModLinearHash.HASH_PARAM_MF;
            if (this.mHashState >= 0L) {
                n3 = (this.mHashState & n2);
            }
            else {
                n3 = (this.mHashState | ~n2);
            }
        } while (n3 < mLowerBound || n3 > mUpperBound);
        final long n4 = n3 + n;
        this.reset();
        return n4;
    }
    
    static {
        ModLinearHash.HASH_STATE_INIT = 3453881378648555325L;
        ModLinearHash.HASH_PARAM_A0 = -195385520352475319L;
        ModLinearHash.HASH_PARAM_A1 = -2660630309253685457L;
        ModLinearHash.HASH_PARAM_AF = -1138441404092675177L;
        ModLinearHash.HASH_PARAM_M0 = -4144643519569966049L;
        ModLinearHash.HASH_PARAM_M1 = -4394632577027451953L;
        ModLinearHash.HASH_PARAM_MF = -1428682232105743577L;
    }
}
