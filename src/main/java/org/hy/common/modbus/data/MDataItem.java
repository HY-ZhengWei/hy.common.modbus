package org.hy.common.modbus.data;

import java.io.Serializable;

import org.hy.common.modbus.enums.ModbusData;





/**
 * Modbus数据项
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-26
 * @version     v1.0
 */
public class MDataItem implements Serializable
{

    private static final long serialVersionUID = 2352785591782811571L;
    
    

    /** 数据变量名称 */
    private String     name;
    
    /** 偏移位置，即相对地址。下标从 0 开始 */
    private Integer    offset;
    
    /** 数据类型。参见 org.hy.common.modbus.enums.ModbusData */
    private ModbusData dataType;
    
    /** 数据（用于写入） */
    private String     data;
    
    
    
    public MDataItem()
    {
        
    }
    
    
    public MDataItem(String i_Name ,Integer i_Offset ,ModbusData i_DataType)
    {
        this(i_Name ,i_Offset ,i_DataType ,null);
    }
    
    
    public MDataItem(String i_Name ,Integer i_Offset ,ModbusData i_DataType ,String i_Data)
    {
        this.name     = i_Name;
        this.offset   = i_Offset;
        this.dataType = i_DataType;
        this.data     = i_Data;
    }
    
    
    /**
     * 获取：数据变量名称
     */
    public String getName()
    {
        return name;
    }

    
    /**
     * 设置：数据变量名称
     * 
     * @param i_Name 数据变量名称
     */
    public void setName(String i_Name)
    {
        this.name = i_Name;
    }

    
    /**
     * 获取：偏移位置，即相对地址。下标从 0 开始
     */
    public Integer getOffset()
    {
        return offset;
    }

    
    /**
     * 设置：偏移位置，即相对地址。下标从 0 开始
     * 
     * @param i_Offset 偏移位置，即相对地址。下标从 0 开始
     */
    public void setOffset(Integer i_Offset)
    {
        this.offset = i_Offset;
    }

    
    /**
     * 获取：数据类型。参见 org.hy.common.modbus.enums.ModbusData
     */
    public ModbusData getDataType()
    {
        return dataType;
    }

    
    /**
     * 设置：数据类型。参见 org.hy.common.modbus.enums.ModbusData
     * 
     * @param i_DataType 数据类型。参见 org.hy.common.modbus.enums.ModbusData
     */
    public void setDataType(ModbusData i_DataType)
    {
        this.dataType = i_DataType;
    }

    
    /**
     * 获取：数据（用于写入）
     */
    public String getData()
    {
        return data;
    }

    
    /**
     * 设置：数据（用于写入）
     * 
     * @param i_Data 数据（用于写入）
     */
    public void setData(String i_Data)
    {
        this.data = i_Data;
    }
    
}
