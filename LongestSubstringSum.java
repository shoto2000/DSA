public class LongestSubstringSum {

    // kadane's algorithm

    public static void main(String[] args) {
        int[] arr = {-2,-3,4,-1,-2,1,5,-3};
        int n = arr.length;

        System.out.println(longest(n,arr));
    }

    public static int longest(int n, int[] arr){
        int sum = 0;
        int max = sum;

        for(int i=0;i<n;i++){
            sum += arr[i];

            max = Math.max(max,sum);

            if(sum<0) sum = 0;
        }

        return max;
    }
}
