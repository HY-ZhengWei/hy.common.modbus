package org.hy.common.modbus.junit.cflow025.program;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;





/**
 * LED显示的基本数据：时间与温度
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-07-14
 * @version     v1.0
 */
public class LEDData
{
    
    /** 时间 */
    private Date    time;
    
    /** 温度 */
    private Integer temperature;
    
    /** 原始温度 */
    private Integer temperatureOrg;
    
    
    
    /**
     * 获取：时间
     */
    public String getTimeString()
    {
        if ( this.time == null )
        {
            return "        ";
        }
        else
        {
            return this.time.getHMS();
        }
    }

    
    /**
     * 获取：时间
     */
    public Date getTime()
    {
        return time;
    }

    
    /**
     * 设置：时间
     * 
     * @param i_Time 时间
     */
    public void setTime(Date i_Time)
    {
        this.time = i_Time;
    }
    
    
    /**
     * 获取：时间
     */
    public Date getTimestamp()
    {
        return time;
    }

    
    /**
     * 设置：时间
     * 
     * @param i_Time 时间
     */
    public void setTimestamp(Date i_Time)
    {
        this.time = i_Time;
    }
    
    
    /**
     * 获取：温度
     */
    public Integer getTemperature()
    {
        return temperature;
    }

    
    /**
     * 获取：温度
     */
    public String getTemperatureString()
    {
        return StringHelp.lpad(Help.NVL(this.temperature ,"") ,4 ," ") + "°C";
    }

    
    /**
     * 设置：温度
     * 
     * @param i_Temperature 温度
     */
    public void setTemperature(Integer i_Temperature)
    {
        this.temperature = i_Temperature;
    }

    
    /**
     * 获取：原始温度
     */
    public Integer getTemperatureOrg()
    {
        return temperatureOrg;
    }

    
    /**
     * 设置：原始温度
     * 
     * @param i_TemperatureOrg 原始温度
     */
    public void setTemperatureOrg(Integer i_TemperatureOrg)
    {
        this.temperatureOrg = i_TemperatureOrg;
    }
    
}
