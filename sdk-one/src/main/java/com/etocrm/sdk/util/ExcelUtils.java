package com.etocrm.sdk.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUtils {

    public static void writeExcel(HttpServletResponse response, List<? extends Object> data, String sheetName, Class clazz) {
        //表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置内容靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        try {
            OutputStream out = getOutputStream(response);
            EasyExcel.write(out, clazz).sheet(sheetName).doWrite(data);
        }catch (Exception e){
            log.error(e.getMessage(),e);

        }

    }

    private static OutputStream getOutputStream( HttpServletResponse response)  {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        String uuid=System.currentTimeMillis()+"";
        response.setHeader("Content-Disposition", "attachment;filename=" +System.currentTimeMillis() + ".xlsx");
        try{
          return  response.getOutputStream();
        }catch (Exception e){
         log.error(e.getMessage(),e);
        }

        return  null;
    }

    public static <E> List<E> readExcelWithModel(InputStream inputStream,Class clas) {
        // 解析每行结果在listener中处理
        ModelExcelListener<E> listener = new ModelExcelListener<E>();
        ExcelReaderBuilder read = EasyExcel.read(inputStream,clas, listener);

/*        ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        //默认只有一列表头
        excelReader.read(new Sheet(1, 1, clazz));
  */
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        ExcelReader read1 = read.build().read(readSheet);

        return listener.getDataList();
    }

    /**
     * 模型解析监听器 -- 每解析一行会回调invoke()方法，整个excel解析结束会执行doAfterAllAnalysed()方法
     */
    private static class ModelExcelListener<E> extends AnalysisEventListener<E> {
        private List<E> dataList = new ArrayList<E>();

        @Override
        public void invoke(E object, AnalysisContext context) {
                dataList.add(object);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
        }
        public List<E> getDataList() {
                return dataList;
        }

        public void setDataList(List<E> dataList) {
            this.dataList = dataList;
        }
    }

}