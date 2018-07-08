package com.example.map3dtest.dom4j;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;

/**
 * Created by 朱宏博 on 2018/4/11.
 */

public class ChartDatasDom4jUtil {
    private String decodingXML;
    public ChartDatasDom4jUtil(String xml){
        decodingXML = xml;
    }

    public ChartDatas dataDecode(){
        ChartDatas data = getData(decodingXML);
        return data;
    }

    public ChartDatas getData(String xml){
        ChartDatas comd = null;
        InputSource in = new InputSource(new StringReader(xml));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        Document document;
        try{
            document = reader.read(in);
            Element root = document.getRootElement();
            comd = (ChartDatas) XmlUtil.fromXmlToBean(root, ChartDatas.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("数据解析错误");

        }
        return comd;
    }
}
