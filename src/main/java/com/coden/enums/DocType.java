package com.coden.enums;


import org.omg.CORBA.UNKNOWN;

public enum DocType {

    // PDF文档
    PDF,
    pdf,
    // word文档
    DOCX,
    DOC,
    // unknown
    UNKNOWN;

    public static DocType getDocType(String suffixName) {
        switch (suffixName) {
            case ".pdf":
            case ".PDF":
                return PDF;
            case ".docx":
                return DOCX;
            case ".doc":
                return DOC;
            default:
                return UNKNOWN;
        }
    }
}
