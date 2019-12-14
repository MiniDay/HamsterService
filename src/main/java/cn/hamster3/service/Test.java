package cn.hamster3.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("E:\\Code\\Java\\HamsterService\\src\\main\\java");
        System.out.println("代码总大小: " + getSize(file) / 1024.0 + " kb");
        System.out.println("代码总行数: " + getLine(file));
    }

    private static long getSize(File file) {
        long l = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            for (File children : files) {
                l += getSize(children);
            }
            return l;
        }
        if (!file.getName().endsWith(".java")) {
            return 0;
        }
        return file.length();
    }

    private static long getLine(File file) throws FileNotFoundException {
        long l = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            for (File children : files) {
                l += getLine(children);
            }
            return l;
        }
        if (!file.getName().endsWith(".java")) {
            return 0;
        }
        Scanner scanner = new Scanner(new FileInputStream(file), "UTF-8");
        while (scanner.hasNext()) {
            scanner.nextLine();
            l++;
        }
        scanner.close();
        return l;
    }
}
