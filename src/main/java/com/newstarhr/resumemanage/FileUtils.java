package com.newstarhr.resumemanage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wangjinsi on 2017/6/7.
 */
public abstract class FileUtils {

    static {
        try {
            com.aspose.pdf.License pdfLicense = new com.aspose.pdf.License();
            pdfLicense.setLicense(FileUtils.class.getClassLoader().getResourceAsStream("pdf-converter.xml"));
            com.aspose.cells.License excelLicense = new com.aspose.cells.License();
            excelLicense.setLicense(FileUtils.class.getClassLoader().getResourceAsStream("excel-converter.xml"));
        } catch (Exception e) {
            throw new RuntimeException("无效的授权文件", e);
        }
    }

    public static void convert(InputStream sourceInputStream, String targetFileName, FileType sourceFileType, FileType targetFileType) {
        int pdfSaveFormat = -1;
        int wordSaveFormat = -1;
        int excelSaveFormat = -1;
        if (targetFileType.equals(FileType.doc)) {
            pdfSaveFormat = com.aspose.pdf.SaveFormat.Doc;
            wordSaveFormat = com.aspose.words.SaveFormat.DOC;
        } else if (targetFileType.equals(FileType.docx)) {
            pdfSaveFormat = com.aspose.pdf.SaveFormat.DocX;
            wordSaveFormat = com.aspose.words.SaveFormat.DOCX;
        } else if (targetFileType.equals(FileType.ppt) || targetFileType.equals(FileType.pptx)) {
            pdfSaveFormat = com.aspose.pdf.SaveFormat.Pptx;
        } else if (targetFileType.equals(FileType.xls) || targetFileType.equals(FileType.xlsx)) {
            pdfSaveFormat = com.aspose.pdf.SaveFormat.Excel;
        } else if (targetFileType.equals(FileType.txt) || targetFileType.equals(FileType.text)) {
            pdfSaveFormat = com.aspose.pdf.SaveFormat.TeX;
            wordSaveFormat = com.aspose.words.SaveFormat.TEXT;
            excelSaveFormat = com.aspose.cells.SaveFormat.CSV;
        } else if (targetFileType.equals(FileType.html)) {
            pdfSaveFormat = com.aspose.pdf.SaveFormat.Html;
            wordSaveFormat = com.aspose.words.SaveFormat.HTML;
            excelSaveFormat = com.aspose.cells.SaveFormat.HTML;
        } else if (targetFileType.equals(FileType.pdf)) {
            wordSaveFormat = com.aspose.words.SaveFormat.PDF;
            excelSaveFormat = com.aspose.cells.SaveFormat.PDF;
        } else {
            throw new RuntimeException("不能识别的类型:" + targetFileType);
        }
        try {
            if (sourceFileType.equals(FileType.pdf)) {
                com.aspose.pdf.Document doc = new com.aspose.pdf.Document(sourceInputStream);
                doc.save(targetFileName, pdfSaveFormat);
                doc.freeMemory();
                doc.close();
            } else if (sourceFileType.equals(FileType.doc) || sourceFileType.equals(FileType.docx)) {
                com.aspose.words.Document doc = new com.aspose.words.Document(sourceInputStream);
                doc.save(targetFileName, wordSaveFormat);
            } else if (sourceFileType.equals(FileType.xls) || sourceFileType.equals(FileType.xlsx)) {
                com.aspose.cells.Workbook doc = new com.aspose.cells.Workbook(sourceInputStream);
                doc.save(targetFileName, excelSaveFormat);
            } else {
                throw new RuntimeException("不支持的源文件格式:" + sourceFileType);
            }
        } catch (Exception e) {
            throw new RuntimeException("转换文件失败:", e);
        }
    }

    public static byte[] readToBytes(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("读取数据量出错");
        }
    }



}
