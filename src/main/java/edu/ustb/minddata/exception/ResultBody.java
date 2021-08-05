package edu.ustb.minddata.exception;

import cn.hutool.core.date.DateUtil;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.enums.StatusInfoInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author UmiSkky
 */
@AllArgsConstructor
@Getter
public class ResultBody {

    private String timestamp;
    private String url;
    private Integer code;
    private String msg;
    private Object data;

    private static ResultBody successWithoutUrl(Object data){
        return new ResultBody(
                DateUtil.now(),
                null,
                ResultEnum.SUCCESS.getResultCode(),
                ResultEnum.SUCCESS.getResultMsg(),
                data);
    }

    public static ResultBody success(Object data){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null){
            HttpServletRequest req = servletRequestAttributes.getRequest();
            return new ResultBody(
                    DateUtil.now(),
                    req.getMethod()+": "+req.getRequestURI(),
                    ResultEnum.SUCCESS.getResultCode(),
                    ResultEnum.SUCCESS.getResultMsg(),
                    data);
        }else{
            return successWithoutUrl(data);
        }

    }

    private static ResultBody errorWithoutUrl(DefinedException definedException){
        return  new ResultBody(
                DateUtil.now(),
                null,
                definedException.getResultCode(),
                definedException.getResultMsg(),
                null
        );
    }

    private static ResultBody errorWithoutUrl(StatusInfoInterface statusInfo){
        return  new ResultBody(
                DateUtil.now(),
                null,
                statusInfo.getResultCode(),
                statusInfo.getResultMsg(),
                null
        );
    }

    public static ResultBody error(DefinedException definedException){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null){
            HttpServletRequest req = servletRequestAttributes.getRequest();
            return  new ResultBody(
                    DateUtil.now(),
                    req.getMethod()+": "+req.getRequestURI(),
                    definedException.getResultCode(),
                    definedException.getResultMsg(),
                    null
            );
        }else{
            return errorWithoutUrl(definedException);
        }
    }

    public static ResultBody error(StatusInfoInterface statusInfo){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null){
            HttpServletRequest req = servletRequestAttributes.getRequest();
            return new ResultBody(
                    DateUtil.now(),
                    req.getMethod()+": "+req.getRequestURI(),
                    statusInfo.getResultCode(),
                    statusInfo.getResultMsg(),
                    null
            );
        }else{
            return errorWithoutUrl(statusInfo);
        }

    }
}
