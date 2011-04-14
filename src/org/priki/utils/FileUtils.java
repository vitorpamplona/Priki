package org.priki.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public String readFile(String file) throws IOException {
        InputStream in = this.getClass().getResource(file).openStream();
        InputStreamReader inRead = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inRead);

        StringBuilder builder = new StringBuilder();
        String temp;
        while ((temp = reader.readLine()) != null) {
            builder.append(temp);
        }

        reader.close();
        inRead.close();
        in.close();
        
        return builder.toString();
        
    }
}
