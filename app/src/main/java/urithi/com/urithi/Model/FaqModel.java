package urithi.com.urithi.Model;

import java.io.Serializable;

public class FaqModel implements Serializable {
    String faq_id,faq_ques,faq_ans;

    public String getFaq_id() {
        return faq_id;
    }

    public void setFaq_id(String faq_id) {
        this.faq_id = faq_id;
    }

    public String getFaq_ques() {
        return faq_ques;
    }

    public void setFaq_ques(String faq_ques) {
        this.faq_ques = faq_ques;
    }

    public String getFaq_ans() {
        return faq_ans;
    }

    public void setFaq_ans(String faq_ans) {
        this.faq_ans = faq_ans;
    }
}
