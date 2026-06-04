import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    final private List<String> algo = Arrays.asList("fuchtermann", "tutte'str1","spectral" );

    private int iterations;
    private int maxXSize;
    private int maxYSize;
    private String algoName; // from usr
    private boolean outputTxt;

    public Config(String algoName ,int iterations, int maxXSize, int maxYSize) {
        this.algoName = algoName;
        this.iterations = iterations;
        this.maxXSize = maxXSize;
        this.maxYSize = maxYSize;
    }
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    public void setMaxXSize(int maxXSize) {
        this.maxXSize = maxXSize;
    }
    public void setMaxYSize(int maxYSize) {
        this.maxYSize = maxYSize;
    }

    public int getIterations() {
        return iterations;
    }
    public int getMaxXSize() {
        return maxXSize;
    }
    public int getMaxYSize() {
        return maxYSize;
    }







}
