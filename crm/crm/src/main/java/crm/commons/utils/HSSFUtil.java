package crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFUtil {

        public static String getCellValueForStr(HSSFCell cell){
            String ret="";
            if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                ret = cell.getStringCellValue();
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                double d = cell.getNumericCellValue();
                ret=(int)d+"";
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
                ret = cell.getBooleanCellValue()+"";
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                ret = cell.getCellFormula();
            } else {
                ret="";
            }
            return ret;
        }

}
