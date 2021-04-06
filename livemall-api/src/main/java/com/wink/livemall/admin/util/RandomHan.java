package com.wink.livemall.admin.util;

import java.util.Random;

public class RandomHan {
    private Random ran = new Random();
    private final static int delta = 0x9fa5 - 0x4e00 + 2;

    public String getRandomHan() {
        char nameOne=(char)(0x4e00 + ran.nextInt(delta));
        char nameTwo=(char)(0x4e00 + ran.nextInt(delta));
        char nameTree=(char)(0x4e00 + ran.nextInt(delta));
        String name= nameOne+""+nameTwo+""+nameTree+"";
        return name;
    }

}
