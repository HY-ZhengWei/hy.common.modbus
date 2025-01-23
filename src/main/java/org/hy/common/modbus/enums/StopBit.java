package org.hy.common.modbus.enums;





/**
 * 停止位的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-23
 * @version     v1.0
 */
public enum StopBit
{
    
    One         (1 ,"1位停止位"),
    
    OnePointFive(2 ,"1.5位停止位"),
    
    Two         (3 ,"2位停止位"),
    
    ;
    
    
    
    /** 值 */
    private Integer value;
    
    /** 描述 */
    private String  comment;
    
    
    
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
    public static StopBit get(Integer i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (StopBit v_Enum : StopBit.values())
        {
            if ( v_Enum.value.equals(i_Value) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    StopBit(Integer i_Value ,String i_Comment)
    {
        this.value   = i_Value;
        this.comment = i_Comment;
    }

    
    
    public Integer getValue()
    {
        return this.value;
    }
    
    
    
    public String getComment()
    {
        return this.comment;
    }
    
    

    public String toString()
    {
        return this.value + "";
    }
    
}
