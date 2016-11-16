/**
 * Created by ludai on 16/11/10.
 */
public class Performance {
    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

}
