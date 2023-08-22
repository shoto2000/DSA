package DSA.Recursion;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SortBalls {
    public static void main(String[] args) {
        int[] nums = new int[]{2,0,2,1,1,0};

        sort(nums);
    }

    static void sort(int[] nums){
        int n = nums.length;

        int zero = 0;
        int one = 0;
        int two = 0;

        for(int i:nums){
            if(i==0) zero++;
            else if(i==1) one++;
            else two++;
        }
        int a=0;
        for(int i=0;i<zero;i++){
            nums[a++] = 0;
        }

        for(int i=0;i<one;i++){
            nums[a++] = 1;
        }

        for(int i=0;i<two;i++){
            nums[a++] = 2;
        }

        System.out.println(Arrays.toString(nums));
    }
}
