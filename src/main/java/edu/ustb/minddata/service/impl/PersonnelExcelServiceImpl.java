package edu.ustb.minddata.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ustb.minddata.entity.Personnel;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import edu.ustb.minddata.mapper.PersonnelMapper;
import edu.ustb.minddata.service.PersonnelExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author UmiSkky
 */
@Service
@Slf4j(topic = "file")
public class PersonnelExcelServiceImpl implements PersonnelExcelService {

    @Value("${start.buffer}")
    private String bufferPath;

    private static final String EXCEL_REGEX = "^.+\\.(?i)((xls)|(xlsx))$";

    private static final String XLSX_REGEX = "^.+\\.(?i)(xlsx)$";

    private static final int TABLE_HEAD_REDUNDANT = -2;
    private static final int TABLE_HEAD_NOT_INITIALIZE = -1;

    private final PersonnelMapper personnelMapper;

    @Autowired
    public PersonnelExcelServiceImpl(PersonnelMapper personnelMapper) {
        this.personnelMapper = personnelMapper;
    }

    @Override
    public String uploadExcelFile(MultipartFile multipartFile) throws Exception {
        // 创建缓存文件夹
        String bufferPath = String.valueOf(Paths.get(this.bufferPath).toAbsolutePath());
        File folder = new File(bufferPath);
        if(!folder.isDirectory()){
            folder.mkdirs();
        }

        // 检查传入文件是否符合格式
        String oldName = "" + multipartFile.getOriginalFilename();
        if(!oldName.matches(EXCEL_REGEX)){
            log.error("[File] " + ResultEnum.FILE_UPLOAD_TYPE_NOT_MATCHED.getResultMsg() + ", " + oldName + " does not match the \".xls\" or \".xlsx\" format.");
            throw new DefinedException(ResultEnum.FILE_UPLOAD_TYPE_NOT_MATCHED);
        }

        // 将文件存入缓存
        String newName = DateUtil.today() + "-" + IdUtil.simpleUUID() + "." + FileUtil.extName(oldName);
        multipartFile.transferTo(new File(folder, newName));

        // 判断文件是否缓存成功，返回文件名
        File bufferFile = Paths.get(bufferPath, newName).toFile();
        if(!bufferFile.isFile() || FileUtil.isEmpty(bufferFile)){
            log.error("[File] " + ResultEnum.FILE_UPLOAD_FAILED.getResultMsg() + ", " + bufferFile.getName() + " not found or is empty!");
            throw new DefinedException(ResultEnum.FILE_UPLOAD_FAILED);
        }else{
            log.info("[File] " + multipartFile.getOriginalFilename() + " is uploaded in " + bufferFile.getAbsolutePath());
            return newName;
        }
    }

    @Override
    public Map<String, Object> insertPersonnelByExcelFile(String excelFileName) throws Exception {

        // 判断文件是否存在
        File file = Paths.get(this.bufferPath, excelFileName).toAbsolutePath().toFile();
        if(!file.isFile() || FileUtil.isEmpty(file)){
            log.error("[File] " + ResultEnum.FILE_UPLOAD_NOT_FOUND.getResultMsg() + ", " + excelFileName + " not found or is empty!");
            throw new DefinedException(ResultEnum.FILE_UPLOAD_NOT_FOUND);
        }

        // 解析excel表格
        List<Personnel> personnelList = parseExcelFile(file);

        List<String> errorPersonnelNumberList = new ArrayList<>();
        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();

        // 进行记录的插入或更新
        int insertCount=0, updateCount=0;
        for (Personnel personnel : personnelList) {
            wrapper.clear();
            wrapper.eq("number", personnel.getNumber());
            Personnel tmp = personnelMapper.selectOne(wrapper);
            if(tmp == null){
                if(personnelMapper.insert(personnel) == 1){
                    insertCount ++;
                }else{
                    errorPersonnelNumberList.add(personnel.getNumber());
                }
            }else{
                tmp.setNumber(personnel.getNumber());
                tmp.setAge(personnel.getAge());
                tmp.setSex(personnel.getSex());
                tmp.setNature(personnel.getNature());
                if(personnelMapper.updateById(tmp) == 1){
                    updateCount ++;
                }else{
                    errorPersonnelNumberList.add(tmp.getNumber());
                }
            }
        }

        Map<String, Object> res = new HashMap<>(4);
        res.put("total", personnelList.size());
        res.put("insert", insertCount);
        res.put("update", updateCount);
        res.put("error", errorPersonnelNumberList);

        log.info("[File] Insert or update personnel from excel file: "
                + "\ntotal: " + personnelList.size()
                + "\ninsert: " + insertCount
                + "\nupdate: " + updateCount
                + "\nerror: " + errorPersonnelNumberList);
        return res;
    }

    /**
     * 处理excel数据表格，生成Personnel对象列表
     * @param excelFile File
     * @return List<Personnel>
     * @throws Exception 异常
     */
    private List<Personnel> parseExcelFile(File excelFile) throws Exception{
        List<Personnel> personnelList = new ArrayList<>();

        Workbook wb;
        if(excelFile.getName().matches(XLSX_REGEX)){
            wb = new XSSFWorkbook(excelFile);
        }else{
            wb = new HSSFWorkbook(new FileInputStream(excelFile));
        }

        // 得到表格索引
        int numberIndex = -1, sexIndex = -1, ageIndex = -1, natureIndex = -1;
        Row headRow = wb.getSheetAt(0).getRow(0);
        for(int i=headRow.getFirstCellNum(); i<headRow.getLastCellNum(); i++){
            switch(headRow.getCell(i).getStringCellValue()){
                case "number":
                    numberIndex = numberIndex==TABLE_HEAD_NOT_INITIALIZE ? i : TABLE_HEAD_REDUNDANT;
                    break;
                case "gender":
                    sexIndex = sexIndex==TABLE_HEAD_NOT_INITIALIZE ? i : TABLE_HEAD_REDUNDANT;
                    break;
                case "age":
                    ageIndex = ageIndex==TABLE_HEAD_NOT_INITIALIZE ? i : TABLE_HEAD_REDUNDANT;
                    break;
                case "name":
                    natureIndex = natureIndex==TABLE_HEAD_NOT_INITIALIZE ? i : TABLE_HEAD_REDUNDANT;
                    break;
                default:
            }
        }

        // 进行表格标题校验
        if(numberIndex== TABLE_HEAD_REDUNDANT || sexIndex== TABLE_HEAD_REDUNDANT || ageIndex== TABLE_HEAD_REDUNDANT || natureIndex== TABLE_HEAD_REDUNDANT){
            log.error("[Excel Parse] " + excelFile.getName() + ": " + ResultEnum.TABLE_HEAD_REDUNDANT.getResultMsg());
            throw new DefinedException(ResultEnum.TABLE_HEAD_REDUNDANT);
        }else if(numberIndex==TABLE_HEAD_NOT_INITIALIZE || sexIndex==TABLE_HEAD_NOT_INITIALIZE || ageIndex==TABLE_HEAD_NOT_INITIALIZE || natureIndex==TABLE_HEAD_NOT_INITIALIZE){
            log.error("[Excel Parse] " + excelFile.getName() + ": " + ResultEnum.TABLE_HEAD_NOT_INITIALIZE.getResultMsg());
            throw new DefinedException(ResultEnum.TABLE_HEAD_NOT_INITIALIZE);
        }


        DataFormatter dataFormatter = new DataFormatter();
        for (Row row : wb.getSheetAt(0)) {
            // 跳过标题行
            if(row.getRowNum() == 0 || StrUtil.isEmpty(dataFormatter.formatCellValue(row.getCell(numberIndex)))){
                continue;
            }

            // 初始化Personnel实体信息
            Personnel p = new Personnel();
            p.setId(IdUtil.randomUUID());
            p.setNumber(dataFormatter.formatCellValue(row.getCell(numberIndex)));
            p.setSex("女".equals(row.getCell(sexIndex).getStringCellValue()) ? 0 : 1);
            p.setAge(Integer.parseInt(StrUtil.isEmpty(dataFormatter.formatCellValue(row.getCell(ageIndex))) ? "-1" : dataFormatter.formatCellValue(row.getCell(ageIndex))));
            Map<String, String> map = new HashMap<>(1);
            map.put("name", ""+row.getCell(natureIndex).getStringCellValue());
            p.setNature(new ObjectMapper().writeValueAsString(map));
            p.setTimestamp(System.currentTimeMillis());
            personnelList.add(p);
        }

        wb.close();
        return personnelList;
    }

}
