package com.coden.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName MSExcelParser
 * @Description MsExcelParse
 * @Version 1.0
 **/
@Slf4j
public class MsExcelParse {

    private MsExcelParse() {
        throw new IllegalStateException("MsExcelParse class error!");
    }

    /**
     * @Description readPdfText
     * @Param [file, textPath]
     **/
    public static void readPdfText(InputStream file, String textPath) {
        try (FileWriter fileWriter = new FileWriter(textPath, true)) {
            fileWriter.write(textPath);
            fileWriter.write(parseExcel(file));
        } catch (Exception e) {
            log.error("read pdf error ==> ", e);
        }
    }

    /**
     * @return java.lang.String
     * @Description parseExcel
     * @Param [inputStream]
     **/
    public static String parseExcel(InputStream inputStream) throws IOException, TikaException, SAXException {

        //detecting the file filterTypeEnum

        BodyContentHandler handler = new BodyContentHandler();

        Metadata metadata = new Metadata();

        ParseContext parseContext = new ParseContext();


        OOXMLParser msOfficeParser = new OOXMLParser();

        msOfficeParser.parse(inputStream, handler, metadata, parseContext);

        return handler.toString();

    }

}

