package edu.ustb.minddata.service.impl;

import cn.hutool.core.io.FileUtil;
import edu.ustb.minddata.config.PathConfig;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import edu.ustb.minddata.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author UmiSkky
 */
@Service
@Slf4j(topic = "database")
public class ReportServiceImpl implements ReportService {

    @Value("${server.port}")
    private Integer serverPort;

    @Override
    public List<String> queryReportUrlByRid(String rid) throws Exception {

        List<String> urlList = new ArrayList<>();
        String urlPrefix = "http://localhost:" + this.serverPort + "/static/" + rid + "/";

        String absolutePath = String.valueOf(Paths.get(PathConfig.getPrefixPath(), rid).toAbsolutePath());
        File file = new File(absolutePath);
        if(!FileUtil.isDirectory(file)){
            log.error("Query report url error: " + ResultEnum.REPORT_NOT_EXIST.getResultMsg() + " rid=" + rid);
            throw new DefinedException(ResultEnum.REPORT_NOT_EXIST);
        }else if(FileUtil.isDirEmpty(file)){
            log.error("Query report url error: " + ResultEnum.REPORT_CORRUPTED.getResultMsg() + " rid=" + rid);
            throw new DefinedException(ResultEnum.REPORT_CORRUPTED);
        }else{
            for (File l : FileUtil.ls(absolutePath)) {
                if("png".equals(FileUtil.extName(l)) || "jpg".equals(FileUtil.extName(l))){
                    urlList.add(urlPrefix + l.getName());
                }
            }
            if(urlList.size() == 0){
                log.error("Query report url error: " + ResultEnum.REPORT_CORRUPTED.getResultMsg() + " rid=" + rid);
                throw new DefinedException(ResultEnum.REPORT_CORRUPTED);
            }
        }
        log.info("Query report url: rid=" + rid + "url: " + urlList);
        return urlList;
    }
}
