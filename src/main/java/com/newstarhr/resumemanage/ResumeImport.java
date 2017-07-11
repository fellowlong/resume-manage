package com.newstarhr.resumemanage;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjinsi on 2017-07-11.
 */
public class ResumeImport {

    Pattern namePattern = Pattern.compile(".*姓名：\\s*(.+)\\s*", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);
    Pattern resumeIdPattern = Pattern.compile(".*简历编号：\\s*(.+)\\s*\\|\\s*", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);
    Pattern industryPattern = Pattern.compile("所在行业：\\s*(.+)\\s*公司名称：", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);
    Pattern companyPattern = Pattern.compile("公司名称：\\s*(.+)\\s*所任职位：", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);
    Pattern positionPattern = Pattern.compile("所任职位：\\s*(.+)\\s*目前薪资：", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);
    Pattern hopePositionPattern = Pattern.compile("期望职位：\\s*(.+)\\s*", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);
    Pattern cityPattern = Pattern.compile("所在地：\\s*(.+)\\s*", Pattern.MULTILINE&Pattern.CASE_INSENSITIVE);



    public void importResume() throws SQLException {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/resume_temp?useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        Connection connection = dataSource.getConnection();

        String source[][] = new String[4][3];
        source[0][0] = "C:\\简历-两个猎聘账户（完整）\\811704599@qq.com委托-word\\";
        source[0][1] = "811704599@qq.com委托";
        source[1][0] = "C:\\简历-两个猎聘账户（完整）\\811704599@qq.com已下载-word\\";
        source[1][1] = "811704599@qq.com已下载";
        source[2][0] = "C:\\简历-两个猎聘账户（完整）\\1002676487@qq.com委托2017-03-01后入库-word\\";
        source[2][1] = "1002676487@qq.com委托2017-03-01后入库";
        source[3][0] = "C:\\简历-两个猎聘账户（完整）\\1002676487@qq.com已下载2017-03-01后入库-word\\";
        source[3][1] = "1002676487@qq.com已下载2017-03-01后入库";

        for (int i = 0 ; i < source.length; i++) {
            String dir = source[i][0];
            String currentAccount = source[i][1];
            File[] files = new File(dir).listFiles();
            for (File file : files) {
                try {
                    PreparedStatement statement = connection.prepareStatement("insert into " +
                            "resume(fileName,binary_content,text_content,account,resumeId,name,industry,company,position,hopePosition,positionType,city)" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                    byte[] bytes = FileUtils.readToBytes(new FileInputStream(file));
                    com.aspose.words.Document doc = new com.aspose.words.Document(new ByteArrayInputStream(bytes));
                    String text = doc.toString(com.aspose.words.SaveFormat.TEXT);
                    String fileName = file.getName();
                    byte[] binary_content = bytes;
                    String text_content = text;
                    String account = currentAccount;
                    String positionType = null;

                    String resumeId = null;
                    Matcher resumeIdMatcher = resumeIdPattern.matcher(text);
                    if (resumeIdMatcher.find()) {
                        resumeId = resumeIdMatcher.group(1);
                        if (resumeId != null) {
                            resumeId = resumeId.trim();
                        }
                    }

                    String name = null;
                    Matcher nameMatcher = namePattern.matcher(text);
                    if (nameMatcher.find()) {
                        name = nameMatcher.group(1);
                        if (name != null) {
                            name = name.trim();
                        }
                    }

                    String industry = null;
                    Matcher industryMatcher = industryPattern.matcher(text);
                    if (industryMatcher.find()) {
                        industry = industryMatcher.group(1);
                        if (industry != null) {
                            industry = industry.trim();
                        }
                    }

                    String company = null;
                    Matcher companyMatcher = companyPattern.matcher(text);
                    if (companyMatcher.find()) {
                        company = companyMatcher.group(1);
                        if (company != null) {
                            company = company.trim();
                        }
                    }

                    String position = null;
                    Matcher positionMatcher = positionPattern.matcher(text);
                    if (positionMatcher.find()) {
                        position = positionMatcher.group(1);
                        if (position != null) {
                            position = position.trim();
                        }
                    }

                    String hopePosition = null;
                    Matcher hopePositionMatcher = hopePositionPattern.matcher(text);
                    if (hopePositionMatcher.find()) {
                        hopePosition = hopePositionMatcher.group(1);
                        if (hopePosition != null) {
                            hopePosition = hopePosition.trim();
                        }
                    }

                    String city = null;
                    Matcher cityMatcher = cityPattern.matcher(text);
                    if (cityMatcher.find()) {
                        city = cityMatcher.group(1);
                        if (city != null) {
                            city = city.trim();
                        }
                    }
                    statement.setObject(1, fileName);
                    statement.setObject(2, binary_content);
                    statement.setObject(3, text_content);
                    statement.setObject(4, account);
                    statement.setObject(5, resumeId);
                    statement.setObject(6, name);
                    statement.setObject(7, industry);
                    statement.setObject(8, company);
                    statement.setObject(9, position);
                    statement.setObject(10, hopePosition);
                    statement.setObject(11, positionType);
                    statement.setObject(12, city);

                    statement.executeUpdate();
                    statement.close();

                    System.out.println("resumeid=" + resumeId
                            + ",\n name=" + name
                            + ",\n company=" + company
                            + ",\n position=" + position
                            + ",\n hopePosition=" + hopePosition
                            + ",\n city=" + city);
                    /*FileUtils.convert(
                            new FileInputStream(file),
                            "E:\\OfficeFiles\\newstar\\测试简历\\html\\" + file.getName() + ".html",
                            FileType.getFileType(file.getName()),
                            FileType.html);*/
                } catch (Exception e) {
                    System.out.println(file.getName());
                    e.printStackTrace();
                }
            }
        }
        connection.close();
    }

}
