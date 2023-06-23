package DesignPattern.Decorator;

import DesignPattern.Decorator.Base.BasePizza;
import DesignPattern.Decorator.Base.FarmFresh;
import DesignPattern.Decorator.Toppings.ExtraCheese;
import DesignPattern.Decorator.Toppings.ExtraMushroom;

public class Main {
    public static void main(String[] args) {
        BasePizza basePizza = new ExtraMushroom(new ExtraCheese(new FarmFresh()));

        System.out.println(basePizza.cost());
    }
}
