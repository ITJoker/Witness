package com.risenb.witness.utils.newNetwork;

import java.io.Serializable;

public class BaseBean extends MutilsBaseBean implements Serializable
{
    private static final long serialVersionUID = 1876345352L;

    private boolean status;
    private String msg;
    private String data;
    private String count;

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    @Override
    public boolean isStatus()
    {
        return status;
    }

    @Override
    public String getMsg()
    {
        return msg;
    }

    @Override
    public String getData()
    {
        return data;
    }

    @Override
    public String getCount()
    {
        return count;
    }

}
