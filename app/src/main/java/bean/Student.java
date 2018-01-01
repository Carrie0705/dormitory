package bean;

/**
 * Created by Carrie0705 on 2017/12/27.
 */

public class Student {
    private String stuId;
    private String stuName;
    private String stuSex;
    private String stuPlace;
    private String stuVcode;
    private String stuGrade;
    private String buildNum;
    private String domitoryNum;

    @Override
    public String toString() {
        return "Student{" +
                "stuId='" + stuId + '\'' +
                ", stuName='" + stuName + '\'' +
                ", stuSex='" + stuSex + '\'' +
                ", stuPlace='" + stuPlace + '\'' +
                ", stuVcode='" + stuVcode + '\'' +
                ", stuGrade='" + stuGrade + '\'' +
                ", buildNum='" + buildNum + '\'' +
                ", domitoryNum='" + domitoryNum + '\'' +
                '}';
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuSex() {
        return stuSex;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex;
    }

    public String getStuPlace() {
        return stuPlace;
    }

    public void setStuPlace(String stuPlace) {
        this.stuPlace = stuPlace;
    }

    public String getStuVcode() {
        return stuVcode;
    }

    public void setStuVcode(String stuVcode) {
        this.stuVcode = stuVcode;
    }

    public String getStuGrade() {
        return stuGrade;
    }

    public void setStuGrade(String stuGrade) {
        this.stuGrade = stuGrade;
    }

    public String getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(String buildNum) {
        this.buildNum = buildNum;
    }

    public String getDomitoryNum() {
        return domitoryNum;
    }

    public void setDomitoryNum(String domitoryNum) {
        this.domitoryNum = domitoryNum;
    }
}
