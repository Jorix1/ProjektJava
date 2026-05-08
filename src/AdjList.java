import java.util.*;

public class AdjList {

    public static class  adjElement{
        int nodeName;
        double weight;
        adjElement(int nodeName,double weight){
            this.nodeName = nodeName;
            this.weight = weight;
        }
    }

    public void adjInsert(LinkedList<adjElement> AdjNodeList , int secondaryNode, double weight ){
        AdjNodeList.add(new adjElement(secondaryNode,weight));
    }
    public int getAdjLen(LinkedList<adjElement> AdjNodeList){

        return AdjNodeList.size();
    }
    public boolean adjListFind(LinkedList<adjElement> AdjNodeList, int nodeName){
        Iterator<adjElement> iterator = AdjNodeList.iterator();
        while(iterator.hasNext()){
            if(iterator.next().nodeName ==  nodeName){
                return true;
            }
        }
        return false;

    }
}
