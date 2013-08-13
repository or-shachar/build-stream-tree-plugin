package com.hp.mercury.ci.jenkins.plugins.oo.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: grunzwei
* Date: 12/3/12
* Time: 3:43 PM
* To change this template use File | Settings | File Templates.
*/ /*
<listResponse>
    <listReturn>
        <item>
            /Library/How Do I flows/How do I: Create a parallel flow
        </item>
        ...
    </listReturn>
</listResponse>
*/
@XmlRootElement(name = "listResponse")
public class OOListResponse {

    //for jaxb
    public OOListResponse() {}

    Collection<OOListItem> items;

    @XmlElementWrapper(name = "listReturn")
    @XmlElement(name = "item")
    public Collection<OOListItem> getItems() {
        return items;
    }

    public void setItems(Collection<OOListItem> items) {
        this.items = items;
    }

    public static class OOListItem {

        String value;

        public OOListItem() {}

        @XmlValue
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
