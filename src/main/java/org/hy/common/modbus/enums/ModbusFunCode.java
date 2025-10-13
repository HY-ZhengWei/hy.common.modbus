package org.hy.common.modbus.enums;





/**
 * Modbus功能码的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-10-13
 * @version     v1.0
 */
public enum ModbusFunCode
{
    
    InputStatus(   "1"    ,"0x 输入线圈"),
    
    CoilStatus(    "2"    ,"1x 输出线圈"),
    
    HoldingRegiste("3"    ,"2x 输出寄存器"),
    
    InputRegister( "4"    ,"3x 输入寄存器"),
    
    Auto(          "AUTO" ,"自动判定"),
    
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
    public static ModbusFunCode get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ModbusFunCode v_Enum : ModbusFunCode.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ModbusFunCode(String i_Value ,String i_Comment)
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
