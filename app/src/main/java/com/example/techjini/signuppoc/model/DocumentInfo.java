package com.example.techjini.signuppoc.model;

/**
 * Created by techjini on 28/7/16.
 */
public class DocumentInfo {
    private String docName;
    private int docId;
    private String docBASE64;
    private boolean isChecked;

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocBASE64() {
        return docBASE64;
    }

    public void setDocBASE64(String docBASE64) {
        this.docBASE64 = docBASE64;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
