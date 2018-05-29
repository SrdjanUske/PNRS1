package rtrk.pnrs.jniexample;


public class FibonacciNative {

    public native int get(int n);

    static {
        System.loadLibrary("fibonacci");
    }
}
