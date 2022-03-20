package hector.developers.contrapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//@IgnoreExtraProperties
public class UserHealthData implements Serializable {

    @SerializedName("date")
    @Expose
    private String date;

    //    section one
    @SerializedName("feverSymptom")
    @Expose
    private String feverSymptom;

    @SerializedName("headacheSymptom")
    @Expose
    private String headacheSymptom;

    @SerializedName("sneezingSymptom")
    @Expose
    private String sneezingSymptom;

    @SerializedName("chestPainSymptom")
    @Expose
    private String chestPainSymptom;

    @SerializedName("bodyPainSymptom")
    @Expose
    private String bodyPainSymptom;

    @SerializedName("nauseaOrVomitingSymptom")
    @Expose
    private String nauseaOrVomitingSymptom;

    @SerializedName("diarrhoeaSymptom")
    @Expose
    private String diarrhoeaSymptom;

    @SerializedName("fluSymptom")
    @Expose
    private String fluSymptom;

    @SerializedName("soreThroatSymptom")
    @Expose
    private String soreThroatSymptom;

    @SerializedName("fatigueSymptom")
    @Expose
    private String fatigueSymptom;

    //    section two
    @SerializedName("newOrWorseningCough")
    @Expose
    private String newOrWorseningCough;

    @SerializedName("difficultyInBreathing")
    @Expose
    private String difficultyInBreathing;

    @SerializedName("lossOfOrDiminishedSenseOfSmell")
    @Expose
    private String lossOfOrDiminishedSenseOfSmell;

    @SerializedName("lossOfOrDiminishedSenseOfTaste")
    @Expose
    private String lossOfOrDiminishedSenseOfTaste;

    //section three
    @SerializedName("contactWithFamily")
    @Expose
    private String contactWithFamily;;

    private Long userId;

    private String firstname;
    private String phone;

    private String risk;

    public UserHealthData() {
    }

    public UserHealthData(String date,String feverSymptom,
                          String headacheSymptom, String sneezingSymptom, String chestPainSymptom,
                          String bodyPainSymptom, String nauseaOrVomitingSymptom, String diarrhoeaSymptom,
                          String fluSymptom, String soreThroatSymptom, String fatigueSymptom,
                          String newOrWorseningCough, String difficultyInBreathing,
                          String lossOfOrDiminishedSenseOfSmell, String lossOfOrDiminishedSenseOfTaste,
                          String contactWithFamily, Long userId, String firstname, String phone,  String risk) {
        this.date = date;
        this.feverSymptom = feverSymptom;
        this.headacheSymptom = headacheSymptom;
        this.sneezingSymptom = sneezingSymptom;
        this.chestPainSymptom = chestPainSymptom;
        this.bodyPainSymptom = bodyPainSymptom;
        this.nauseaOrVomitingSymptom = nauseaOrVomitingSymptom;
        this.diarrhoeaSymptom = diarrhoeaSymptom;
        this.fluSymptom = fluSymptom;
        this.soreThroatSymptom = soreThroatSymptom;
        this.fatigueSymptom = fatigueSymptom;
        this.newOrWorseningCough = newOrWorseningCough;
        this.difficultyInBreathing = difficultyInBreathing;
        this.lossOfOrDiminishedSenseOfSmell = lossOfOrDiminishedSenseOfSmell;
        this.lossOfOrDiminishedSenseOfTaste = lossOfOrDiminishedSenseOfTaste;
        this.contactWithFamily = contactWithFamily;
        this.userId = userId;
        this.firstname = firstname;
        this.phone = phone;
        this.risk = risk;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getFeverSymptom() {
        return feverSymptom;
    }

    public void setFeverSymptom(String feverSymptom) {
        this.feverSymptom = feverSymptom;
    }

    public String getHeadacheSymptom() {
        return headacheSymptom;
    }

    public void setHeadacheSymptom(String headacheSymptom) {
        this.headacheSymptom = headacheSymptom;
    }

    public String getSneezingSymptom() {
        return sneezingSymptom;
    }

    public void setSneezingSymptom(String sneezingSymptom) {
        this.sneezingSymptom = sneezingSymptom;
    }

    public String getChestPainSymptom() {
        return chestPainSymptom;
    }

    public void setChestPainSymptom(String chestPainSymptom) {
        this.chestPainSymptom = chestPainSymptom;
    }

    public String getBodyPainSymptom() {
        return bodyPainSymptom;
    }

    public void setBodyPainSymptom(String bodyPainSymptom) {
        this.bodyPainSymptom = bodyPainSymptom;
    }

    public String getNauseaOrVomitingSymptom() {
        return nauseaOrVomitingSymptom;
    }

    public void setNauseaOrVomitingSymptom(String nauseaOrVomitingSymptom) {
        this.nauseaOrVomitingSymptom = nauseaOrVomitingSymptom;
    }

    public String getDiarrhoeaSymptom() {
        return diarrhoeaSymptom;
    }

    public void setDiarrhoeaSymptom(String diarrhoeaSymptom) {
        this.diarrhoeaSymptom = diarrhoeaSymptom;
    }

    public String getFluSymptom() {
        return fluSymptom;
    }

    public void setFluSymptom(String fluSymptom) {
        this.fluSymptom = fluSymptom;
    }

    public String getSoreThroatSymptom() {
        return soreThroatSymptom;
    }

    public void setSoreThroatSymptom(String soreThroatSymptom) {
        this.soreThroatSymptom = soreThroatSymptom;
    }

    public String getFatigueSymptom() {
        return fatigueSymptom;
    }

    public void setFatigueSymptom(String fatigueSymptom) {
        this.fatigueSymptom = fatigueSymptom;
    }

    public String getNewOrWorseningCough() {
        return newOrWorseningCough;
    }

    public void setNewOrWorseningCough(String newOrWorseningCough) {
        this.newOrWorseningCough = newOrWorseningCough;
    }

    public String getDifficultyInBreathing() {
        return difficultyInBreathing;
    }

    public void setDifficultyInBreathing(String difficultyInBreathing) {
        this.difficultyInBreathing = difficultyInBreathing;
    }

    public String getLossOfOrDiminishedSenseOfSmell() {
        return lossOfOrDiminishedSenseOfSmell;
    }

    public void setLossOfOrDiminishedSenseOfSmell(String lossOfOrDiminishedSenseOfSmell) {
        this.lossOfOrDiminishedSenseOfSmell = lossOfOrDiminishedSenseOfSmell;
    }

    public String getLossOfOrDiminishedSenseOfTaste() {
        return lossOfOrDiminishedSenseOfTaste;
    }

    public void setLossOfOrDiminishedSenseOfTaste(String lossOfOrDiminishedSenseOfTaste) {
        this.lossOfOrDiminishedSenseOfTaste = lossOfOrDiminishedSenseOfTaste;
    }

    public String getContactWithFamily() {
        return contactWithFamily;
    }

    public void setContactWithFamily(String contactWithFamily) {
        this.contactWithFamily = contactWithFamily;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}