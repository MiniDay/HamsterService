package cn.hamster3.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("E:\\Code\\Java\\HamsterService\\src");
        System.out.println("代码总大小: " + getSize(file) / 1024.0 + " kb");
        System.out.println("代码总行数: " + getLine(file));
    }

    private static long getSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            for (File children : files) {
                size += getSize(children);
            }
            return size;
        }
        if (!file.getName().endsWith(".java")) {
            return 0;
        }
        return file.length();
    }

    private static long getLine(File file) throws FileNotFoundException {
        long line = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            for (File children : files) {
                line += getLine(children);
            }
            return line;
        }
        if (!file.getName().endsWith(".java")) {
            return 0;
        }
        Scanner scanner = new Scanner(new FileInputStream(file), "UTF-8");
        while (scanner.hasNext()) {
            scanner.nextLine();
            line++;
        }
        scanner.close();
        return line;
    }
}
