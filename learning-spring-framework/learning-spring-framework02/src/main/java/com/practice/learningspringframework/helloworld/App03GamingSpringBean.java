package com.practice.learningspringframework.helloworld;

import com.practice.learningspringframework.iterartion2.GameRunner;
import com.practice.learningspringframework.iterartion2.GamingConsole;
import com.practice.learningspringframework.iterartion2.SuperContra;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App03GamingSpringBean {
    public static void main(String[] args) {
        try(var context =
                    new AnnotationConfigApplicationContext
                            (GamingConfiguration.class)){
            context.getBean(GamingConsole.class).up();
            context.getBean(GameRunner.class).run();
        }

    }
}
