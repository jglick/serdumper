package com.github.jglick.serdumper;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        try (InputStream is = new FileInputStream(args[0])) {
            System.out.println(new SerParser(is).parse(""));
        }
    }
}
