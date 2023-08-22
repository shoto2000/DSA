package DSA.Recursion;

public class NumberExample {
    public static void main(String[] args) {
        print(9);
    }

    static void print(int n){
        if(n==0){
            return;
        }
        System.out.println(n);
        print(n-1);
    }
}
