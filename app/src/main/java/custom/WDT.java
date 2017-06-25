package custom;

/**
 * Created by HighStrit on 27/03/2017.
 */
//WDT represents Week Day and Term
    //same class can be used as a class for week day and class

public class WDT {

    private String name, stdClass, category, content, title, halfTermNo, studyTitle;
    private String subjectsId, classesId, termsId, id, studyNo;
    private String  bodyTitle, bodyContent, bodyNo, bodyType, expectedTime, actualTime, day, week, summaryTitle, summaryContent;

    public WDT(){
        this.name = "";
        this.stdClass = "";
        this.category = "";
        this.title = "";
        this.content = "";
        this.halfTermNo = "";
        this.subjectsId = "";
        this.classesId = "";
        this.termsId ="";
        this.bodyTitle="";
        this.bodyContent = "";
        this.bodyNo = "";
        this.bodyType = "";
        this.expectedTime = "";
    }

    public WDT(String name){
        this.name = name;
    }

    public WDT(String name, String category){
        this.name = name;
        this.category = category;
    }

    public WDT(String name, String stdClass, String category){
        this.name = name;
        this.stdClass = stdClass;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStdClass() {
        return stdClass;
    }

    public void setStdClass(String stdClass) {
        this.stdClass = stdClass;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHalfTermNo() {
        return halfTermNo;
    }

    public void setHalfTermNo(String halfTermNo) {
        this.halfTermNo = halfTermNo;
    }

    public String getSubjectsId() {
        return subjectsId;
    }

    public void setSubjectsId(String subjectsId) {
        this.subjectsId = subjectsId;
    }

    public String getClassesId() {
        return classesId;
    }

    public void setClassesId(String classesId) {
        this.classesId = classesId;
    }

    public String getTermsId() {
        return termsId;
    }

    public void setTermsId(String termsId) {
        this.termsId = termsId;
    }


    public String getBodyTitle() {
        return bodyTitle;
    }

    public void setBodyTitle(String bodyTitle) {
        this.bodyTitle = bodyTitle;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public String getBodyNo() {
        return bodyNo;
    }

    public void setBodyNo(String bodyNo) {
        this.bodyNo = bodyNo;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSummaryTitle() {
        return summaryTitle;
    }

    public void setSummaryTitle(String summaryTitle) {
        this.summaryTitle = summaryTitle;
    }

    public String getSummaryContent() {
        return summaryContent;
    }

    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudyTitle() {
        return studyTitle;
    }

    public void setStudyTitle(String studyTitle) {
        this.studyTitle = studyTitle;
    }

    public String getStudyNo() {
        return studyNo;
    }

    public void setStudyNo(String studyNo) {
        this.studyNo = studyNo;
    }
}
