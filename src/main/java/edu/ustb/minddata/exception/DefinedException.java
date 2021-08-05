package edu.ustb.minddata.exception;

import edu.ustb.minddata.enums.StatusInfoInterface;
import lombok.Getter;
import lombok.Setter;

/**
 * @author UmiSkky
 */
@Getter
@Setter
public class DefinedException extends RuntimeException{

    private Integer resultCode;

    private String resultMsg;

    public DefinedException() {
        super();
    }

    public DefinedException(StatusInfoInterface statusInfoInterface) {
        super(statusInfoInterface.getResultMsg());
        this.resultCode = statusInfoInterface.getResultCode();
        this.resultMsg = statusInfoInterface.getResultMsg();
    }

    public DefinedException(Integer resultCode, String resultMsg) {
        super(resultMsg);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}
