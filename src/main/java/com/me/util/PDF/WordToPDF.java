package com.me.util.PDF;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.*;

/**
 * 作者 YUEJS
 * 时间 2020-01-15
 */
public class WordToPDF {

    private static InputStream fileInput;
    private static File outputFile;

    private static String fileSavePath = "";

    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense(String docx, String pdf) {
        boolean result = false;
        try {
            // 凭证
            String licenseStr =
                    "<License>\n"
                            + " <Data>\n"
                            + " <Products>\n"
                            + " <Product>Aspose.Total for Java</Product>\n"
                            + " <Product>Aspose.Words for Java</Product>\n"
                            + " </Products>\n"
                            + " <EditionType>Enterprise</EditionType>\n"
                            + " <SubscriptionExpiry>20991231</SubscriptionExpiry>\n"
                            + " <LicenseExpiry>20991231</LicenseExpiry>\n"
                            + " <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n"
                            + " </Data>\n"
                            + " <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n"
                            + "</License>";
            InputStream license = new ByteArrayInputStream(
                    licenseStr.getBytes("UTF-8"));
            fileInput = new FileInputStream(docx);// 待处理的文件
            outputFile = new File(pdf);// 输出路径
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static synchronized String savePdf(String wordPath, String pdfPath) {
        try {
            if (!getLicense(wordPath, pdfPath)) {
                return "";
            }
            Document pdf = new Document(fileInput);
            FileOutputStream fileOS = new FileOutputStream(outputFile);
            pdf.save(fileOS, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputFile.getPath().replace(fileSavePath, "");
    }

}
