package com.practice.learningspringframework.helloworld;

import com.practice.learningspringframework.iterartion2.GameRunner;
import com.practice.learningspringframework.iterartion2.GamingConsole;
import com.practice.learningspringframework.iterartion2.SuperContra;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GamingConfiguration {

    @Bean
    public GamingConsole game() {
        var game = new SuperContra();
        return game;
    }

    @Bean
    public GameRunner gameRunner(GamingConsole game){
        return new GameRunner(game);
    }
}
