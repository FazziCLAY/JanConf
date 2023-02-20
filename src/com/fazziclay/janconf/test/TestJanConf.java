package com.fazziclay.janconf.test;

import com.fazziclay.janconf.JanConf;
import com.fazziclay.javaneoutil.FileUtil;

import java.io.File;

public class TestJanConf {
    private final static File TEST = new File("test");

    public static void main(String[] args) {
        File file = new File(TEST, "config.jconf");

        JanConf jconf = new JanConf(FileUtil.getText(file, ""));


        System.out.println("\n\n\n=======================");
        System.out.println(jconf.toString(1, false));
        System.out.println(jconf.type("custom_variables"));

        FileUtil.setText(new File(TEST, "out_config.jconf"), jconf.toString(1, false));
    }
}
