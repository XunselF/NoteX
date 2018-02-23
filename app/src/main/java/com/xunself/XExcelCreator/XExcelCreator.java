package com.xunself.XExcelCreator;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static jxl.write.WritableFont.ARIAL;
import static jxl.write.WritableFont.NO_BOLD;

/**
 * Created by XunselF on 2018/2/22.
 */

public class XExcelCreator {
    private WritableWorkbook mWritableWorkbook;
    private WritableSheet mSheet;

    private String mFileName;    //文件名
    private String mSheetName;   //表名

    private int mRow;   //现在的行数
    private int mColumn;    //现在的列数

    private int mWidth;   //宽度
    private int mHeight;    //高度


    private WritableCellFormat mFormat;     //设置字体


    //初始化
    public XExcelCreator(String fileName,String sheetName,int sPosition){
        try{
            mWritableWorkbook = Workbook.createWorkbook(new File("mnt/sdcard/" + fileName + ".xls"));
            mSheet = mWritableWorkbook.createSheet(sheetName,sPosition);

            mFileName = fileName;
            mSheetName = sheetName;
            mRow = 0;
            mColumn = 0;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //设置高度
    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    //设置宽度
    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    /**
     * 设置字体样式
     * @param fontStyle
     * @param fontSize
     * @param ifFontBlod
     */
    public void setFontStyle(WritableFont.FontName fontStyle,int fontSize,boolean ifFontBlod){
        WritableFont font;
        if (ifFontBlod){        //是否粗体
            font = new WritableFont(fontStyle,fontSize,WritableFont.BOLD);
        }else{
            font = new WritableFont(fontStyle,fontSize,WritableFont.NO_BOLD);
        }
        mFormat = new WritableCellFormat(font);
        try{
            mFormat.setWrap(true);
        }catch(WriteException e){
            e.printStackTrace();
        }

    }


    /**
     * 创建文本
     * @param text
     * @return
     */
    public boolean createText(String text){
        if (mSheet == null) {
            return false;
        }
        if (mFormat == null){   //字体样式进行初始化
            setFontStyle(ARIAL,10,false);
        }

        try{
            //设置宽度与高度
            if (mWidth != 0){
                mSheet.setRowView(mRow,mWidth);
            }if (mHeight != 0){
                mSheet.setColumnView(mColumn,mHeight);
            }
            Label label = new Label(mColumn,mRow,text,mFormat);
            mSheet.addCell(label);

            //列+1
            mColumn ++;
        }catch (WriteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 多个数据进行添加
     * @param texts
     */
    public void createAllTexts(String[] texts){
        for (int i = 0; i < texts.length; i++){
            createText(texts[i]);
        }
        mRow ++;
        mColumn = 0;
    }

    /**
     * 写入并关闭
     */
    public void writeData(){
        try{
            mWritableWorkbook.write();
            mWritableWorkbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
