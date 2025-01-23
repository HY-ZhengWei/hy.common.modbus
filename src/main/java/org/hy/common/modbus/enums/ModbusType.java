package org.hy.common.modbus.enums;





/**
 * Modbus类型的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-23
 * @version     v1.0
 */
public enum ModbusType
{
    
    Master("MASTER" ,"主站"),
    
    Slave ("SLAVE"  ,"从站"),
    
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
    public static ModbusType get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ModbusType v_Enum : ModbusType.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ModbusType(String i_Value ,String i_Comment)
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
