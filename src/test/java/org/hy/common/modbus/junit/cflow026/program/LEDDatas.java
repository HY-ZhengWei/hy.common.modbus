package org.hy.common.modbus.junit.cflow026.program;

import java.util.List;

import org.hy.common.modbus.junit.cflow025.program.LEDData;





/**
 * 智能体修正后温度数据集合
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-07-15
 * @version     v1.0
 */
public class LEDDatas
{
    
    /** 数据请求状态 */
    private String        state;
    
    /** 智能体修正后温度 */
    private List<LEDData> dataItems;

    
    
    /**
     * 获取：数据请求状态
     */
    public String getState()
    {
        return state;
    }

    
    /**
     * 设置：数据请求状态
     * 
     * @param i_State 数据请求状态
     */
    public void setState(String i_State)
    {
        this.state = i_State;
    }

    
    /**
     * 获取：智能体修正后温度
     */
    public List<LEDData> getDataItems()
    {
        return dataItems;
    }

    
    /**
     * 设置：智能体修正后温度
     * 
     * @param i_DataItems 智能体修正后温度
     */
    public void setDataItems(List<LEDData> i_DataItems)
    {
        this.dataItems = i_DataItems;
    }
    
}
