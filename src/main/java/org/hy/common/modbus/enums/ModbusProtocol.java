package org.hy.common.modbus.enums;





/**
 * Modbus协议类型的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-23
 * @version     v1.0
 */
public enum ModbusProtocol
{
    
    RTU  ("RTU"   ,"RTU串口协议"),
    
    ASCII("ASCII" ,"ASCII串口协议"),
    
    TCP  ("TCP"   ,"TCP以太网协议"),
    
    UDP  ("UDP"   ,"UDP以太网协议"),
    
    ;
    
    
    
    /** 值 */
    private String value;
    
    /** 描述 */
    private String comment;
    
    
    
    /**
     * 数值转为常量
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-29
     * @version     v1.0
     *
     * @param i_Value
     * @return
     */
    public static ModbusProtocol get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ModbusProtocol v_Enum : ModbusProtocol.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ModbusProtocol(String i_Value ,String i_Comment)
    {
        this.value   = i_Value;
        this.comment = i_Comment;
    }

    
    
    public String getValue()
    {
        return this.value;
    }
    
    
    
    public String getComment()
    {
        return this.comment;
    }
    
    

    public String toString()
    {
        return this.value;
    }
    
}
