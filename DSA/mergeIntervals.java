package DSA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class mergeIntervals {
    public static void main(String[] args) {
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{1,3});
        list.add(new int[]{2,6});
        list.add(new int[]{8,10});
        list.add(new int[]{15,18});
        mergeInterval(list);

    }

    static void mergeInterval(List<int[]> list){
        Stack<Integer> stk1 = new Stack<>();
        Stack<Integer> stk2 = new Stack<>();

        List<int[]> ans = new ArrayList<>();


        stk1.add(list.get(0)[0]);
        stk2.add(list.get(0)[1]);

        for(int i=1;i<list.size();i++){
            if(!stk2.isEmpty() && stk2.peek()>=list.get(i)[0]){
                stk2.pop();
                stk2.add(list.get(i)[1]);
            }
            else{
                ans.add(new int[] {stk1.pop(),stk2.pop()});
                stk1.add(list.get(i)[0]);
                stk2.add(list.get(i)[1]);
            }
        }

        ans.add(new int[] {stk1.pop(),stk2.pop()});


        for(int[] a:ans){
            System.out.println(Arrays.toString(a));
        }
    }
}
