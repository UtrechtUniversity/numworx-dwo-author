package fi.ivmdrawgwt.client;


import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class IVMfeedbackGWTField {
    IVMdrawGWT owner;
    Logger logger = Logger.getLogger("feedbackDebug");

    public IVMfeedbackGWTField(IVMdrawGWT owner) {
        this.owner = owner;

    }


    public void mouseUpEvent(String msg) {
        this.owner.label.setText(msg);
    }

}
