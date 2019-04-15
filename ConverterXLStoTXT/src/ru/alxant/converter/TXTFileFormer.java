package ru.alxant.converter;

/**
 * Created by Alxant on 10.03.2018.
 */

import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * personalAccount;FullName;Street;Home;Apartment;Amount;Date;numberPayment;note;advancedNote
 */
public class TXTFileFormer {
    private static ArrayList<String> outStrings;
    private static String filePath;

    public TXTFileFormer(String filePath) {
        TXTFileFormer.filePath = filePath;
    }

    public boolean tryToWrite(ArrayList<Receipt> receipts){
        outStrings = new ArrayList<>();
        createListOutStrings(receipts);
        createAndFullTXT();
        return true;
    }

    private static void createAndFullTXT(){
        String receiptDate = XLSDailyReport.getReportDate().replace("/","_");
        try {
            PrintWriter writer = new PrintWriter(filePath + receiptDate+".txt", "Windows-1251");
            for (String string: outStrings){
                writer.println(string);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void createListOutStrings(ArrayList<Receipt> receipts) {
        for (Receipt currentRec: receipts) {
            StringBuilder tmpString = new StringBuilder();
            tmpString.append(currentRec.getPersonalAccount()).append(";");
            tmpString.append(currentRec.getFullName()).append(";");
            tmpString.append(currentRec.getAddress().getStreet()).append(";")
                    .append(currentRec.getAddress().getHome()).append(";")
                    .append(currentRec.getAddress().getApartment()).append(";");

            tmpString.append(currentRec.getAmount()).append(";");
            tmpString.append(currentRec.getDate()).append(";");
            tmpString.append(currentRec.getPayNumber()).append(";");
            tmpString.append(currentRec.getNote()).append(";");
            if(currentRec.getAdvancedNote() == null){
                tmpString.append("");
            }else {
                tmpString.append(currentRec.getAdvancedNote());
            }
            outStrings.add(tmpString.toString());
        }
    }



}
