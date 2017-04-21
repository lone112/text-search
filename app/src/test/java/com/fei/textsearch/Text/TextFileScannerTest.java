package com.fei.textsearch.Text;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertFalse;

/**
 * Created by fei on 4/21/2017.
 */
public class TextFileScannerTest {
    @Test
    public void run() throws Exception {
        String filePath = this.getClass().getResource("/en.txt").getFile();
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("exists");
        }


        String[] kw = new String[3];
        kw[0] = "management";
        kw[1] = "technology";
        kw[2] = "especially";
        TextFileScanner scanner = new TextFileScanner(kw);
        FileScanResult result = scanner.run(filePath);

        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");
        for (TextSegment segment : result.getSegments()) {
            int start = segment.getIndex().getFirst();
            int end = segment.getIndex().getSecond();

            String tmp = str.substring(start, end);
            System.out.println(tmp);
            assertFalse(tmp.contains(" "));
            System.out.println(segment.getSplitText());
            System.out.println(segment.getPercent());
        }
    }

    @Test
    public void run2() throws Exception {
        String filePath = this.getClass().getResource("/en-unix.txt").getFile();
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("exists");
        }


        String[] kw = new String[3];
        kw[0] = "management";
        kw[1] = "technology";
        kw[2] = "especially";
        TextFileScanner scanner = new TextFileScanner(kw);
        FileScanResult result = scanner.run(filePath);
        System.out.println("find key word " + result.getCount());
        System.out.println("find times " + result.getTimes());
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");
        for (TextSegment segment : result.getSegments()) {
            int start = segment.getIndex().getFirst();
            int end = segment.getIndex().getSecond();

            String tmp = str.substring(start, end);
            System.out.println(tmp);

            System.out.println(segment.getSplitText());
            System.out.println(segment.getPercent());
            assertFalse(tmp.contains(" "));
        }
    }
}
