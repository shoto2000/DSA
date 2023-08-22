package DSA.Recursion;

import java.util.HashMap;
import java.util.Map;

public class Duplicates {
    public static void main(String[] args) {
        int[] arr = new int[] {8,9,11,14,11,9,5};

        findDuplicates(arr);
    }

    static void findDuplicates(int[] arr){
        Map<Integer,Integer> map = new HashMap<>();

        for(int i:arr){
            if(map.containsKey(i)){
                map.put(i,map.get(i)+1);
            }
            else{
                map.put(i,1);
            }
        }

        System.out.println(map);
    }
}
