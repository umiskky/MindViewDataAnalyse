package edu.ustb.minddata.entity.request;

import edu.ustb.minddata.entity.Personnel;
import lombok.Data;

/**
 * @author UmiSkky
 */
@Data
public class PersonnelDTO {

    private String number;

    private Integer sex;

    private Integer age;

    private String nature;

    public Personnel createPersonnel(){
        Personnel personnel = new Personnel();
        personnel.setId(null);
        personnel.setNumber(this.number);
        personnel.setSex(this.sex);
        personnel.setAge(this.age);
        personnel.setNature(this.nature);
        personnel.setTimestamp(null);
        return personnel;
    }
}
