package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class EditEnterpriseRequest {
        private String epAddr;
        private String epDesc;
        private String epId;
        private String epName;
        /**
         * 企业类型，比如：互联网企业、国有企业
         */
        private String epType;
}
