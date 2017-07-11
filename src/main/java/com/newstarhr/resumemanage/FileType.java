package com.newstarhr.resumemanage;

public enum FileType {
    doc, docx, xls, xlsx, ppt, pptx, pdf, html, txt, text;

    public static FileType getFileType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        int separator = fileName.lastIndexOf(".");
        if (separator < 0) {
            return null;
        }
        String fileTypeStr = fileName.trim().substring(separator + 1);
        if (fileTypeStr.equalsIgnoreCase(doc.name())) {
            return doc;
        } else if (fileTypeStr.equalsIgnoreCase(docx.name())) {
            return docx;
        } else if (fileTypeStr.equalsIgnoreCase(xls.name())) {
            return xls;
        } else if (fileTypeStr.equalsIgnoreCase(xlsx.name())) {
            return xlsx;
        } else if (fileTypeStr.equalsIgnoreCase(ppt.name())) {
            return ppt;
        } else if (fileTypeStr.equalsIgnoreCase(pptx.name())) {
            return pptx;
        } else if (fileTypeStr.equalsIgnoreCase(pdf.name())) {
            return pdf;
        } else if (fileTypeStr.equalsIgnoreCase(html.name())) {
            return html;
        } else if (fileTypeStr.equalsIgnoreCase(txt.name())) {
            return txt;
        } else if (fileTypeStr.equalsIgnoreCase(text.name())) {
            return text;
        } else {
            throw new RuntimeException("不知道的文件类型：" + fileName);
        }
    }


}