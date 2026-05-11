import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    final private List<String> algo = Arrays.asList("fuchtermann", "tutte'str1","spectral" );

    private int iterations;
    private int maxXSize;
    private int maxYSize;
    private String algoName; // from usr
    private boolean outText;

    public Config(String algoName ,int iterations, int maxXSize, int maxYSize, boolean outText) {
        this.algoName = algoName;
        this.iterations = iterations;
        this.maxXSize = maxXSize;
        this.maxYSize = maxYSize;
        this.outText = outText;
    }
    public Config(){
        this.algoName = algo.get(1); // tutte jest najszybszy to będzie default
        this.iterations = 500;
        this.maxXSize = 200; // wartość dodatnia i ujemna
        this.maxYSize = 200; // to samo
    }
    public Config(String [] args){
        String name = "";
        for(String s : args){
            name += s;
        }
        String regex = "-i\\s+([\\w.-]+)\\s+-o\\s+([\\w.-]+)\\s+-a\\s+(\\w+)(?:\\s+-b)?";
        Pattern patern = Pattern.compile(regex);
        Matcher matcher = patern.matcher(name);
        if(matcher.find()){
            this.algoName = matcher.group(1);
        }
    }

    private int LevDist(String str1, String str2, int m, int n) {

        if(m == 0){
            return str2.length();
        }
        if (n == 0) {
            return str1.length();
        }
        if(str1.charAt(m-1) == str2.charAt(n-1)) {
            return LevDist(str1, str2, m-1, n-1);
        }
        return 1 + Math.min(LevDist(str1, str2, m, n-1 ),
                Math.min(LevDist(str1, str2, m, n-1 ), LevDist(str1, str2, m-1, n-1)));
    }
//    public void chosenAlgo(String name){ Może się to wstawi do main jak będzie potrzebne !!!!!
//        String bestAlgo = algo.get(1);
//        int bestDistance = algo.getFirst().length();
//        for(String curent : algo){
//            if(bestDistance<LevDist(curent,name,maxXSize,maxYSize)) bestAlgo = curent;
//
//        }
//
//
//    }


    public void chosenAlgo(int numberOfAlgo){
        this.algoName = algo.get(numberOfAlgo);
    }// to można będzie wywalić jak nie potrzebne

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
