package com.entropy_factory.activismap.core.activis;

import com.entropy_factory.activismap.core.db.base.BaseEntity;
import com.entropy_factory.activismap.core.db.model.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ander on 4/10/16.
 */
public class ActivisResponse<Element extends Entity> {

    private static final String TAG = "ActivisResponse";

    private static final int NO_ERROR = -1;

    private int error = NO_ERROR;
    private String errorMessage = "";
    private List<Element> elements = new ArrayList<>();

    public ActivisResponse(List<Element> elements) {
        this.elements = elements;
    }

    public ActivisResponse(Element... elements) {
        this.elements = Arrays.asList(elements);
    }

    public ActivisResponse(int error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public List<Element> getElements() {
        return elements;
    }

    public Element getElementAt(int index) {
        return elements.get(index);
    }

    public int getTotalElements() {
        return elements.size();
    }

    public boolean hasError() {
        return error != NO_ERROR;
    }

    public int getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ELEMENTS: " + elements.size() + ", ERROR: " + error + ", ERROR MSG: " + errorMessage;
    }
}
