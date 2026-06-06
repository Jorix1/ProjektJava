import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    final private List<String> algo = Arrays.asList("fuchtermann", "tutte'str1","spectral" );
    private double hooksConst = -0.1;
    private double coulombaConst = 1.5;
    private int iterations;
    private int maxXSize;
    private int maxYSize;
    private String algoName; // from usr

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
    public void setCoulombaConst(double coulombaConst) {
        if(coulombaConst == 0){
            System.out.println("coulombaConst can't be  zero and must be positive number");

        } else if (coulombaConst < 0) {
            this.coulombaConst = -(coulombaConst);

        }
        this.coulombaConst = coulombaConst;
    }
    public void setHooksConst(double hooksConst) {
        if(hooksConst == 0) {
            System.err.println("hooksConst cant be equal 0 and it must be negative real number");

        } else if (hooksConst > 0) {
            this.hooksConst = -(hooksConst);
        }
        this.hooksConst = hooksConst;
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
    public double getHooksConst(){return hooksConst;}
    public double getCoulombaConst(){return coulombaConst;}






}
