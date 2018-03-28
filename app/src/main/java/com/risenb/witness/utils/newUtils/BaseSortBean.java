package com.risenb.witness.utils.newUtils;

public abstract class BaseSortBean {
    public BaseSortBean() {
    }

    public abstract String getBaseSortBeanTag();

    public String getBaseSortBeanPYS() {
        return this.getBaseSortBeanTag() != null && !"".equals(this.getBaseSortBeanTag())?PYUtils.getPinYin().getPYS(this.getBaseSortBeanTag()):"";
    }

    public String getBaseSortBeanPYC() {
        return this.getBaseSortBeanTag() != null && !"".equals(this.getBaseSortBeanTag())?(this.getBaseSortBeanTag().length() > 1?PYUtils.getPinYin().getPYC(this.getBaseSortBeanTag()) + this.getBaseSortBeanTag().charAt(1):PYUtils.getPinYin().getPYC(this.getBaseSortBeanTag())):"";
    }

    public char getBaseSortBeanID() {
        if(this.getBaseSortBeanTag() != null && !"".equals(this.getBaseSortBeanTag())) {
            char msb = PYUtils.getPinYin().getPYS(this.getBaseSortBeanTag()).charAt(0);
            return msb < 65?'*':(msb > 90?'#':msb);
        } else {
            return '\u0000';
        }
    }
}
