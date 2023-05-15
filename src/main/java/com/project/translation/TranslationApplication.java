package com.project.translation;

import com.project.translation.utils.TranslationUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();

        File inputFile = new File(com.getPath() + "\\example.txt");
        File outputFile = new File(com.getPath() + "\\10.txt");

        try {
            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(outputFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) { // 判断该行是否为空行
                    if (Pattern.compile("[\\u4e00-\\u9fa5]").matcher(line).find()) { // 判断该行是否包含中文字符
                        Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]+").matcher(line); // 匹配中文字符
                        StringBuffer sb = new StringBuffer();
                        while (matcher.find()) {
                            String group = matcher.group();
                            String transGroup = TranslationUtil.getTrans(group); // 将匹配到的中文字符翻译成英文
                            matcher.appendReplacement(sb, transGroup); // 将原字符串中匹配到的中文字符替换为翻译后的字符串
                        }
                        matcher.appendTail(sb); // 将剩余的字符串全部添加到字符串缓冲区中
                        line = sb.toString(); // 获取处理后的字符串
                    }
                    System.out.println(line);
                    writer.println(line);
                }
            }

            scanner.close();
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




}
