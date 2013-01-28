/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.ui.guibuilder;

import java.util.ArrayList;
import java.util.List;

import com.deepnighttwo.util.ResourceUtil;

public class EditorSection {

    private String sectionLabel;

    private List<PropertyLine> props;

    public EditorSection() {
        props = new ArrayList<PropertyLine>();
    }

    public List<PropertyLine> getProps() {
        return props;
    }

    public void setProps(List<PropertyLine> props) {
        this.props = props;
    }

    public String getSectionLabel() {
        return ResourceUtil.getString(sectionLabel);
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

}
