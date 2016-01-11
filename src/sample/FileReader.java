package sample;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by tombe on 07-Jan-16.
 */
public class FileReader {
    private String line;

    public String[] readFile(String url){
        BufferedReader bufferedReader = null;
        String[] splitted = null;
        try{
            bufferedReader = new BufferedReader(new java.io.FileReader(url));
            line = bufferedReader.readLine();

            splitted = line.split(",");
            System.out.println(line);
        } catch (IOException e){

        }
        return splitted;
    }
}
