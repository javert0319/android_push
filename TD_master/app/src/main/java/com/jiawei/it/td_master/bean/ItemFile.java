package com.jiawei.it.td_master.bean;

/**
 * @author JAIWEI
 * @company Thredim
 * @date on 2018/5/31.
 * @org www.thredim.com (宁波视睿迪光电有限公司)
 * @email thredim@thredim.com
 * @describe FileBean
 */
public class ItemFile {
    private int index;
    private String fileName;
    private String percentage;
    private String pb;
    private String down;

    public ItemFile(String fileName) {
        this.fileName = fileName;
    }

    public ItemFile(String fileName, String percentage) {
        this.fileName = fileName;
        this.percentage = percentage;
    }
    public ItemFile(int index, String fileName, String percentage) {
        this.index = index;
        this.fileName = fileName;
        this.percentage = percentage;
    }
    public ItemFile(int index, String fileName, String percentage, String pb) {
        this.index = index;
        this.fileName = fileName;
        this.percentage = percentage;
        this.pb = pb;
    }
    public ItemFile(int index, String fileName, String percentage, String pb, String down) {
        this.index = index;
        this.fileName = fileName;
        this.percentage = percentage;
        this.pb = pb;
        this.down = down;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }
}
