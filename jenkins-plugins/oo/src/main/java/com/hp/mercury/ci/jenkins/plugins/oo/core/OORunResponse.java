package com.hp.mercury.ci.jenkins.plugins.oo.core;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.criteria.Criteria;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:44 PM
* To change this template use File | Settings | File Templates.
*/ /* response format:
<?xml version="1.0" encoding="UTF-8"?>
<runResponse>
    <runReturn>
        <item>
            <name>runId</name>
            <value>23</value>
        </item>
        ... (more items)
    </runReturn>
</runResponse>
*/
@XmlRootElement(name = "runResponse")
public class OORunResponse {

    Collection<OORunReturnItem> items;
    private String reportUrl;

    //for jaxb
    public OORunResponse() {}

    @XmlElementWrapper(name = "runReturn")
    @XmlElement(name = "item")
    public Collection<OORunReturnItem> getItems() {
        return items;
    }

    public void setItems(Collection<OORunReturnItem> items) {
        this.items = items;
    }

    public String getReportUrl() {

        if (this.reportUrl == null) {
            final ArrayList<OORunReturnItem> itemsCopy = new ArrayList<OORunReturnItem>(items);
            CollectionUtils.filter(itemsCopy, new Criteria<OORunReturnItem>() {
                @Override
                public boolean isSuccessful(OORunReturnItem tested) {
                    return tested.getName().equals("displayRunReportUrl");
                }
            });
            if (!itemsCopy.isEmpty()) {
                this.reportUrl = itemsCopy.get(0).getValue();
            }
        }

        return this.reportUrl;
    }

    public boolean hasReport() {

        return getReportUrl() != null;
    }

    public static class OORunReturnItem {

        private String name;
        private String value;

        //for jaxb
        public OORunReturnItem() {}

        @XmlElement(name = "name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlElement(name = "value")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
