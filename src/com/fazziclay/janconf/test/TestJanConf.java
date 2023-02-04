package com.fazziclay.janconf.test;

import com.fazziclay.janconf.JanConf;
import com.fazziclay.javaneoutil.FileUtil;

import java.io.File;

public class TestJanConf {
    public static void main(String[] args) {
        File file = new File("config.jconf");

        JanConf jconf = new JanConf(FileUtil.getText(file, ""));


        System.out.println("\n\n\n=======================");
        System.out.println(jconf.toString(2));
        System.out.println(jconf.type("custom_variables"));

        FileUtil.setText(new File("out_config.jconf"), jconf.toString(2));
    }
}
